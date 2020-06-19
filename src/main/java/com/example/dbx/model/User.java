package com.example.dbx.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }), })
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7240279187325512917L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    
	@ManyToOne
	@JoinColumn(name = "org_unit_id")
	private OrgUnit orgUnit;

    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    private UserRole role;

    private Boolean isEnabled;

    public User() {
    }

    public User(String name, OrgUnit orgUnit, String username, String password) {
        this.name = name;
        this.orgUnit = orgUnit;
        this.username = username;
        this.password = password;
        this.role = UserRole.ROLE_USER;
        this.isEnabled = false;
    }
}