package com.middleware.lab.model.db;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @Column
    private String payment_id;

    @Column
    private String cart_number;

    @Column
    private String cart_tid;
     
    @Column
    private BigDecimal value;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date created_date;
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderLab orderLab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id_FK", nullable=false, updatable=false)
    private PaymentMethod paymentMethod;

  
}
