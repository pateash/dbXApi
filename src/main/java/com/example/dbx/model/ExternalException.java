package com.example.dbx.model;

//Every incoming exception will be mapped to an object of this class
public class ExternalException {
	private String source; 
	
	private String category;
	
	private String description;
	
	private String severity;
	
	private String businessComponent;
	
	private String orgUnit;
	
	private String technicalDescription;

	public ExternalException() {
		
	}
	
	public ExternalException(String source, String category, String description, String severity,
			String businessComponent, String orgUnit, String technicalDescription) {
		super();
		this.source = source;
		this.category = category;
		this.description = description;
		this.severity = severity;
		this.businessComponent = businessComponent;
		this.orgUnit = orgUnit;
		this.technicalDescription = technicalDescription;
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

	@Override
	public String toString() {
		return "ExternalException [source=" + source + ", category=" + category + ", description=" + description
				+ ", severity=" + severity + ", businessComponent=" + businessComponent + ", orgUnit=" + orgUnit
				+ ", technicalDescription=" + technicalDescription + "]";
	}
	
	
	
}
