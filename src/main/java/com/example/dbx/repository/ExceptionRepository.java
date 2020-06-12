package com.example.dbx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;

@Repository
public interface ExceptionRepository extends JpaRepository<AcceptedExceptionBean, Long> {
	Optional<AcceptedExceptionBean> findByIdAndOrgUnitId(Long id, Long orgUnitId);

	Page<AcceptedExceptionBean> findByCategoryContaining(String category, Pageable pageable);

	Page<AcceptedExceptionBean> findByCategory(String category, Pageable pageable);

	Page<AcceptedExceptionBean> findBySourceContaining(String source, Pageable pageable);

	Page<AcceptedExceptionBean> findBySource(String source, Pageable pageable);

	Page<AcceptedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndOrgUnitId(
			String source, String category, Long id, Pageable pageable);

	Page<AcceptedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndOrgUnitId(
			String source, String category, ExceptionSeverity severity, Long id, Pageable pageable);

	Page<AcceptedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatusAndOrgUnitId(
			String source, String category, ExceptionSeverity severity, ExceptionStatus status, Long id,
			Pageable pageable);

	Page<AcceptedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatusAndOrgUnitId(
			String source, String category, ExceptionStatus status, Long id, Pageable pageable);

	Page<AcceptedExceptionBean> findBySeverityAndOrgUnitId(ExceptionSeverity severity, Long id, Pageable pageable);
}
