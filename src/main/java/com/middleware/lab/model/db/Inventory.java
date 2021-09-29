package com.middleware.lab.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

@Entity @Getter @Setter @NoArgsConstructor

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "search_inventory",
        procedureName = "lab_nutrition_db.search_inventory",
        parameters = {
          @StoredProcedureParameter(mode=ParameterMode.IN, name="sku", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="warehouse", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="pag", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="num", type=String.class),
    })
})

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer inventory_id;
    @Column
    private String transaction_id;
    private Date timestamp;
    @Column
    private String sku;
    @Column(nullable = false)
    private String warehouse_id;
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
    @Column
    private Integer quantity;

  

}
