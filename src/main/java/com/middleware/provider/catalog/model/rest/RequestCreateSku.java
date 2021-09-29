package com.middleware.provider.catalog.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestCreateSku {
    private Integer ProductId;
    private boolean IsActive;
    private String Name;
    private String RefId;
    private BigDecimal PackagedHeight;
    private BigDecimal PackagedLength;
    private BigDecimal PackagedWidth;
    private BigDecimal PackagedWeightKg;
    private BigDecimal Height;
    private BigDecimal Length;
    private BigDecimal Width;
    private BigDecimal WeightKg;
    private BigDecimal CubicWeight;
    private boolean IsKit;
    private String CreationDate;
    private BigDecimal RewardValue;
    private String EstimatedDateArrival;
    private String stockKeepinUnitEans;
    private Integer CommercialConditionId;
    private String MeasurementUnit;
    private BigDecimal UnitMultiplier;
    private String ModalType;
    private boolean KitItensSellApart;
    }
