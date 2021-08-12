package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.Items;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface ItemsRepository extends CrudRepository<Items, Integer> {

    @Query(value = "SELECT * FROM lab_nutrition_db.items a WHERE a.order_id_fk = ?1", nativeQuery = true)
    List<Items> findAllByIdOrder(String idOrder);
}
