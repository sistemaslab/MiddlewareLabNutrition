package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.AutomaticUpdateDate;
import com.middleware.data.annotation.CreatorUser;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Natalia Manrique
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class TraceabilityLog {
    
    @Id()
    private Long id;
    @CreatorUser String username;
    private String component;
    private String traceName;
    private String objectRefId;
    private String urlService;
    private String requestBody;
    private Date requestDate;
    private String responseBody;
    private Date responseDate;
    private String statusCode;
    private String message;
    private String detailedMessage;
    @AutomaticCreationDate private Date createdDate;
    @AutomaticUpdateDate private Date updatedDate;

    
    public TraceabilityLog(String component, 
            String traceName, String objectRefId, String urlService, String requestBody, 
            Date requestDate, String responseBody, Date responseDate, 
            String statusCode, String message, String detailedMessage) {
        this.traceName = traceName;
        this.objectRefId = objectRefId;
        this.urlService = urlService;
        this.requestBody = requestBody;
        this.requestDate = requestDate;
        this.responseBody = responseBody;
        this.responseDate = responseDate;
        this.statusCode = statusCode;
        this.message = message;
        this.detailedMessage = detailedMessage;
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

    @Override
    public String toString() {
        return "TraceabilityLog{" 
                + "id=" + id 
                + ", username=" + username 
                + ", component=" + component 
                + ", traceName=" + traceName 
                + ", objectRefId=" + objectRefId 
                + ", requestBody=" + requestBody 
                + ", requestDate=" + requestDate 
                + ", responseBody=" + responseBody 
                + ", responseDate=" + responseDate 
                + ", statusCode=" + statusCode 
                + ", message=" + message 
                + ", detailedMessage=" + detailedMessage 
                 
                
                + '}';
    }
    
}
