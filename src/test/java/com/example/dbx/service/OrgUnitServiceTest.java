package com.example.dbx.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.OrgUnitsResult;
import com.example.dbx.repository.OrgUnitRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgUnitServiceTest {
    @InjectMocks
    private OrgUnitService orgUnitService;

    @Mock
    private OrgUnitRepository orgUnitRepository; // = Mockito.mock(OrgUnitRepository.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddOrgUnit() {
        OrgUnit orgUnit = new OrgUnit("orgunit");
        when(orgUnitRepository.save(any(OrgUnit.class))).thenReturn(dummyOrgUnit());
        OrgUnit savedOrgUnit = orgUnitService.addOrgUnit(orgUnit);
        assertNotNull(savedOrgUnit.getId());
    }

    @Test
    void testDeleteOrgUnit() {
        String res = orgUnitService.deleteOrgUnit(new Long(1));
        assertEquals("Org Unit deleted successfully!", res);
    }

    @Test
    void testGetAllOrgUnits() {
        when(orgUnitRepository.findAll(any(Pageable.class))).thenReturn(dummyPage());
        OrgUnitsResult res = orgUnitService.getAllOrgUnits(1, 1);

        assertNotNull(res);
        assertNotNull(res.getOrgUnits());
        assertEquals(res.getTotalElements(), new Long(0));
    }

    private OrgUnit dummyOrgUnit() {
        OrgUnit orgUnit = new OrgUnit("test");
        orgUnit.setId(new Long(1));
        return orgUnit;
    }

    private Page<OrgUnit> dummyPage() {
        return new PageImpl<>(new ArrayList<>());
    }
}