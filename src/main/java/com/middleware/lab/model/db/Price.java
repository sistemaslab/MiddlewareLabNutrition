package com.middleware.lab.model.db;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "get_prices",
            procedureName = "lab_nutrition_db.get_prices",
            parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "id", type = String.class),})
})

public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer price_id;
    @Column
    private String transaction_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;
    @Column
    private String sku;
    @Column
    private String currency;
    @Column
    private BigDecimal base_price;
    @Column
    private BigDecimal list_price;
    @Column
    private int is_synchronized_vtex;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date created_date;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date update_date;

}
