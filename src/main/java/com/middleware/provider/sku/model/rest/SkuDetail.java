package com.middleware.provider.sku.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)

@Getter @Setter @NoArgsConstructor
public class SkuDetail implements Serializable{
	private String Id;     	
	private String NameComplete;   	
	private String ProductName;     	
	private String ProductDescription;     	
	private String ProductRefId;     	
	private Boolean IsActive;     	
	private String ImageUrl;     	
	private String DetailUrl;     	
	private String BrandId;     	
	private String BrandName;       
	private String SkuName;       
	private AlternateIds AlternateIds;       

    public SkuDetail(String Id, String NameComplete, String ProductName, String ProductDescription, String ProductRefId, Boolean IsActive, String ImageUrl, String DetailUrl, String BrandId, String BrandName, String SkuName, AlternateIds AlternateIds) {
        this.Id = Id;
        this.NameComplete = NameComplete;
        this.ProductName = ProductName;
        this.ProductDescription = ProductDescription;
        this.ProductRefId = ProductRefId;
        this.IsActive = IsActive;
        this.ImageUrl = ImageUrl;
        this.DetailUrl = DetailUrl;
        this.BrandId = BrandId;
        this.BrandName = BrandName;
        this.SkuName = SkuName;
        this.AlternateIds = AlternateIds;
    }

   
        
	

}
