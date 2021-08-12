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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer shipping_id;
    @Column
    private String tracking_number;
    @Column
    private boolean is_pickup;
    @Size(min=0, max=3, message = "Debe tener maximo 3 caracteres")
    @Column
    private String pickup_id;
    @Column
    private String pickup_name;
    @Column
    private String tracking_url;
    @Column
    private String courier;
    @Column
    private String codigo_postal;
    @Column
    private String ref_dir;
    @Column
    private String complement;
    @Column
    private String receiver_name;
    @Column
    private String receiver_document;
    @Column
    private Date shipping_date;
    @Column
    private BigDecimal shipping_price;
    @Column
    private String warehouse;
    @Column
    private String delivery_range;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderLab orderLab;

    public boolean isIs_pickup() {
        return is_pickup;
    }

    public void setIs_pickup(boolean is_pickup) {
        this.is_pickup = is_pickup;
    }

 
}
