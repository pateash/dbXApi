package com.example.dbx.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

//This is the exception that we will store in the database as accepted exception.
@NoArgsConstructor
@Data
@Entity
@Table(name = "exceptions")
public class AcceptedExceptionBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // We have to fill

	private Timestamp timeGenerated; // We have to fill

	private String source;

	private String category;

	private String description;

	@Enumerated(EnumType.ORDINAL)
	private ExceptionSeverity severity;

	@ManyToOne
	@JoinColumn(name = "business_component_id")
	private BusinessComponent businessComponent;

	@ManyToOne
	@JoinColumn(name = "org_unit_id")
	private OrgUnit orgUnit;

	private String technicalDescription;

	private ExceptionStatus status; // We have to fill, by default it will be "unresolved"

	private Timestamp updateTime; // We have to fill, initially it'll be null by default

	private String comment; // We have to fill, initially it'll be null by default

	public AcceptedExceptionBean(String source, String category, String description, ExceptionSeverity severity,
			BusinessComponent businessComponent, OrgUnit orgUnit, String technicalDescription, String comment) {
		super();
		this.timeGenerated = new Timestamp(System.currentTimeMillis());
		this.source = source;
		this.category = category;
		this.description = description;
		this.severity = severity;
		this.businessComponent = businessComponent;
		this.orgUnit = orgUnit;
		this.technicalDescription = technicalDescription;
		this.status = ExceptionStatus.STATUS_UNRESOLVED;
		this.updateTime = null;
		this.comment = comment;
	}
}
