package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.message.request.LoginForm;
import com.example.dbx.message.request.SignupForm;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@MockBean
	private JwtProvider jwtProvider;

	@Test
	void testGetAllOrgUnits() throws Exception {
		List<OrgUnit> orgUnits = new ArrayList<>();

		orgUnits.add(OrgUnit.builder().id(1l).name("TEST").build());

		given(authService.getAllOrgUnits()).willReturn(orgUnits);

		mockMvc.perform(get("/auth/orgUnit").contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	void testSignUp() throws Exception {
		given(authService.registerUser(any(SignupForm.class))).willReturn(AuthService.USER_REGISTERED_SUCCESSFULLY_MSG);

		SignupForm form = new SignupForm("name", "username", 1l, "123456");

		mockMvc.perform(post("/auth/signup").contentType("application/json").characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(form))).andExpect(status().isOk());

	}

	@Test
	void testSignUpAndPasswordIncorrect() throws Exception {
		given(authService.registerUser(any(SignupForm.class))).willReturn(AuthService.USER_REGISTERED_SUCCESSFULLY_MSG);

		SignupForm form = new SignupForm("name", "username", 1l, "1234");

		mockMvc.perform(post("/auth/signup").contentType("application/json").characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(form))).andExpect(status().isBadRequest());

	}

	@Test
	void testSignIn() throws Exception {
		given(authService.findUserByUsername(anyString()))
				.willReturn(Optional.of(User.builder().username("test").role(UserRole.ROLE_USER).build()));

		LoginForm form = new LoginForm("username", "123456");

		mockMvc.perform(post("/auth/signin").contentType("application/json").characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(form))).andExpect(status().isInternalServerError());

	}

}
