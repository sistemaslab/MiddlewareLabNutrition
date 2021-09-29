package com.middleware.provider.orders.model.rest;
//"Domain":"Fulfillment","OrderId":"982300726203-01","State":"waiting-ffmt-authorization","LastState":"on-order-completed-ffm",
//"LastChange":"2019-12-09T22:12:09.6894865Z",
//"CurrentChange":"2019-12-09T22:12:10.5029409Z","Origin":{"Account":"blacksip","Key":"vtexappkey-blacksip-ZANMXH"
public class Origin {
	private String account;     	
	private String key;     
	

    public Origin() {
    }
    
    public Origin( String account,String key) {
        this.account = account;
        this.key = key;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
