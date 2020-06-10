package com.example.dbx.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//This is the exception that we will store in the database as accepted exception.
@Entity
@Table(name="exception")
public class AcceptedExceptionBean{
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id; //We have to fill
	
	private Date timeGenerated; //We have to fill
	
	private String source; 
	
	private String category;
	
	private String description;
	
	//Type of severity is int.
	// 0-> low , 1-> medium , 2-> high
	private int severity;
	
	private String businessComponent;
	
	private String orgUnit;
	
	private String technicalDescription;
	
	private int status; //We have to fill, by default it will be "unresolved"
	
	private Date updateTime; //We have to fill, initially it'll be null by default
	
	private String comment; //We have to fill, initially it'll be null by default

	public AcceptedExceptionBean() {
		super();
	}

	public AcceptedExceptionBean(Integer id, Date timeGenerated, String source, String category, String description,
			int severity, String businessComponent, String orgUnit, String technicalDescription, int status,
			Date updateTime, String comment) {
		super();
		this.id = id;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTimeGenerated() {
		return timeGenerated;
	}

	public void setTimeGenerated(Date timeGenerated) {
		this.timeGenerated = timeGenerated;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(String businessComponent) {
		this.businessComponent = businessComponent;
	}

	public String getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getTechnicalDescription() {
		return technicalDescription;
	}

	public void setTechnicalDescription(String technicalDescription) {
		this.technicalDescription = technicalDescription;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}	
}
