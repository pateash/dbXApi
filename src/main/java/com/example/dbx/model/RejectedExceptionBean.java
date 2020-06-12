package com.example.dbx.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

//All the rejected exceptions will be stored as an object of this class in the rejected exceptions database.
@Data
@NoArgsConstructor
@Entity
@Table(name = "rejected_exceptions")
public class RejectedExceptionBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // We have to fill

	private Timestamp timeGenerated; // We have to fill

	private String source;

	private String category;

	private String description;

	private ExceptionSeverity severity;

	private String businessComponent;

	private String orgUnit;

	private String technicalDescription;

	private ExceptionStatus status; // We have to fill, by default it will be "unresolved"

	// private Date updateTime; // We have to fill, initially it'll be null by
	// default

	private String comment; // We have to fill, initially it'll be null by default

	public RejectedExceptionBean(String source, String category, String description, ExceptionSeverity severity,
			String businessComponent, String orgUnit, String technicalDescription, String comment) {
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
		this.comment = comment;
	}
}
