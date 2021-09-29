package com.middleware.provider.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.model.db.Billing;
import com.middleware.lab.model.db.Client;
import com.middleware.lab.model.db.Items;
import com.middleware.lab.model.db.OrderLab;
import com.middleware.lab.model.db.Payment;
import com.middleware.lab.model.db.PaymentMethod;
import com.middleware.lab.model.db.Shipping;
import com.middleware.lab.repository.BillingRepository;
import com.middleware.lab.repository.ClientRepository;
import com.middleware.lab.repository.ItemsRepository;
import com.middleware.lab.repository.OrderRepository;
import com.middleware.lab.repository.PaymentMethodRepository;
import com.middleware.lab.repository.PaymentRepository;
import com.middleware.lab.repository.ShippingRepository;
import com.middleware.lab.repository.SkuRepository;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.orders.model.rest.RequestHook;
import com.middleware.provider.vtex.model.rest.PaymentsOrder;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import com.middleware.provider.vtex.model.rest.Transactions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import com.middleware.lab.client.LabClient;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.OrderLogRepository;
import com.middleware.lab.repository.SkuHomologationRepository;
import com.middleware.provider.consumer.navasoft.SoapClient;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.orders.model.rest.ItemsDiscount;
import com.middleware.provider.orders.model.rest.ResponseCreateOrderLab;
import com.middleware.provider.orders.model.rest.ResponseOrderSearchComplete;
import com.middleware.provider.sku.model.rest.AlternateIds;
import com.middleware.provider.sku.model.rest.SkuDetail;
import com.middleware.provider.vtex.model.rest.Components;
import com.middleware.provider.vtex.model.rest.Price;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import lab.navasoft.model.soap.Loadbilldata;
import lab.navasoft.model.soap.LoadbilldataResponse;
import org.apache.commons.codec.binary.Base64;

@Component
public class HookListener {

    @Autowired
    LabClient coolboxClient;

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLogRepository orderLogRepository;

    @Autowired
    private SkuHomologationRepository skuHomologationRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    SoapClient soapClient;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${lab.url}")
    private String urlOrder;

    @Value("${lab.callbackOrder}")
    private String callbackOrder;

    @Value("${vtex.urlOrder}")
    private String urlOrderVtex;

    @Value("${vtex.url.prices}")
    private String urlGetPriceVtex;

    @Value("${lab.accountNameShort}")
    private String accountNameShort;

    @Value("${lab.accountName}")
    private String accountName;

    @Value("${lab.ivg}")
    private String labIvg;

    @Value("${lab.base}")
    private String labBase;

    @Value("${path.pedidos}")
    private String path;

    private static final Logger LOG = Logger.getLogger(HookListener.class.getName());

    @JmsListener(destination = "HookQueue")

    public void receiveMessage(String msj)
            throws ModelMappingException, IOException, ServiceConnectionException, JmsException, JsonProcessingException, blacksip.vtex.client.exception.ServiceConnectionException {

        LOG.log(Level.INFO, "Received <{0}>", msj);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestHook requestHook = objectMapper.readValue(msj, RequestHook.class);
        if (requestHook.getOrderId() != null) {
            ResponseOrderVtex responseOrder = sendRequestOrderToVtex(requestHook.getOrderId());
            LOG.log(Level.INFO, "responseOrder{0}", responseOrder.toString());
            OrderLab orderLab = saveOrder(responseOrder);
            LOG.log(Level.INFO, "status - >{0}", responseOrder.getStatus());
            if (responseOrder.getStatus().equals("ready-for-handling")) {
                if (orderLab.getIs_synchronized_navasoft() != 1) {
                    sendOrderToLab(orderLab, shippingRepository.findByOrderId(orderLab.getOrder_num()), billingRepository.findByOrderId(orderLab.getOrder_num()),
                            paymentRepository.findByOrderId(orderLab.getOrder_num()), paymentMethodRepository.findBySystem(getSystemPayment(responseOrder)));
                }

            } else {
                LOG.log(Level.INFO, "status - >{0}, No es posible enviar", responseOrder.getStatus());

                //No esta completa para enviar 
            }
        }

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

    private ResponseOrderVtex sendRequestOrderToVtex(String idOrder) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException {
        return vtexApiRestClient.sendRequestOrderToVTEX(idOrder, urlOrderVtex);
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        LOG.info("LLAMANDO SOAP " + this.urlOrder);
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
