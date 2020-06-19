package com.example.dbx.service;

import java.util.ArrayList;
import java.util.List;


import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.OrgUnitsResult;
import com.example.dbx.repository.OrgUnitRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrgUnitService {
    private final OrgUnitRepository orgUnitRepository;

    public OrgUnitsResult getAllOrgUnits(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Page<OrgUnit> pageResult;

        pageResult = orgUnitRepository.findAll(pageRequest);
        List<OrgUnit> units = new ArrayList<>(pageResult.getContent());

        units.removeIf(o -> o.getName().equals("-"));

        return new OrgUnitsResult(units, pageResult.getTotalElements());
    }

    public OrgUnit addOrgUnit(OrgUnit orgUnit) {
        OrgUnit newOrgUnit = new OrgUnit(orgUnit.getName());
        orgUnitRepository.save(newOrgUnit);
        return newOrgUnit;
    }

    public String deleteOrgUnit(Long id) {
        orgUnitRepository.deleteById(id);
        return "Org Unit deleted successfully!";
    }
}