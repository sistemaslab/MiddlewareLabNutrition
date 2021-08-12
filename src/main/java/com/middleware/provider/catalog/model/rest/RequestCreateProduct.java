package com.middleware.provider.catalog.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestCreateProduct {

    private String Name;
    private Integer DepartmentId;
    private Integer CategoryId;
    private Integer BrandId;
    private String LinkId;
    private String RefId;
    private boolean IsVisible;
    private String Description;
    private String DescriptionShort;
    private String ReleaseDate;
    private String KeyWords;
    private String Title;
    private boolean IsActive;
    private String TaxCode;
    private String MetaTagDescription;
    private BigDecimal SupplierId;
    private boolean ShowWithoutStock;
    private String AdWordsRemarketingCode;
    private String LomadeeCampaignCode;
    private BigDecimal Score;
}
