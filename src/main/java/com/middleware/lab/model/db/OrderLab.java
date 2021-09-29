package com.middleware.lab.model.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity @Getter @Setter @NoArgsConstructor

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "get_orders",
        procedureName = "lab_nutrition_db.get_orders",
        parameters = {
          @StoredProcedureParameter(mode=ParameterMode.IN, name="id", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="pag", type=int.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="num", type=int.class),
    }),

    @NamedStoredProcedureQuery(
        name = "get_ordersFailed",
        procedureName = "lab_nutrition_db.get_ordersFailed",
        parameters = {
          @StoredProcedureParameter(mode=ParameterMode.IN, name="id", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="pag", type=int.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="num", type=int.class),
    })
})

public class OrderLab {

   @Id
    private String order_num;

   @Column
    private String order_num_lab;
   
    @Column
    private String sequence;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date created_date;    

    @Column(name = "order_date")
    private Date order_date;  
    
    @Column
    private Integer empresa;

    @Column
    private String order_status;

    @Column
    private BigDecimal total_value;
    
    @Column
    private BigDecimal total_off_billing;
    
     @Column
    private BigDecimal total_items;
     
    @Column
    private BigDecimal total_discount;

    @Column
    private int is_synchronized_navasoft;

    @Column
    private boolean is_validate;
      
    @Column
    private boolean is_corporate;
    
    @Column
    private boolean have_sales;
    
    @Column
    private String sales_name;
    
    @Column
    private boolean have_receiver;
    
    @OneToMany(mappedBy = "orderLab", cascade = CascadeType.ALL)
    private List<Items> items;

    @OneToOne(mappedBy = "orderLab", cascade = CascadeType.ALL)
    private Shipping shipping;

    @OneToOne(mappedBy = "orderLab", cascade = CascadeType.ALL)
    private Billing billing;
    
    @OneToOne(mappedBy = "orderLab", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "orderLab", cascade = CascadeType.ALL)
    private Payment payment;

    public boolean isIs_corporate() {
        return is_corporate;
    }

    public void setIs_corporate(boolean is_corporate) {
        this.is_corporate = is_corporate;
    }

    public boolean isIs_validate() {
        return is_validate;
    }

    public void setIs_validate(boolean is_validate) {
        this.is_validate = is_validate;
    }
    
    

  
}
