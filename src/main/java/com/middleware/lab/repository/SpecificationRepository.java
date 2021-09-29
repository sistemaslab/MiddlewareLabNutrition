package com.middleware.lab.repository;

import com.middleware.lab.model.db.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpecificationRepository extends JpaRepository<Specification, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.specification WHERE id_product = ?1", nativeQuery = true)
    public Specification findByProductId(String idProduct);
}
