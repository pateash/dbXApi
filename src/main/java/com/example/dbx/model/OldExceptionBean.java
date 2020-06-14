package com.example.dbx.model;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "old_exceptions")
public class OldExceptionBean {
	
	@ManyToOne
	@JoinColumn(name="exception_id")
	private AcceptedExceptionBean exceptionId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int version;
	
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

	public OldExceptionBean(AcceptedExceptionBean exceptionId, int version, Timestamp timeGenerated, String source,
			String category, String description, ExceptionSeverity severity, BusinessComponent businessComponent,
			OrgUnit orgUnit, String technicalDescription, ExceptionStatus status, Timestamp updateTime,
			String comment) {
		super();
		this.exceptionId = exceptionId;
		this.version = version;
		this.timeGenerated = timeGenerated;
		this.source = source;
		this.category = category;
		this.description = description;
		this.severity = severity;
		this.businessComponent = businessComponent;
		this.orgUnit = orgUnit;
		this.technicalDescription = technicalDescription;
		this.status = status;
		this.updateTime = updateTime;
		this.comment = comment;
	}
	
	public OldExceptionBean(AcceptedExceptionBean exceptionId, String source,
			String category, String description, ExceptionSeverity severity, BusinessComponent businessComponent,
			OrgUnit orgUnit, String technicalDescription, ExceptionStatus status, Timestamp updateTime,
			String comment) {
		super();
		this.exceptionId = exceptionId;
		this.timeGenerated = new Timestamp(System.currentTimeMillis());
		this.source = source;
		this.category = category;
		this.description = description;
		this.severity = severity;
		this.businessComponent = businessComponent;
		this.orgUnit = orgUnit;
		this.technicalDescription = technicalDescription;
		this.status = status;
		this.updateTime = updateTime;
		this.comment = comment;
	}
	
}
