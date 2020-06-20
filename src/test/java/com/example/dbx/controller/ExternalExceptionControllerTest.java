package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.ExternalException;
import com.example.dbx.service.ExternalExceptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ExternalExceptionController.class)
public class ExternalExceptionControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ExternalExceptionService externalExceptionService;
	
	@Test
	void whenValidInput_thenReturns200() throws Exception {
		ExternalException externalException = new ExternalException(
				"App1", //source
				"runtime", //category
				"sample description", //description
				"high", //severity
				"component1", //business component
				"HR", //org unit
				"Sample technical description" //technical description
				);
		
		mockMvc.perform(post("/exception")
		.contentType("application/json")
		.content(objectMapper.writeValueAsString(externalException)))
		.andExpect(status().isOk());
	}
}
