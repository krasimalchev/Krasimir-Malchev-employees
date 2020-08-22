package com.sirma.inteview.findteammaxduration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamExperience implements Comparable<TeamExperience>{
	
	// the duration in days the employees have worked together per project.
	// The project is represented by its ID
	private HashMap<Integer, Long> projectsTime;
	private List<EmployeeExperience> employees = new ArrayList<EmployeeExperience>();
	// total duration in all projects measured in days
	private long totalDuration;
	private int oneEmployeeID;
	private int otherEmployeeID;
	
	public TeamExperience(Integer projectID, Long duration,
				EmployeeExperience empl1, EmployeeExperience empl2) {
		this.oneEmployeeID = empl1.getEmployeeID();
		this.otherEmployeeID = empl2.getEmployeeID();
		projectsTime = new HashMap<>();
		projectsTime.put(projectID, duration);
		totalDuration = duration;
		employees.add(empl1);
		employees.add(empl2);
		
	}
	
	public void addProject(Integer projectID, Long duration,
						   EmployeeExperience empl1,
						   EmployeeExperience empl2) {
		
		Long elapsedTime = projectsTime.get(projectID);
		if(elapsedTime != null)
			elapsedTime += duration;
		else elapsedTime = duration;
		projectsTime.put(projectID, elapsedTime);
		totalDuration += duration;
		
		employees.add(empl1);
		employees.add(empl2);
	}

	@Override
	public int compareTo(TeamExperience that) {
		if(this.totalDuration < that.totalDuration) return -1;
		if(this.totalDuration > that.totalDuration) return 1;
		return 0;
		
		
	}
	
	public Long getTotalDuration() {
		return totalDuration;
	}

	public int getOneEmployee() {
		return this.oneEmployeeID;
	}
	
	public int getOtherEmployee() {
		return this.otherEmployeeID;
	}
	
	public HashMap<Integer, Long> getProjectsTime(){
		return projectsTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = prime * hash + Integer.valueOf(oneEmployeeID).hashCode();
		hash = prime * hash + Integer.valueOf(otherEmployeeID).hashCode();
		hash = prime * hash + ((projectsTime == null) ? 
									0 : 
									projectsTime.hashCode());
		hash = prime * hash + Long.valueOf(totalDuration).hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		TeamExperience other = (TeamExperience) obj;
		
		return ((oneEmployeeID == other.oneEmployeeID) && 
				(otherEmployeeID == other.otherEmployeeID)) ||
				((oneEmployeeID == other.otherEmployeeID) &&
				 (otherEmployeeID == other.oneEmployeeID)) ;
	}
	

}
