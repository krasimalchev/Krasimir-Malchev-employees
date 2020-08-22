package com.sirma.inteview.findteammaxduration.model;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sirma.inteview.findteammaxduration.view.TeamFrame;

import java.util.Scanner;

public class TeamDataController {
	
	private static final String DELIMITER = ",";
	private static final String NULL_DATE = "NULL";
	private static final int EMP_IDX = 0;
	private static final int PRJ_IDX = 1;
	private static final int DATE_FROM_IDX = 2;
	private static final int DATE_TO_IDX = 3;
	
	private List<TeamExperience> teams = new ArrayList<TeamExperience>();
	private File file;
	
	public TeamDataController(File file) {
		this.file = file;
	}
	
	
	public TeamDataController(String filename) {
		this.file = new File(filename);
	}
	
	public TeamExperience findTeam(List<TeamExperience> teams,
								   Integer emp1ID,
								   Integer emp2ID) {
		for(int i = 0; i < teams.size(); i++) {
			TeamExperience team = teams.get(i);
			if((team.getOneEmployee() == emp1ID &&
				team.getOtherEmployee() == emp2ID) ||
				team.getOneEmployee() == emp2ID &&
				team.getOtherEmployee() == emp1ID) {
				return team;
			}
		}
		return null;
	}

	private List<TeamExperience> calculateEmployeesTeamExperience(
										List<EmployeeExperience> data) {

        List<TeamExperience> teams = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = i + 1; j < data.size(); j++) {
            	EmployeeExperience empl1 = data.get(i);
                EmployeeExperience empl2 = data.get(j);
                if(empl1.getEmployeeID() != empl2.getEmployeeID()) {
                	long workDays = calcExperienceTogether(empl1, empl2);
                    if (workDays > 0) {
                    	
                    	TeamExperience team = findTeam(teams,
                    			 					   empl1.getEmployeeID(),
                    			 					   empl2.getEmployeeID());
                    	if(team != null) {
                    		team.addProject(empl1.getProjectID(),
                    				        workDays,
                    				        empl1, empl2);
                    	}else {
                    		teams.add(new TeamExperience(empl2.getProjectID(),
                    									 workDays,
                    									 empl1, empl2));
                    	}
                    } 
                }
                
            }
        }

        return teams;
    }
	

	
	/*
	 * Two periods won't overlap if the first one is completely before the
	 * second (EndA < StartB) or the first is completely after the 
	 * second (StartA > EndB). Therefore the periods will overlap
	 * if NOT ((EndA < StartB) or (StartA > EndB)). Applying De Morgan's laws
	 * we derive the formula:
	 * NOT((StartA > EndB)) AND NOT((EndA < StartB)) = (StartA <= EndB) AND (EndA >= StartB)
	 */
	private boolean periodsOverlap(LocalDate startA,
								   LocalDate endA,
								   LocalDate startB,
								   LocalDate endB) {
		return (startA.isBefore(endB) || startA.isEqual(endB)) &&
				(endA.isAfter(startB) || endA.isEqual(startB));
	}
	
	private long timeDifferenceInDays(LocalDate startA,
									  LocalDate endA,
									  LocalDate startB,
									  LocalDate endB) {
		// The Period class is widely used to modify values of given a date or to obtain the 
		// difference between two dates:
		// Period.between(initialDate, finalDate).getDays();
		// Also, the period between two dates can be obtained in a specific unit such as days 
		// or month or years, using ChronoUnit.between:
		//ChronoUnit.DAYS.between(initialDate, finalDate);
		
		//determine what is the start and end date of the period
		LocalDate startDate = startA.isBefore(startB) ? startB : startA;
		LocalDate endDate = endA.isAfter(endB) ? endB : endA;

		return ChronoUnit.DAYS.between(startDate, endDate);	
	}
	
	
	private long calcExperienceTogether(EmployeeExperience empl1, EmployeeExperience empl2) {
		
		long timeDiff = 0l;
		
		if(empl1.getProjectID() == empl2.getProjectID()){
			if(periodsOverlap(empl1.getDateFrom(), empl1.getDateTo(),
							  empl2.getDateFrom(), empl2.getDateTo())){
				timeDiff = timeDifferenceInDays(empl1.getDateFrom(), 
						empl1.getDateTo(), empl2.getDateFrom(),
						empl2.getDateTo());
			}
		}
		
		return timeDiff;
	}
	
	public TeamExperience findMaximumDuration() {
		
		long maxDuration = 0;
		TeamExperience team = null;
		for(int i = 0; i < teams.size(); i++) {
			if(teams.get(i).getTotalDuration().longValue() > maxDuration) {
				maxDuration = teams.get(i).getTotalDuration().longValue();
				team = teams.get(i);
			}
		}
		return team;
	}
	
	public static void printTeam(TeamExperience team) {
		if(team != null) {
			System.out.println("One of the employees has an ID: " + team.getOneEmployee());
			System.out.println("The other employee has an ID: " + team.getOtherEmployee());
			HashMap<Integer, Long> projects = team.getProjectsTime();
			 if(!projects.isEmpty()) {
		         Iterator<Entry<Integer, Long>> it = projects.entrySet().iterator();
		         while(it.hasNext()) {
		        	 Map.Entry<Integer, Long> obj = (Entry<Integer, Long>)it.next();
		        	 System.out.println("Project ID " + obj.getKey() +
		        			 			" with duration " + obj.getValue());
		         }
			 }
			
			System.out.println("Duration: " + team.getTotalDuration());
		}else
		   System.out.println("No team found!");
	}
	
	public static void printTeamsExperience(List<TeamExperience> teams) {
		for(int i = 0; i < teams.size(); i++) {
			printTeam(teams.get(i));
		}
	}
	
	public static void printEmployeeExperience(List<EmployeeExperience> list) {
		for(int i = 0; i < list.size(); i++) {
			EmployeeExperience exp = list.get(i);
			System.out.print(exp.getEmployeeID());
			System.out.print(", ");
			System.out.print(exp.getProjectID());
			System.out.print(", ");
			System.out.print(exp.getDateFrom());
			System.out.print(", ");
			System.out.println(exp.getDateTo());
		}
	}
	
	private List<EmployeeExperience> readEmployees() throws Exception{
		
		List<EmployeeExperience> items = new ArrayList<EmployeeExperience>();

		try(Scanner scanner = new Scanner(file)){
			int ln = 0;
			while (scanner.hasNextLine()) {
				ln++;
				String line = scanner.nextLine();
				String[] tokens = line.split(DELIMITER);
				if(tokens.length != 4) {
					throw new Exception("Expected: EmpID, ProjectID, DateFrom, DateTo on line "
									+ line + " in file " + file.getName());
				}
				
				int employeeID = 0;
				try {
					employeeID = Integer.parseInt(tokens[EMP_IDX]);
				} catch(NumberFormatException ne) {
					throw new Exception("Ivalid employee ID on line " + Integer.toString(ln));
				}
				
				int projectID = 0;
				try {
					projectID = Integer.parseInt(tokens[PRJ_IDX].trim());
				} catch(NumberFormatException ne) {
					throw new Exception("Ivalid project ID on line " + Integer.toString(ln));
				}
				
				LocalDate startDate = LocalDate.parse(tokens[DATE_FROM_IDX].trim());
				if(startDate == null)
					throw new Exception("Expected YYYY-MM-DD for dateFrom on line "
											+ Integer.toString(ln));
			
				LocalDate endDate;
				boolean wasEndDateNull = false;
				if(tokens[DATE_TO_IDX].trim().equalsIgnoreCase(NULL_DATE) == true) {
					endDate = LocalDate.now();
					wasEndDateNull = true;
				} else
					endDate = LocalDate.parse(tokens[DATE_TO_IDX].trim());
			if(endDate == null)
				throw new Exception("Expected YYYY-MM-DD for dateTo on line "
										+ Integer.toString(ln));
			items.add(new EmployeeExperience(employeeID, projectID, startDate,
					endDate, wasEndDateNull, ChronoUnit.DAYS.between(startDate, endDate)));
		}
	}
		
		return items;
	}
	
	public void calculateTeams() {
		try {
			List<EmployeeExperience> data = readEmployees();
			//printEmployeeExperience(data);
			teams = calculateEmployeesTeamExperience(data);
			//printTeamsExperience(teams);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		if(args.length > 1 ) {
			System.out.println("Invalid input. Usage: findTeamMaxDuration [filename]");
			return;
		}
		
		if(args.length == 1) {
			TeamDataController controller = new TeamDataController(args[0]);
			controller.calculateTeams();
			TeamExperience team = controller.findMaximumDuration();
			printTeam(team);
		}else {
			 javax.swing.SwingUtilities.invokeLater(new Runnable() { 
				 public void run() {
					 TeamFrame frame = new TeamFrame();
					 frame.createAndShowGUI(); 
				 } 
			 });
		}
	}

}
