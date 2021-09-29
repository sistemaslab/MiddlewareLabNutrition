package com.middleware.lab.model.db;

import java.math.BigDecimal;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer item_id;

    @Column
    private String sku;
    
    @Column
    private String item_cod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id_FK", nullable = false, updatable = false)
    private OrderLab orderLab;

    @Column
    private String name;
    
    @Column
    private String measurement_unit;
    
    @Column
    private boolean is_sales;
    
    @Column
    private String sales_name;
    
    @Column
    private boolean is_gift;
    
    @Column
    private BigDecimal discount;
    
    @Column
    private BigDecimal unit_price;
    
    @Column
    private BigDecimal selling_price;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal total_price;

    public boolean isIs_sales() {
        return is_sales;
    }

    public void setIs_sales(boolean is_sales) {
        this.is_sales = is_sales;
    }

    public boolean isIs_gift() {
        return is_gift;
    }

    public void setIs_gift(boolean is_gift) {
        this.is_gift = is_gift;
    }

    
}
