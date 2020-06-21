package com.example.dbx.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.ParseException;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;
import com.example.dbx.model.ExceptionSummary;
import com.example.dbx.model.OldExceptionBean;
import com.example.dbx.model.OldExceptionsResult;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.ExceptionSummaryRepository;
import com.example.dbx.repository.OldExceptionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ExceptionServiceTest {
	
	/************************************************************************************************************/
	//MOCKS AND SIMILAR ACTIONS
	
	@InjectMocks
	private ExceptionService exceptionService;
	
	@Mock
	private ExceptionSummaryRepository exceptionSummaryRepository;
	
	@Mock
	private ExceptionRepository exceptionRepository;
	
	@Mock 
	private BusinessComponentRepository businessComponentRepository;
	
	@Mock
	private OldExceptionRepository oldExceptionRepository;
	
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    /*******************************************************************************************************************/
    //TESTS
    
    @Test
    void testGetExceptionSummary() throws ParseException {
    	Long dummyOrgUnitId=1l;
    	Long dummyCount = 2L;
    	List<Object[]> dummyObjList = new ArrayList<>();
    	
    	when(exceptionSummaryRepository.findTotalExceptions(anyLong())).thenReturn(dummyCount);
    	when(exceptionSummaryRepository.findTotalUnresolvedExceptions(anyLong())).thenReturn(dummyCount);
    	when(exceptionSummaryRepository.findTotalResolvedExceptions(anyLong())).thenReturn(dummyCount);
    	when(exceptionSummaryRepository.findTotalLowSeverityExceptions(anyLong())).thenReturn(dummyCount);
    	when(exceptionSummaryRepository.findTotalMediumSeverityExceptions(anyLong())).thenReturn(dummyCount);
    	when(exceptionSummaryRepository.findTotalHighSeverityExceptions(anyLong())).thenReturn(dummyCount);
    	
		when(exceptionSummaryRepository.findExceptionCountByCategory(anyLong())).thenReturn(dummyObjList);
		when(exceptionSummaryRepository.findExceptionCountByDate(any(Date.class), any(Date.class) , anyLong())).thenReturn(dummyObjList);
		
		ExceptionSummary exceptionSummary = exceptionService.getExceptionSummary(dummyOrgUnitId);
		assertNotNull(exceptionSummary);
    }
    
    @Test
    void testGetExceptionVersions() {
    	
    	AcceptedExceptionBean dummyAcceptedExceptionBean = createAcceptedExceptionBean();
    	
    	Optional<AcceptedExceptionBean> dummyOptionalAcceptedExceptionBean = Optional.of(dummyAcceptedExceptionBean);
    	
    	when(exceptionRepository.findByIdAndOrgUnitId(anyLong() , anyLong())).thenReturn(dummyOptionalAcceptedExceptionBean);
    	when(oldExceptionRepository.findByExceptionId(anyLong(), any(Pageable.class))).thenReturn(dummyOldExceptionBeanPage());
    	
    	OldExceptionsResult res = exceptionService.getExceptionVersions(1l , 5l, 0 , 5);
    	assertEquals(res.getOldExceptions() , dummyOldExceptionBeanPage().getContent());
    	//assertEquals(res.getTotalElements() , dummyPage().getTotalElements()); //successful
    }
    
    /*******************************************************************************************************************/
    //HELPER FUNCTIONS
    
    //used by testGetExceptionVersions()
    private AcceptedExceptionBean createAcceptedExceptionBean() {
    	OrgUnit dummyOrgUnit = new OrgUnit("HR");
    	dummyOrgUnit.setId(5l);
    	
    	BusinessComponent dummyBusinessComponent = new BusinessComponent("component1" , dummyOrgUnit);
    	AcceptedExceptionBean dummyAcceptedExceptionBean = new AcceptedExceptionBean(
    			1l, //id
    			new Timestamp(System.currentTimeMillis()), //timestamp
    			"App1", //source
    			"runtime", //category
    			"sample description", //description
    			ExceptionSeverity.SEVERITY_LOW, //severity (enum)
    			dummyBusinessComponent, //business component
    			dummyOrgUnit, //org unit
    			"sample technical description", //technical description
    			ExceptionStatus.STATUS_UNRESOLVED, //exception status (enum)
    			null, //update timestamp
    			null //comment
    			);
    	
    	return dummyAcceptedExceptionBean;
    }
    
    //used by testGetExceptionVersions()
    private Page<OldExceptionBean> dummyOldExceptionBeanPage() {
        List<OldExceptionBean> oldExceptionBeans = new ArrayList<>();
        
    	OrgUnit dummyOrgUnit = new OrgUnit("HR");
    	dummyOrgUnit.setId(5l);
        BusinessComponent dummyBusinessComponent = new BusinessComponent("component1" , dummyOrgUnit);

        oldExceptionBeans.add(new OldExceptionBean(
        		10l, //version primary key
        		1l, //id of exception
        		2, //int version
        		ExceptionSeverity.SEVERITY_LOW, //exception severity enum
        		dummyBusinessComponent, //business component
        		"sample technical description", //technical description
        		ExceptionStatus.STATUS_UNRESOLVED,
        		null, //timestamp
        		null
        		));
        
        oldExceptionBeans.add(new OldExceptionBean(
        		11l, //version primary key
        		1l, //id of exception
        		1, //int version
        		ExceptionSeverity.SEVERITY_MEDIUM, //exception severity enum
        		dummyBusinessComponent, //business component
        		"sample technical description 2", //technical description
        		ExceptionStatus.STATUS_UNRESOLVED,
        		null, //timestamp
        		null
        		));

        return new PageImpl<>(oldExceptionBeans);
    }
    /**********************************************************************************************************************/
}
