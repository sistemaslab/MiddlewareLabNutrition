package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.AutomaticUpdateDate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Natalia Manrique
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderId;
    private String detailMessage;
    private Date createdDate;

    public OrderLog(Long id, String orderId, String detailMessage, Date createdDate) {
        this.id = id;
        this.orderId = orderId;
        this.detailMessage = detailMessage;
        this.createdDate = createdDate;
    }
   

   

}
