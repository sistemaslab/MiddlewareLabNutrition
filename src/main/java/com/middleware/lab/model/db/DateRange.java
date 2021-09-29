package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.Id;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Alejandro Cadena
 */
@Getter @Setter
public class DateRange {
    
    @Id(autoIncrement = true)
    private Long id;
    private Long fixedPriceId;
    private Date fromDate;
    private Date toDate;
    @AutomaticCreationDate
    private Date createdDate;

    public DateRange() {
    }

    public DateRange(Long fixedPriceId, Date fromDate, Date toDate) {
        this.fixedPriceId = fixedPriceId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public DateRange(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @JsonIgnore 
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "DateRange{" + "id=" + id + ", fixedPriceId=" + fixedPriceId + ", fromDate=" + fromDate + ", toDate=" + toDate  + '}';
    }
    
}
