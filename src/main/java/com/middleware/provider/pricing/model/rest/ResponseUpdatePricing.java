package com.middleware.provider.pricing.model.rest;

public class ResponseUpdatePricing {
	private String transactionId;     
	private String message;     
	private String codeId;     
    
    public ResponseUpdatePricing() {
    }
    
    public ResponseUpdatePricing( String transactionId,String message,String codeId) {
        this.transactionId = transactionId;
        this.message = message;
        this.codeId = codeId;
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	

    
}
