package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.BusinessComponentsResult;
import com.example.dbx.model.UserRole;
import com.example.dbx.security.WithMockCustomUser;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.BusinessComponentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BusinessComponentController.class)
public class BusinessComponentControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private BusinessComponentService businessComponentService;

        @MockBean
        private UserDetailsServiceImpl userDetailsService;

        @MockBean
        private JwtAuthEntryPoint jwtAuthEntryPoint;

        @MockBean
        private JwtProvider jwtProvider;

        @WithMockCustomUser(name = "admin", orgUnitId = 1l, role = "ROLE_ADMIN", username = "admin")
        @Test
        void testGetAllBusinessComponents() throws Exception {
                List<BusinessComponent> businessComponents = new ArrayList<>();

                businessComponents.add(BusinessComponent.builder().id(1l).name("TEST").build());

                BusinessComponentsResult res = new BusinessComponentsResult(businessComponents,
                                new Long(businessComponents.size()));

                given(businessComponentService.getAllBusinessComponent(2, 7, UserRole.ROLE_ADMIN, 1l)).willReturn(res);

                mockMvc.perform(get("/api/businessComponent?page=2&pageSize=7").contentType("application/json")
                                .characterEncoding("utf-8")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements", is(businessComponents.size())));
        }

        @WithMockCustomUser(name = "admin", orgUnitId = 1l, role = "ROLE_ADMIN", username = "admin")
        @Test
        void testGetBusinessComponent() throws Exception {
                BusinessComponent businessComponent = BusinessComponent.builder().id(1l).name("TEST").build();

                given(businessComponentService.getBusinessComponent(any(), any(), any())).willReturn(businessComponent);

                mockMvc.perform(get("/api/businessComponent/1").contentType("application/json")
                                .characterEncoding("utf-8")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)));
        }

        @WithMockCustomUser(role = "ROLE_USER")
        @Test
        void testAddBusinessComponent() throws Exception {
                BusinessComponent businessComponent = BusinessComponent.builder().name("test").build();

                given(businessComponentService.addBusinessComponent(any(BusinessComponent.class), anyLong()))
                                .willReturn(businessComponent);

                mockMvc.perform(post("/api/businessComponent").contentType("application/json")
                                .characterEncoding("utf-8").content(objectMapper.writeValueAsString(businessComponent)))
                                .andExpect(status().isOk());
        }

        @WithMockCustomUser(name = "admin", orgUnitId = 1l, role = "ROLE_ADMIN", username = "admin")
        @Test
        void testUpdateBusinessComponent() throws Exception {
                BusinessComponent businessComponent = BusinessComponent.builder().id(1l).name("TEST").build();

                given(businessComponentService.updateBusinessComponent(any(), any())).willReturn(businessComponent);

                mockMvc.perform(patch("/api/businessComponent/1").contentType("application/json")
                                .characterEncoding("utf-8").content(objectMapper.writeValueAsString(businessComponent)))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
        }

}
