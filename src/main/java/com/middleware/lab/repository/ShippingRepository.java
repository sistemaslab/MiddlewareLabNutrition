package com.middleware.lab.repository;
import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.Shipping;
import org.springframework.data.jpa.repository.Query;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ShippingRepository extends CrudRepository<Shipping, Integer> {
	@Query(value = "SELECT * FROM lab_nutrition_db.shipping WHERE order_id = ?1", nativeQuery = true)
	Shipping findByOrderId(String code);
	
} 