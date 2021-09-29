package com.middleware.provider.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.data.mapper.MapperInventory;
import com.middleware.lab.model.db.Billing;
import com.middleware.lab.model.db.Items;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.middleware.lab.model.db.OrderLab;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.BillingRepository;
import com.middleware.lab.repository.ClientRepository;
import com.middleware.lab.repository.ItemsRepository;
import com.middleware.lab.repository.OrderLogRepository;
import com.middleware.lab.repository.OrderRepository;
import com.middleware.lab.repository.ShippingRepository;
import com.middleware.provider.consumer.navasoft.SoapClient;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.ResponseUpdateInventory;
import com.middleware.provider.orders.model.rest.RequestHook;
import com.middleware.provider.orders.model.rest.RequestTicket;
import com.middleware.provider.orders.model.rest.RequestTicketLab;
import com.middleware.provider.orders.model.rest.RequestTracking;
import com.middleware.provider.orders.model.rest.RequestTrackingEvents;
import com.middleware.provider.orders.model.rest.ResponseOrderFailed;
import com.middleware.provider.orders.model.rest.ResponseOrderFailedComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderSearch;
import com.middleware.provider.orders.model.rest.ResponseOrderSearchComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderStatus;
import com.middleware.provider.orders.model.rest.ResponseOrderStatusLab;
import com.middleware.provider.utils.Parser;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import lab.navasoft.model.soap.GetstatuspedidoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

@Service("OrdersBsImpl")
public class OrdersBsImpl implements OrdersBs {

    @Autowired
    InventoryBs inventoryBs;

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLogRepository orderLogRepository;

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    SoapClient soapClient;

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${lab.accountName}")
    private String accountName;

    @Value("${lab.url}")
    private String url;

    @Value("${lab.callbackInvoice}")
    private String callbackInvoice;

    @Value("${vtex.urlOrder}")
    private String urlOrderVtex;

    private final EntityManager entityManager;

    @Autowired
    public OrdersBsImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    private static final Logger LOG = Logger.getLogger(OrdersBsImpl.class.getName());

    @Override
    public void setHook(String hook) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        RequestHook requestHook = new RequestHook();
        try {
            requestHook = objectMapper.readValue(hook, RequestHook.class);
            LOG.log(Level.WARNING, "BODY requestHook: {0}", requestHook.toString());
            LOG.info("Enviando mensaje a la cola de Hook");
            jmsTemplate.setDefaultDestinationName("HookQueue");
            jmsTemplate.convertAndSend(Parser.objToJsonString(requestHook));

        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResponseOrderStatus cancelOrder(String numPedido) {

        String url = accountName + "/api/oms/pvt/orders/" + numPedido + "/cancel";
        ResponseOrderStatus responseCancelOrder = new ResponseOrderStatus();
        Date date = new Date(System.currentTimeMillis());
        responseCancelOrder.setDate(date);
        responseCancelOrder.setOrderId(numPedido);
        try {
            if (sendCancelOrderToVtex(url, numPedido)) {
                responseCancelOrder.setCode("200");

            } else {
                responseCancelOrder.setCode("400");
            }

        } catch (ModelMappingException | ServiceConnectionException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            responseCancelOrder.setCode("500");

        }

        return responseCancelOrder;
    }

    @Override
    public void updateTicket() {
        List<OrderLab> orders = orderRepository.findOrdersToInvoiced();
        for (OrderLab order : orders) {
            GetstatuspedidoResponse getstatuspedidoResponse = soapClient.getInvoice(Mapper.toInvoicedLab(order.getOrder_num_lab()), this.url, this.callbackInvoice);
            getstatuspedidoResponse.getGetstatuspedidoResult();
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseOrderStatusLab responseOrderStatus;
            try {
                responseOrderStatus = objectMapper.readValue(getstatuspedidoResponse.getGetstatuspedidoResult(), ResponseOrderStatusLab.class);
                OrderLab orderLab = orderRepository.findByIdOrder(order.getOrder_num());

                switch (responseOrderStatus.getStatus()) {
                    case "1":
                        // facturado
                        Billing billing = billingRepository.findByOrderId(order.getOrder_num());
                        billing.setInvoice_number(responseOrderStatus.getNrofac());
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                        billing.setIssuance_date(formatter1.parse(responseOrderStatus.getFecfac()));
                        if (!responseOrderStatus.getUrltracking().isEmpty() || responseOrderStatus.getUrltracking() != null) {
                            billing.setUrl_tracking(responseOrderStatus.getUrltracking());
                        }
                        billing.setIs_billing(false);
                        billingRepository.save(billing);
                        ResponseOrderStatus responseOrder = sendInvoiced(orderLab.getOrder_num(), Mapper.toInvoicedVtex(responseOrderStatus));
                        if (responseOrder.getCode().equals("200")) {
                            //Paso bien 
                            billing.setIs_billing(true);
                            billingRepository.save(billing);
                            orderLab.setOrder_status("invoiced");
                            orderRepository.save(orderLab);
                            LOG.info("Pedido Facturado " + orderLab.getOrder_num());

                        } else {
                            LOG.warning("Error Facturado " + orderLab.getOrder_num());
                        }

                        break;
                    case "2":
                        // entregado
                        break;
                    case "0":
                        // en proceso
                        break;
                    case "*":
                        // cancelar
                        ResponseOrderStatus responseOrderCancel = cancelOrder(orderLab.getOrder_num());
                        if (responseOrderCancel.getCode().equals("200")) {
                            //Paso bien 
                            orderLab.setOrder_status("canceled");
                            orderRepository.save(orderLab);
                        } else {
                            LOG.warning("Error Cancelado " + orderLab.getOrder_num());
                        }
                        break;
                    default:
                    // code block
                }

            } catch (JsonProcessingException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ServiceConnectionException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ModelMappingException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public void updateDelivered() {
        List<OrderLab> orders = orderRepository.findOrdersToDelivered();

        System.out.println("TAM " + orders.size());
        for (OrderLab order : orders) {
            GetstatuspedidoResponse getstatuspedidoResponse = soapClient.getInvoice(Mapper.toInvoicedLab(order.getOrder_num_lab()), this.url, this.callbackInvoice);
            getstatuspedidoResponse.getGetstatuspedidoResult();
            System.out.println("RESPUESTAS INVOICE" + getstatuspedidoResponse.getGetstatuspedidoResult());

            ObjectMapper objectMapper = new ObjectMapper();
            ResponseOrderStatusLab responseOrderStatus;
            try {
                responseOrderStatus = objectMapper.readValue(getstatuspedidoResponse.getGetstatuspedidoResult(), ResponseOrderStatusLab.class);
                OrderLab orderLab = orderRepository.findByIdOrder(order.getOrder_num());

                switch (responseOrderStatus.getStatus()) {
                    case "1":
                        ResponseOrderStatus responseOrder = new ResponseOrderStatus();
                        String url = accountName + "/api/oms/pvt/orders/" + order.getOrder_num() + "/invoice/";
                        boolean delivered = false;

                        // facturado
                        Billing billing = billingRepository.findByOrderId(order.getOrder_num());
                        try {
                            if (billing.getUrl_tracking().isEmpty()) {

                            }
                        } catch (Exception e) {
                            System.out.println("VACIO");
                            if (!responseOrderStatus.getUrltracking().isEmpty() || responseOrderStatus.getUrltracking() != null) {
                                billing.setUrl_tracking(responseOrderStatus.getUrltracking());
                                billingRepository.save(billing);
                                responseOrder = updateTracking(Mapper.toTrackingVtex(billing, responseOrderStatus, delivered), order.getOrder_num(), billing.getInvoice_number());
                            }
                        }

                        if (responseOrderStatus.getStatusdespacho().equals("Entregado") || responseOrderStatus.getStatusdespacho().equals("Ended")) {
                            delivered = true;
                            responseOrder = updateTrackingEvents(order.getOrder_num(), billing.getInvoice_number(), Mapper.toRequestTrackingEvents(delivered));
                            delivered = true;
                            if (responseOrder.getCode().equals("200")) {
                                //Paso bien 
                                orderLab.setOrder_status("delivered");
                                orderRepository.save(orderLab);
                                LOG.info("Pedido Entregado " + orderLab.getOrder_num());

                            } else {
                                LOG.warning("Error Delivered " + orderLab.getOrder_num());
                            }
                        }

                        break;
                    case "2":
                        // entregado
                        break;
                    case "0":
                        // en proceso
                        break;
                    case "*":
                        // cancelar
                        ResponseOrderStatus responseOrderCancel = cancelOrder(orderLab.getOrder_num());
                        if (responseOrderCancel.getCode().equals("200")) {
                            //Paso bien 
                            orderLab.setOrder_status("canceled");
                            orderRepository.save(orderLab);
                        } else {
                            LOG.warning("Error Cancelado " + orderLab.getOrder_num());
                        }
                        break;
                    default:
                    // code block
                }

            } catch (JsonProcessingException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public boolean updateTicketById(String orderId) {

        GetstatuspedidoResponse getstatuspedidoResponse = soapClient.getInvoice(Mapper.toInvoicedLab(orderId), this.url, this.callbackInvoice);
        getstatuspedidoResponse.getGetstatuspedidoResult();
        System.out.println("RESPUESTAS INVOICE" + getstatuspedidoResponse.getGetstatuspedidoResult());

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseOrderStatusLab responseOrderStatus;
        try {
            responseOrderStatus = objectMapper.readValue(getstatuspedidoResponse.getGetstatuspedidoResult(), ResponseOrderStatusLab.class);
            OrderLab orderLab = orderRepository.findByIdOrder(orderId);

            switch (responseOrderStatus.getStatus()) {
                case "1":
                    // facturado
                    Billing billing = billingRepository.findByOrderId(orderId);
                    billing.setInvoice_number(responseOrderStatus.getNrofac());
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    billing.setIssuance_date(formatter1.parse(responseOrderStatus.getFecfac()));
                    if (!responseOrderStatus.getUrltracking().isEmpty() || responseOrderStatus.getUrltracking() != null) {
                        billing.setUrl_tracking(responseOrderStatus.getUrltracking());
                    }
                    billing.setIs_billing(false);
                    billingRepository.save(billing);
                    ResponseOrderStatus responseOrder = sendInvoiced(orderLab.getOrder_num(), Mapper.toInvoicedVtex(responseOrderStatus));
                    if (responseOrder.getCode().equals("200")) {
                        //Paso bien 
                        billing.setIs_billing(true);
                        billingRepository.save(billing);
                        orderLab.setOrder_status("invoiced");
                        orderRepository.save(orderLab);
                        LOG.info("Pedido Facturado " + orderLab.getOrder_num());

                    } else {
                        LOG.warning("Error Facturado " + orderLab.getOrder_num());
                    }

                    break;
                case "2":
                    // entregado
                    break;
                case "0":
                    // en proceso
                    break;
                case "*":
                    // cancelar
                    ResponseOrderStatus responseOrderCancel = cancelOrder(orderLab.getOrder_num());
                    if (responseOrderCancel.getCode().equals("200")) {
                        //Paso bien 
                        orderLab.setOrder_status("canceled");
                        orderRepository.save(orderLab);
                    } else {
                        LOG.warning("Error Cancelado " + orderLab.getOrder_num());
                    }
                    break;
                default:
                // code block
                }

        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceConnectionException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ModelMappingException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public void validateInvoice() {
        List<OrderLab> orders = orderRepository.findAlltoValidate();
        for (OrderLab order : orders) {
            try {
                ResponseOrderVtex responseOrder = sendRequestOrderToVtex(order.getOrder_num());

                if (responseOrder.getStatus().equals("invoiced")) {
                    OrderLab orderUpdate = orderRepository.findByIdOrder(order.getOrder_num());
                    orderUpdate.setIs_validate(true);
                    orderRepository.save(orderUpdate);
                } else {
                    //Start Handling
                    String urlH = accountName + "/api/oms/pvt/orders/" + order.getOrder_num() + "/start-handling";
                    sendStartHandling(urlH, order.getOrder_num());
                }

            } catch (ServiceConnectionException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ModelMappingException ex) {
                Logger.getLogger(OrdersBsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private ResponseOrderStatus sendInvoiced(String numPedido, RequestTicketLab requestTicketLab) throws ServiceConnectionException, ModelMappingException, IOException, InterruptedException {
        //START HANDLING
        String urlH = accountName + "/api/oms/pvt/orders/" + numPedido + "/start-handling";
        String url = accountName + "/api/oms/pvt/orders/" + numPedido + "/invoice";
        ResponseOrderStatus responseOrderStatus = new ResponseOrderStatus();

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        responseOrderStatus.setDate(date);
        responseOrderStatus.setOrderId(numPedido);
        System.out.println("" + numPedido);
        OrderLab order = orderRepository.findByIdOrder(numPedido);
        if (order != null) {
            try {

                if (order.getOrder_status().equals("payment-approved")) {
                    if (sendTicketOrderToVtex(url, requestTicketLab, numPedido)) {
                        responseOrderStatus.setCode("200 - OK");
                    } else {
                        responseOrderStatus.setCode("400");
                    }

                } else {
                    ResponseOrderVtex responseOrder = sendRequestOrderToVtex(order.getOrder_num());
                    if (responseOrder.getStatus().equals("ready-for-handling")) {
                        if (sendStartHandling(urlH, numPedido)) {

                            if (sendTicketOrderToVtex(url, requestTicketLab, numPedido)) {
                                responseOrderStatus.setCode("200 - OK");
                            } else {
                                responseOrderStatus.setCode("400 - Error en el numero de orden");
                            }
                        } else {
                            responseOrderStatus.setCode("400 - Error Start Handling");
                        }

                    } else {
                        if (sendTicketOrderToVtex(url, requestTicketLab, numPedido)) {
                            responseOrderStatus.setCode("200 - OK");
                        } else {
                            responseOrderStatus.setCode("400 - Error en el numero de orden");
                        }
                    }
                }

            } catch (ModelMappingException | ServiceConnectionException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                responseOrderStatus.setCode("500");

            }
        } else {
            responseOrderStatus.setCode("400");
        }
        //Actualizar inventario de los items facturados 
        this.updateInventory(responseOrderStatus, order);
        return responseOrderStatus;
    }

    private boolean sendTrankingOrderToVtex(String url, RequestTracking requestTracking) throws ServiceConnectionException, ModelMappingException, IOException {
        try {
            if (vtexApiRestClient.trackingOrder(url, requestTracking)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private ResponseOrderVtex sendRequestOrderToVtex(String idOrder) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException {
        return vtexApiRestClient.sendRequestOrderToVTEX(idOrder, urlOrderVtex);
    }

    private void updateInventory(ResponseOrderStatus responseOrderStatus, OrderLab order) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException, InterruptedException {
        if (responseOrderStatus.getCode().equals("200 - OK")) {
            List<SkuHomologation> skus = new ArrayList<>();
            SkuHomologation sku = new SkuHomologation();
            List<Items> items = order.getItems();
            for (Items item : items) {
                sku.setRefId(item.getItem_cod());
                sku.setSku(item.getSku());
                skus.add(sku);
                sku = new SkuHomologation();
            }
            inventoryBs.updatesInvoices(skus, "invoice " + order.getOrder_num());
        }
    }

    private boolean sendTicketOrderToVtex(String url, RequestTicketLab requestTicketLab, String id) throws ServiceConnectionException, ModelMappingException, IOException {

        try {
            OrderLab order = orderRepository.findByIdOrder(id);
            Billing billing = billingRepository.findByOrderId(id);
            RequestTicket requestTicket = Mapper.toRequestTicket(requestTicketLab, order);

            System.out.print("llamado a VTEX PARA invoice");

            if (vtexApiRestClient.invoiceOrder(url, requestTicket)) {
                // Se debe actualizar en la BD el estado a invoice
                System.out.print("ACTUALIZO" + id);
                billing.setInvoice_value(order.getTotal_value());
                billing.setIssuance_date(requestTicketLab.getIssuanceDate());
                billing.setInvoice_number(requestTicket.getInvoiceNumber());
                billingRepository.save(billing);
                order.setOrder_status("invoiced");
                orderRepository.save(order);
                System.out.print("ACTUALIZO");

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    private boolean sendStartHandling(String url, String id) throws ServiceConnectionException, ModelMappingException, IOException {

        try {
            OrderLab order = orderRepository.findByIdOrder(id);
            System.out.print("llamado a VTEX PARA start handling");

            if (!order.getOrder_status().equals("start-handling")) {
                if (vtexApiRestClient.startHandling(url)) {
                    order.setOrder_status("start-handling");
                    orderRepository.save(order);
                    System.out.print("ACTUALIZO");
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOG.warning("Error START HANDLING ");
            return true;

        }

    }

    private boolean sendRequestInvoiceNavasoft(String url, RequestTicketLab requestTicketLab, String id) throws ServiceConnectionException, ModelMappingException, IOException {

        try {
            OrderLab order = orderRepository.findByIdOrder(id);
            Billing billing = billingRepository.findByOrderId(id);
            RequestTicket requestTicket = Mapper.toRequestTicket(requestTicketLab, order);

            System.out.print("llamado a VTEX PARA invoice");

            if (vtexApiRestClient.invoiceOrder(url, requestTicket)) {
                // Se debe actualizar en la BD el estado a invoice
                System.out.print("ACTUALIZO" + id);
                billing.setInvoice_value(order.getTotal_value());
                billing.setIssuance_date(requestTicketLab.getIssuanceDate());
                billing.setInvoice_number(requestTicket.getInvoiceNumber());
                billingRepository.save(billing);
                order.setOrder_status("invoiced");
                orderRepository.save(order);
                System.out.print("ACTUALIZO");

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public ResponseOrderStatus updateTracking(RequestTracking requestTracking, String numPedido, String factura) {

        String url = accountName + "/api/oms/pvt/orders/" + numPedido + "/invoice/" + factura;
        ResponseOrderStatus responseOrderStatus = new ResponseOrderStatus();
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        responseOrderStatus.setDate(date);
        responseOrderStatus.setOrderId(numPedido);
        try {
            if (sendTrankingOrderToVtex(url, requestTracking, numPedido)) {
                responseOrderStatus.setCode("200 - OK");
            } else {
                responseOrderStatus.setCode("400 - Error en el numero de orden");
            }

        } catch (ModelMappingException | ServiceConnectionException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            responseOrderStatus.setCode("500 - ERROR");

        }
        return responseOrderStatus;

    }

    public ResponseOrderStatus updateTrackingEvents(String numPedido, String invoice, RequestTrackingEvents requestTrackingEvents) {

        String url = accountName + "/api/oms/pvt/orders/" + numPedido + "/invoice/" + invoice + "/tracking";
        ResponseOrderStatus responseOrderStatus = new ResponseOrderStatus();
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        responseOrderStatus.setDate(date);
        responseOrderStatus.setOrderId(numPedido);

        try {
            if (sendEventsTrankingOrderToVtex(url, requestTrackingEvents, numPedido)) {
                responseOrderStatus.setCode("200 - OK");
            } else {
                responseOrderStatus.setCode("400 - Error en el numero de orden");
            }

        } catch (ModelMappingException | ServiceConnectionException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            responseOrderStatus.setCode("500 - ERROR");

        }

        return responseOrderStatus;
    }

    private boolean sendCancelOrderToVtex(String url, String id) throws ServiceConnectionException, ModelMappingException, IOException {
        System.out.print("llamado a VTEX PARA cancel");

        if (vtexApiRestClient.cancelOrder(url)) {
            // Se debe actualizar en la BD el estado a cancelado
            System.out.print("ACTUALIZO" + id);

            OrderLab order = orderRepository.findByIdOrder(id);

            order.setOrder_status("canceled");
            orderRepository.save(order);
            System.out.print("ACTUALIZO");

            return true;
        } else {
            return false;
        }
    }

    private boolean sendTrankingOrderToVtex(String url, RequestTracking requestTracking, String id) throws ServiceConnectionException, ModelMappingException, IOException {
        try {
            System.out.print("llamado a VTEX PARA tracking");
            if (vtexApiRestClient.trackingOrder(url, requestTracking)) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sendEventsTrankingOrderToVtex(String url, RequestTrackingEvents requestTrackingEvents, String id) throws ServiceConnectionException, ModelMappingException, IOException {
        try {
            if (vtexApiRestClient.trackingEventsOrder(url, requestTrackingEvents)) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ResponseOrderSearch> searchOrder(String numPedido) {

        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_nutrition_db.get_orders");
        storedProcedureQuery.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("id", numPedido);
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseOrderSearch> list = results.stream().map(result -> new ResponseOrderSearch(
                (String) result[0],
                (Date) result[1],
                (String) result[2],
                (String) result[3],
                (String) result[4],
                (BigDecimal) result[5]
        )).collect(Collectors.toList());

        return list;

    }

    @Override
    public ResponseOrderSearchComplete searchOrders(int pag, int num, String filter, String value) {
        ResponseOrderSearchComplete response = new ResponseOrderSearchComplete();
        String sh = "";
        if (filter == "") {
            sh = "null";
        } else {
            sh = value;
        }
        System.out.println("ENVIARE" + sh + " " + pag + " " + num);
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_nutrition_db.get_orders");
        storedProcedureQuery.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("pag", int.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("num", int.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("id", sh);
        storedProcedureQuery.setParameter("pag", pag);
        storedProcedureQuery.setParameter("num", (num - 1) * pag);
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseOrderSearch> list = results.stream().map(result -> new ResponseOrderSearch(
                (String) result[0],
                (Date) result[1],
                (String) result[2],
                (String) result[3],
                (String) result[4],
                (BigDecimal) result[5]
        )).collect(Collectors.toList());
        response.setData(list);
        response.setTotal(orderRepository.count());

        System.out.println("TOTAL" + orderRepository.count());
        System.out.println("LISTA" + list.toString());
        return response;

    }

    @Override
    public ResponseOrderFailedComplete searchOrdersFailed(int pag, int num, String filter, String value) {
        ResponseOrderFailedComplete response = new ResponseOrderFailedComplete();
        String sh = "";
        if (filter == "") {
            sh = "null";
        } else {
            sh = value;
        }
        System.out.println("ENVIARE" + sh + " " + pag + " " + num);
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_nutrition_db.get_ordersFailed");
        storedProcedureQuery.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("pag", int.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("num", int.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("id", sh);
        storedProcedureQuery.setParameter("pag", pag);
        storedProcedureQuery.setParameter("num", (num - 1) * pag);
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseOrderFailed> list = results.stream().map(result -> new ResponseOrderFailed(
                (String) result[0],
                (Date) result[1],
                (String) result[2]
        )).collect(Collectors.toList());
        response.setData(list);
        response.setTotal(orderLogRepository.count());

        System.out.println("TOTAL" + orderLogRepository.count());
        System.out.println("LISTA" + list.toString());
        return response;

    }
}
