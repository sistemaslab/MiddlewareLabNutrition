package com.middleware.provider.orders.model.rest;
//"Domain":"Fulfillment","OrderId":"982300726203-01","State":"waiting-ffmt-authorization","LastState":"on-order-completed-ffm",
//"LastChange":"2019-12-09T22:12:09.6894865Z",
//"CurrentChange":"2019-12-09T22:12:10.5029409Z","Origin":{"Account":"blacksip","Key":"vtexappkey-blacksip-ZANMXH"
public class RequestHook {
	private String domain;     	
	private String orderId;     
	private String state;     
	private String lastState;     
	private String lastChange;     
	private String currentChange;     
	private Origin origin;     


    public RequestHook() {
    }
    
    public RequestHook( String domain,String orderId,String state,String lastState,
    		String lastChange,String currentChange,Origin origin) {
        this.domain = domain;
        this.orderId = orderId;
        this.state = state;
        this.lastState = lastState;
        this.lastChange = lastChange;
        this.currentChange = currentChange;
        this.origin = origin;
    }

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLastState() {
		return lastState;
	}

	public void setLastState(String lastState) {
		this.lastState = lastState;
	}

	public String getLastChange() {
		return lastChange;
	}

	public void setLastChange(String lastChange) {
		this.lastChange = lastChange;
	}

	public String getCurrentChange() {
		return currentChange;
	}

	public void setCurrentChange(String currentChange) {
		this.currentChange = currentChange;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

    @Override
    public String toString() {
        return "RequestHook{" + "domain=" + domain + ", orderId=" + orderId + ", state=" + state + ", lastState=" + lastState + ", lastChange=" + lastChange + ", currentChange=" + currentChange + ", origin=" + origin + '}';
    }

	

	




    
}
