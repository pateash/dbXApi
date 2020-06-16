package com.example.dbx.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.dbx.model.OldExceptionBean;

public interface OldExceptionRepository extends JpaRepository<OldExceptionBean, Long> {
    Page<OldExceptionBean> findByExceptionId(Long id, Pageable pageable);

    List<OldExceptionBean> findByExceptionId(Long id);

    Long countByExceptionId(Long id);
}
