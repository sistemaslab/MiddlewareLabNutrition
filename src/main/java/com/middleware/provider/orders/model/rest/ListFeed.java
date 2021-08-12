package com.middleware.provider.orders.model.rest;
//"Domain":"Fulfillment","OrderId":"982300726203-01","State":"waiting-ffmt-authorization","LastState":"on-order-completed-ffm",
//"LastChange":"2019-12-09T22:12:09.6894865Z",
//"CurrentChange":"2019-12-09T22:12:10.5029409Z","Origin":{"Account":"blacksip","Key":"vtexappkey-blacksip-ZANMXH"
public class ListFeed {
	private String eventId;     	
	private String handle;     
	private String domain;     
	private String state;     
	private String lastState;     
	private String orderId;     
	private String lastChange;     
	private String currentChange;     
	private String availableDate;     


    public ListFeed() {
    }
    
    public ListFeed( String eventId,String handle,String domain,String state,
    		String lastState,String orderId,String lastChange,String currentChange,
    		String availableDate) {
        this.eventId = eventId;
        this.handle = handle;
        this.domain = domain;
        this.state = state;
        this.lastState = lastState;
        this.orderId = orderId;
        this.lastChange = lastChange;
        this.currentChange = currentChange;
        this.availableDate = availableDate;
    }

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(String availableDate) {
		this.availableDate = availableDate;
	}

    
}
