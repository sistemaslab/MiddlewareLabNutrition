package com.middleware.lab.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer client_id;
    
    @Column
    private String client_code;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String document_type;

    @Column
    private String document;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private boolean isCorporate;
    
    @Column
    private boolean isEmployed;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderLab orderLab;

    public boolean isIsCorporate() {
        return isCorporate;
    }

    public void setIsCorporate(boolean isCorporate) {
        this.isCorporate = isCorporate;
    }

    public boolean isIsEmployed() {
        return isEmployed;
    }

    public void setIsEmployed(boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

  

}
