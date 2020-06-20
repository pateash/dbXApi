package com.example.dbx.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.OrgUnitsResult;
import com.example.dbx.security.jwt.JwtAuthEntryPoint;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.security.services.UserDetailsServiceImpl;
import com.example.dbx.service.OrgUnitService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrgUnitController.class)
public class OrgUnitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrgUnitService orgUnitService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @MockBean
    private JwtProvider jwtProvider;

    @WithMockUser(roles = { "ADMIN" })
    @Test
    void testGetAllOrgUnits() throws Exception {
        List<OrgUnit> orgUnits = new ArrayList<>();

        orgUnits.add(OrgUnit.builder().id(1l).name("TEST").build());

        OrgUnitsResult res = new OrgUnitsResult(orgUnits, new Long(orgUnits.size()));

        given(orgUnitService.getAllOrgUnits(2, 7)).willReturn(res);

        mockMvc.perform(
                get("/api/orgUnit?page=2&pageSize=7").contentType("application/json").characterEncoding("utf-8"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements", is(orgUnits.size())));
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    void testAddOrgUnit() throws Exception {
        OrgUnit orgUnit = new OrgUnit("name");

        given(orgUnitService.addOrgUnit(any(OrgUnit.class))).willReturn(new OrgUnit(1l, "name"));

        mockMvc.perform(post("/api/orgUnit").contentType("application/json").characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(orgUnit))).andExpect(status().isOk());
    }

}
