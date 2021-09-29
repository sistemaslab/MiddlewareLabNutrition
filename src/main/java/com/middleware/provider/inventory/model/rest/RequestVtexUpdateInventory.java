package com.middleware.provider.inventory.model.rest;

public class RequestVtexUpdateInventory {
private String unlimitedQuantity;   
private String dateUtcOnBalanceSystem;   
private String quantity;   
    
    public RequestVtexUpdateInventory() {
    }
    
    public RequestVtexUpdateInventory( String unlimitedQuantity, String dateUtcOnBalanceSystem,String quantity) {
        this.unlimitedQuantity = unlimitedQuantity;
        this.dateUtcOnBalanceSystem = dateUtcOnBalanceSystem;
        this.quantity = quantity;
    }

	public String getUnlimitedQuantity() {
		return unlimitedQuantity;
	}

	public void setUnlimitedQuantity(String unlimitedQuantity) {
		this.unlimitedQuantity = unlimitedQuantity;
	}

	public String getDateUtcOnBalanceSystem() {
		return dateUtcOnBalanceSystem;
	}

	public void setDateUtcOnBalanceSystem(String dateUtcOnBalanceSystem) {
		this.dateUtcOnBalanceSystem = dateUtcOnBalanceSystem;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


    
}
