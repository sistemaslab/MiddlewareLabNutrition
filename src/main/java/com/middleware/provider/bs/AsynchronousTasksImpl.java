package com.middleware.provider.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.model.db.Inventory;
import com.middleware.lab.model.db.Price;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.Warehouse;
import com.middleware.lab.repository.InventoryRepository;
import com.middleware.lab.repository.PricingRepository;
import com.middleware.lab.repository.SkuRepository;
import com.middleware.lab.repository.WarehouseRepository;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.FailedUpdateInventory;
import com.middleware.provider.inventory.model.rest.ProductInventory;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.inventory.model.rest.RequestVtexUpdateInventory;
import com.middleware.provider.pricing.model.rest.FailedUpdatePricing;
import com.middleware.provider.pricing.model.rest.ProductPricing;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import com.middleware.provider.pricing.model.rest.RequestVtexUpdatePricing;
import com.middleware.provider.sku.model.rest.SkuDetail;
import com.middleware.provider.sku.model.rest.SkuList;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.middleware.lab.client.LabClient;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.SkuHomologationRepository;

/**
 * Spring service to handle asynchronous Rest API calls.
 *
 * @author Jonathan Briceno
 * @edited Natalia Manrique
 */
@Service("asynchronousTasksImpl")
public class AsynchronousTasksImpl implements AsynchronousTasks {

    private static final Logger LOG = Logger.getLogger(AsynchronousTasksImpl.class.getName());

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private SkuHomologationRepository skuHomologationRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Value("${lab.accountName}")
    private String accountName;

    @Value("${vtex.url.inventory}")
    private String urlInventory;

    @Value("${vtex.url.inventoryEnd}")
    private String inventoryEnd;

    @Autowired
    LabClient labClient;

    @Autowired
    private PricingRepository pricingRepository;

    /**
     *
     *
     * @throws InterruptedException
     * @throws ServiceConnectionException
     * @throws IOException
     * @throws ModelMappingException
     */
    @Override
    @Async
    public void UpdateSkus() throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String actualDate = formatter.format(date);

        String logMessage = "[ " + actualDate + " ]" + "NuevoHilo: callbackCallerTask INI " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        String url = accountName + "/api/catalog_system/pvt/sku/stockkeepingunitids?page=1&pagesize=10000000";
        ResponseEntity<String> rep = vtexApiRestClient.getAllSkus(url);
        String sku = rep.getBody();
        SkuList[] skus = objectMapper.readValue(sku, SkuList[].class);

        // Armar la URL
        ArrayList<String> refIds = new ArrayList<String>();

        for (SkuList skulist : skus) {
            String urlref = accountName + "/api/catalog_system/pvt/sku/stockkeepingunitbyid/" + skulist.getSkus().toString();
            SkuDetail skuDetail = vtexApiRestClient.getRefBySku(urlref);
            try {
                skuRepository.save(Mapper.inventoryToSku(skuDetail.getProductRefId(), skulist.getSkus().toString()));
            } catch (ParseException ex) {
                Logger.getLogger(AsynchronousTasksImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            refIds.add(skuDetail.getProductRefId());
            System.out.println("SKU: " + skulist.getSkus().toString() + " REF: " + skuDetail.getProductRefId());

        }

        actualDate = formatter.format(date);
        logMessage = "[ " + actualDate + " ]" + "NuevoHilo:  callbackCallerTask fin " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);
        LOG.log(Level.WARNING, "callbackCallerTask - FIN {0}", System.currentTimeMillis());

    }

    @Async
    @Override
    public void UpdatePricing(RequestUpdatePricing requestUpdatePricing) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String actualDate = formatter.format(date);

        String logMessage = "[ " + actualDate + " ]" + "NuevoHilo: UpdatePricing INI " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);
        for (ProductPricing prices : requestUpdatePricing.getPrices()) {
            process(prices, requestUpdatePricing);
        }
        actualDate = formatter.format(date);
        logMessage = "[ " + actualDate + " ]" + "NuevoHilo:  UpdatePricing fin " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);
        LOG.log(Level.WARNING, "UpdatePricing - FIN {0}", System.currentTimeMillis());

    }

    @Async
    private void process(ProductPricing prices, RequestUpdatePricing requestUpdatePricing) throws ServiceConnectionException, JsonProcessingException, ModelMappingException, IOException {
        Object result = skuHomologationRepository.findByIdProduct(prices.getProductId());

        if (result != null) {
            SkuHomologation sku = skuHomologationRepository.findByIdProduct(prices.getProductId());
            String url = sku.getSku();

            Integer id = SavePricing(prices, requestUpdatePricing, url);
            // VALIDAR EL TIMESTAMP
            if (id != 0) {
                // Armar la URL
                if (sendPricingToVtex(url, prices, id).getStatusCode() != HttpStatus.OK) {
                    // Si no se puede cargar a VTEX en tres intentos
                    LOG.info("ERROR EN VTEX - No se realizo el update pricing en VTEX");
                    String response = sendPricingToVtex(url, prices, id).getBody();
//                        logRepository.save(Mapper.getLogSendPricingVtex(url, prices, id, response, requestUpdatePricing.getTransactionId()));
                    // No se pudo enviar a VTEX y se notifica a Coolbox
                    FailedUpdatePricing failedUpdatePricing = Mapper.toPricingFailed(prices,
                            requestUpdatePricing, "Supero intentos envio VTEX");
                }
            }
        } else {
            System.out.println("NO HAY SKU");
            LOG.info("Error de SKU - El SKU  que se va a actualizar el precio no existe");
            //           logRepository.save(Mapper.getLogSendPricingVtexIdNull(prices.getProductId(), requestUpdatePricing.getTransactionId(), requestUpdatePricing));
            // No se pudo enviar a VTEX y se notifica a Coolbox
            FailedUpdatePricing failedUpdatePricing = Mapper.toPricingFailedSku(prices.getProductId(),
                    requestUpdatePricing.getTransactionId(), "ERROR DE SKU - Sku no existe");
        }
    }

    private Integer SavePricing(ProductPricing product, RequestUpdatePricing requestUpdatePricing, String sku) throws ServiceConnectionException, JsonProcessingException {
        // Guardar la transaccion de actualización de precio
        Integer id_Product = 0;
        Price price = new Price();
        BigDecimal zero = new BigDecimal("0");

        try {
            System.out.print(product.getBasePrice().compareTo(zero) + " VALOR WRONG");
            if (product.getBasePrice().compareTo(zero) == 1) {

                price.setBase_price(product.getBasePrice());
                price.setList_price(product.getListPrice());
                price.setCurrency(product.getCurrency());
                price.setTimestamp(product.getTimestamp());
                price.setTransaction_id(requestUpdatePricing.getTransactionId());
                price.setSku(sku);
                Date date = new Date(System.currentTimeMillis());
                System.out.println(date);
                price.setCreated_date(date);

                pricingRepository.save(price);
                id_Product = price.getPrice_id();
                System.out.println("SAVE " + price.getPrice_id());
            } else {
                LOG.info("ERROR EN PRECIO - Notificación");
//                logRepository.save(Mapper.getLogPriceZero(product));
                FailedUpdatePricing failedUpdatePricing = Mapper.toPricingFailed(product,
                        requestUpdatePricing, "W-009 / Error en precio");
            }

        } catch (Exception e) {
            System.out.println("Timestamp repetido");
            LOG.info("ERROR EN EL TIMESTAMP PRICING- Notificación");
//            logRepository.save(Mapper.getLogTimestamp(product, requestUpdatePricing.getTransactionId()));
            FailedUpdatePricing failedUpdatePricing = Mapper.toPricingFailed(product,
                    requestUpdatePricing, "W-006 / Error en el Timestamp");

        }
        return id_Product;
    }

    private ResponseEntity<String> sendPricingToVtex(String url, ProductPricing product, Integer id)
            throws ServiceConnectionException, ModelMappingException, IOException {
        System.out.print("llamado a VTEX");
        RequestVtexUpdatePricing requestVtexUpdatePricing = Mapper.toVtexRequestPricing(product);
        ResponseEntity<String> response = null;
        boolean res = false;
        String urlP;

        urlP = "https://api.vtex.com/labnutrition/pricing/prices/" + url;
        response = vtexApiRestClient.setPricingBySKU(urlP, requestVtexUpdatePricing);
        if (response.getStatusCode() == HttpStatus.OK) {
            res = true;
        }

        if (res) {
            // Se debe actualizar en la BD el estado a actualizado
            System.out.print("ACTUALIZO" + id);
            Price price = pricingRepository.findById(id).orElse(null);
            price.setIs_synchronized_vtex(1);
            pricingRepository.save(price);
            System.out.print("ACTUALIZO");
        }
        return response;
    }

    @Override
    @Async
    public void UpdateInventory(RequestUpdateInventory requestUpdateInventory) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String actualDate = formatter.format(date);

        String logMessage = "[ " + actualDate + " ]" + "NuevoHilo: UpdateInventory INI " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);
        for (ProductInventory product : requestUpdateInventory.getProduct()) {
            processI(product, requestUpdateInventory);
        }
        actualDate = formatter.format(date);
        logMessage = "[ " + actualDate + " ]" + "NuevoHilo:  UpdateInventory fin " + " Thread : " + Thread.currentThread().getId();
        LOG.warning(logMessage);
        LOG.log(Level.WARNING, "UpdateInventory - FIN {0}", System.currentTimeMillis());

    }

    @Async
    private void processI(ProductInventory product, RequestUpdateInventory requestUpdateInventory) throws ServiceConnectionException, JsonProcessingException, ModelMappingException, IOException {
        Object result = skuHomologationRepository.findByIdProduct(product.getProductId());

        if (result != null) {
            SkuHomologation sku = skuHomologationRepository.findByIdProduct(product.getProductId());
            // Armar la URL
            Integer id = SaveInventory(product, requestUpdateInventory, sku);
            Warehouse warehouse = warehouseRepository.findByIdWarehouse(product.getWarehouse());
            if (warehouse != null) {
                String url = urlInventory + sku.getSku().toString() + "/warehouses/" + warehouse.getWarehouse_id() + inventoryEnd;
                String response = "";

                if (sendInventoryToVtex(url, product.getQuantity().toString(), id, accountName)
                        .getStatusCode() != HttpStatus.OK) {
                    LOG.info("ERROR EN VTEX - No se realizo el update inventory en VTEX");
                    response = sendInventoryToVtex(url, product.getQuantity().toString(), id, accountName).getBody();
                    // No se pudo enviar a VTEX y se notifica a Coolbox
                    FailedUpdateInventory failedUpdateInventory = Mapper.toInventoryFailed(product,
                            requestUpdateInventory, "W-003 / Supero intentos envio VTEX");

                }
            } else {
                LOG.info("ERROR EN DB - ERROR DE WAREHOUSE - El WAREHOUSE  que se va a actualizar el inventario no existe  ");
                // No se pudo enviar a VTEX y se notifica a Coolbox
                FailedUpdateInventory failedUpdateInventory = Mapper.toInventoryFailedSku(product.getProductId(),
                        requestUpdateInventory.getTransactionId(), "W-010 / ERROR DE WAREHOUSE - Warehouse no existe");

            }

        } else {
            LOG.info("ERROR EN DB - ERROR DE SKU - El SKU  que se va a actualizar el inventario no existe ");
            // No se pudo enviar a VTEX y se notifica a Coolbox
            FailedUpdateInventory failedUpdateInventory = Mapper.toInventoryFailedSku(product.getProductId(),
                    requestUpdateInventory.getTransactionId(), "W-007 / ERROR DE SKU - Sku no existe");

        }

    }

    private ResponseEntity<String> sendInventoryToVtex(String url, String quantity, Integer id, String whiteLabel)
            throws ServiceConnectionException, ModelMappingException, IOException {
        RequestVtexUpdateInventory requestVtexUpdateInventory = Mapper.toVtexRequest(quantity);
        ResponseEntity<String> response = vtexApiRestClient.setInventoryBySKU(url, requestVtexUpdateInventory, whiteLabel);
        if (response.getStatusCode() == HttpStatus.OK) {
            // Se debe actualizar en la BD el estado a actualizado
            System.out.print("ACTUALIZO" + id);
            Inventory inventory = inventoryRepository.findById(id).orElse(null);
            inventory.setIs_synchronized_vtex(1);
            inventoryRepository.save(inventory);
            System.out.print("ACTUALIZO");
        }
        return response;

    }

    private boolean validateTimestamp(ProductInventory product, String skuId, RequestUpdateInventory requestUpdateInventory, String warehouse) throws ServiceConnectionException, JsonProcessingException {
        System.out.println("ENTRE A VALIDAR TIME");
// validar en base de datos el timestamp
        long r = inventoryRepository.count(skuId, warehouse);

        if (r == 0) {
            System.out.print("No habian registros");
            return true;
        } else {
            System.out.println("VOY A HACER ESTO " + skuId + " " + warehouse);
            Inventory inventory = inventoryRepository.findTimestamp(skuId, warehouse);
            if (product.getTimestamp().after(inventory.getTimestamp())) {
                return true;
            } else {
                // Notificar que el timestamp no corresponde
                LOG.info("ERROR EN EL TIMESTAMP INVENTORY- Notificación ");
                FailedUpdateInventory failedUpdateInventory = Mapper.toInventoryFailed(product,
                        requestUpdateInventory, "W-005 / Error en el Timestamp");
                return false;
            }
        }
    }

    private Integer SaveInventory(ProductInventory product, RequestUpdateInventory requestUpdateInventory, SkuHomologation sku) throws ServiceConnectionException, JsonProcessingException {
        // Guardar la transaccion de actualización de inventario
        Inventory inventory = new Inventory();
        Integer id_Product = 0;
        try {
            inventory.setWarehouse_id(product.getWarehouse());
            inventory.setTransaction_id(requestUpdateInventory.getTransactionId());
            System.out.println("TIMESTAMP" + product.getTimestamp().toString());
            inventory.setTimestamp(product.getTimestamp());
            inventory.setSku(sku.getSku());
            inventory.setQuantity(product.getQuantity());
            Date date = new Date(System.currentTimeMillis());
            System.out.println(date);

            inventory.setCreated_date(date);
            inventoryRepository.save(inventory);
            id_Product = inventory.getInventory_id();
            System.out.println("SAVE" + inventory.getInventory_id());
        } catch (Exception e) {
            System.out.println("Timestamp repetido");
            LOG.info("ERROR EN EL TIMESTAMP INVENTORY- Notificación a ");
            FailedUpdateInventory failedUpdateInventory = Mapper.toInventoryFailed(product,
                    requestUpdateInventory, "W-005 / Error en el Timestamp");
        }

        return id_Product;
    }

}
