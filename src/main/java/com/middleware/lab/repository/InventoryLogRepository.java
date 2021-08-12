package com.middleware.lab.repository;



import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.InventoryLog;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface InventoryLogRepository extends CrudRepository<InventoryLog, Integer> {
	
} 