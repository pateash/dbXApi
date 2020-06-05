package com.example.dbx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "org_units", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }), })
public class OrgUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public OrgUnit() {
    	super();
    }

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    public OrgUnit(String name) {
        this.name = name;
    }
}