package com.example.dbx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.dbx.model.BusinessComponent;

@Repository
public interface BusinessComponentRepository extends JpaRepository<BusinessComponent, Long> {
    // Optional<OrgUnit> findByName(String name);
    Page<BusinessComponent> findAll(Pageable page);

    Page<BusinessComponent> findByOrgUnitId(Long id, Pageable page);

    Optional<BusinessComponent> findOneByIdAndOrgUnitId(Long id, Long orgUnitId);

    Optional<BusinessComponent> findOneById(Long id);

    BusinessComponent findByName(String name);

    BusinessComponent findByNameAndOrgUnitIdAndIsEnabled(String name, Long id, Boolean isEnabled);

    Boolean existsByName(String name);
}