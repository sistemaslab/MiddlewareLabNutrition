package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Warehouse;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends CrudRepository<Warehouse, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.warehouse WHERE warehouse_id = ?1", nativeQuery = true)
    Warehouse findByIdWarehouse(String idWarehouse);
}
