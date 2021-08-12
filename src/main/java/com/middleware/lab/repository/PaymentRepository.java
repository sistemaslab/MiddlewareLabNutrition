package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.Payment;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    @Query(value = "SELECT * FROM lab_nutrition_db.payment WHERE order_id = ?1", nativeQuery = true)
    Payment findByOrderId(String code);

}
