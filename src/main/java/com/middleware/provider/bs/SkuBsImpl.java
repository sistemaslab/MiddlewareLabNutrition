package com.middleware.provider.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.repository.WarehouseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.sku.model.rest.RequestSkus;
import com.middleware.provider.sku.model.rest.ResponseSkus;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import org.springframework.http.ResponseEntity;
import com.middleware.lab.client.LabClient;
import com.middleware.lab.data.mapper.MapperCatalog;
import com.middleware.lab.model.db.Brand;
import com.middleware.lab.model.db.Category;
import com.middleware.lab.model.db.Product;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.model.db.Specification;
import com.middleware.lab.repository.SkuHomologationRepository;
import com.middleware.provider.catalog.model.rest.ResponseSearchWarehouses;
import com.middleware.lab.model.db.Warehouse;
import com.middleware.lab.repository.BrandRepository;
import com.middleware.lab.repository.CategoryRepository;
import com.middleware.lab.repository.ProductRepository;
import com.middleware.lab.repository.SkuRepository;
import com.middleware.lab.repository.SpecificationRepository;
import com.middleware.provider.catalog.model.rest.RequestCreateProduct;
import com.middleware.provider.catalog.model.rest.RequestCreateProductSpecification;
import com.middleware.provider.catalog.model.rest.RequestCreateSku;
import com.middleware.provider.catalog.model.rest.ResponseCreateProduct;
import com.middleware.provider.catalog.model.rest.ResponseCreateProductSpecification;
import com.middleware.provider.catalog.model.rest.ResponseCreateSku;
import com.middleware.provider.catalog.model.rest.ResponseProductsLab;
import com.middleware.provider.catalog.model.rest.ResponseSaveProduct;
import com.middleware.provider.catalog.model.rest.SpecificationsRequest;
import com.middleware.provider.catalog.model.rest.SpecificationsResponse;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesListResponse;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesResponse;
import com.middleware.provider.consumer.navasoft.SoapClient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab.navasoft.model.soap.GetitemsResponse;
import lab.navasoft.model.soap.GetnewitemsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

@Service("SkuBsImpl")
public class SkuBsImpl implements SkuBs {

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    @Autowired
    SkuHomologationRepository skuHomologationRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SpecificationRepository specificationRepository;

    @Autowired
    SkuRepository skuRepository;

    @Autowired
    SoapClient soapClient;

    @Value("${lab.url}")
    private String url;

    @Value("${lab.callbackProducts}")
    private String callbackProducts;

    @Value("${lab.callbackProduct}")
    private String callbackProduct;

    @Value("${vtex.url.warehouse}")
    private String urlWarehouse;

    @Value("${vtex.url.catalog.skuList}")
    private String urlSkuList;

    @Value("${vtex.url.catalog.sku}")
    private String urlSku;

    @Value("${vtex.url.catalog.setSku}")
    private String urlSetSku;

    @Value("${vtex.url.catalog.product}")
    private String urlProduct;

    @Value("${vtex.url.catalog.productByRef}")
    private String urlProductById;

    private final EntityManager entityManager;
    private static final Logger LOG = Logger.getLogger(SkuBsImpl.class.getName());

    @Autowired
    public SkuBsImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResponseEntity<String> detailSku(String IdProduct) throws ServiceConnectionException, ModelMappingException, IOException {
        return searchSkuVtex(urlSkuList);

    }

    @Override
    public List<ResponseSkus> getSkus() throws ServiceConnectionException, ModelMappingException, IOException {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_db.get_skus");
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseSkus> list = results.stream().map(result -> new ResponseSkus(
                (String) result[0],
                (String) result[1],
                (Date) result[2]
        )).collect(Collectors.toList());

        System.out.println("TAM" + list.size());
        return list;

    }

    private ResponseEntity<String> searchSkuVtex(String url)
            throws ServiceConnectionException, ModelMappingException, IOException {
        ResponseEntity<String> response = vtexApiRestClient.searchSkuVtex(url);

        return response;
    }

    @Override
    public boolean requestToSku(RequestSkus requestSkus, String idSku)
            throws ServiceConnectionException, ModelMappingException, IOException {
        System.out.println("VOY A GUARDAR " + idSku + "EL REF" + requestSkus.getRefId());
        skuHomologationRepository.save(Mapper.toSku(idSku, requestSkus.getRefId()));
        return true;
    }

    @Override
    public void searchWarehouse()
            throws ServiceConnectionException, ModelMappingException, IOException {
        List<ResponseSearchWarehouses> warehouses = vtexApiRestClient.searchWarehouses(urlWarehouse);
        for (ResponseSearchWarehouses warehouse : warehouses) {
            LOG.info("WAREHOUSE " + warehouse.getId());
            Warehouse w = warehouseRepository.findByIdWarehouse(warehouse.getId());
            if (w == null) {
                Warehouse ware = new Warehouse();
                ware.setWarehouse_id(warehouse.getId());
                ware.setWarehouse_name(warehouse.getName());
                warehouseRepository.save(ware);
                ware = new Warehouse();
            } else {
                LOG.info("YA EXISTE " + warehouse.getId());
            }
        }

    }

    @Override
    public boolean setBrand(Integer brandId, String name)
            throws ServiceConnectionException, ModelMappingException, IOException {
        System.out.println("VOY A GUARDAR " + brandId + "EL name" + name);
        brandRepository.save(Mapper.toBrand(brandId, name));
        return true;
    }

    @Override
    public boolean setCategory(Integer categoryId, String name, Integer department, String departmentName)
            throws ServiceConnectionException, ModelMappingException, IOException {
        System.out.println("VOY A GUARDAR " + categoryId + "EL name" + name);
        categoryRepository.save(Mapper.toCategory(categoryId, name, department, departmentName));
        return true;
    }

    @Override
    public boolean syncProduct(String productId)
            throws ServiceConnectionException, ModelMappingException, IOException {
        boolean success = true;
        try {
            GetitemsResponse getitemsResponse = soapClient.getProductById(Mapper.toProductLab(productId), this.url, this.callbackProduct);
            getitemsResponse.getGetitemsResult();
            System.out.println("RESPUESTAS Catalog" + getitemsResponse.getGetitemsResult());
            String response = getitemsResponse.getGetitemsResult();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);

            ResponseProductsLab responseProductsLab;
            responseProductsLab = objectMapper.readValue(response, ResponseProductsLab.class);
            responseProductsLab = MapperCatalog.removeSpace(responseProductsLab);
            System.out.println("RES " + responseProductsLab.toString());
            //VALIDAR QUE SEA WEB
            if (responseProductsLab.isVerweb()) {
                if (responseProductsLab.getCodref().isEmpty() || responseProductsLab.getCodref().equals("") || responseProductsLab.getCodref() == null) {
                    //SE DEBE CREAR EL PRODUCTO 
                    //BUSCAR MARCA Y CATEGORIA 
                    Brand brand = brandRepository.findByName(responseProductsLab.getMarc().toUpperCase());
                    if (brand != null) {
                        Category category = categoryRepository.findByName(responseProductsLab.getCat().toUpperCase());
                        if (category != null) {
                            ResponseSaveProduct responseSaveProduct = saveProductInDB(MapperCatalog.toProductVtex(responseProductsLab, brand, category));
                            //Especificaciones de producto 
                            Product product = productRepository.findByRefId(responseProductsLab.getCodref().trim());

                            ResponseSaveProduct responseSaveSpecification = saveSpecificationsInDB(MapperCatalog.toSpecificationDTO(responseProductsLab, product.getRefId()));

                            success = responseSaveProduct.isSuccess();
                            if (success) {
                                success = saveSkuInDB(MapperCatalog.toSKUDTO(responseProductsLab, product.getVtexId()));
                                if (success) {
                                    //ADD ESPECIFICATIONS
                                    //Actualizar tabla SKU
                                } else {
                                    //LOG
                                    success = false;
                                }

                            } else {
                                //LOG
                                success = false;
                            }

                        } else {
                            //LOG
                            success = false;
                        }

                    } else {
                        //LOG
                        success = false;
                    }
                } else {
                    // EL PRODUCTO YA EXISTE 
                    //BUSCAR EL PRODUCTO 
                    ResponseCreateProduct product = vtexApiRestClient.searchProduct(urlProductById + responseProductsLab.getCodref().trim());
                    if (product == null) {
                        //CODIGO DE PRODUCTO NO EXITE 
                    } else {
                        success = saveSkuInDB(MapperCatalog.toSKUDTO(responseProductsLab, product.getId()));
                        if (success) {
                            try {
                                //ADD ESPECIFICATIONS
                                System.out.println("SKU"+responseProductsLab.getCodsku());
                                Sku sku = skuRepository.findByRefId(responseProductsLab.getCodsku());
                                System.out.println("SKU"+sku.toString());
                                List<SpecificationsResponse> specificationsResponse = vtexApiRestClient.getSkuSpecifications(sku.getId().toString());
                                System.out.println("TRAJO LA LISTA");
                                //Actualizar Tamaño
                                syncSpecification("50", sku.getTamano(), sku.getId().toString(), specificationsResponse);
                                //Actualizar Color
                                syncSpecification("49", sku.getColor(), sku.getId().toString(), specificationsResponse);
                                //Actualizar tabla SKU    
                            } catch (ServiceConnectionException ex) {
                                LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.toString(), "ERROR"});
                            } catch (Exception e) {
                                LOG.log(Level.WARNING, "{0}{1}", new Object[]{e.toString(), "ERROR"});

                            }

                        } else {
                            //LOG
                            success = false;
                        }
                    }

                }

            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{e.getMessage(), "ERROR"});

            success = false;

        }
        return success;
    }

    public void syncSpecification(String field, String value, String skuIdString, List<SpecificationsResponse> specificationsResponseList) throws JsonProcessingException, IOException, ServiceConnectionException {
        SpecificationsRequest specificationsRequest = new SpecificationsRequest();
        specificationsRequest.setFieldId(Long.valueOf(field));
        boolean colorExist = false;
        Integer position = 0;
        System.out.println("ENTRE ");

        List<SpecificationsValuesListResponse> specificationsValuesListResponse = vtexApiRestClient.getSpecificationsValues(field);
        for (SpecificationsValuesListResponse specificationsValuesListResponse1 : specificationsValuesListResponse) {
            System.out.println("validadando la lista");

            String specification = specificationsValuesListResponse1.getValue();
            if (specification.equals(value)) {
                System.out.println("POS " + specificationsValuesListResponse1.getPosition());
                specificationsRequest.setFieldValueId(specificationsValuesListResponse1.getFieldValueId());
                position = specificationsValuesListResponse1.getPosition();
                colorExist = true;
            }
        }
        if (!colorExist) {
            //crear color 
            System.out.println("specification no existe");

            SpecificationsValuesResponse specificationsValuesResponse = vtexApiRestClient.createSpecificationField(MapperCatalog.toFieldValues(value, field, position));
            specificationsRequest.setFieldValueId(specificationsValuesResponse.getFieldValueId());
        }
        //cargar color 
        if (specificationsResponseList.isEmpty()) {
            System.out.println("cargando el color " + skuIdString + "RQ " + specificationsRequest.toString());
            SpecificationsResponse specificationsResponse = vtexApiRestClient.createSkuSpecifications(specificationsRequest, skuIdString);
        } else {
            for (SpecificationsResponse specificationsResponse : specificationsResponseList) {
                if (specificationsResponse.getFieldId().equals(field)) {
                    specificationsRequest.setId(specificationsResponse.getId());
                    SpecificationsResponse specificationsResponses = vtexApiRestClient.updateSkuSpecifications(specificationsRequest, skuIdString);
                }
            }
        }
    }

    private ResponseSaveProduct saveProductInDB(Product newProduct) throws JsonProcessingException {
        ResponseSaveProduct responseSaveProduct = new ResponseSaveProduct();
        boolean success = true;
        String message = "";
        if (productRepository.existsById(newProduct.getRefId()) == true) {
            System.out.println("ENTRE A SAVE Product");
            Product oldProduct = productRepository.findByRefId(newProduct.getRefId());
            if (oldProduct != null) {
                newProduct = setUpdatedAttributes(newProduct, oldProduct);
                if (validateIsCompleteInfo(newProduct)) {
                    //ENVIAR A VTEX
                    if (saveProductInVtex(newProduct)) {
                        newProduct.setIsSynchronizedVtex(true);
                        success = true;
                    } else {
                        newProduct.setIsSynchronizedVtex(false);
                        success = false;
                    }
                }
                try {
                    productRepository.save(newProduct);
                    success = true;
                } catch (Exception ex) {
                    String msg = "Error almacenando producto: " + newProduct.getRefId() + " en base de datos";
                    //LOGGER.error(msg, ex);
                    success = false;
                    message = msg + ". " + ex.getStackTrace();
                }
            }
        } else {
            try {
                if (validateIsCompleteInfo(newProduct)) {
                    if (saveProductInVtex(newProduct)) {
                        newProduct.setIsSynchronizedVtex(true);
                        success = true;
                    } else {
                        newProduct.setIsSynchronizedVtex(false);
                        success = false;
                    }
                }
                productRepository.save(newProduct);
                message = "El producto se ha almacenado correctamente";
            } catch (Exception ex) {
                String msg = "Error almacenando producto: " + newProduct.getRefId() + " en base de datos";
                //LOGGER.error(msg, ex);
                success = false;
                message = msg + ". " + ex.getStackTrace();
            }
        }
        //GUARDAR EL LOG DEL PRODUCTO 
        responseSaveProduct.setMsj(message);
        responseSaveProduct.setSuccess(success);

        return responseSaveProduct;
    }

    private ResponseSaveProduct saveSpecificationsInDB(Specification newSpecification) throws JsonProcessingException {
        ResponseSaveProduct responseSaveProduct = new ResponseSaveProduct();
        boolean success = true;
        String message = "";
        if (specificationRepository.existsById(newSpecification.getIdProduct()) == true) {
            System.out.println("ENTRE A SAVE SPECIFICATION");
            Specification oldSpecification = specificationRepository.findByProductId(newSpecification.getIdProduct());
            if (oldSpecification != null) {
                newSpecification = setUpdatedSpecifications(newSpecification, oldSpecification);
                if (validateIsCompleteInfoSpecifications(newSpecification)) {
                    //ENVIAR A VTEX
                    if (saveSpecificationsInVtex(newSpecification)) {
                        newSpecification.setIsSynchronizedVtex(true);
                        success = true;
                    } else {
                        newSpecification.setIsSynchronizedVtex(false);
                        success = false;
                    }
                }
                try {
                    specificationRepository.save(newSpecification);
                    success = true;
                } catch (Exception ex) {
                    String msg = "Error almacenando Specificacion: " + newSpecification.getIdProduct() + " en base de datos";
                    //LOGGER.error(msg, ex);
                    success = false;
                    message = msg + ". " + ex.getStackTrace();
                }
            }
        } else {
            try {
                if (validateIsCompleteInfoSpecifications(newSpecification)) {
                    if (saveSpecificationsInVtex(newSpecification)) {
                        newSpecification.setIsSynchronizedVtex(true);
                        success = true;
                    } else {
                        newSpecification.setIsSynchronizedVtex(false);
                        success = false;
                    }
                }
                specificationRepository.save(newSpecification);
                message = "El producto se ha almacenado correctamente";
            } catch (Exception ex) {
                String msg = "Error almacenando Specificacion: " + newSpecification.getIdProduct() + " en base de datos";
                //LOGGER.error(msg, ex);
                success = false;
                message = msg + ". " + ex.getStackTrace();
            }
        }
        //GUARDAR EL LOG DEL PRODUCTO 
        responseSaveProduct.setMsj(message);
        responseSaveProduct.setSuccess(success);

        return responseSaveProduct;
    }

    public Product setUpdatedAttributes(Product newProduct, Product oldProduct) {
        if (oldProduct != null) {
            if (newProduct.getVtexId() != null) {
                oldProduct.setVtexId(newProduct.getVtexId());
            }
            if (newProduct.getBrandId() != null) {
                oldProduct.setBrandId(newProduct.getBrandId());
            }
            if (newProduct.getCategoryId() != null) {
                oldProduct.setCategoryId(newProduct.getCategoryId());
            }
            if (newProduct.getDepartmentId() != null) {
                oldProduct.setDepartmentId(newProduct.getDepartmentId());
            }
            if (newProduct.getDescription() != null) {
                oldProduct.setDescription(newProduct.getDescription());
                String descriptionShort;
                if (newProduct.getDescriptionShort() != null) {
                    descriptionShort = newProduct.getDescriptionShort();
                } else {
                    descriptionShort = newProduct.getDescription();
                }
                if (descriptionShort.length() > 800) {
                    descriptionShort = descriptionShort.substring(0, 799);
                }
                oldProduct.setDescriptionShort(descriptionShort);
            } else if (oldProduct.getDescription() != null) {
                String description = oldProduct.getDescription();
                if (description.length() > 2000) {
                    description = description.substring(0, 1999);
                }
                oldProduct.setDescription(description);
            }
            if (newProduct.getIsActive() != null) {
                oldProduct.setIsActive(newProduct.getIsActive());
            }
            if (newProduct.getIsVisible() != null) {
                oldProduct.setIsVisible(newProduct.getIsVisible());
            }
            if (newProduct.getKeyWords() != null) {
                oldProduct.setKeyWords(newProduct.getKeyWords());
            }
            if (newProduct.getListStoreId() != null) {
                oldProduct.setListStoreId(newProduct.getListStoreId());
            }
            if (newProduct.getMetaTagDescription() != null) {
                oldProduct.setMetaTagDescription(newProduct.getMetaTagDescription());
            }
            if (newProduct.getName() != null) {
                oldProduct.setName(newProduct.getName());
            }
            if (newProduct.getRefId() != null) {
                oldProduct.setRefId(newProduct.getRefId());
            }
            if (newProduct.getTitle() != null) {
                oldProduct.setTitle(newProduct.getTitle());
            }
            if (newProduct.getShowWithoutStock() != null) {
                oldProduct.setShowWithoutStock(newProduct.getShowWithoutStock());
            }

        }
        return oldProduct;
    }

    public Specification setUpdatedSpecifications(Specification newSpecification, Specification oldSpecification) {
        if (oldSpecification != null) {
            if (newSpecification.getBannerDescripcion() != null) {
                oldSpecification.setBannerDescripcion(newSpecification.getBannerDescripcion());
            }
            if (newSpecification.getCampoDos() != null) {
                oldSpecification.setCampoDos(newSpecification.getCampoDos());
            }
            if (newSpecification.getCampoTres() != null) {
                oldSpecification.setCampoTres(newSpecification.getCampoTres());
            }
            if (newSpecification.getCampoUno() != null) {
                oldSpecification.setCampoUno(newSpecification.getCampoUno());
            }
            if (newSpecification.getDicen() != null) {
                oldSpecification.setDicen(newSpecification.getDicen());
            }
            if (newSpecification.getEmpleo() != null) {
                oldSpecification.setEmpleo(newSpecification.getEmpleo());
            }
            if (newSpecification.getGlosario() != null) {
                oldSpecification.setGlosario(newSpecification.getGlosario());
            }
            if (newSpecification.getMarca() != null) {
                oldSpecification.setMarca(newSpecification.getMarca());
            }
            if (newSpecification.getObjetivo() != null) {
                oldSpecification.setObjetivo(newSpecification.getObjetivo());
            }
            if (newSpecification.getObjetivos() != null) {
                oldSpecification.setObjetivos(newSpecification.getObjetivos());
            }
            if (newSpecification.getOrigen() != null) {
                oldSpecification.setOrigen(newSpecification.getOrigen());
            }
            if (newSpecification.getPropiedades() != null) {
                oldSpecification.setPropiedades(newSpecification.getPropiedades());
            }
            if (newSpecification.getRecomendaciones() != null) {
                oldSpecification.setRecomendaciones(newSpecification.getRecomendaciones());
            }
            if (newSpecification.getSubtitulo() != null) {
                oldSpecification.setSubtitulo(newSpecification.getSubtitulo());
            }
            if (newSpecification.getTamanos() != null) {
                oldSpecification.setTamanos(newSpecification.getTamanos());
            }
            if (newSpecification.getTitulo() != null) {
                oldSpecification.setTitulo(newSpecification.getTitulo());
            }
        }
        return oldSpecification;
    }

    private boolean saveProductInVtex(Product product) throws JsonProcessingException {
        boolean success = true;
        String message;
        RequestCreateProduct requestCreateProduct = MapperCatalog.toProductVtex(product);
        try {
            ResponseCreateProduct responseCreateProduct = vtexApiRestClient.setNewProduct(requestCreateProduct, urlProduct);
            product.setVtexId(responseCreateProduct.getId());
            product.setIsSynchronizedVtex(true);
            productRepository.save(product);
            message = "Producto cargado en vtex correctamente";
        } catch (ServiceConnectionException ex) {

            String msg = "saveProductInVtex-refId: " + product.getRefId()
                    + ". Error conectando con Vtex: status="
                    + ex.getMessage();
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});
            success = false;
            message = msg;

        } catch (JsonProcessingException ex) {
            String msg = "saveProductInVtex-refId: " + product.getRefId()
                    + ". Error parseando: " + ex.getOriginalMessage();
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});
            success = false;
            message = msg;
        } catch (Exception ex) {
            product.setVtexId(null);
            product.setIsSynchronizedVtex(false);
            String msg = "saveProductInVtex-refId: " + product.getRefId()
                    + "Error actualizando producto en base de datos";
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});

            success = false;
            message = msg;
        }
        //GUARDAR EL LOG DEL PRODUCTO 
        return success;
    }

    private boolean saveSpecificationsInVtex(Specification newSpecification) throws JsonProcessingException {
        boolean success = true;
        String message;
        List<RequestCreateProductSpecification> requestCreateProductSpecification = MapperCatalog.toProductSpecificationsVtex(newSpecification);
        try {
            for (RequestCreateProductSpecification requestCreateProductSpecification1 : requestCreateProductSpecification) {
                ResponseCreateProductSpecification responseCreateProductSpecification = vtexApiRestClient.setNewSpecification(requestCreateProductSpecification1, urlProduct);
                newSpecification.setIsSynchronizedVtex(true);
                specificationRepository.save(newSpecification);
                message = "Producto cargado en vtex correctamente";
            }

        } catch (ServiceConnectionException ex) {

            String msg = "saveSpecificationsInVtex-IdProduct: " + newSpecification.getIdProduct()
                    + ". Error conectando con Vtex: status="
                    + ex.getMessage();
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});
            success = false;
            message = msg;

        } catch (JsonProcessingException ex) {
            String msg = "saveSpecificationsInVtex-IdProduct: " + newSpecification.getIdProduct()
                    + ". Error parseando: " + ex.getOriginalMessage();
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});
            success = false;
            message = msg;
        } catch (Exception ex) {
            newSpecification.setIsSynchronizedVtex(false);
            String msg = "saveSpecificationsInVtex-IdProduct: " + newSpecification.getIdProduct()
                    + "Error actualizando producto en base de datos";
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{ex.getMessage(), msg});

            success = false;
            message = msg;
        }
        //GUARDAR EL LOG DEL PRODUCTO 
        return success;
    }

    private boolean validateIsCompleteInfo(Product product) {
        boolean isCompleteInfo;
        try {
            Assert.notNull(product.getBrandId(), "brandId es requerido");
            Assert.notNull(product.getCategoryId(), "categoryId es requerido");
            Assert.notNull(product.getDepartmentId(), "departmentId es requerido");
            Assert.notNull(product.getDescription(), "description es requerido");
            Assert.notNull(product.getKeyWords(), "keyWords es requerido");
            Assert.notNull(product.getMetaTagDescription(), "metaTagDescription es requerido");
            Assert.notNull(product.getRefId(), "refId es requerido");
            Assert.notNull(product.getName(), "name es requerido");
            isCompleteInfo = true;
        } catch (IllegalArgumentException | IllegalStateException iae) {
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{iae.getMessage(), iae});
            isCompleteInfo = false;
        }
        product.setIsCompleteInfo(isCompleteInfo);
        return isCompleteInfo;
    }

    private boolean validateIsCompleteInfoSpecifications(Specification specification) {
        boolean isCompleteInfo;
        try {
            Assert.notNull(specification.getBannerDescripcion(), "Banner Descripcion es requerido");
            Assert.notNull(specification.getCampoDos(), "Campo Dos es requerido");
            Assert.notNull(specification.getCampoTres(), "Campo Tres es requerido");
            Assert.notNull(specification.getCampoUno(), "Campo Uno es requerido");
            Assert.notNull(specification.getDicen(), "Dicen es requerido");
            Assert.notNull(specification.getEmpleo(), "Empleo es requerido");
            Assert.notNull(specification.getGlosario(), "Glosario es requerido");
            Assert.notNull(specification.getMarca(), "Marca es requerido");
            Assert.notNull(specification.getObjetivo(), "Objetivo es requerido");
            Assert.notNull(specification.getObjetivos(), "Objetivos es requerido");
            Assert.notNull(specification.getOrigen(), "Origen es requerido");
            Assert.notNull(specification.getPropiedades(), "Propiedades es requerido");
            Assert.notNull(specification.getRecomendaciones(), " Recomendaciones es requerido");
            Assert.notNull(specification.getSubtitulo(), "Subtitulo es requerido");
            Assert.notNull(specification.getTamanos(), "Tamanos es requerido");
            Assert.notNull(specification.getTitulo(), "Titulo es requerido");

            isCompleteInfo = true;
        } catch (IllegalArgumentException | IllegalStateException iae) {
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{iae.getMessage(), iae});
            isCompleteInfo = false;
        }
        specification.setIsCompleteInfo(isCompleteInfo);
        return isCompleteInfo;
    }

    private boolean validateIsCompleteInfoSku(Sku sku) {
        boolean isCompleteInfo;
        try {
            Assert.notNull(sku.getRefId(), "refId es requerido");
            Assert.notNull(sku.getProductId(), "productId es requerido");
            Assert.notNull(sku.getRealHeight(), "realHeight es requerido");
            Assert.notNull(sku.getRealLength(), "realLength es requerido");
            Assert.notNull(sku.getRealWeightKg(), "realWeightKg es requerido");
            Assert.notNull(sku.getRealWidth(), "realWidth es requerido");
            Assert.notNull(sku.getName(), "name es requerido");
            /*boolean nameExist = skuDao.nameExists(sku.getName(), sku.getSapId());
            Assert.state(!nameExist, "Ya existe un sku con nombre="+sku.getName());*/
            isCompleteInfo = true;
        } catch (IllegalArgumentException | IllegalStateException iae) {
            LOG.log(Level.WARNING, "{0}{1}", new Object[]{iae.getMessage(), iae});
            isCompleteInfo = false;
        }
        sku.setIsCompleteInfo(isCompleteInfo);
        return isCompleteInfo;
    }

    @Override
    public void updateCatalog()
            throws ServiceConnectionException, ModelMappingException, IOException {

        GetnewitemsResponse getnewitemsResponse = soapClient.getProducts(Mapper.toProductsLab(90), this.url, this.callbackProducts);
        getnewitemsResponse.getGetnewitemsResult();
        System.out.println("RESPUESTAS Catalog" + getnewitemsResponse.getGetnewitemsResult());
        String response = "[" + getnewitemsResponse.getGetnewitemsResult() + "]";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        ArrayList<ResponseProductsLab> responseOrderStatus;
        responseOrderStatus = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ResponseProductsLab.class));

        for (ResponseProductsLab responseOrderStatu : responseOrderStatus) {
            System.out.println(responseOrderStatu.toString());

            //Guardar los datos en BD 
            //Validar que esten todos los datos 
        }

    }

    private boolean saveSkuInDB(Sku newSku) throws IOException, ModelMappingException {
        boolean success = true;
        String message;
        if (skuRepository.existsById(newSku.getRefId()) == true) {
            this.validateIsCompleteInfoSku(newSku);

            Sku oldSku = skuRepository.findByRefId(newSku.getRefId());
            if (oldSku != null) {
                if (validateIsCompleteInfoSku(newSku)) {
                    newSku.setIsCompleteInfo(true);
                    System.out.println("COMPLETO");
                    if (saveSkuInVtex(newSku)) {
                        newSku.setIsSynchronizedVtex(true);
                        success = true;
                    } else {
                        newSku.setIsSynchronizedVtex(false);
                        success = false;
                    }
                }
                try {

                    skuRepository.save(newSku);
                    success = true;
                    message = "El sku se ha actualizado correctamente";
                    return true;
                } catch (Exception ex) {
                    String msg = "Error almacenando sku: " + newSku.getRefId() + " en base de datos";
                    LOG.warning(msg + ex);
                    success = false;
                    message = msg + ". " + Arrays.toString(ex.getStackTrace());
                    return success;
                }
            }
        }
        try {
            if (validateIsCompleteInfoSku(newSku)) {
                if (saveSkuInVtex(newSku)) {
                    newSku.setIsSynchronizedVtex(true);
                    success = true;
                } else {
                    newSku.setIsSynchronizedVtex(false);
                    success = false;
                }
            }
            newSku.setIsSynchronizedVtex(false);
            skuRepository.save(newSku);
            message = "El sku se ha almacenado correctamente";
        } catch (Exception ex) {
            String msg = "Error almacenando sku: " + newSku.getRefId() + " en base de datos (" + newSku + ")";
            LOG.warning(msg + ex);
            success = false;
            message = msg + ". " + Arrays.toString(ex.getStackTrace());
        }
        return success;
    }

    private boolean saveSkuInVtex(Sku sku) throws IOException, ModelMappingException {
        boolean success = false;
        String message = "";

        System.out.println("ProdictID" + sku.getProductId());
        if (sku.getProductId() != null) {
            RequestCreateSku requestCreateSku = MapperCatalog.toStockKeepingUnitDTO(Integer.valueOf(sku.getProductId()), sku);
            try {
                ResponseCreateSku responseCreateSku = vtexApiRestClient.setNewSku(requestCreateSku, urlSetSku);
                sku.setId(responseCreateSku.getId());
                skuHomologationRepository.save(new SkuHomologation(responseCreateSku.getId().toString(), responseCreateSku.getRefId()));

                if (sku.getAttachments() == null) {
                    sku.setIsSynchronizedVtex(true);
                    skuRepository.save(sku);
                    success = true;
                    message = "Sku cargado en vtex correctamente";
                } else {
                    skuRepository.save(sku);
                    if (success) {
                        message = "Sku cargado en vtex correctamente con anexos de servicios";
                    } else {
                        message = "Sku cargado en vtex correctamente. No se pudo cargar anexos de servicios";
                    }
                }
            } catch (ServiceConnectionException ex) {
                if (ex.getMessage().contains("Sku can not be created because the RefId")) {
                    LOG.warning("Ya existe el sku en Vtex");
                    try {
                        // Buscar sku_id y reenviar actualizacion
                        String skuIdString = vtexApiRestClient.getSkuByRefId(sku.getRefId());
                        if (skuIdString != null && !skuIdString.isEmpty()) {
                            sku.setId(Integer.valueOf(skuIdString.replace("\"", "")));
                            sku.setIsSynchronizedVtex(true);
                            System.out.println("SKU TO UPDATE " + skuIdString.replace("\"", ""));
                            vtexApiRestClient.updateSku(requestCreateSku, skuIdString.replace("\"", ""));
                            skuRepository.save(sku);
                            success = true;
                            message = "Actualizo skuId relacionado al refId=" + sku.getRefId();
                        } else {
                            success = false;
                            message = "No se pudo obtener skuId relacionado al refId=" + sku.getRefId();
                        }
                    } catch (JsonProcessingException | ServiceConnectionException ex1) {
                        String msg = "Error obteniendo identificador de sku ya existente: refId=" + sku.getRefId();
                        LOG.warning(msg + ex1);
                        success = false;
                        message = msg + ". " + Arrays.toString(ex1.getStackTrace());
                    }
                } else {
                    String msg = "saveSkuInVtex-refId: " + sku.getRefId()
                            + ". Error conectando con Vtex: status="
                            + ex.getMessage()
                            + ", responseBody=" + ex.getMessage();
                    LOG.warning(msg + ex);
                    success = false;
                    message = msg + ". " + Arrays.toString(ex.getStackTrace());
                }
            } catch (JsonProcessingException ex) {
                String msg = "saveSkuInVtex-refId: " + sku.getRefId()
                        + ". Error parseando: " + ex.getOriginalMessage();
                LOG.warning(msg + ex);
                success = false;
                message = msg + ". " + Arrays.toString(ex.getStackTrace());
            } catch (Exception ex) {
                String msg = "saveProductInVtex-refId: " + sku.getRefId()
                        + "Error actualizando sku en base de datos";
                sku.setId(null);
                sku.setIsSynchronizedVtex(false);
                LOG.warning(msg + ex);
                success = false;
                message = msg + ". " + Arrays.toString(ex.getStackTrace());
            }
        }

        return success;
    }

}
