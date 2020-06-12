package com.example.dbx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.BusinessComponent;

@Repository
public interface BusinessComponentRepository extends JpaRepository<BusinessComponent, Long> {
    // Optional<OrgUnit> findByName(String name);
    Page<BusinessComponent> findAll(Pageable page);

    BusinessComponent findByName(String name);

    Boolean existsByName(String name);
}