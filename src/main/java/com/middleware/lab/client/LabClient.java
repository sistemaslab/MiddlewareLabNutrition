package com.middleware.lab.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.orders.model.rest.RequestCreateOrder;
import com.middleware.provider.orders.model.rest.RequestCreateUser;
import com.middleware.provider.orders.model.rest.ResponseCreateOrder;
import com.middleware.provider.sku.model.rest.ResponseAllSkus;

/**
 *
 * @author Natalia Manrique
 */
public interface LabClient {

    public boolean sendFailure(Object failedUpdateInventory, String url) throws ServiceConnectionException, JsonProcessingException;

    public ResponseCreateOrder sendOrderToCoolbox(RequestCreateOrder requestCreateOrder, String url) throws ServiceConnectionException, JsonProcessingException;

    public ResponseCreateOrder sendClientToCoolbox(RequestCreateUser requestCreateUser, String url) throws ServiceConnectionException, JsonProcessingException;

    public ResponseAllSkus getSkus(String url) throws ServiceConnectionException, JsonProcessingException;

}
