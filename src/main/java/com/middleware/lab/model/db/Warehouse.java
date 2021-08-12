package com.middleware.lab.model.db;

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
public class Warehouse {

    @Id
    @Column
    private String warehouse_id;
    
    @Column
    private String warehouse_name;
    
   
}
