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
import java.util.List;

/**
 *
 * @author Alejandro Cadena
 */
public class PriceDetail {
    
    @Id
    private String sapId;
    private BigDecimal listPrice;
    private BigDecimal costPrice;
    private BigDecimal markup;
    @Ignore private List<FixedPrice> fixedPrices;
    private Boolean isSynchronizedVtex;
    @AutomaticCreationDate private Date createdDate;
    @AutomaticUpdateDate private Date updatedDate;
    @CreatorUser private String createdByUser;
    @UpdaterUser private String updatedByUser;

    public PriceDetail() {
    }

    public PriceDetail(String sapId, BigDecimal listPrice, BigDecimal costPrice, 
            BigDecimal markup, Boolean isSynchronizedVtex, Date createdDate, 
            Date updatedDate, String createdByUser, String updatedByUser) {
        this.sapId = sapId;
        this.listPrice = listPrice;
        this.costPrice = costPrice;
        this.markup = markup;
        this.isSynchronizedVtex = isSynchronizedVtex;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdByUser = createdByUser;
        this.updatedByUser = updatedByUser;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }

    public List<FixedPrice> getFixedPrices() {
        return fixedPrices;
    }

    public void setFixedPrices(List<FixedPrice> fixedPrices) {
        this.fixedPrices = fixedPrices;
    }

    public Boolean getIsSynchronizedVtex() {
        return isSynchronizedVtex;
    }

    public void setIsSynchronizedVtex(Boolean isSynchronizedVtex) {
        this.isSynchronizedVtex = isSynchronizedVtex;
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
        return "PriceDetail{" 
                + "sapId=" + sapId 
                + ", listPrice=" + listPrice 
                + ", costPrice=" + costPrice 
                + ", markup=" + markup 
                + ", fixedPrices=" + fixedPrices
                + '}';
    }
    
}
