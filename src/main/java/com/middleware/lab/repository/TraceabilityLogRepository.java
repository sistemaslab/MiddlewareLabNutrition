package com.middleware.lab.repository;

import com.middleware.lab.model.db.TraceabilityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TraceabilityLogRepository extends JpaRepository<TraceabilityLog, Long> {
    @Query(value = "SELECT * FROM billing WHERE order_id = ?1", nativeQuery = true)
    public String getSequence(String orderId);

}