package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Brand;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends CrudRepository<Brand, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.brand WHERE brand_id = ?1", nativeQuery = true)
    Brand findByIdBrand(String idBrand);
    
    @Query(value = "SELECT * FROM lab_nutrition_db.brand WHERE brand_name = ?1", nativeQuery = true)
    Brand findByName(String nameBrand);
}
