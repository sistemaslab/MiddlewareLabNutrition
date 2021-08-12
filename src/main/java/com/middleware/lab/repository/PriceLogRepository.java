package com.middleware.lab.repository;



import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.PriceLog;

public interface PriceLogRepository extends CrudRepository<PriceLog, Integer> {
	
} 