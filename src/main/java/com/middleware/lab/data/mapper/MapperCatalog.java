package com.middleware.lab.data.mapper;

import com.middleware.lab.model.db.Brand;
import com.middleware.lab.model.db.Category;
import com.middleware.lab.model.db.Product;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.Specification;
import com.middleware.provider.catalog.model.rest.RequestCreateProduct;
import com.middleware.provider.catalog.model.rest.RequestCreateProductSpecification;
import com.middleware.provider.catalog.model.rest.RequestCreateSku;
import com.middleware.provider.catalog.model.rest.ResponseProductsLab;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesRequest;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MapperCatalog {

    public static ResponseProductsLab removeSpace(ResponseProductsLab product) {
        ResponseProductsLab responseProductsLab = new ResponseProductsLab();
        responseProductsLab.setCat(product.getCat().trim());
        responseProductsLab.setCodref(product.getCodref().trim());
        responseProductsLab.setCodsku(product.getCodsku().trim());
        responseProductsLab.setDepart(product.getDepart().trim());
        responseProductsLab.setDescr(product.getDescr().trim());
        responseProductsLab.setDescrip(product.getDescrip().trim());
        responseProductsLab.setDicen(product.getDicen().trim());
        responseProductsLab.setEmpleo(product.getEmpleo().trim());
        responseProductsLab.setFecreg(product.getFecreg().trim());
        responseProductsLab.setGlosa(product.getGlosa().trim());
        responseProductsLab.setMarc(product.getMarc().trim());
        responseProductsLab.setProductname(product.getProductname().trim());
        responseProductsLab.setPropi(product.getPropi().trim());
        responseProductsLab.setReco(product.getReco().trim());
        responseProductsLab.setSkuname(product.getSkuname().trim());
        responseProductsLab.setSubtitulo(product.getSubtitulo().trim());
        responseProductsLab.setTamano(product.getTamano().trim());
        responseProductsLab.setTitulo(product.getTitulo().trim());
        responseProductsLab.setUmed(product.getUmed().trim());
        responseProductsLab.setUsr_001(product.getUsr_001().trim());
        responseProductsLab.setUsr_002(product.getUsr_002().trim());
        responseProductsLab.setUsr_003(product.getUsr_003().trim());
        responseProductsLab.setUsr_015(product.getUsr_015().trim());
        responseProductsLab.setUsr_016(product.getUsr_016().trim());
        responseProductsLab.setUsr_017(product.getUsr_017().trim());
        responseProductsLab.setUsr_018(product.getUsr_018().trim());
        responseProductsLab.setVerweb(product.isVerweb());
        responseProductsLab.setPeso(product.getPeso());
        responseProductsLab.setPreu(product.getPreu());
        responseProductsLab.setAlto(product.getAlto());
        responseProductsLab.setLargo(product.getLargo());
        responseProductsLab.setAncho(product.getAncho());
        return responseProductsLab;

    }

    public static RequestCreateProduct toProductVtex(Product product) {
        RequestCreateProduct requestCreateProduct = new RequestCreateProduct();
        requestCreateProduct.setBrandId(product.getBrandId());
        requestCreateProduct.setCategoryId(product.getCategoryId());
        requestCreateProduct.setDepartmentId(product.getDepartmentId());
        requestCreateProduct.setDescription(product.getDescription());
        requestCreateProduct.setDescriptionShort(product.getDescriptionShort());
        requestCreateProduct.setIsActive(product.getIsActive());
        requestCreateProduct.setIsVisible(product.getIsVisible());
        requestCreateProduct.setKeyWords(product.getKeyWords());
        requestCreateProduct.setLinkId(product.getLinkId());
        requestCreateProduct.setMetaTagDescription(product.getMetaTagDescription());
        requestCreateProduct.setName(product.getName());
        requestCreateProduct.setRefId(product.getRefId());
        requestCreateProduct.setTitle(product.getTitle());
        return requestCreateProduct;

    }

    public static Product toProductVtex(ResponseProductsLab responseProductsLab, Brand brand, Category category) {
        Product product = new Product();
        //Homologar MARCA
        product.setBrandId(brand.getBrand_id());
        //MAPEAR CATEGORIA
        product.setCategoryId(category.getCategory_id());
        //MAPEAR DEPARTAMENTO
        product.setDepartmentId(category.getDepartment_id());
        product.setDescription(responseProductsLab.getDescr().trim());
        product.setDescriptionShort(responseProductsLab.getDescrip().trim());
        product.setIsActive(true);
        product.setIsVisible(true);
        //NAME + DEP + CAT
        String name = responseProductsLab.getProductname().trim().toLowerCase().replace(" ", "-");
        product.setKeyWords(name + ", " + responseProductsLab.getDepart().trim().toLowerCase() + ", " + responseProductsLab.getCat().trim().toLowerCase());

        //NAME + REF EX = c4-original-on-the-go-842595106572
        product.setLinkId(name + "-" + responseProductsLab.getCodref().trim());
        product.setMetaTagDescription(responseProductsLab.getDescrip().trim());
        product.setName(responseProductsLab.getProductname().trim());
        product.setRefId(responseProductsLab.getCodref().trim());
        product.setTitle(responseProductsLab.getProductname().trim());
        return product;

    }

    public static Sku toSKUDTO(ResponseProductsLab responseProductsLab, Integer productId) {
        Sku product = new Sku();
        //Homologar MARCA
        product.setColor(responseProductsLab.getUsr_015());
        product.setIsActive(true);
        product.setIsAvailable(true);
        product.setIsKit(false);
        product.setName(responseProductsLab.getSkuname());
        product.setProductId(productId.toString());
        product.setRealHeight(BigDecimal.valueOf(responseProductsLab.getAncho()));
        product.setRealLength(BigDecimal.valueOf(responseProductsLab.getLargo()));
        product.setRealWeightKg(BigDecimal.valueOf(responseProductsLab.getPeso()));
        product.setRealWidth(BigDecimal.valueOf(responseProductsLab.getAncho()));
        product.setRefId(responseProductsLab.getCodsku());
        product.setTamano(responseProductsLab.getTamano());
        product.setStockKeepinUnitEans("un");

        product.setIsActive(true);
        System.out.println("product" + product.toString());
        return product;

    }

    public static SpecificationsValuesRequest toFieldValues(String value, String field, Integer position) {
        SpecificationsValuesRequest specificationsValuesRequest = new SpecificationsValuesRequest();
        specificationsValuesRequest.setActive(true);
        specificationsValuesRequest.setFieldId(Long.valueOf(field));
        specificationsValuesRequest.setName(value);
        specificationsValuesRequest.setPosition(position);
        specificationsValuesRequest.setText(value);

        return specificationsValuesRequest;

    }

    public static List<RequestCreateProductSpecification> toProductSpecificationsVtex(Specification specification) {
        List<RequestCreateProductSpecification> productEspecificationList = new ArrayList<RequestCreateProductSpecification>();
        RequestCreateProductSpecification productEspecification = new RequestCreateProductSpecification();
        List<String> value = new ArrayList<String>();

        productEspecification.setName("Sabores");
        value = new ArrayList<>();
        value.add(specification.getSabores());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Objetivos");
        value = new ArrayList<>();
        value.add(specification.getObjetivos());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Tamaños");
        value = new ArrayList<>();
        value.add(specification.getTamanos());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Marca");
        value = new ArrayList<>();
        value.add(specification.getMarca());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Banner descripción");
        value = new ArrayList<>();
        value.add(specification.getBannerDescripcion());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Objetivo");
        value = new ArrayList<>();
        value.add(specification.getObjetivo());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Titulo");
        value = new ArrayList<>();
        value.add(specification.getTitulo());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Subtitulo");
        value = new ArrayList<>();
        value.add(specification.getSubtitulo());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("¿Qué dicen los Cientificos?");
        value = new ArrayList<>();
        value.add(specification.getDicen());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Marca");
        value = new ArrayList<>();
        value.add(specification.getMarca());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Propiedades");
        value = new ArrayList<>();
        value.add(specification.getPropiedades());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Modo de empleo");
        value = new ArrayList<>();
        value.add(specification.getEmpleo());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Marca");
        value = new ArrayList<>();
        value.add(specification.getMarca());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Recomendaciones");
        value = new ArrayList<>();
        value.add(specification.getRecomendaciones());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Glosario");
        value = new ArrayList<>();
        value.add(specification.getGlosario());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Campo 1");
        value = new ArrayList<>();
        value.add(specification.getCampoUno());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Campo2");
        value = new ArrayList<>();
        value.add(specification.getCampoDos());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();
        productEspecification.setName("Campo3");
        value = new ArrayList<>();
        value.add(specification.getCampoTres());
        productEspecification.setValue(value);
        productEspecificationList.add(productEspecification);
        productEspecification = new RequestCreateProductSpecification();

        return productEspecificationList;

    }

    public static RequestCreateSku toStockKeepingUnitDTO(Integer id, Sku sku) {
        RequestCreateSku product = new RequestCreateSku();
        product.setProductId(id);
        product.setIsActive(true);
        product.setName(sku.getName());
        product.setRefId(sku.getRefId());
        product.setHeight(sku.getRealHeight());
        product.setLength(sku.getRealLength());
        product.setWidth(sku.getRealWidth());
        product.setWeightKg(sku.getRealWeightKg());
        product.setIsKit(false);
        product.setMeasurementUnit("un");
        product.setKitItensSellApart(false);
        return product;

    }

    public static Specification toSpecificationDTO(ResponseProductsLab responseProductsLab, String idProduct) {
        Specification specification = new Specification();
        specification.setBannerDescripcion(responseProductsLab.getUsr_018());
        specification.setCampoDos(responseProductsLab.getUsr_002());
        specification.setCampoUno(responseProductsLab.getUsr_001());
        specification.setCampoTres(responseProductsLab.getUsr_003());
        specification.setDicen(responseProductsLab.getDicen());
        specification.setEmpleo(responseProductsLab.getEmpleo());
        specification.setGlosario(responseProductsLab.getGlosa());
        specification.setIdProduct(idProduct);
        specification.setMarca(responseProductsLab.getMarc());
        specification.setObjetivo(responseProductsLab.getUsr_016());
        specification.setObjetivos(responseProductsLab.getUsr_017());
        specification.setPropiedades(responseProductsLab.getPropi());
        specification.setRecomendaciones(responseProductsLab.getReco());
        specification.setSubtitulo(responseProductsLab.getSubtitulo());
        specification.setTamanos(responseProductsLab.getTamano());
        specification.setTitulo(responseProductsLab.getTitulo());
        specification.setSabores(responseProductsLab.getUsr_015());

        return specification;

    }

}
