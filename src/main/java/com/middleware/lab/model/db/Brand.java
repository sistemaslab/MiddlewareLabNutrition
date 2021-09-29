package com.middleware.lab.model.db;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Brand {

    @Id
    @Column
    private Integer brand_id;
    
    @Column
    private String brand_name;
    
   
}
