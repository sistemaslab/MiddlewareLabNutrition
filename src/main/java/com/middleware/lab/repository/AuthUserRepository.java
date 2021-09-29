package com.middleware.lab.repository;

import com.middleware.lab.model.db.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthUserRepository extends JpaRepository<AuthUser, String> {
    @Query(value = "SELECT * FROM shipping WHERE order_id = ?1", nativeQuery = true)
    public AuthUser findByName(String MIDDLEWARE_USER);

}