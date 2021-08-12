package com.middleware.lab.model.db;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter @Setter @NoArgsConstructor

public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer billing_id;
    @Column
    private String business_name;

    @Column
    private String business_doc;
 
    @Column
    private String business_phone;
    
    @Column
    private String invoice_number;

    @Column
    private BigDecimal invoice_value;

    @Column
    private Date issuance_date;
    
    @Column
    private String url_tracking;

    @Column
    private String address;
    
    @Column
    private boolean is_billing;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date created_date;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modify_date;
    
     @OneToOne
    @JoinColumn(name = "order_id")
    private OrderLab orderLab;

    public boolean isIs_billing() {
        return is_billing;
    }

    public void setIs_billing(boolean is_billing) {
        this.is_billing = is_billing;
    }

    
}
