package com.middleware.provider.bs;

import com.middleware.provider.orders.model.rest.ResponseOrderFailedComplete;
import com.middleware.provider.orders.model.rest.ResponseOrderSearch;
import com.middleware.provider.orders.model.rest.ResponseOrderSearchComplete;
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

}
