package com.middleware.provider.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.orders.model.rest.ResponseOrderFailedComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderSearch;
import com.middleware.provider.orders.model.rest.ResponseOrderSearchComplete;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import java.io.IOException;
import java.util.List;

public interface OrdersBs {

    public void setHook(String hook);

    public void updateTicket();
    
    public void updateDelivered();
    
    public boolean updateTicketById(String orderId);

    public void validateInvoice();

    public ResponseOrderSearchComplete searchOrders(int pag, int num, String filter, String value);

    public ResponseOrderFailedComplete searchOrdersFailed(int pag, int num, String filter, String value);

    public List<ResponseOrderSearch> searchOrder(String numPedido);
    
    public ResponseOrderVtex sendRequestOrderToVtex(String numPedido) throws ServiceConnectionException, JsonProcessingException, IOException, ModelMappingException ;

}
