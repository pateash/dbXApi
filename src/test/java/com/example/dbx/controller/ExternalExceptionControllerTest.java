package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExternalException;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.ExternalExceptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ExternalExceptionController.class)
public class ExternalExceptionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ExternalExceptionService externalExceptionService;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@MockBean
	private JwtProvider jwtProvider;

	@Test
	void whenValidInput_thenReturns200() throws Exception {
		ExternalException externalException = new ExternalException("App1", // source
				"runtime", // category
				"sample description", // description
				"high", // severity
				"component1", // business component
				"HR", // org unit
				"Sample technical description" // technical description
		);

		given(externalExceptionService.addException(any())).willReturn(new AcceptedExceptionBean());

		mockMvc.perform(post("/exception").contentType("application/json").characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(externalException))).andExpect(status().isCreated());
	}
}
