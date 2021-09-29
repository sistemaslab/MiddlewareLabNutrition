package com.middleware.lab.repository;

import com.middleware.lab.model.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}