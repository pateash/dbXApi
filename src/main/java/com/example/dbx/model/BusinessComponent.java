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
@Table(name = "business_components", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }), })
public class BusinessComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public BusinessComponent() {
        super();
    }

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    public BusinessComponent(String name) {
        this.name = name;
    }
}   