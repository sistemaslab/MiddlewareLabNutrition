package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.AutomaticUpdateDate;
import com.middleware.data.annotation.CreatorUser;
import com.middleware.data.annotation.Id;
import com.middleware.data.annotation.Ignore;
import com.middleware.data.annotation.UpdaterUser;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Alejandro Cadena
 */
public class FixedPrice {
    
    @Id(autoIncrement = true)
    private Long id;
    private String sapId;
    private BigDecimal value;
    private BigDecimal listPrice;
    private String tradePolicyId;
    private BigDecimal minQuantity;
    @Ignore private DateRange dateRange;
    @AutomaticCreationDate private Date createdDate;
    @AutomaticUpdateDate private Date updatedDate;
    @CreatorUser private String createdByUser;
    @UpdaterUser private String updatedByUser;

    public FixedPrice() {
    }

    public FixedPrice(Long id, String sapId, BigDecimal value, BigDecimal listPrice, String tradePolicyId, BigDecimal minQuantity, DateRange dateRange, Date createdDate, Date updatedDate, String createdByUser, String updatedByUser) {
        this.id = id;
        this.sapId = sapId;
        this.value = value;
        this.listPrice = listPrice;
        this.tradePolicyId = tradePolicyId;
        this.minQuantity = minQuantity;
        this.dateRange = dateRange;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdByUser = createdByUser;
        this.updatedByUser = updatedByUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public String getTradePolicyId() {
        return tradePolicyId;
    }

    public void setTradePolicyId(String tradePolicyId) {
        this.tradePolicyId = tradePolicyId;
    }

    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
    }
    
    public DateRange getDateRange() {
		return dateRange;
	}

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
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
        return "FixedPrice{" 
                + "id=" + id 
                + ", sapId=" + sapId 
                + ", value=" + value 
                + ", listPrice=" + listPrice 
                + ", tradePolicyId=" + tradePolicyId 
                + ", minQuantity=" + minQuantity 
                + ", dateRange=" + dateRange 
                 
                
                 
                 
                + '}';
    }
    
}
