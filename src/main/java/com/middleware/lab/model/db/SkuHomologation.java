package com.middleware.lab.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sku", "refId"}))

public class SkuHomologation {

    @Id
    @Column(unique = true)
    private String sku;
    @Column(unique = true)
    private String refId;

    public SkuHomologation(String sku, String refId) {
        this.sku = sku;
        this.refId = refId;
    }

}
