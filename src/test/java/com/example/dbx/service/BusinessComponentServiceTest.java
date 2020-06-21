package com.example.dbx.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.BusinessComponentsResult;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.OrgUnitRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class BusinessComponentServiceTest {
    @InjectMocks
    private BusinessComponentService businessComponentService;

    @Mock
    private BusinessComponentRepository businessComponentRepository;

    @Mock
    private OrgUnitRepository orgUnitRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllBusinessComponentForAdmin() {
        when(businessComponentRepository.findAll(any(Pageable.class))).thenReturn(dummyPage(1));

        BusinessComponentsResult res = businessComponentService.getAllBusinessComponent(1, 1, UserRole.ROLE_ADMIN, 0l);

        assertNotNull(res);
        assertNotNull(res.getBusinessComponents());
        assertEquals(1l, res.getTotalElements());

    }

    @Test
    public void testGetAllBusinessComponentForUser() {
        when(businessComponentRepository.findByOrgUnitId(any(Long.class), any(Pageable.class)))
                .thenReturn(dummyPage(2));

        BusinessComponentsResult res = businessComponentService.getAllBusinessComponent(1, 1, UserRole.ROLE_USER, 0l);

        assertNotNull(res);
        assertNotNull(res.getBusinessComponents());
        assertEquals((2), res.getTotalElements());

    }

    @Test
    public void testGetBusinessComponentForAdmin() {
        when(businessComponentRepository.findById(any(Long.class))).thenReturn(dummyBusinessComponent(true));

        BusinessComponent res = businessComponentService.getBusinessComponent(1l, UserRole.ROLE_ADMIN, 0l);

        assertNotNull(res);
    }

    @Test
    public void testGetBusinessComponentForUser() {
        when(businessComponentRepository.findOneByIdAndOrgUnitId(any(Long.class), any(Long.class)))
                .thenReturn(dummyBusinessComponent(true));

        BusinessComponent res = businessComponentService.getBusinessComponent(1l, UserRole.ROLE_USER, 0l);

        assertNotNull(res);

    }

    @Test
    public void testGetBusinessComponentNull() {
        Long id = 1l;

        when(businessComponentRepository.findOneByIdAndOrgUnitId(any(Long.class), any(Long.class)))
                .thenReturn(dummyBusinessComponent(null));

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            businessComponentService.getBusinessComponent(id, UserRole.ROLE_ADMIN, id);
        });

        String expectedMessage = BusinessComponentService.notExistsMsg(id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddBusinessComponent() {
        when(orgUnitRepository.findById(any(Long.class))).thenReturn(dummyOrgUnit(true));
        when(businessComponentRepository.save(any(BusinessComponent.class)))
                .thenReturn(dummyBusinessComponent(true).get());

        BusinessComponent businessComponent = BusinessComponent.builder().name("name").build();
        BusinessComponent res = businessComponentService.addBusinessComponent(businessComponent, 1l);

        assertNotNull(res);
        assertNotNull(res.getId());
    }

    @Test
    public void testAddBusinessComponentOrgUnitNull() {
        Long id = 1l;
        BusinessComponent businessComponent = BusinessComponent.builder().name("name").build();

        when(orgUnitRepository.findById(any(Long.class))).thenReturn(dummyOrgUnit(null));
        when(businessComponentRepository.save(any(BusinessComponent.class)))
                .thenReturn(dummyBusinessComponent(true).get());

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            businessComponentService.addBusinessComponent(businessComponent, id);
        });

        String expectedMessage = OrgUnitService.notExistsMsg(id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateBusinessComponent() {
        when(businessComponentRepository.findById(any(Long.class))).thenReturn(dummyBusinessComponent(true));
        when(businessComponentRepository.save(any(BusinessComponent.class)))
                .thenReturn(dummyBusinessComponent(true).get());

        BusinessComponent businessComponent = BusinessComponent.builder().name("test").build();
        BusinessComponent res = businessComponentService.updateBusinessComponent(1l, businessComponent);

        assertNotNull(res);
        assertNotNull(res.getId());
    }

    @Test
    public void testUpdateBusinessComponentNoFound() {
        Long id = 1l;
        BusinessComponent businessComponent = BusinessComponent.builder().name("test").build();

        when(businessComponentRepository.findById(any(Long.class))).thenReturn(dummyBusinessComponent(null));
        when(businessComponentRepository.save(any(BusinessComponent.class)))
                .thenReturn(dummyBusinessComponent(true).get());

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            businessComponentService.updateBusinessComponent(id, businessComponent);
        });

        String expectedMessage = BusinessComponentService.notExistsMsg(id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeleteBusinessComponent() {
        assertNotNull(businessComponentService.deleteBusinessComponent(1l));
    }

    private Page<BusinessComponent> dummyPage(int len) {
        List<BusinessComponent> businessComponents = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            businessComponents.add(new BusinessComponent());
        }

        return new PageImpl<>(businessComponents);
    }

    private Optional<BusinessComponent> dummyBusinessComponent(Boolean isNull) {
        BusinessComponent businessComponent = BusinessComponent.builder().name("test").orgUnit(new OrgUnit("teet"))
                .build();
        businessComponent.setId(1l);
        Optional<BusinessComponent> res = isNull == null ? Optional.empty() : Optional.of(businessComponent);
        return res;
    }

    private Optional<OrgUnit> dummyOrgUnit(Boolean isNull) {
        OrgUnit orgUnit = new OrgUnit("test");
        orgUnit.setId(1l);
        Optional<OrgUnit> res = isNull == null ? Optional.empty() : Optional.of(orgUnit);
        return res;
    }
}