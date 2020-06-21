package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.model.RejectedExceptionsResult;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.RejectedExceptionsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RejectedExceptionController.class)
public class RejectedExceptionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RejectedExceptionsService rejectedExceptionsService;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@MockBean
	private JwtProvider jwtProvider;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testGetAllRejectedExceptions() throws Exception {
		List<RejectedExceptionBean> rejectedExceptionBeans = new ArrayList<>();

		rejectedExceptionBeans.add(RejectedExceptionBean.builder().id(1l).build());

		RejectedExceptionsResult res = new RejectedExceptionsResult(rejectedExceptionBeans,
				new Long(rejectedExceptionBeans.size()));

		given(rejectedExceptionsService.exceptions(any(), any(), any(), any(), any())).willReturn(res);

		ExceptionFilter filter = new ExceptionFilter("severity", "severityOrder", "status", "category", "source");

		mockMvc.perform(
				get("/api/rejectedException?page=2&pageSize=7&filter={filter}", objectMapper.writeValueAsString(filter))
						.contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.totalElements", is(rejectedExceptionBeans.size())));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testGetRejectedException() throws Exception {
		RejectedExceptionBean bean = RejectedExceptionBean.builder().id(1l).build();
		given(rejectedExceptionsService.getExceptionBean(any(Long.class))).willReturn(bean);

		mockMvc.perform(get("/api/rejectedException/1").contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDeleteRejectedException() throws Exception {
		RejectedExceptionBean bean = RejectedExceptionBean.builder().id(1l).build();
		given(rejectedExceptionsService.updateExceptionBean(any(Long.class))).willReturn(bean);

		mockMvc.perform(delete("/api/rejectedException/1").contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk());
	}
}
