package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.Client;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query(value = "SELECT * FROM lab_nutrition_db.client WHERE order_id = ?1", nativeQuery = true)
    Client findByOrderId(String code);
}
