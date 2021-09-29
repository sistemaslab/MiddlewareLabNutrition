package com.middleware.lab.model.db;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod {

    @Id
    private Integer payment_method_id;

    @Column
    private String name_vtex;

    @Column 
    private String system_vtex;

    @Column
    private String type_lab;
    
    @Column
    private String method_lab;

    
    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    private List<Payment> payment;
      
      
}
