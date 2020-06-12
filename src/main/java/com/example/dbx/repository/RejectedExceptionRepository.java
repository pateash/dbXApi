package com.example.dbx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;
import com.example.dbx.model.RejectedExceptionBean;

@Repository
public interface RejectedExceptionRepository extends JpaRepository<RejectedExceptionBean, Long> {
    Page<RejectedExceptionBean> findByCategoryContaining(String category, Pageable pageable);

    Page<RejectedExceptionBean> findByCategory(String category, Pageable pageable);

    Page<RejectedExceptionBean> findBySourceContaining(String source, Pageable pageable);

    Page<RejectedExceptionBean> findBySource(String source, Pageable pageable);

    Page<RejectedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(String source,
            String category, Pageable pageable);

    Page<RejectedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverity(
            String source, String category, ExceptionSeverity severity, Pageable pageable);

    Page<RejectedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatus(
            String source, String category, ExceptionSeverity severity, ExceptionStatus status, Pageable pageable);

    Page<RejectedExceptionBean> findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatus(String source,
            String category, ExceptionStatus status, Pageable pageable);

    Page<RejectedExceptionBean> findBySeverity(ExceptionSeverity severity, Pageable pageable);
}
