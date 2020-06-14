package com.example.dbx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dbx.model.OldExceptionBean;

public interface OldExceptionRepository extends JpaRepository<OldExceptionBean , Long>{

}
