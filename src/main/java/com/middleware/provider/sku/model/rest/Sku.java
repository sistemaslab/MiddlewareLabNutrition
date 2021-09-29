package com.middleware.provider.sku.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)

@Getter @Setter @NoArgsConstructor
public class Sku implements Serializable{
	private String sku;       	
	
}
