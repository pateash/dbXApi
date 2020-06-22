package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExceptionBeanUpdate;
import com.example.dbx.model.ExceptionSummary;
import com.example.dbx.model.ExceptionsResult;
import com.example.dbx.model.OldExceptionBean;
import com.example.dbx.model.OldExceptionsResult;
import com.example.dbx.security.WithMockCustomUser;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.ExceptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ExceptionController.class)
@WithMockCustomUser(name = "hr", orgUnitId = 1l, role = "ROLE_USER", username = "hr")
public class ExceptionControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private ExceptionService exceptionService;

        @MockBean
        private UserDetailsServiceImpl userDetailsService;

        @MockBean
        private JwtAuthEntryPoint jwtAuthEntryPoint;

        @MockBean
        private JwtProvider jwtProvider;

        @Test
        void testGetAllExceptions() throws Exception {
                List<AcceptedExceptionBean> acceptedExceptionBeans = new ArrayList<>();

                acceptedExceptionBeans.add(AcceptedExceptionBean.builder().id(1l).build());

                ExceptionsResult res = new ExceptionsResult(acceptedExceptionBeans,
                                new Long(acceptedExceptionBeans.size()));

                given(exceptionService.exceptions(any(), any(), any(), any(), any(), any())).willReturn(res);

                mockMvc.perform(get("/api/exception?page=2&pageSize=7").contentType("application/json")
                                .characterEncoding("utf-8")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements", is(acceptedExceptionBeans.size())));
        }

        @Test
        void testGetException() throws Exception {
                AcceptedExceptionBean acceptedExceptionBean = AcceptedExceptionBean.builder().id(1l).build();

                given(exceptionService.getExceptionBean(any(), any())).willReturn(acceptedExceptionBean);

                mockMvc.perform(get("/api/exception/1").contentType("application/json").characterEncoding("utf-8"))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
        }

        @Test
        void testUpdateException() throws Exception {
                AcceptedExceptionBean acceptedExceptionBean = AcceptedExceptionBean.builder().id(1l).build();

                given(exceptionService.updateExceptionBean(any(), any(), any())).willReturn(acceptedExceptionBean);

                mockMvc.perform(patch("/api/exception/1").contentType("application/json").characterEncoding("utf-8")
                                .content(objectMapper.writeValueAsString(new ExceptionBeanUpdate())))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
        }

        @Test
        void testGetOldException() throws Exception {
                List<OldExceptionBean> oldExceptionBeans = new ArrayList<>();

                oldExceptionBeans.add(OldExceptionBean.builder().id(1l).build());

                OldExceptionsResult res = new OldExceptionsResult(oldExceptionBeans,
                                new Long(oldExceptionBeans.size()));

                given(exceptionService.getExceptionVersions(any(), any(), any(), any())).willReturn(res);

                mockMvc.perform(get("/api/oldException/1").contentType("application/json").characterEncoding("utf-8"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements", is(oldExceptionBeans.size())));
        }

        @Test
        void testExceptionSummary() throws Exception {
                ExceptionSummary summary = new ExceptionSummary();

                given(exceptionService.getExceptionSummary(any())).willReturn(summary);

                mockMvc.perform(get("/api/exception/summary").contentType("application/json")
                                .characterEncoding("utf-8")).andExpect(status().isOk());
        }
}
