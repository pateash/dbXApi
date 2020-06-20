package com.example.dbx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "business_components", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "org_unit_id" }), })
public class BusinessComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "org_unit_id")
    private OrgUnit orgUnit;

    private Boolean isEnabled;

    @Transient
    private Long orgUnitCreateId;
}