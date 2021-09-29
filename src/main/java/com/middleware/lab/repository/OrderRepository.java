package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;
import com.middleware.lab.model.db.OrderLab;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface OrderRepository extends CrudRepository<OrderLab,Integer> {

    @Query(value = "SELECT * FROM lab_nutrition_db.order_lab o WHERE o.order_num  = ?1", nativeQuery = true)
    OrderLab findByIdOrder(String idOrder);
    
     @Query(value = "SELECT * FROM lab_nutrition_db.order_lab o WHERE o.is_validate  = 0 and o.order_status = 'invoiced'", nativeQuery = true)
    List<OrderLab> findAlltoValidate();
    
    @Query(value = "SELECT * FROM lab_nutrition_db.order_lab o WHERE o.order_status  = 'ready-for-handling' ", nativeQuery = true)
    List<OrderLab> findOrdersToInvoiced();
    
    @Query(value = "SELECT * FROM lab_nutrition_db.order_lab o WHERE o.order_status  = 'invoiced' and created_date > '2020-11-06'", nativeQuery = true)
    List<OrderLab> findOrdersToDelivered();
}
