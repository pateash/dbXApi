package com.example.dbx.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.ExceptionBean;

@Repository
public interface ExceptionRepository extends JpaRepository<ExceptionBean , Integer>{
	List<ExceptionBean> findAllByCategory(String category , Pageable pageable);
	
	List<ExceptionBean> findAllBySource(String source , Pageable pageable);
	
	
}
