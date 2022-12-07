package com.spring.cinemacity.repository;


import com.spring.cinemacity.model.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection,Long> {
}
