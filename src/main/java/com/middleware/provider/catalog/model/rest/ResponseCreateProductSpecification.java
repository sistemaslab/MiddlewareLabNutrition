package com.middleware.provider.catalog.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseCreateProductSpecification {

    private List<String> value;
    private Integer id;
    private String name;
}
