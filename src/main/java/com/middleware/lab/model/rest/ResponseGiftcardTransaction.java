
package com.middleware.lab.model.rest;


/**
 *
 * @author Natalia Manrique
 */
public class ResponseGiftcardTransaction {
    private String cardId;     	
    private String transactionId;     	

    public ResponseGiftcardTransaction() {
    }

    public ResponseGiftcardTransaction(String cardId, String transactionId) {
        this.cardId = cardId;
        this.transactionId = transactionId;
    }

    public String getRedemptionCode() {
        return cardId;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.cardId = redemptionCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
   
   

}
