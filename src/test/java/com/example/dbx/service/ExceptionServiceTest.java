package com.example.dbx.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
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

import com.example.dbx.exception.ExceptionNotFound;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.ExceptionBeanUpdate;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionServiceTest {

	/************************************************************************************************************/
	// MOCKS AND SIMILAR ACTIONS

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
	// TESTS

	@Test
	void testGetExceptionSummary() throws ParseException {
		Long dummyOrgUnitId = 1l;
		Long dummyCount = 2L;
		List<Object[]> dummyObjList = new ArrayList<>();

		when(exceptionSummaryRepository.findTotalExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalUnresolvedExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalResolvedExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalLowSeverityExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalMediumSeverityExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalHighSeverityExceptions(anyLong())).thenReturn(dummyCount);

		when(exceptionSummaryRepository.findExceptionCountByCategory(anyLong())).thenReturn(dummyObjList);
		when(exceptionSummaryRepository.findExceptionCountByDate(any(Date.class), any(Date.class), anyLong()))
				.thenReturn(dummyObjList);

		ExceptionSummary exceptionSummary = exceptionService.getExceptionSummary(dummyOrgUnitId);
		assertNotNull(exceptionSummary);

	}

	@Test
	void testGetExceptionSummaryWithList() throws ParseException {
		Long dummyOrgUnitId = 1l;
		Long dummyCount = 2L;
		List<Object[]> dummyObjListCategory = new ArrayList<>();
		List<Object[]> dummyObjListDate = new ArrayList<>();

		Object[] dummyDataCategory = { "1", BigInteger.valueOf(1l) };
		Object[] dummyDataDate = { 1, BigInteger.valueOf(1l) };

		dummyObjListCategory.add(dummyDataCategory);
		dummyObjListDate.add(dummyDataDate);

		when(exceptionSummaryRepository.findTotalExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalUnresolvedExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalResolvedExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalLowSeverityExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalMediumSeverityExceptions(anyLong())).thenReturn(dummyCount);
		when(exceptionSummaryRepository.findTotalHighSeverityExceptions(anyLong())).thenReturn(dummyCount);

		when(exceptionSummaryRepository.findExceptionCountByCategory(anyLong())).thenReturn(dummyObjListCategory);
		when(exceptionSummaryRepository.findExceptionCountByDate(any(Date.class), any(Date.class), anyLong()))
				.thenReturn(dummyObjListDate);

		ExceptionSummary exceptionSummary = exceptionService.getExceptionSummary(dummyOrgUnitId);
		assertNotNull(exceptionSummary);

	}

	@Test
	void testGetExceptionVersions() {
		Long id = 1l;

		AcceptedExceptionBean dummyAcceptedExceptionBean = createAcceptedExceptionBean(id);

		Optional<AcceptedExceptionBean> dummyOptionalAcceptedExceptionBean = Optional.of(dummyAcceptedExceptionBean);

		when(exceptionRepository.findByIdAndOrgUnitId(anyLong(), anyLong()))
				.thenReturn(dummyOptionalAcceptedExceptionBean);
		when(oldExceptionRepository.findByExceptionId(anyLong(), any(Pageable.class)))
				.thenReturn(dummyOldExceptionBeanPage());

		OldExceptionsResult res = exceptionService.getExceptionVersions(1l, 5l, 0, 5);
		assertEquals(res.getOldExceptions(), dummyOldExceptionBeanPage().getContent());
		assertEquals(res.getTotalElements(), dummyOldExceptionBeanPage().getTotalElements());
	}

	@Test
	void testUpdateExceptionBean() {
		Long id = 1l;
		// Long dummyOrgUnitId = 5l;

		AcceptedExceptionBean dummyAcceptedExceptionBean = createAcceptedExceptionBean(id);
		Optional<AcceptedExceptionBean> dummyOptionalAcceptedExceptionBean = Optional.of(dummyAcceptedExceptionBean);

		ExceptionBeanUpdate dummyExceptionBeanUpdate = createDummyExceptionBeanUpdate();

		when(exceptionRepository.findByIdAndOrgUnitId(anyLong(), anyLong()))
				.thenReturn(dummyOptionalAcceptedExceptionBean);
		when(businessComponentRepository.findByIdAndOrgUnitIdAndIsEnabled(anyLong(), anyLong(), eq(true)))
				.thenReturn(dummyBusinessComponent());
		when(oldExceptionRepository.save(any(OldExceptionBean.class)))
				.thenReturn(getDummyOldExceptionBeanFromAcceptedExceptionBean(dummyAcceptedExceptionBean));

		dummyAcceptedExceptionBean.setSeverity(dummyExceptionBeanUpdate.getSeverity());
		dummyAcceptedExceptionBean.setStatus(dummyExceptionBeanUpdate.getStatus());
		dummyAcceptedExceptionBean.setBusinessComponent(dummyBusinessComponent());
		dummyAcceptedExceptionBean.setTechnicalDescription(dummyExceptionBeanUpdate.getTechnicalDescription());
		dummyAcceptedExceptionBean.setComment(dummyExceptionBeanUpdate.getComment());
		dummyAcceptedExceptionBean.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		when(exceptionRepository.save(any(AcceptedExceptionBean.class))).thenReturn(dummyAcceptedExceptionBean);

		AcceptedExceptionBean res = exceptionService.updateExceptionBean(id, dummyExceptionBeanUpdate,
				dummyBusinessComponent().getOrgUnit().getId());

		assertNotNull(getDummyOldExceptionBeanFromAcceptedExceptionBean(dummyAcceptedExceptionBean));
		assertEquals(res, dummyAcceptedExceptionBean);

	}

	@Test
	void testGetExceptionBean() {
		long id = 1l;
		long orgUnitId = 5l;

		OrgUnit dummyOrgUnit = new OrgUnit("HR");
		dummyOrgUnit.setId(orgUnitId);

		AcceptedExceptionBean dummyAcceptedExceptionBean = createAcceptedExceptionBean(id);
		dummyAcceptedExceptionBean.setOrgUnit(dummyOrgUnit);
		Optional<AcceptedExceptionBean> dummyOptionalAcceptedExceptionBean = Optional.of(dummyAcceptedExceptionBean);

		when(exceptionRepository.findByIdAndOrgUnitId(anyLong(), anyLong()))
				.thenReturn(dummyOptionalAcceptedExceptionBean);

		AcceptedExceptionBean res = exceptionService.getExceptionBean(id, orgUnitId);

		assertNotNull(res);
		assertEquals(id, res.getId());
		assertEquals(orgUnitId, res.getOrgUnit().getId());
	}

	@Test
	public void testUpdateUpdateExceptionNull() {
		Long id = 1l;
		ExceptionBeanUpdate eb = new ExceptionBeanUpdate();

		when(exceptionRepository.findByIdAndOrgUnitId(any(Long.class), anyLong())).thenReturn(Optional.empty());

		ExceptionNotFound exception = assertThrows(ExceptionNotFound.class, () -> {
			exceptionService.updateExceptionBean(1l, eb, 1l);
		});

		String expectedMessage = ExceptionService.notExistsMsg(id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testGetUpdateExceptionNull() {
		Long id = 1l;

		when(exceptionRepository.findByIdAndOrgUnitId(any(Long.class), anyLong())).thenReturn(Optional.empty());

		ExceptionNotFound exception = assertThrows(ExceptionNotFound.class, () -> {
			exceptionService.getExceptionBean(1l, 1l);
		});

		String expectedMessage = ExceptionService.notExistsMsg(id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testGetExceptionVersionsNull() {
		Long id = 1l;

		when(exceptionRepository.findByIdAndOrgUnitId(any(Long.class), anyLong())).thenReturn(Optional.empty());

		ExceptionNotFound exception = assertThrows(ExceptionNotFound.class, () -> {
			exceptionService.getExceptionVersions(1l, 1l, 1, 1);
		});

		String expectedMessage = ExceptionService.notExistsMsg(id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	/*******************************************************************************************************************/
	// HELPER FUNCTIONS

	private ExceptionBeanUpdate createDummyExceptionBeanUpdate() {

		ExceptionBeanUpdate dummyExceptionBeanUpdate = new ExceptionBeanUpdate(ExceptionSeverity.SEVERITY_HIGH,
				ExceptionStatus.STATUS_RESOLVED, dummyBusinessComponent(), "Changed Technical Description",
				"Changed Comment");

		return dummyExceptionBeanUpdate;
	}

	// Used by testUpdateExceptionBean()
	private OldExceptionBean getDummyOldExceptionBeanFromAcceptedExceptionBean(
			AcceptedExceptionBean dummyAcceptedExceptionBean) {
		OldExceptionBean dummyOldExceptionBean = new OldExceptionBean(null, dummyAcceptedExceptionBean.getId(), 0,
				dummyAcceptedExceptionBean.getSeverity(), dummyAcceptedExceptionBean.getBusinessComponent(),
				dummyAcceptedExceptionBean.getTechnicalDescription(), dummyAcceptedExceptionBean.getStatus(),
				new Timestamp(System.currentTimeMillis()), dummyAcceptedExceptionBean.getComment());

		return dummyOldExceptionBean;
	}

	// used by testGetExceptionVersions()
	private AcceptedExceptionBean createAcceptedExceptionBean(Long id) {
		OrgUnit dummyOrgUnit = new OrgUnit("HR");
		dummyOrgUnit.setId(5l);

		BusinessComponent dummyBusinessComponent = new BusinessComponent();
		dummyBusinessComponent.setName("component1");
		dummyBusinessComponent.setOrgUnit(dummyOrgUnit);

		AcceptedExceptionBean dummyAcceptedExceptionBean = new AcceptedExceptionBean(id, // id
				new Timestamp(System.currentTimeMillis()), // timestamp
				"App1", // source
				"runtime", // category
				"sample description", // description
				ExceptionSeverity.SEVERITY_LOW, // severity (enum)
				dummyBusinessComponent, // business component
				dummyOrgUnit, // org unit
				"sample technical description", // technical description
				ExceptionStatus.STATUS_UNRESOLVED, // exception status (enum)
				null, // update timestamp
				null // comment
		);

		return dummyAcceptedExceptionBean;
	}

	// used by testGetExceptionVersions()
	private Page<OldExceptionBean> dummyOldExceptionBeanPage() {
		List<OldExceptionBean> oldExceptionBeans = new ArrayList<>();

		OrgUnit dummyOrgUnit = new OrgUnit("HR");
		dummyOrgUnit.setId(5l);
		BusinessComponent dummyBusinessComponent = new BusinessComponent();
		dummyBusinessComponent.setName("component1");
		dummyBusinessComponent.setOrgUnit(dummyOrgUnit);

		oldExceptionBeans.add(new OldExceptionBean(10l, // version primary key
				1l, // id of exception
				2, // int version
				ExceptionSeverity.SEVERITY_LOW, // exception severity enum
				dummyBusinessComponent, // business component
				"sample technical description", // technical description
				ExceptionStatus.STATUS_UNRESOLVED, null, // timestamp
				null // comment
		));

		oldExceptionBeans.add(new OldExceptionBean(11l, // version primary key
				1l, // id of exception
				1, // int version
				ExceptionSeverity.SEVERITY_MEDIUM, // exception severity enum
				dummyBusinessComponent, // business component
				"sample technical description 2", // technical description
				ExceptionStatus.STATUS_UNRESOLVED, null, // timestamp
				null // comment
		));

		return new PageImpl<>(oldExceptionBeans);
	}

	private BusinessComponent dummyBusinessComponent() {
		OrgUnit dummyOrgUnit = new OrgUnit("HR");
		dummyOrgUnit.setId(5l);
		BusinessComponent dummyBusinessComponent = new BusinessComponent();
		dummyBusinessComponent.setName("component1");
		dummyBusinessComponent.setOrgUnit(dummyOrgUnit);

		return dummyBusinessComponent;
	}
	/**********************************************************************************************************************/
}
