package com.example.dbx.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

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
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.model.RejectedExceptionsResult;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.RejectedExceptionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RejectedExceptionsServiceTest {
	@InjectMocks
	private RejectedExceptionsService rejectedExceptionsService;

	@Mock
	private RejectedExceptionRepository rejectedExceptionRepository;

	@Mock
	private ExceptionRepository exceptionRepository;

	@Mock
	private BusinessComponentRepository businessComponentRepository;

	@Mock
	private OrgUnitRepository orgUnitRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	private Page<RejectedExceptionBean> dummyRejectedEXceptionsPage() {
		List<RejectedExceptionBean> rejectedExceptionBeans = new ArrayList<>();

		rejectedExceptionBeans.add(RejectedExceptionBean.builder().category("category")
				.businessComponent("businessComponent").id(1l).build());

		rejectedExceptionBeans.add(RejectedExceptionBean.builder().category("category")
				.businessComponent("businessComponent").id(2l).build());

		return new PageImpl<>(rejectedExceptionBeans);
	}

	@Test
	void testGetAllExceptionsWithNoFilter() {
		when(rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(anyString(),
				anyString(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions("id", "ASC", 0, 5,
				ExceptionFilter.builder().category("").source("").build());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	void testGetAllExceptionsWithNoFilterNoSort() {
		when(rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(anyString(),
				anyString(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions(null, null, 0, 5, new ExceptionFilter());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	void testGetAllExceptionsWithFilterSeverityOrderNoSort() {
		when(rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(anyString(),
				anyString(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions(null, null, 0, 5,
				ExceptionFilter.builder().severityOrder("ASC").build());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	void testGetAllExceptionsWithFilterSeverity() {
		when(rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverity(
				anyString(), anyString(), any(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions(null, null, 0, 5,
				ExceptionFilter.builder().severity("high").build());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	void testGetAllExceptionsWithFilterStatus() {
		when(rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatus(
				anyString(), anyString(), any(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions(null, null, 0, 5,
				ExceptionFilter.builder().status("resolved").build());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	void testGetAllExceptionsWithFilterSeverityAndStatus() {
		when(rejectedExceptionRepository
				.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatus(anyString(),
						anyString(), any(), any(), any(Pageable.class))).thenReturn(dummyRejectedEXceptionsPage());

		RejectedExceptionsResult res = rejectedExceptionsService.exceptions(null, null, 0, 5,
				ExceptionFilter.builder().severity("high").status("resolved").build());

		assertNotNull(res);
		assertNotNull(res.getRejectedExceptions());
		assertEquals(2l, res.getTotalElements());
	}

	@Test
	public void testGetException() {
		when(rejectedExceptionRepository.findById(any(Long.class)))
				.thenReturn(Optional.of(RejectedExceptionBean.builder().id(1l).category("category").build()));

		RejectedExceptionBean res = rejectedExceptionsService.getExceptionBean(1l);

		assertNotNull(res);
	}

	@Test
	public void testGetExceptionNull() {
		Long id = 1l;

		when(rejectedExceptionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		ExceptionNotFound exception = assertThrows(ExceptionNotFound.class, () -> {
			rejectedExceptionsService.getExceptionBean(1l);
		});

		String expectedMessage = ExceptionService.notExistsMsg(id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testUpdateExceptionNull() {
		Long id = 1l;

		when(rejectedExceptionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		ExceptionNotFound exception = assertThrows(ExceptionNotFound.class, () -> {
			rejectedExceptionsService.updateExceptionBean(1l);
		});

		String expectedMessage = ExceptionService.notExistsMsg(id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	/*
	 * @Test public void testUpdateException() {
	 * when(rejectedExceptionRepository.findById(any(Long.class))).thenReturn(
	 * Optional.of(RejectedExceptionBean.builder().id(1l).category("category").
	 * source("source").build()));
	 * when(orgUnitRepository.findByName(anyString())).thenReturn(new OrgUnit(1l,
	 * "name"));
	 * 
	 * RejectedExceptionBean res =
	 * rejectedExceptionsService.updateExceptionBean(1l);
	 * 
	 * assertNotNull(res); }
	 */
}
