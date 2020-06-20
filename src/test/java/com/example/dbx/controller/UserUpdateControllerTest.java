package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.User;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserUpdateController.class)
public class UserUpdateControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@MockBean
	private JwtProvider jwtProvider;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testUpdateUser() throws Exception {
		User user = User.builder().id(1l).name("TEST").build();
		given(userService.updateUser(any(Long.class), any(User.class))).willReturn(user);

		mockMvc.perform(patch("/api/user/1").contentType("application/json").characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk());
	}
}
