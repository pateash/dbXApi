package com.example.dbx.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
    private OrgUnitRepository orgUnitRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddOrgUnit() {
        when(orgUnitRepository.save(any(OrgUnit.class))).thenReturn(dummyOrgUnit());
        
        OrgUnit orgUnit = new OrgUnit("orgunit");
        OrgUnit savedOrgUnit = orgUnitService.addOrgUnit(orgUnit);
        
        assertNotNull(savedOrgUnit);
        assertNotNull(savedOrgUnit.getId());
    }

    @Test
    void testDeleteOrgUnit() {
        String res = orgUnitService.deleteOrgUnit(1l);

        assertEquals("Org Unit deleted successfully!", res);
    }

    @Test
    void testGetAllOrgUnits() {
        when(orgUnitRepository.findAll(any(Pageable.class))).thenReturn(dummyPage());

        OrgUnitsResult res = orgUnitService.getAllOrgUnits(1, 1);

        assertNotNull(res);
        assertNotNull(res.getOrgUnits());
        assertEquals(0l, res.getTotalElements());
    }

    private OrgUnit dummyOrgUnit() {
        OrgUnit orgUnit = new OrgUnit("test");
        orgUnit.setId(1l);
        return orgUnit;
    }

    private Page<OrgUnit> dummyPage() {
        List<OrgUnit> orgUnits = new ArrayList<>();

        orgUnits.add(new OrgUnit("-"));

        return new PageImpl<>(orgUnits);
    }
}