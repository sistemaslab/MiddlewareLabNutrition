package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.AutomaticUpdateDate;
import com.middleware.data.annotation.CreatorUser;
import com.middleware.data.annotation.UpdaterUser;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Alejandro Cadena
 */
@Entity
@Getter @Setter
public class Product {
    
    @Id
    private String refId;
    private Integer vtexId;
    private Integer brandId;
    private Integer categoryId;
    private Integer departmentId;
    private String description;
    private String descriptionShort;
    private Boolean isActive;
    private Boolean isVisible;
    private String keyWords;
    private String listStoreId;
    private String metaTagDescription;
    private String name;
    private String title;
    private Boolean showWithoutStock;
    private String linkId;
    private Boolean isCompleteInfo;
    private Boolean isSynchronizedVtex;
    private boolean updateFromDashboard;
    @AutomaticCreationDate private Date createdDate;
    @AutomaticUpdateDate private Date updatedDate;
    @CreatorUser private String createdByUser;
    @UpdaterUser private String updatedByUser;

    public Product() {
        this.updateFromDashboard = false;
    }

    public Product(String refId, Integer vtexId, Integer brandId, Integer categoryId, Integer departmentId, String description, String descriptionShort, Boolean isActive, Boolean isVisible, String keyWords, String listStoreId, String metaTagDescription, String name, String title, Boolean showWithoutStock, String linkId, Boolean isCompleteInfo, Boolean isSynchronizedVtex, boolean updateFromDashboard, Date createdDate, Date updatedDate, String createdByUser, String updatedByUser) {
        this.refId = refId;
        this.vtexId = vtexId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.departmentId = departmentId;
        this.description = description;
        this.descriptionShort = descriptionShort;
        this.isActive = isActive;
        this.isVisible = isVisible;
        this.keyWords = keyWords;
        this.listStoreId = listStoreId;
        this.metaTagDescription = metaTagDescription;
        this.name = name;
        this.title = title;
        this.showWithoutStock = showWithoutStock;
        this.linkId = linkId;
        this.isCompleteInfo = isCompleteInfo;
        this.isSynchronizedVtex = isSynchronizedVtex;
        this.updateFromDashboard = updateFromDashboard;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdByUser = createdByUser;
        this.updatedByUser = updatedByUser;
    }

    
    
    @JsonIgnore 
    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @JsonIgnore 
    public String getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(String updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

}
