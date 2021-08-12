package com.middleware.provider.vtex.model.rest;

public class ClientProfileData {

    private String email;     
    private String firstName;     
    private String lastName;     
    private String document;     
    private String documentType;     
    private String userProfileId;     
    private String phone;   
    private String corporateName;   
    private String tradeName;   
    private String corporateDocument;   
    private String stateInscription;   
    private String corporatePhone;   
    private boolean isCorporate;   
    
    public ClientProfileData() {
    }

    public ClientProfileData(String email, String firstName, String lastName, String document, String documentType, String userProfileId, String phone, String corporateName, String tradeName, String corporateDocument, String stateInscription, String corporatePhone, boolean isCorporate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.documentType = documentType;
        this.userProfileId = userProfileId;
        this.phone = phone;
        this.corporateName = corporateName;
        this.tradeName = tradeName;
        this.corporateDocument = corporateDocument;
        this.stateInscription = stateInscription;
        this.corporatePhone = corporatePhone;
        this.isCorporate = isCorporate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCorporateDocument() {
        return corporateDocument;
    }

    public void setCorporateDocument(String corporateDocument) {
        this.corporateDocument = corporateDocument;
    }

    public String getStateInscription() {
        return stateInscription;
    }

    public void setStateInscription(String stateInscription) {
        this.stateInscription = stateInscription;
    }

    public String getCorporatePhone() {
        return corporatePhone;
    }

    public void setCorporatePhone(String corporatePhone) {
        this.corporatePhone = corporatePhone;
    }

    public boolean isIsCorporate() {
        return isCorporate;
    }

    public void setIsCorporate(boolean isCorporate) {
        this.isCorporate = isCorporate;
    }

}
