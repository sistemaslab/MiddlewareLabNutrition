package com.middleware.lab.model.db;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor

public class VtexLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String orderId;
    @Column
    private String wsName;
    @Column
    private String request;
    @Column
    private String response;
    @Column
    private Date createdDate;
    @Column
    private String createdByUser;

    public VtexLog(String orderId, String wsName, String request, String response) {
        this.orderId = orderId;
        this.wsName = wsName;
        this.request = request;
        this.response = response;
    }

}
