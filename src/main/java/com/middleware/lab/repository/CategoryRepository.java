package com.middleware.lab.repository;

import org.springframework.data.repository.CrudRepository;

import com.middleware.lab.model.db.Category;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends CrudRepository<Category, String> {

    @Query(value = "SELECT * FROM lab_nutrition_db.category WHERE category_id = ?1", nativeQuery = true)
    Category findByIdCategory(String idCategory);
    
    @Query(value = "SELECT * FROM lab_nutrition_db.category WHERE category_name = ?1", nativeQuery = true)
    Category findByName(String nameCategory);
}
