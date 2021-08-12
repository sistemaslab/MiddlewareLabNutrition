package com.middleware.lab.repository;

import com.middleware.lab.model.db.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {

}