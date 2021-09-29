package com.middleware.lab.data.mapper;

import com.middleware.lab.model.db.InventoryLog;
import com.middleware.lab.model.db.PriceLog;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.provider.inventory.model.rest.ResponseInventoryLab;
import com.middleware.provider.pricing.model.rest.FailedUpdatePricing;
import com.middleware.provider.pricing.model.rest.ProductPricing;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import com.middleware.provider.pricing.model.rest.RequestVtexUpdatePricing;
import com.middleware.provider.pricing.model.rest.ResponsePricingLab;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lab.navasoft.model.soap.Getpreciolista;

public class MapperPrices {

    public static RequestVtexUpdatePricing toVtexRequestPricing(ProductPricing productPricing) {
        RequestVtexUpdatePricing requestVtexUpdatePricing = new RequestVtexUpdatePricing();
        requestVtexUpdatePricing.setBasePrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setCostPrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setListPrice(productPricing.getListPrice());

        return requestVtexUpdatePricing;
    }

    public static RequestUpdatePricing toRequestPricing(ArrayList<ResponsePricingLab> responsePricingLab) {
        RequestUpdatePricing requestUpdatePricing = new RequestUpdatePricing();
        List<ProductPricing> prices = new ArrayList<ProductPricing>();
        Date date = new Date();

        for (ResponsePricingLab priceLab : responsePricingLab) {
            ProductPricing product = new ProductPricing();
            product.setBasePrice(priceLab.getPreu());
            product.setListPrice(priceLab.getPreu());
            product.setCurrency("PEN");
            product.setTimestamp(date);
            product.setProductId(priceLab.getCodf().trim());
            prices.add(product);
        }
        
        requestUpdatePricing.setPrices(prices);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        String strDate= formatter.format(date);  
        requestUpdatePricing.setTransactionId(strDate);
        
        return requestUpdatePricing;
    }

    public static FailedUpdatePricing toPricingFailed(ProductPricing productPricing,
            RequestUpdatePricing requestUpdatePricing, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setCode("W-004");
        failedUpdatePricing.setTransactionId(requestUpdatePricing.getTransactionId());
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        failedUpdatePricing.setProduct(productPricing);
        return failedUpdatePricing;
    }

    public static FailedUpdatePricing toPricingFailedWithoutproduct(RequestUpdatePricing requestUpdatePricing, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setCode("W-004");
        failedUpdatePricing.setTransactionId(requestUpdatePricing.getTransactionId());
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        return failedUpdatePricing;
    }

    public static FailedUpdatePricing toPricingFailedSku(String productId, String transactionId, String msj) {
        FailedUpdatePricing failedUpdatePricing = new FailedUpdatePricing();
        failedUpdatePricing.setTransactionId(transactionId);
        failedUpdatePricing.setTransactionType("pricing");
        failedUpdatePricing.setMessage(msj);
        failedUpdatePricing.setCode("W-008");
        ProductPricing productPricing = new ProductPricing();
        productPricing.setProductId(productId);
        failedUpdatePricing.setProduct(productPricing);

        return failedUpdatePricing;
    }

    public static Sku pricingToSku(ProductPricing productPricing) {
        Sku sku = new Sku();
        sku.setProductId(productPricing.getProductId());
        return sku;
    }

   public static Getpreciolista toPricesLab(String skus) {
        Getpreciolista getpreciolista = new Getpreciolista();
        getpreciolista.setItems(skus);
        getpreciolista.setTipo("json");
        return getpreciolista;

    }
   
   
    public static PriceLog toPriceLog(SkuHomologation skuHomologation, ResponsePricingLab responsePricingLab) {
        PriceLog priceLog = new PriceLog();
        priceLog.setError(responsePricingLab.getError());
        priceLog.setSku(skuHomologation.getRefId());
        return priceLog;

    }

}
