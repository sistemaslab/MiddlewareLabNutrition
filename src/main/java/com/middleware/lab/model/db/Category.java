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
public class Category {

    @Id
    @Column
    private Integer category_id;
    
    @Column
    private Integer department_id;
    
    @Column
    private String category_name;
    
    @Column
    private String department_name;
}
