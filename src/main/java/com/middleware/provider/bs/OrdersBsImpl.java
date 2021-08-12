package com.middleware.provider.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.data.mapper.MapperInventory;
import com.middleware.lab.model.db.Billing;
import com.middleware.lab.model.db.Client;
import com.middleware.lab.model.db.Items;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.middleware.lab.model.db.OrderLab;
import com.middleware.lab.model.db.Payment;
import com.middleware.lab.model.db.PaymentMethod;
import com.middleware.lab.model.db.Shipping;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.BillingRepository;
import com.middleware.lab.repository.ClientRepository;
import com.middleware.lab.repository.ItemsRepository;
import com.middleware.lab.repository.OrderLogRepository;
import com.middleware.lab.repository.OrderRepository;
import com.middleware.lab.repository.PaymentMethodRepository;
import com.middleware.lab.repository.PaymentRepository;
import com.middleware.lab.repository.ShippingRepository;
import com.middleware.lab.repository.SkuHomologationRepository;
import com.middleware.provider.consumer.navasoft.SoapClient;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.ResponseUpdateInventory;
import com.middleware.provider.orders.model.rest.ItemsDiscount;
import com.middleware.provider.orders.model.rest.RequestHook;
import com.middleware.provider.orders.model.rest.RequestTicket;
import com.middleware.provider.orders.model.rest.RequestTicketLab;
import com.middleware.provider.orders.model.rest.RequestTracking;
import com.middleware.provider.orders.model.rest.RequestTrackingEvents;
import com.middleware.provider.orders.model.rest.ResponseCreateOrderLab;
import com.middleware.provider.orders.model.rest.ResponseOrderFailed;
import com.middleware.provider.orders.model.rest.ResponseOrderFailedComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderSearch;
import com.middleware.provider.orders.model.rest.ResponseOrderSearchComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderStatus;
import com.middleware.provider.orders.model.rest.ResponseOrderStatusLab;
import com.middleware.provider.sku.model.rest.AlternateIds;
import com.middleware.provider.sku.model.rest.SkuDetail;
import com.middleware.provider.utils.Parser;
import com.middleware.provider.vtex.model.rest.Components;
import com.middleware.provider.vtex.model.rest.PaymentsOrder;
import com.middleware.provider.vtex.model.rest.Price;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import com.middleware.provider.vtex.model.rest.Transactions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
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
import lab.navasoft.model.soap.LoadbilldataResponse;
import org.apache.commons.codec.binary.Base64;
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

    @Value("${lab.url}")
    private String urlOrder;

    @Value("${lab.callbackOrder}")
    private String callbackOrder;

    @Value("${vtex.url.prices}")
    private String urlGetPriceVtex;

    @Value("${lab.accountNameShort}")
    private String accountNameShort;

    @Value("${lab.ivg}")
    private String labIvg;

    @Value("${lab.base}")
    private String labBase;

    @Value("${path.pedidos}")
    private String path;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private final EntityManager entityManager;

    @Autowired
    private SkuHomologationRepository skuHomologationRepository;

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

                    } else if (sendTicketOrderToVtex(url, requestTicketLab, numPedido)) {
                        responseOrderStatus.setCode("200 - OK");
                    } else {
                        responseOrderStatus.setCode("400 - Error en el numero de orden");
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

    @Override
    public ResponseOrderVtex sendRequestOrderToVtex(String idOrder) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException {
        processOrder(vtexApiRestClient.sendRequestOrderToVTEX(idOrder, urlOrderVtex));
        return null;
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

    public boolean processOrder(ResponseOrderVtex responseOrder) {
        try {
            LOG.log(Level.INFO, "responseOrder{0}", responseOrder.toString());
            OrderLab orderLab = saveOrder(responseOrder);
            LOG.log(Level.INFO, "status - >{0}", responseOrder.getStatus());
            //if (responseOrder.getStatus().equals("ready-for-handling")) {
            if (orderLab.getIs_synchronized_navasoft() != 1) {
                sendOrderToLab(orderLab, shippingRepository.findByOrderId(orderLab.getOrder_num()), billingRepository.findByOrderId(orderLab.getOrder_num()),
                        paymentRepository.findByOrderId(orderLab.getOrder_num()), paymentMethodRepository.findBySystem(getSystemPayment(responseOrder)));
                return true;
            }

            /* } else {
                LOG.log(Level.INFO, "status - >{0}, No es posible enviar", responseOrder.getStatus());

                //No esta completa para enviar 
            }*/
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    private OrderLab saveOrder(ResponseOrderVtex responseOrder) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException {
        LOG.log(Level.INFO, "EL ID{0}", responseOrder.getOrderId());
        OrderLab orderLab = orderRepository.findByIdOrder(responseOrder.getOrderId());
        if (responseOrder != null && orderLab == null) {
            LOG.info(responseOrder.toString());
            orderLab = orderRepository.save(Mapper.toOrderDB(responseOrder));
            //guardar datos de shipping y posterior obbtenerlos 
            shippingRepository.save(Mapper.toShippingOrder(responseOrder, orderLab));
            //guardar datos de del cliente y posterior obbtenerlos 
            String url = "https://" + accountNameShort + ".vtexcommercestable.com.br/api/dataentities/CL/search?document=" + responseOrder.getClientProfileData().getDocument();
            String email = "";
            boolean employed = false;
            com.middleware.provider.vtex.model.rest.Client[] clients2 = vtexApiRestClient.searchDni(url);
            for (com.middleware.provider.vtex.model.rest.Client client1 : clients2) {
                email = client1.getEmail();
                employed = client1.isIsEmployed();
            }
            clientRepository.save(Mapper.toClientOrder(responseOrder, orderLab, email, employed));
            //Valida si pidio factura
            billingRepository.save(Mapper.toBillingOrder(responseOrder, orderLab));
            PaymentMethod paymentMethod = paymentMethodRepository.findBySystem(getSystemPayment(responseOrder));
            paymentRepository.save(Mapper.toPaymentOrder(responseOrder, paymentMethod, orderLab));

            Items itemsDb = new Items();
            List<com.middleware.provider.vtex.model.rest.Items> itemsVtex = responseOrder.getItems();
            LOG.log(Level.INFO, "{0}TAMA\u00d1O", itemsVtex.size());
            for (com.middleware.provider.vtex.model.rest.Items item : itemsVtex) {
                String urlref = accountName + "/api/catalog_system/pvt/sku/stockkeepingunitbyid/" + item.getId();
                SkuDetail skuDetail = vtexApiRestClient.getRefBySku(urlref);
                item.setName(skuDetail.getSkuName());
                if (item.getComponents().isEmpty()) {
                    SkuHomologation sku = skuHomologationRepository.findBySku(item.getId());
                    if (sku == null) {
                        AlternateIds alternateIds = skuDetail.getAlternateIds();
                        if (skuDetail == null) {
                            orderLogRepository.save(Mapper.toOrderLog("ERROR - El SKU no existe", orderLab.getOrder_num()));

                        } else {
                            SkuHomologation sku2 = new SkuHomologation(item.getId(), alternateIds.getRefId());
                            skuHomologationRepository.save(sku2);
                            itemsRepository.save(Mapper.toItems(item, orderLab, responseOrder, alternateIds.getRefId()));
                            itemsDb = new Items();

                        }

                    } else {
                        itemsRepository.save(Mapper.toItems(item, orderLab, responseOrder, sku.getRefId()));
                        itemsDb = new Items();
                    }

                } else {
                    for (int j = 0; j < item.getQuantity(); j++) {

                        for (Components components : item.getComponents()) {
                            String urlrefC = accountName + "/api/catalog_system/pvt/sku/stockkeepingunitbyid/" + components.getId();
                            SkuDetail skuDetail2 = vtexApiRestClient.getRefBySku(urlrefC);
                            components.setName(skuDetail2.getSkuName());
                            System.out.println("EL NAME " + skuDetail2.getSkuName());
                            SkuHomologation sku = skuHomologationRepository.findBySku(components.getId());
                            SkuHomologation sku2 = new SkuHomologation();
                            if (sku == null) {
                                AlternateIds alternateIds = skuDetail2.getAlternateIds();
                                sku2.setSku(components.getId());
                                sku2.setRefId(alternateIds.getRefId());
                            }
                            Price price = vtexApiRestClient.getPrice(urlGetPriceVtex + "/" + components.getId());
                            if (price.getListPrice() == null) {
                                components.setPrice(price.getCostPrice());
                            } else {
                                components.setPrice(price.getListPrice());
                            }
                            if (sku == null) {
                                itemsRepository.save(Mapper.toItemsComponent(item, components, responseOrder, orderLab, sku2));
                            } else {
                                itemsRepository.save(Mapper.toItemsComponent(item, components, responseOrder, orderLab, sku));
                            }
                            components = new Components();
                        }
                    }

                    /*
                    for (int j = 0; j < item.getQuantity(); j++) {
                        BigDecimal suma = BigDecimal.ZERO;
                        int it = 0;
                        for (Components components : item.getComponents()) {                            
                            String urlrefC = accountName + "/api/catalog_system/pvt/sku/stockkeepingunitbyid/" + components.getId();
                            SkuDetail skuDetail2 = vtexApiRestClient.getRefBySku(urlrefC);
                            components.setName(skuDetail2.getSkuName());
                            System.out.println("EL NAME " + skuDetail2.getSkuName());

                            SkuHomologation sku = skuHomologationRepository.findBySku(components.getId());      
                            SkuHomologation sku2 = new SkuHomologation(); 
                            if (sku == null) {
                                AlternateIds alternateIds = skuDetail2.getAlternateIds();
                                sku2.setSku(components.getId());
                                sku2.setRefId(alternateIds.getRefId());
                            }                            
                            it++;
                            LOG.log(Level.INFO, "{0}compare{1}", new Object[]{item.getComponents().size(), it});
                            Price price = vtexApiRestClient.getPrice(urlGetPriceVtex + "/" + components.getId());
                            if (price.getListPrice() == null) {
                                components.setPrice(price.getCostPrice());
                                for (int i = 0; i < components.getQuantity(); i++) {
                                    suma = suma.add(price.getCostPrice());
                                }

                            } else {
                                components.setPrice(price.getListPrice());
                                for (int i = 0; i < components.getQuantity(); i++) {
                                    suma = suma.add(price.getListPrice());
                                }

                            }
                            if (item.getComponents().size() == it) {
                                LOG.info("ENTRE AL DE PROMO "+ suma);
                                if (sku == null) {
                                    itemsRepository.save(Mapper.toItemsComponentPromo(components, item, orderLab, suma, sku2));
                                }else{
                                    itemsRepository.save(Mapper.toItemsComponentPromo(components, item, orderLab, suma, sku));
                                }
                                itemsDb = new Items();
                            } else {
                                if (sku == null) {
                                    itemsRepository.save(Mapper.toItemsComponent(components, orderLab, sku2));

                                }else{
                                    itemsRepository.save(Mapper.toItemsComponent(components, orderLab, sku));
                                }
                                itemsDb = new Items();
                            }
                        }
                    }*/
                }
            }
        } else {
            orderLab.setOrder_status(responseOrder.getStatus());
            orderRepository.save(orderLab);
            LOG.info("no guardo order por que ya existia, y en teoria vuelve a llegar pq la encole");
        }

        return orderLab;
    }

    private boolean sendOrderToLab(OrderLab order, Shipping shipping, Billing billing, Payment payment, PaymentMethod paymentMethod) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException, blacksip.vtex.client.exception.ServiceConnectionException {
        String line = "";
        List<Items> items = new ArrayList<>();

        items = itemsRepository.findAllByIdOrder(order.getOrder_num());
        Client client = clientRepository.findByOrderId(order.getOrder_num());
        LOG.info("RUTA " + this.path + order.getOrder_num() + ".txt");
        //Crear el archivo de texto
        try {
            FileWriter writer = new FileWriter(this.path + order.getOrder_num() + ".txt", false);
            //Tipo de documento
            line = "01|" + client.getFirst_name() + " " + client.getLast_name() + "|" + ((client.getDocument_type().equals("dni")) ? "01" : "04") + "|" + client.getDocument() + "|" + client.getEmail() + "|" + ((client.isIsCorporate()) ? "1" : "0") + "|"
                    + client.getPhone() + "|" + shipping.getRef_dir() + "|" + shipping.getRef_dir() + " " + shipping.getComplement() + "|"
                    + ((client.isIsCorporate()) ? billing.getBusiness_name() : "") + "|" + ((client.isIsCorporate()) ? billing.getBusiness_doc() : "") + "|"
                    + shipping.getReceiver_name() + "|" + shipping.getLatitude() + "|" + shipping.getLongitude() + "|";
            writer.write(line + "\n");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(order.getOrder_date());
            String dateShipping = format.format(shipping.getShipping_date());
            PaymentMethod paymentMetho = payment.getPaymentMethod();

            line = "02|" + dateString + "|32|" + order.getOrder_num_lab() + "|" + (order.getTotal_value().subtract(shipping.getShipping_price())) + "|" + order.getTotal_value()
                    + "|" + shipping.getShipping_price() + "|" + getIvg(order.getTotal_value().subtract(shipping.getShipping_price()))
                    + "|" + ((paymentMetho.getSystem_vtex().equals("16")) ? (payment.getValue().add(order.getTotal_discount().multiply(BigDecimal.valueOf(-1)))).multiply(BigDecimal.valueOf(-1)) : order.getTotal_discount())
                    + "|PEN|" + order.getTotal_discount() + "|" + ((paymentMetho.getSystem_vtex().equals("16")) ? payment.getValue() : "0") + "|0|"
                    + paymentMetho.getMethod_lab() + "|" + ((paymentMetho.getType_lab() == null) ? "" : paymentMetho.getType_lab()) + "|" + ((payment.getCart_tid() == null) ? "" : payment.getCart_tid()) + "||" + ((shipping.isIs_pickup()) ? "3" : "2") + "|" + dateShipping + "|"
                    + ((shipping.isIs_pickup()) ? shipping.getPickup_id() : ((shipping.getWarehouse().equals("1_1")) ? "037" : shipping.getWarehouse())) + "|" + 0 + "|" + ((paymentMetho.getSystem_vtex().equals("16")) ? payment.getCart_number() : "") + "|";
            writer.write(line + "\n");
            System.out.println("shippingPrice " + shipping.getShipping_price());
            if (!shipping.isIs_pickup()) {
                if (shipping.getShipping_price().compareTo(BigDecimal.ZERO) == 0) {
                    System.out.println("ENTRE");
                } else {
                    line = "03|FLETECOB|un|FLETE|1|" + shipping.getShipping_price() + "|S|" + 0 + "|" + shipping.getShipping_price() + "|0|";
                    writer.write(line + "\n");
                }
            }
            List<ItemsDiscount> itemsDiscount = new ArrayList<>();

            for (Items item : items) {
                //if have discount             
                if (item.getDiscount().compareTo(BigDecimal.ZERO) != 0) {
                    line = "03|" + item.getItem_cod() + "|" + item.getMeasurement_unit() + "|" + item.getName() + "|" + item.getQuantity() + "|"
                            + item.getUnit_price() + "|S|" + 0 + "|" + item.getTotal_price() + "|0|";
                    writer.write(line + "\n");
                    //crear array de descuentos 
                    String name = ((item.getSales_name() == null) ? "Descuento " + item.getName() : item.getSales_name());
                    if (itemsDiscount.isEmpty()) {
                        ItemsDiscount itemDiscount = new ItemsDiscount();
                        itemDiscount.setDiscount(item.getDiscount());
                        itemDiscount.setMeasurement_unit(item.getMeasurement_unit());
                        itemDiscount.setName(name);
                        itemsDiscount.add(itemDiscount);
                    } else {
                        boolean id = false;
                        for (ItemsDiscount itemsDiscount1 : itemsDiscount) {
                            if (itemsDiscount1.getName().equals(name)) {
                                itemsDiscount1.setDiscount(itemsDiscount1.getDiscount().add(item.getDiscount()));
                                id = true;
                            }
                        }
                        if (id == false) {
                            ItemsDiscount itemDiscount = new ItemsDiscount();
                            itemDiscount.setDiscount(item.getDiscount());
                            itemDiscount.setMeasurement_unit(item.getMeasurement_unit());
                            itemDiscount.setName(name);
                            itemsDiscount.add(itemDiscount);
                        }
                    }

                } else {
                    line = "03|" + item.getItem_cod() + "|" + item.getMeasurement_unit() + "|" + item.getName() + "|" + item.getQuantity() + "|"
                            + item.getUnit_price() + "|S|" + 0 + "|" + item.getTotal_price() + "|0|";
                    writer.write(line + "\n");
                }
            }
            for (ItemsDiscount itemsDiscount1 : itemsDiscount) {
                line = "03|" + "DS00" + "|" + itemsDiscount1.getMeasurement_unit() + "|" + itemsDiscount1.getName() + "|" + 1 + "|"
                        + itemsDiscount1.getDiscount() + "|S|" + 0 + "|" + itemsDiscount1.getDiscount() + "|0|";
                writer.write(line + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = encodeFileToBase64Binary(this.path + order.getOrder_num() + ".txt");
        LOG.info("RESPUESTAS" + response);
        //Consumir servicio
        LOG.info("LLAMANDO SOAP " + urlOrder);
        LoadbilldataResponse loadBillDataResponse = soapClient.setOrder(Mapper.toOrderLab(client.getDocument(), order.getOrder_num(), response), this.urlOrder, this.callbackOrder);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseCreateOrderLab responseCreateOrderLab = objectMapper.readValue(loadBillDataResponse.getLoadbilldataResult(), ResponseCreateOrderLab.class);
        OrderLab orderLab = orderRepository.findByIdOrder(order.getOrder_num());

        if (responseCreateOrderLab.getStatus().equals("-1")) {
            orderLogRepository.save(Mapper.toOrderLog(responseCreateOrderLab.getStatusname(), order.getOrder_num()));
        } else {
            orderLab.setIs_synchronized_navasoft(1);
            orderRepository.save(orderLab);
        }
        LOG.info(loadBillDataResponse.getLoadbilldataResult());

        return false;
    }

    public String encodeFileToBase64Binary(String fileName)
            throws IOException {
        LOG.info("ENTRO LA ENCODE");
        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static String getSystemPayment(ResponseOrderVtex responseOrder) {

        List<PaymentsOrder> paymentsVtex = new ArrayList<PaymentsOrder>();
        String system = "";
        List<Transactions> transactions = responseOrder.getPaymentData().getTransactions();
        for (Transactions transaction : transactions) {
            paymentsVtex = transaction.getPayments();
            LOG.info("payymyents" + paymentsVtex.size());
            for (PaymentsOrder paymentsOrder : paymentsVtex) {
                system = paymentsOrder.getPaymentSystem();
            }
        }
        return system;

    }

    public BigDecimal getBase(BigDecimal total) {
        BigDecimal b = new BigDecimal(this.labBase);
        return total.multiply(b);
    }

    public BigDecimal getIvg(BigDecimal total) {
        BigDecimal b = new BigDecimal(this.labIvg);
        return total.multiply(b);
    }

    public static BigDecimal setPoint(BigDecimal num) {
        return num.movePointLeft(2);
    }

}
