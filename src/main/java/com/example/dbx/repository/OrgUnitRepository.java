package com.example.dbx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.dbx.model.OrgUnit;

@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long> {
//    Optional<OrgUnit> findByName(String name);
    
    OrgUnit findByName(String name);

    Boolean existsByName(String name);
}