package com.middleware.lab.repository;
import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.Billing;
import org.springframework.data.jpa.repository.Query;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface BillingRepository extends CrudRepository<Billing, Integer> {
	@Query(value = "SELECT * FROM lab_nutrition_db.billing WHERE order_id = ?1", nativeQuery = true)
	Billing findByOrderId(String code);
        
        
} 