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
 * @author Natalia Manrique
 */
@Entity
@Getter @Setter
public class Sku {
    
    @Id
    private String refId;
    private Integer id;
    private String productId;
    private String name;
    private Boolean isActive;
    private Boolean isAvailable;
    private Boolean isKit;
    private BigDecimal realWeightKg;
    private BigDecimal realLength;
    private BigDecimal realWidth;
    private BigDecimal realHeight;
    private BigDecimal rewardValue;
    private String stockKeepinUnitEans;
    private String color;
    private String tamano;
    private String attachments;
    private Boolean isCompleteInfo;
    private Boolean isSynchronizedVtex;
    private boolean updateFromDashboard;
    @AutomaticCreationDate private Date createdDate;
    @AutomaticUpdateDate private Date updatedDate;
    @CreatorUser private String createdByUser;
    @UpdaterUser private String updatedByUser;

    public Sku() {
        this.updateFromDashboard = false;
    }

    public Sku(String refId, Integer id, String productId, String name, Boolean isActive, Boolean isAvailable, Boolean isKit, BigDecimal realWeightKg, BigDecimal realLength, BigDecimal realWidth, BigDecimal realHeight, BigDecimal rewardValue, String stockKeepinUnitEans, String attachments, Boolean isCompleteInfo, Boolean isSynchronizedVtex, boolean updateFromDashboard, Date createdDate, Date updatedDate, String createdByUser, String updatedByUser) {
        this.refId = refId;
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.isActive = isActive;
        this.isAvailable = isAvailable;
        this.isKit = isKit;
        this.realWeightKg = realWeightKg;
        this.realLength = realLength;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.rewardValue = rewardValue;
        this.stockKeepinUnitEans = stockKeepinUnitEans;
        this.attachments = attachments;
        this.isCompleteInfo = isCompleteInfo;
        this.isSynchronizedVtex = isSynchronizedVtex;
        this.updateFromDashboard = updateFromDashboard;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdByUser = createdByUser;
        this.updatedByUser = updatedByUser;
    }

    @JsonIgnore 
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonIgnore 
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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

    @Override
    public String toString() {
        return "Sku{" + "refId=" + refId + ", id=" + id + ", productId=" + productId + ", name=" + name + ", isActive=" + isActive + ", isAvailable=" + isAvailable + ", isKit=" + isKit + ", realWeightKg=" + realWeightKg + ", realLength=" + realLength + ", realWidth=" + realWidth + ", realHeight=" + realHeight + ", refId=" + refId + ", rewardValue=" + rewardValue + ", stockKeepinUnitEans=" + stockKeepinUnitEans + ", attachments=" + attachments + ", isCompleteInfo=" + isCompleteInfo + ", isSynchronizedVtex=" + isSynchronizedVtex + ", updateFromDashboard=" + updateFromDashboard + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", createdByUser=" + createdByUser + ", updatedByUser=" + updatedByUser + '}';
    }

}
