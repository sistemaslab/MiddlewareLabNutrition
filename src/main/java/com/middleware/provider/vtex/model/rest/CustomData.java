package com.middleware.provider.vtex.model.rest;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CustomData {

    private ArrayList<CustomApps> customApps;     

    public CustomData(ArrayList<CustomApps> customApps) {
        this.customApps = customApps;
    }
   
    
}
