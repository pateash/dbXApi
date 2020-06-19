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
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "old_exceptions")
public class OldExceptionBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long exceptionId;

	@Transient
	private int version;

	@Enumerated(EnumType.ORDINAL)
	private ExceptionSeverity severity;

	@ManyToOne
	@JoinColumn(name = "business_component_id")
	private BusinessComponent businessComponent;

	private String technicalDescription;

	private ExceptionStatus status; // We have to fill, by default it will be "unresolved"

	private Timestamp updateTime; // We have to fill, initially it'll be null by default

	private String comment; // We have to fill, initially it'll be null by default

	public OldExceptionBean(Long exceptionId, ExceptionSeverity severity, BusinessComponent businessComponent,
			String technicalDescription, ExceptionStatus status, String comment) {
		super();
		this.id = null;
		this.exceptionId = exceptionId;
		this.severity = severity;
		this.businessComponent = businessComponent;
		this.technicalDescription = technicalDescription;
		this.status = status;
		this.updateTime = new Timestamp(System.currentTimeMillis());
		this.comment = comment;
	}
}
