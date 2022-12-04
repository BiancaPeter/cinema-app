package com.spring.cinemacity.repository;

import com.spring.cinemacity.model.Role;
import com.spring.cinemacity.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleType(RoleType roleType);

}
