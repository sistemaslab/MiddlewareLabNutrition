package com.middleware.lab.data.mapper;

import com.middleware.lab.model.db.InventoryLog;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.provider.inventory.model.rest.ProductInventory;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.inventory.model.rest.ResponseInventoryLab;
import com.middleware.provider.pricing.model.rest.FailedUpdatePricing;
import com.middleware.provider.pricing.model.rest.ProductPricing;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import com.middleware.provider.pricing.model.rest.RequestVtexUpdatePricing;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lab.navasoft.model.soap.Getstockalm;

public class MapperInventory {

    public static RequestVtexUpdatePricing toVtexRequestPricing(ProductPricing productPricing) {
        RequestVtexUpdatePricing requestVtexUpdatePricing = new RequestVtexUpdatePricing();
        requestVtexUpdatePricing.setBasePrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setCostPrice(productPricing.getBasePrice());
        requestVtexUpdatePricing.setListPrice(productPricing.getListPrice());

        return requestVtexUpdatePricing;
    }

    public static RequestUpdateInventory toRequestInventory(ArrayList<ResponseInventoryLab> responseInventoryLab, String job) {
        RequestUpdateInventory requestUpdateInventory = new RequestUpdateInventory();
        List<ProductInventory> products = new ArrayList<ProductInventory>();
        Date date = new Date();

        for (ResponseInventoryLab stockLab : responseInventoryLab) {
            ProductInventory product = new ProductInventory();
            if (stockLab.getCodtie().equals("011")) {
                product.setProductId(stockLab.getCodf().trim());
                product.setQuantity(stockLab.getStoc());
                product.setTimestamp(date);
                product.setWarehouse(stockLab.getCodtie());
                products.add(product);
                product = new ProductInventory();
                product.setProductId(stockLab.getCodf().trim());
                product.setQuantity(stockLab.getStoc());
                product.setTimestamp(date);
                product.setWarehouse("015");
                products.add(product);
                product = new ProductInventory();
            } else if (stockLab.getCodtie().equals("015")) {
                //No lo crea
            } else {
                product.setProductId(stockLab.getCodf().trim());
                product.setQuantity(stockLab.getStoc());
                product.setTimestamp(date);
                product.setWarehouse(stockLab.getCodtie());
                products.add(product);
                product = new ProductInventory();
            }
        }

        requestUpdateInventory.setProduct(products);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        requestUpdateInventory.setTransactionId(job + " " + strDate);

        return requestUpdateInventory;
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

    public static Getstockalm toInventoryLab(String skus) {
        Getstockalm getstockalm = new Getstockalm();
        getstockalm.setItems(skus);
        getstockalm.setTipo("json");
        return getstockalm;

    }

    public static InventoryLog toInventoryLog(SkuHomologation skuHomologation, String responseInventoryLab) {
        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setError(responseInventoryLab);
        inventoryLog.setSku(skuHomologation.getRefId());
        return inventoryLog;

    }
    
    
}
