package com.middleware.lab.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VtexCredential {
	 
    private String appKey;
    private String appToken;

    public VtexCredential(String appKey, String appToken) {
        this.appKey = appKey;
        this.appToken = appToken;
    }

}
