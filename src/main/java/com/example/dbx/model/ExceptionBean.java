package com.example.dbx.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="exception")
public class ExceptionBean {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id; //We have to fill
	
	private Date timeGenerated; //We have to fill
	
	private String source; 
	
	private String category;
	
	private String description;
	
	private String severity;
	
	private String businessComponent;
	
	private String orgUnit;
	
	private String technicalDescription;
	
	private String status; //We have to fill, by default it will be "unresolved"
	
	private Date updateTime; //We have to fill, initially it'll be null by default
	
	private String comment; //We have to fill, initially it'll be null by default
	
	public ExceptionBean() {
		super();
	}

	public ExceptionBean(int id, Date timeGenerated, String source, String category, String description,
			String severity, String businessComponent, String orgUnit, String technicalDescription, String status,
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



	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ExceptionBean [id=" + id + ", timeGenerated=" + timeGenerated + ", source=" + source + ", category="
				+ category + ", description=" + description + ", severity=" + severity + ", businessComponent="
				+ businessComponent + ", orgUnit=" + orgUnit + ", technicalDescription=" + technicalDescription
				+ ", status=" + status + ", updateTime=" + updateTime + ", comment=" + comment + "]";
	}

	
	
}
