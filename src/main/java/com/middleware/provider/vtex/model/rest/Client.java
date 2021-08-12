package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class Client {

    private String email;     
    private String id;     
    private String document;     
    private String gender;     
    private String phone;     
    private String firstName;     
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-5")        
    private Date birthDate;     
    private boolean isEmployed;     

    public Client(String email, String id, String document, String gender, String phone, String firstName, String lastName, Date birthDate, boolean isEmployed) {
        this.email = email;
        this.id = id;
        this.document = document;
        this.gender = gender;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.isEmployed = isEmployed;
    }

    public boolean isIsEmployed() {
        return isEmployed;
    }

    public void setIsEmployed(boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    

}
