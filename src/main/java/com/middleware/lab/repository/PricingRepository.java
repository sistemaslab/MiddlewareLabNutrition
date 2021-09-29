package com.middleware.lab.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Price;;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PricingRepository extends CrudRepository<Price, Integer> {
	@Query(value = "SELECT * FROM lab_nutrition_db.price WHERE sku_id_FK = ?1", nativeQuery = true)
	Price findByIdProduct(String idProduct);
	@Query(value = "SELECT * FROM lab_nutrition_db.price where sku_id_fk = ?1 and is_synchronized_vtex = 1 and  timestamp = (select max(timestamp)  FROM lab_nutrition_db.price where sku_id_fk = ?1 and is_synchronized_vtex = 1); ", nativeQuery = true)
	Price findTimestamp(String idProduct);
	@Query(value = "SELECT count(*) FROM lab_nutrition_db.price where sku_id_fk = ?1 and is_synchronized_vtex = 1", nativeQuery = true)
	long count(String idProduct);
	@Query(value = "SELECT * FROM lab_nutrition_db.price where sku_id_fk = ?1  and  timestamp = (select max(timestamp)  FROM lab_nutrition_db.price where sku_id_fk = ?1 );", nativeQuery = true)
	Price searchPricing(String idProduct);
}

