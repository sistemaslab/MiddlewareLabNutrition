package com.middleware.provider.catalog.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseSearchWarehouses {

    private String id;
    private String name;
    private boolean isActive;

}
