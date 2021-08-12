package com.middleware.lab.repository;

import com.middleware.lab.model.db.SkuHomologation;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

public interface SkuHomologationRepository extends CrudRepository<SkuHomologation, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.sku_homologation WHERE sku = ?1", nativeQuery = true)
    SkuHomologation findBySku(String sku);
    
    @Query(value = "SELECT * FROM lab_nutrition_db.sku_homologation", nativeQuery = true)
    List<SkuHomologation> findAllSkus();
    
    @Query(value = "SELECT * FROM lab_nutrition_db.sku_homologation WHERE ref_id = ?1", nativeQuery = true)
    SkuHomologation findByIdProduct(String productId);
}
