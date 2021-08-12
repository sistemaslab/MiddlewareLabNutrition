package com.middleware.provider.inventory.model.rest;
import java.math.BigInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ResponseInventoryLab {
    
	private String Error;
	private String codtie;
	private String codalm;
	private String codf;	
	private Integer stoc;	

    public ResponseInventoryLab(String Error, String codtie, String codalm, String codf, Integer stoc) {
        this.Error = Error;
        this.codtie = codtie;
        this.codalm = codalm;
        this.codf = codf;
        this.stoc = stoc;
    }

        
   
}
