package com.middleware.lab.data.mapper;

import com.middleware.lab.model.db.Billing;
import com.middleware.lab.model.db.Brand;
import com.middleware.lab.model.db.Category;

import com.middleware.lab.model.db.Inventory;
import com.middleware.lab.model.db.OrderLab;
import com.middleware.lab.model.db.Price;
import com.middleware.lab.model.db.Shipping;
import com.middleware.lab.model.db.Payment;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.Client;
import com.middleware.lab.model.db.Items;
import com.middleware.lab.model.db.OrderLog;
import com.middleware.lab.model.db.PaymentMethod;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.provider.inventory.model.rest.FailedUpdateInventory;
import com.middleware.provider.pricing.model.rest.FailedUpdatePricing;
import com.middleware.provider.inventory.model.rest.ProductInventory;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.inventory.model.rest.RequestVtexUpdateInventory;
import com.middleware.provider.inventory.model.rest.ResponseSearchInventory;
import com.middleware.provider.orders.model.rest.RequestTicket;
import com.middleware.provider.orders.model.rest.RequestTicketLab;
import com.middleware.provider.orders.model.rest.RequestTracking;
import com.middleware.provider.orders.model.rest.RequestTrackingEvents;
import com.middleware.provider.orders.model.rest.ResponseOrder;
import com.middleware.provider.orders.model.rest.ResponseOrderStatusLab;
import com.middleware.provider.pricing.model.rest.ProductPricing;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import com.middleware.provider.pricing.model.rest.RequestVtexUpdatePricing;
import com.middleware.provider.pricing.model.rest.ResponseSearchPricing;
import com.middleware.provider.vtex.model.rest.Components;
import com.middleware.provider.vtex.model.rest.CustomApps;
import com.middleware.provider.vtex.model.rest.DeliveryIds;
import com.middleware.provider.vtex.model.rest.LogisticsInfo;
import com.middleware.provider.vtex.model.rest.PaymentsOrder;
import com.middleware.provider.vtex.model.rest.PriceTags;
import com.middleware.provider.vtex.model.rest.RateAndBenefitsIdentifiers;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import com.middleware.provider.vtex.model.rest.Totals;
import com.middleware.provider.vtex.model.rest.Transactions;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lab.navasoft.model.soap.Getitems;
import lab.navasoft.model.soap.Getnewitems;
import lab.navasoft.model.soap.Getpreciolista;
import lab.navasoft.model.soap.Getstatuspedido;
import lab.navasoft.model.soap.Loadbilldata;

public class Mapper {

    public static RequestVtexUpdateInventory toVtexRequest(String quantity) {
        RequestVtexUpdateInventory requestVtexUpdateInventory = new RequestVtexUpdateInventory();
        requestVtexUpdateInventory.setQuantity(quantity);
        requestVtexUpdateInventory.setDateUtcOnBalanceSystem("");
        requestVtexUpdateInventory.setUnlimitedQuantity("False");
        return requestVtexUpdateInventory;
    }

    public static RequestVtexUpdatePricing toVtexRequestPricing(ProductPricing productPricing) {
        RequestVtexUpdatePricing requestVtexUpdatePricing = new RequestVtexUpdatePricing();
        requestVtexUpdatePricing.setBasePrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setCostPrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setListPrice(productPricing.getListPrice());

        return requestVtexUpdatePricing;
    }

    public static FailedUpdateInventory toInventoryFailed(ProductInventory productInventory,
            RequestUpdateInventory requestUpdateInventory, String msj) {
        FailedUpdateInventory failedUpdateInventory = new FailedUpdateInventory();
        failedUpdateInventory.setTransactionId(requestUpdateInventory.getTransactionId());
        failedUpdateInventory.setCode("W-005");
        failedUpdateInventory.setTransactionType("inventory");
        failedUpdateInventory.setMessage(msj);
        failedUpdateInventory.setProduct(productInventory);
        return failedUpdateInventory;
    }

    public static FailedUpdateInventory toInventoryFailedWithoutproduct(
            RequestUpdateInventory requestUpdateInventory, String msj) {
        FailedUpdateInventory failedUpdateInventory = new FailedUpdateInventory();
        failedUpdateInventory.setCode("W-003");
        failedUpdateInventory.setTransactionId(requestUpdateInventory.getTransactionId());
        failedUpdateInventory.setTransactionType("inventory");
        failedUpdateInventory.setMessage(msj);
        return failedUpdateInventory;
    }

    public static FailedUpdateInventory toInventoryFailedSku(String productId, String transactionId, String msj) {
        FailedUpdateInventory failedUpdateInventory = new FailedUpdateInventory();
        failedUpdateInventory.setTransactionId(transactionId);
        failedUpdateInventory.setTransactionType("inventory");
        failedUpdateInventory.setCode("W-007");
        failedUpdateInventory.setMessage(msj);
        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(productId);
        failedUpdateInventory.setProduct(productInventory);
        return failedUpdateInventory;
    }

    public static ResponseSearchInventory inventoryToResponseSearchInventory(Inventory inventory) {
        ResponseSearchInventory responseSearch = new ResponseSearchInventory();
        responseSearch.setTimestamp(inventory.getTimestamp());
        responseSearch.setQuantity(inventory.getQuantity());
        responseSearch.setWarehouse(inventory.getWarehouse_id());
        //    Sku sku = inventory.getSku();
        //    responseSearch.setProduct_id(sku.getId().toString());
        return responseSearch;
    }

    public static ResponseSearchPricing inventoryToResponseSearchPricing(Price price) {
        ResponseSearchPricing responseSearch = new ResponseSearchPricing();
        responseSearch.setBase_price(price.getBase_price());
        responseSearch.setList_price(price.getList_price());
        //     Sku sku = price.getSku();
        return responseSearch;
    }

    public static FailedUpdatePricing toPricingFailed(ProductPricing productPricing,
            RequestUpdatePricing requestUpdatePricing, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setCode("W-004");
        failedUpdatePricing.setTransactionId(requestUpdatePricing.getTransactionId());
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        failedUpdatePricing.setProduct(productPricing);
        return failedUpdatePricing;
    }

    public static FailedUpdatePricing toPricingFailedWithoutproduct(RequestUpdatePricing requestUpdatePricing, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setCode("W-004");
        failedUpdatePricing.setTransactionId(requestUpdatePricing.getTransactionId());
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        return failedUpdatePricing;
    }

    public static FailedUpdatePricing toPricingFailedSku(String productId, String transactionId, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setTransactionId(transactionId);
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        failedUpdatePricing.setCode("W-008");
        ProductPricing productPricing = new ProductPricing();
        productPricing.setProductId(productId);
        failedUpdatePricing.setProduct(productPricing);

        return failedUpdatePricing;
    }

    public static Sku pricingToSku(ProductPricing productPricing) {
        Sku sku = new Sku();
        sku.setProductId(productPricing.getProductId());
        return sku;
    }

    public static Sku inventoryToSku(String productId, String skuId) throws ParseException {
        Sku sku = new Sku();
        sku.setProductId(productId);
        return sku;
    }

    public static OrderLog toOrderLog(String msg, String orderId) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId);
        orderLog.setDetailMessage(msg);
        Date date = new Date();
        orderLog.setCreatedDate(date);

        return orderLog;
    }

    public static OrderLog toOrderLogSKU(String orderId) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId);
        orderLog.setDetailMessage("Error al homologar SKU");
        Date date = new Date();
        orderLog.setCreatedDate(date);

        return orderLog;
    }

    public static OrderLab toOrderDB(ResponseOrderVtex responseOrder) {
        //Mapear el objeto para la base de datos.

        OrderLab orderLab = new OrderLab();
        orderLab.setOrder_num(responseOrder.getOrderId());
        orderLab.setSequence(responseOrder.getSequence());
        orderLab.setOrder_date(responseOrder.getCreationDate());
        orderLab.setEmpresa(1);
        orderLab.setOrder_status(responseOrder.getStatus());
        orderLab.setTotal_value(setPoint(responseOrder.getValue()));
        orderLab.setIs_synchronized_navasoft(0);
        String orderId = responseOrder.getOrderId().substring(1, 13);
        orderLab.setOrder_num_lab(orderId);
        orderLab.setIs_validate(false);
        if (responseOrder.getClientProfileData().isIsCorporate()) {
            orderLab.setIs_corporate(true);
            if (responseOrder.getCustomData() != null) {
                if (responseOrder.getCustomData().getCustomApps() != null) {
                    List<CustomApps> listCustomApps = responseOrder.getCustomData().getCustomApps();
                    for (CustomApps customApp : listCustomApps) {
                        if (customApp.getId().equals("receptor")) {
                            orderLab.setHave_receiver(true);
                        } else {
                            orderLab.setHave_receiver(false);
                        }
                    }
                }
            } else {
                orderLab.setHave_receiver(false);

            }

        } else {
            orderLab.setIs_corporate(false);
            if (responseOrder.getCustomData() == null) {
                orderLab.setHave_receiver(false);
            } else {
                orderLab.setHave_receiver(true);
            }
        }
        if (responseOrder.getRatesAndBenefitsData().getRateAndBenefitsIdentifiers().isEmpty()) {
            orderLab.setHave_sales(false);
        } else {
            String name = "";
            for (RateAndBenefitsIdentifiers object : responseOrder.getRatesAndBenefitsData().getRateAndBenefitsIdentifiers()) {
                name = name + object.getName() + " ";
            }
            orderLab.setHave_sales(true);
            orderLab.setSales_name(name);
        }
        orderLab.setIs_synchronized_navasoft(0);
        List<Totals> total = responseOrder.getTotals();
        for (Totals totals : total) {
            if (totals.getId().equals("Discounts")) {
                if (totals.getValue() == BigDecimal.ZERO) {
                    orderLab.setHave_sales(false);
                }
                orderLab.setTotal_discount(setPoint(totals.getValue()));
            }
            if (totals.getId().equals("Items")) {
                orderLab.setTotal_items(setPoint(totals.getValue()));
            }
            if (totals.getId().equals("Shipping")) {
                orderLab.setTotal_off_billing(setPoint(responseOrder.getValue().subtract(totals.getValue())));
            }
        }
        return orderLab;
    }

    public static Shipping toShippingOrder(ResponseOrderVtex responseOrder, OrderLab order) {
        Shipping shipping = new Shipping();
        if (responseOrder.getShippingData().getAddress().getAddressType().equals("pickup")) {
            System.out.println("ENTRE");
            shipping.setIs_pickup(true);
            shipping.setPickup_id(responseOrder.getShippingData().getAddress().getAddressId());
            shipping.setPickup_name(responseOrder.getShippingData().getAddress().getStreet());
        }
        if (order.isHave_receiver()) {
            List<CustomApps> listCustomApps = responseOrder.getCustomData().getCustomApps();
            for (CustomApps customApp : listCustomApps) {
                if (customApp.getId().equals("receptor")) {
                    shipping.setReceiver_document(customApp.getFields().getDocument());
                    shipping.setReceiver_name(customApp.getFields().getFirstName() + " " + customApp.getFields().getLastName());
                }
            }
        } else {
            shipping.setReceiver_name(responseOrder.getShippingData().getAddress().getReceiverName());
        }
        List<LogisticsInfo> logistic = responseOrder.getShippingData().getLogisticsInfo();
        for (LogisticsInfo logisticsInfoItem : logistic) {
            shipping.setShipping_date(logisticsInfoItem.getShippingEstimateDate());
            if (!shipping.isIs_pickup()) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                TimeZone timezone = TimeZone.getTimeZone("UTC-5");
                format.setTimeZone(timezone);
                if (logisticsInfoItem.getDeliveryWindow() != null) {
                    System.out.println("ANTES" + logisticsInfoItem.getDeliveryWindow().getStartDateUtc() + " " + logisticsInfoItem.getDeliveryWindow().getEndDateUtc());
                    String startDate = format.format(logisticsInfoItem.getDeliveryWindow().getStartDateUtc());
                    String endDate = format.format(logisticsInfoItem.getDeliveryWindow().getEndDateUtc());
                    shipping.setDelivery_range(startDate + " - " + endDate);
                    System.out.println("HORA; " + startDate + " - " + endDate);
                }
                List<Totals> totals = responseOrder.getTotals();
                for (Totals total : totals) {
                    if (total.getId().equals("Shipping")) {
                        shipping.setShipping_price(setPoint(total.getValue()));
                    }
                }
                List<DeliveryIds> delivery = logisticsInfoItem.getDeliveryIds();
                for (DeliveryIds deliveryIds : delivery) {
                    shipping.setWarehouse(deliveryIds.getWarehouseId());
                }

            } else {
                shipping.setShipping_price(BigDecimal.ZERO);

            }
            shipping.setCourier(logisticsInfoItem.getDeliveryCompany());
            shipping.setRef_dir((responseOrder.getShippingData().getAddress().getNumber() == null) ? responseOrder.getShippingData().getAddress().getStreet() : responseOrder.getShippingData().getAddress().getStreet() + " " + responseOrder.getShippingData().getAddress().getNumber());
            shipping.setComplement(((responseOrder.getShippingData().getAddress().getComplement() == null) ? "" : responseOrder.getShippingData().getAddress().getComplement()) + " " + ((responseOrder.getShippingData().getAddress().getNeighborhood() == null) ? "" : responseOrder.getShippingData().getAddress().getNeighborhood()) + " " + ((responseOrder.getShippingData().getAddress().getCity() == null) ? "" : responseOrder.getShippingData().getAddress().getCity()) + " " + ((responseOrder.getShippingData().getAddress().getCountry() == null) ? "" : responseOrder.getShippingData().getAddress().getCountry()));

        }
        shipping.setCodigo_postal(responseOrder.getShippingData().getAddress().getPostalCode());
        shipping.setLatitude(responseOrder.getShippingData().getAddress().getGeoCoordinates()[1]);
        shipping.setLongitude(responseOrder.getShippingData().getAddress().getGeoCoordinates()[0]);
        System.out.println("LA SHIPI" + responseOrder.getShippingData().getAddress().getAddressType());

        shipping.setOrderLab(order);
        return shipping;

    }

    public static Payment toPaymentOrder(ResponseOrderVtex responseOrder, PaymentMethod paymentMethod, OrderLab order) {

        Payment payment = new Payment();
        List<PaymentsOrder> paymentsVtex = new ArrayList<PaymentsOrder>();

        List<Transactions> transactions = responseOrder.getPaymentData().getTransactions();
        for (Transactions transaction : transactions) {
            paymentsVtex = transaction.getPayments();
            System.out.println("payymyents" + paymentsVtex.size());
            for (PaymentsOrder paymentsOrder : paymentsVtex) {
                if (paymentsOrder.getPaymentSystem().equals("16")) {
                    //Si es gifcard
                    payment.setCart_number(paymentsOrder.getGiftCardId());
                } else {
                    if (paymentMethod.getMethod_lab().equals("2")) {
                        payment.setCart_number(paymentsOrder.getFirstDigits() + "******" + paymentsOrder.getLastDigits());
                        payment.setCart_tid(paymentsOrder.getTid());
                    }
                }
                payment.setPayment_id(paymentsOrder.getId());
                payment.setValue(setPoint(responseOrder.getValue()));
                payment.setPaymentMethod(paymentMethod);
            }
        }
        payment.setOrderLab(order);
        return payment;

    }

    public static Billing toBillingOrder(ResponseOrderVtex responseOrder, OrderLab order) {

        Billing billing = new Billing();
        if (responseOrder.getClientProfileData().isIsCorporate()) {
            billing.setBusiness_doc(responseOrder.getClientProfileData().getCorporateDocument());
            billing.setBusiness_name(responseOrder.getClientProfileData().getCorporateName());
            billing.setBusiness_phone(responseOrder.getClientProfileData().getCorporatePhone());
            if (responseOrder.getCustomData() != null) {
                if (responseOrder.getCustomData().getCustomApps() != null) {
                    List<CustomApps> listCustomApps = responseOrder.getCustomData().getCustomApps();
                    for (CustomApps customApp : listCustomApps) {
                        if (customApp.getId().equals("additionalinfofacture")) {
                            billing.setAddress(customApp.getFields().getFiscalAddress());
                        }
                    }
                }
            }
        }
        billing.setIs_billing(false);
        billing.setOrderLab(order);
        return billing;
    }

    public static Client toClientOrder(ResponseOrderVtex responseOrder, OrderLab order, String email, boolean employed) {
        Client client = new Client();
        client.setClient_code(responseOrder.getClientProfileData().getUserProfileId());
        client.setFirst_name(responseOrder.getClientProfileData().getFirstName());
        client.setLast_name(responseOrder.getClientProfileData().getLastName());
        client.setDocument_type(responseOrder.getClientProfileData().getDocumentType());
        System.out.println("let" + responseOrder.getClientProfileData().getDocument().length());
        if (responseOrder.getClientProfileData().getDocument().length() > 8) {
            client.setDocument(responseOrder.getClientProfileData().getDocument().substring(1));
        } else {
            client.setDocument(responseOrder.getClientProfileData().getDocument());
        }
        String values[] = splitTel(responseOrder.getClientProfileData().getPhone(), 2);
        client.setPhone(values[0]);
        client.setEmail(email);
        client.setIsCorporate(responseOrder.getClientProfileData().isIsCorporate());
        client.setIsEmployed(employed);
        client.setOrderLab(order);
        return client;
    }

    public static Items toItems(com.middleware.provider.vtex.model.rest.Items item, OrderLab orderLab, ResponseOrderVtex responseOrder, String sku) {
        Items itemsDb = new Items();

        List<RateAndBenefitsIdentifiers> rateAndBenefitsIdentifiers = responseOrder.getRatesAndBenefitsData().getRateAndBenefitsIdentifiers();
        for (RateAndBenefitsIdentifiers rateAndBenefitsIdentifier : rateAndBenefitsIdentifiers) {
            HashMap<String, String> map = rateAndBenefitsIdentifier.getMatchedParameters();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("key" + key + "value" + value);
                String[] values = value.split("[|]|,");
                for (String value1 : values) {
                    System.out.println("EL VALOR QUE COMPARARA" + value1);
                    if (item.getId().equals(value1) || item.getProductId().equals(value1)) {
                        itemsDb.setIs_sales(true);
                        itemsDb.setSales_name(rateAndBenefitsIdentifier.getName());
                    }
                }
            }
        }
        List<PriceTags> priceTags = item.getPriceTags();
        if (!priceTags.isEmpty()) {
            for (PriceTags priceTag : priceTags) {
                System.out.println("PT" + priceTag.toString());
                if (priceTag.getIdentifier() == null) {
                    if (itemsDb.getDiscount() == null) {
                        itemsDb.setDiscount(BigDecimal.ZERO);
                    }
                } else {
                    String cadena = priceTag.getName();
                    int resultado = cadena.indexOf("shipping");
                    System.out.println("INT " + resultado);
                    if (itemsDb.getDiscount() == null) {
                        if (resultado == -1) {
                            System.out.println("ENTRE A -1");
                            itemsDb.setDiscount(setPoint(priceTag.getValue()));

                        } else {
                            System.out.println("NO ENTRE");
                            itemsDb.setDiscount(BigDecimal.ZERO);
                        }
                    } else {
                        if (resultado == -1) {
                            System.out.println("ENTRE A -1");
                            itemsDb.setDiscount(itemsDb.getDiscount().add(setPoint(priceTag.getValue())));

                        }
                    }

                }
            }
        } else {
            itemsDb.setDiscount(BigDecimal.ZERO);
        }

        itemsDb.setSku(item.getId());
        itemsDb.setItem_cod(sku);

        itemsDb.setMeasurement_unit(item.getMeasurementUnit());
        System.out.println(
                "name" + item.getName());
        itemsDb.setName(item.getName());
        itemsDb.setQuantity(item.getQuantity());
        itemsDb.setUnit_price(setPoint(item.getCostPrice()));
        itemsDb.setSelling_price(setPoint(item.getSellingPrice()));
        itemsDb.setIs_gift(item.isIsGift());
        itemsDb.setTotal_price(setPoint(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity().intValue())));
        itemsDb.setOrderLab(orderLab);
        return itemsDb;

    }

    public static Items toItemsComponent(com.middleware.provider.vtex.model.rest.Items item, Components components, ResponseOrderVtex responseOrder, OrderLab orderLab, SkuHomologation sku) {
        Items itemsDb = new Items();
        List<RateAndBenefitsIdentifiers> rateAndBenefitsIdentifiers = responseOrder.getRatesAndBenefitsData().getRateAndBenefitsIdentifiers();

        for (RateAndBenefitsIdentifiers rateAndBenefitsIdentifier : rateAndBenefitsIdentifiers) {
            HashMap<String, String> map = rateAndBenefitsIdentifier.getMatchedParameters();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("key" + key + "value" + value);
                String[] values = value.split("[|]|,");
                for (String value1 : values) {
                    System.out.println("EL VALOR QUE COMPARARA" + value1);
                    if (item.getId().equals(value1) || item.getProductId().equals(value1)) {
                        itemsDb.setIs_sales(true);
                        itemsDb.setSales_name(rateAndBenefitsIdentifier.getName());
                    }
                }
            }
        }
        List<PriceTags> priceTags = components.getPriceTags();
        System.out.println("pt" + priceTags.toString());
        if (priceTags.isEmpty()) {
            itemsDb.setIs_sales(false);
            itemsDb.setDiscount(BigDecimal.ZERO);

        } else {
            itemsDb.setIs_sales(true);
            for (PriceTags priceTag : priceTags) {
                System.out.println("PT" + priceTag.toString());
                if (priceTag.getIdentifier() == null) {
                    if (itemsDb.getDiscount() == null) {
                        itemsDb.setDiscount(BigDecimal.ZERO);
                    }
                } else {
                    itemsDb.setDiscount(setPoint(priceTag.getValue()));
                }
            }
        }

        itemsDb.setSku(components.getId());
        itemsDb.setItem_cod(sku.getRefId());
        itemsDb.setMeasurement_unit(components.getMeasurementUnit());
        itemsDb.setName(components.getName());
        itemsDb.setQuantity(components.getQuantity());
        itemsDb.setUnit_price((components.getPrice()));
        itemsDb.setSelling_price(setPoint(components.getSellingPrice()));
        itemsDb.setIs_gift(false);
        itemsDb.setTotal_price(((components.getPrice())).multiply(BigDecimal.valueOf(components.getQuantity().intValue())));
        itemsDb.setOrderLab(orderLab);
        return itemsDb;

    }

    public static Items toItemsComponentPromo(Components item, com.middleware.provider.vtex.model.rest.Items item2, OrderLab orderLab, BigDecimal total, SkuHomologation sku) {
        Items itemsDb = new Items();
        itemsDb.setIs_sales(true);
        itemsDb.setSales_name("Combo " + item2.getName());
        itemsDb.setDiscount(((total.subtract(setPoint(item2.getPrice()))).multiply(BigDecimal.valueOf(-1))));
        itemsDb.setSku(item.getId());
        itemsDb.setItem_cod(sku.getRefId());
        itemsDb.setMeasurement_unit(item.getMeasurementUnit());
        itemsDb.setName(item.getName());
        itemsDb.setQuantity(item.getQuantity());
        itemsDb.setUnit_price((item.getPrice()));
        itemsDb.setSelling_price(setPoint(item.getSellingPrice()));
        itemsDb.setIs_gift(false);
        itemsDb.setTotal_price((item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity().intValue())));
        itemsDb.setOrderLab(orderLab);
        return itemsDb;

    }

    public static Loadbilldata toOrderLab(String client, String orderId, String f) {
        Loadbilldata orden = new Loadbilldata();
        orden.setCdocu("32");
        orden.setF(f);
        String[] orderI = orderId.split("-");
        orden.setFileName(client + "-32-" + orderI[0]);
        orden.setTipo("json");
        return orden;
    }

    public static ResponseOrder toResponseOrder(OrderLab orderCoolbox, List<Items> items, Client client, Shipping shipping, Billing billing) {
        ResponseOrder responseOrder = new ResponseOrder();
        responseOrder.setNumPedido(orderCoolbox.getOrder_num());
        responseOrder.setCodCliente(client.getDocument());
        responseOrder.setUbigeo(shipping.getCodigo_postal());
        responseOrder.setDireccion(shipping.getRef_dir());
        responseOrder.setRefdireccion(shipping.getRef_dir());
        responseOrder.setEstadoPedido(orderCoolbox.getOrder_status());
        responseOrder.setFechaPedido(orderCoolbox.getCreated_date());
        responseOrder.setNomPersonaEntregar(shipping.getReceiver_name());
        if (client.isIsCorporate()) {
            responseOrder.setNumDocumento(billing.getBusiness_doc());
            responseOrder.setRazonSocial(billing.getBusiness_name());
            responseOrder.setDireccionFiscal(billing.getAddress());
        }
        responseOrder.setMontoaPagar(orderCoolbox.getTotal_value());
        responseOrder.setTelefono(client.getPhone());
        if (shipping.isIs_pickup()) {
            responseOrder.setTiendaRecojo(shipping.getPickup_name());

        }
        responseOrder.setFechaEntrega(shipping.getShipping_date());
        List<com.middleware.provider.orders.model.rest.Items> listItemC = new ArrayList<com.middleware.provider.orders.model.rest.Items>();

        com.middleware.provider.orders.model.rest.Items itemC = new com.middleware.provider.orders.model.rest.Items();
        for (Items item : items) {
            itemC.setId(item.getItem_cod());
            itemC.setPrice(item.getTotal_price());
            itemC.setQuantity(item.getQuantity());
            listItemC.add(itemC);
            itemC = new com.middleware.provider.orders.model.rest.Items();
        }
        responseOrder.setItems(listItemC);
        return responseOrder;

    }

    public static SkuHomologation toSku(String sku, String refId) {
        SkuHomologation skuHomologation = new SkuHomologation();
        skuHomologation.setRefId(refId);
        skuHomologation.setSku(sku);
        return skuHomologation;

    }

    public static Brand toBrand(Integer brandId, String name) {
        Brand brand = new Brand();
        brand.setBrand_id(brandId);
        brand.setBrand_name(name);
        return brand;
    }

    public static Category toCategory(Integer categoryId, String name, Integer departmentId, String departmentName) {
        Category category = new Category();
        category.setCategory_id(categoryId);
        category.setCategory_name(name);
        category.setDepartment_id(departmentId);
        category.setDepartment_name(departmentName);
        return category;
    }

    public static String toDNI(String doc) {

        String cadena = "";
        int longitud = 11 - doc.length();
        for (int i = 0; i < longitud; i++) {
            cadena = cadena + "0";
        }
        cadena = cadena + doc;
        return cadena;
    }

    public static RequestTicket toRequestTicket(RequestTicketLab requestTicketLab, OrderLab order) {
        RequestTicket requestTicket = new RequestTicket();
        requestTicket.setInvoiceNumber(requestTicketLab.getInvoiceNumber());
        requestTicket.setInvoiceValue(removePoint(order.getTotal_value()));
        requestTicket.setIssuanceDate(requestTicketLab.getIssuanceDate());

        return requestTicket;

    }

    public static RequestTicketLab toInvoicedVtex(ResponseOrderStatusLab response) throws ParseException {
        RequestTicketLab requestTicketLab = new RequestTicketLab();
        requestTicketLab.setInvoiceNumber(response.getNrofac());
        if (!response.getUrltracking().isEmpty() || response.getUrltracking() != null) {
            requestTicketLab.setTrackingUrl(response.getUrltracking());
            requestTicketLab.setTrackingNumber(response.getNrofac());
        }
        requestTicketLab.setTrackingUrl(response.getNrofac());
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        requestTicketLab.setIssuanceDate(formatter1.parse(response.getFecfac()));
        return requestTicketLab;

    }

    public static RequestTracking toTrackingVtex(Billing billing, ResponseOrderStatusLab responseOrderStatus, boolean delivered) throws ParseException {
        RequestTracking requestTracking = new RequestTracking();
        if (!responseOrderStatus.getUrltracking().isEmpty() || responseOrderStatus.getUrltracking() != null) {
            requestTracking.setTrackingUrl(responseOrderStatus.getUrltracking());
            requestTracking.setTrackingNumber(billing.getInvoice_number());

        }

        return requestTracking;

    }

    public static RequestTrackingEvents toRequestTrackingEvents(boolean delivered) throws ParseException {
        RequestTrackingEvents requestTrackingEvents = new RequestTrackingEvents();
        requestTrackingEvents.setIsDelivered(delivered);

        return requestTrackingEvents;

    }

    public static Getstatuspedido toInvoicedLab(String orderId) {
        Getstatuspedido getstatuspedido = new Getstatuspedido();
        getstatuspedido.setNroped(orderId);
        getstatuspedido.setTipo("json");
        return getstatuspedido;

    }

    public static Getnewitems toProductsLab(int dias) {
        Getnewitems getnewitems = new Getnewitems();
        getnewitems.setUltdias(dias);
        getnewitems.setTipo("json");
        return getnewitems;

    }

    public static Getitems toProductLab(String productId) {
        Getitems getitems = new Getitems();
        getitems.setItems(productId);
        getitems.setTipo("json");
        return getitems;

    }

    public static BigDecimal getBase(BigDecimal total) {
        BigDecimal b = new BigDecimal("0.82");
        return total.multiply(b);
    }

    public static BigDecimal getIvg(BigDecimal total) {
        BigDecimal b = new BigDecimal("0.18");
        return total.multiply(b);
    }

    public static BigDecimal setPoint(BigDecimal num) {
        return num.movePointLeft(2);
    }

    public static BigDecimal removePoint(BigDecimal num) {
        return num.movePointRight(2);
    }

    public static String[] splitTel(String tel, int len) {

        String values[] = new String[2];
        if (tel.contains("+")) {
            System.out.println("");
            values[0] = tel.substring(len + 1);
            values[1] = tel.substring(0, len + 1);
        } else {
            values[0] = tel.substring(len);
            values[1] = "+" + tel.substring(0, len);
        }

        System.out.println(values[0] + " " + values[1]);

        return values;
    }

}
