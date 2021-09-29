package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Sku;
import org.springframework.data.jpa.repository.Query;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface SkuRepository extends CrudRepository<Sku, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.sku s WHERE s.ref_id = ?1", nativeQuery = true)
    Sku findByRefId(String refId);

    @Query(value = "SELECT * FROM lab_nutrition_db.sku s WHERE s.id = ?1", nativeQuery = true)
    Sku findByVtexId(Integer id);

}
