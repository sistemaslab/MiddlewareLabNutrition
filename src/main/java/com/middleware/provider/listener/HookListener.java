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
import com.middleware.provider.bs.OrdersBs;
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
    private OrdersBs ordersBs;

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

  

    private static final Logger LOG = Logger.getLogger(HookListener.class.getName());

    @JmsListener(destination = "HookQueue")

    public void receiveMessage(String msj)
            throws ModelMappingException, IOException, ServiceConnectionException, JmsException, JsonProcessingException, blacksip.vtex.client.exception.ServiceConnectionException {

        LOG.log(Level.INFO, "Received <{0}>", msj);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestHook requestHook = objectMapper.readValue(msj, RequestHook.class);
        if (requestHook.getOrderId() != null) {
            ResponseOrderVtex responseOrder = ordersBs.sendRequestOrderToVtex(requestHook.getOrderId());


        }

    }


}
