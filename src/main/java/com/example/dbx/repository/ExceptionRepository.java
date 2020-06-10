package com.example.dbx.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.AcceptedExceptionBean;

@Repository
public interface ExceptionRepository extends JpaRepository<AcceptedExceptionBean , Integer>{
	List<AcceptedExceptionBean> findAllByCategory(String category , Pageable pageable);
	
	List<AcceptedExceptionBean> findAllBySource(String source , Pageable pageable);
	
	List<AcceptedExceptionBean> findAllBySeverity(int severity , Pageable pageable);
	
}
