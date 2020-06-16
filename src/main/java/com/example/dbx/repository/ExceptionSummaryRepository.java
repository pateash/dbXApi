package com.example.dbx.repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dbx.model.AcceptedExceptionBean;

//This repository find the summary counts for various fields
@Repository
public interface ExceptionSummaryRepository extends JpaRepository<AcceptedExceptionBean , Long>{

	@Query(value="SELECT COUNT(*) FROM exceptions WHERE org_unit_id=?1" , nativeQuery = true)
	Long findTotalExceptions(Long org_unit_id);
	
	@Query(value="SELECT COUNT(status) FROM exceptions WHERE (org_unit_id=?1 AND status = 0)" , nativeQuery = true)
	Long findTotalUnresolvedExceptions(Long org_unit_id);
	
	@Query(value="SELECT COUNT(status) FROM exceptions WHERE (org_unit_id=?1 AND status = 1)" , nativeQuery = true)
	Long findTotalResolvedExceptions(Long org_unit_id);
	
	@Query(value="SELECT COUNT(*) FROM exceptions WHERE (org_unit_id = ?1 AND severity = 0 )" , nativeQuery = true)
	Long findTotalLowSeverityExceptions(Long org_unit_id);
	
	@Query(value="SELECT COUNT(*) FROM exceptions WHERE (org_unit_id = ?1 AND severity = 1 )" , nativeQuery = true)
	Long findTotalMediumSeverityExceptions(Long org_unit_id);
	
	@Query(value="SELECT COUNT(*) FROM exceptions WHERE (org_unit_id = ?1 AND severity = 2 )" , nativeQuery = true)
	Long findTotalHighSeverityExceptions(Long org_unit_id);
	
	@Query(value="SELECT  category , COUNT(*) FROM exceptions WHERE org_unit_id = ?1 GROUP BY category" , nativeQuery = true)
	public List<Object[]> findExceptionCountByCategory(Long org_unit_id);
	
}
