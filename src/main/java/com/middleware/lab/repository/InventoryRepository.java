package com.middleware.lab.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Inventory;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface InventoryRepository extends CrudRepository<Inventory, Integer> {
	@Query(value = "SELECT * FROM lab_nutrition_db.inventory INNER JOIN coolbox_db.sku ON  inventory.sku_id_FK = sku.sku_id WHERE sku.product_id = ?1;", nativeQuery = true)
	Inventory findByIdProduct(String idProduct);
	@Query(value = "SELECT * FROM lab_nutrition_db.inventory where sku_id_fk = ?1 and warehouse_id= ?2 and is_synchronized_vtex = 1 and  timestamp = (select max(timestamp)  FROM lab_nutrition_db.inventory where sku_id_fk = ?1 and warehouse_id= ?2 and is_synchronized_vtex = 1); ", nativeQuery = true)
	Inventory findTimestamp(String idProduct,String warehouse);
	@Query(value = "SELECT count(*) FROM lab_nutrition_db.inventory where sku_id_fk = ?1 and warehouse_id= ?2 and is_synchronized_vtex = 1", nativeQuery = true)
	long count(String idProduct,String warehouse);
	@Query(value = "SELECT * FROM lab_nutrition_db.inventory where sku_id_fk = ?1  and  timestamp = (select max(timestamp)  FROM lab_nutrition_db.inventory where sku_id_fk = ?1 );", nativeQuery = true)
	
        List<Inventory> searchInventory(String idProduct);
	} 