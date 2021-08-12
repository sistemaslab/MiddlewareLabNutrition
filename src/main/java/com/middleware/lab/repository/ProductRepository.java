package com.middleware.lab.repository;

import com.middleware.lab.model.db.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.product WHERE vtex_id = ?", nativeQuery = true)
    public Product findVtexId(String productId);

    @Query(value = "SELECT * FROM lab_nutrition_db.product WHERE ref_id = ?", nativeQuery = true)
    public Product findByRefId(String refId);

}
