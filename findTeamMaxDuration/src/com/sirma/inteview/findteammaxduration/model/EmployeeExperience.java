package com.sirma.inteview.findteammaxduration.model;

import java.time.LocalDate;

public class EmployeeExperience {
	
	public static Integer NO_ID = -1;

	private Integer empID;
	private Integer projectID;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private boolean dateToIsNull;
	private Long projectTime;
	
	public EmployeeExperience(Integer empID, Integer projectID,
							  LocalDate dateFrom, LocalDate dateTo,
							  boolean dateToIsNull, Long projectTime) {
		this.empID = empID;
		this.projectID = projectID;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.dateToIsNull = dateToIsNull;
		this.projectTime= projectTime;
	}
	
	public EmployeeExperience() {
		this.empID = NO_ID;
		this.projectID = NO_ID;
		this.dateFrom = LocalDate.now();
		this.dateTo = LocalDate.now();
		this.dateToIsNull = false;
		this.projectID = 0;
	}
	
	public boolean getDateToIsNull() {
		return dateToIsNull;
	}
	
	public Integer getEmployeeID() {
		return empID;
	}
	
	public Integer getProjectID() {
		return projectID;
	}
	
	public LocalDate getDateFrom() {
		return dateFrom;
	}
	
	public LocalDate getDateTo() {
		return dateTo;
	}
	
	public void setEmployeeID(Integer id) {
		this.empID = id;
	}
	
	public void setProjectID(Integer id) {
		this.projectID = id;
	}
	
	public void setDateFrom(LocalDate date) {
		dateFrom = date;
	}
	
	public void setDateTo(LocalDate date) {
		dateTo = date;
	}
	
	public Long getProjectTime() {
		return projectTime;
	}
	
	
	public void setDateToIsNull(boolean dateToIsNull) {
		this.dateToIsNull = dateToIsNull;
	}
	
}
