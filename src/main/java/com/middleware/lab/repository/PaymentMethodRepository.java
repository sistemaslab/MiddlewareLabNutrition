package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.PaymentMethod;
import org.springframework.data.jpa.repository.Query;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {

    @Query(value = "SELECT * FROM lab_nutrition_db.payment_method p where p.system_vtex = ?1", nativeQuery = true)
    PaymentMethod findBySystem(String idSystem);
}
