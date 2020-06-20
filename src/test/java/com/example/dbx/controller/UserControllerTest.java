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
import com.example.dbx.model.UserRole;
import com.example.dbx.model.UsersResult;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

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
	void testGetAllUsers() throws Exception {
		List<User> users = new ArrayList<>();

		users.add(User.builder().id(1l).name("TEST").build());

		UsersResult res = new UsersResult(users, new Long(users.size()));

		given(userService.getAllUsers(2, 7, UserRole.ROLE_USER, null)).willReturn(res);

		mockMvc.perform(get("/api/user?page=2&pageSize=7").contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.totalElements", is(users.size())));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testGetUser() throws Exception {
		User user = User.builder().id(1l).name("TEST").build();
		given(userService.getUserById(any(Long.class))).willReturn(user);

		mockMvc.perform(get("/api/user/1").contentType("application/json").characterEncoding("utf-8"))
				.andExpect(status().isOk());
	}
}
