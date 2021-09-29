package com.middleware.lab.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middleware.data.annotation.AutomaticCreationDate;
import com.middleware.data.annotation.AutomaticUpdateDate;
import com.middleware.data.annotation.Id;
import java.util.Date;

/**
 *
 * @author Alejandro Cadena
 */
public class AuthUserRole {
    
    @Id(autoIncrement = true)
    private Long id;
    private String username;
    private String role;
    @JsonIgnore @AutomaticCreationDate private Date createdDate;
    @JsonIgnore @AutomaticUpdateDate private Date updatedDate;

    public AuthUserRole() {
    }

    public AuthUserRole(String username, String role) {
        this.username = username;
        this.role = role;
    }
    
    @JsonIgnore 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @JsonIgnore 
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
