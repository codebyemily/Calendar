/**
 * CS 151 HW1 Solution 
 * @author Emily Thach
 * @version 1.0 9/4/2025
 */

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * This class prints out the user interface and 
 * allows the user to navigate around the calendar 
 * application, using the user's stored calendar data. 
 */
public class MyCalendarTester {
	
	//instance variables	
	private LocalDate todaysDate = LocalDate.now();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMMM d, yyyy");
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private MyCalendar myCal = new MyCalendar();
	
	/**
	 * This method initializes the user's calendar testing experience, 
	 * grabs user's initial input to navigate the user interface 
	 * and completes actions based on it. 
	 */
    public static void main(String[] args) {
    	MyCalendarTester testCal = new MyCalendarTester();
        //initialize calendar
    	Scanner sc = new Scanner(System.in);
        printCalendar(testCal.todaysDate, false, testCal.myCal);
        
        String userInput = "";
       
        while(!userInput.equals("Q")) {
        	userInput = sc.nextLine();
        	switch(userInput) {
        		case "V":
        			testCal.view(sc);
        			break;
        		case "C":
        			testCal.create(sc);
        			break;
        		case "G":
        			testCal.go(sc);
        			break;
        		case "E":
        			testCal.eventList(sc);
        			break;
        		case "D":
        			testCal.delete(sc);
        			break;
        		case "Q":
        			sc.close();
        			testCal.quit();
        			break;
        		default: 
        			System.out.println("Invalid option. Please choose from the menu.");
        	    	System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
        	}
        }
    }

    /**
     * This method uses the user's current date and current calendar data to print out 
     * a calendar based on the month of the current date. If the user selects [M] through the 
     * view option, calendar changes to show the dates in the month that have events instead of 
     * highlighting today's date.  
     * 
     * @param date - LocalDate value of the user's current date
     * @param isMonthView - boolean value if user selects month view 
     * @param cal - MyCalendar value of the user's current calendar data
     */
    public static void printCalendar(LocalDate date, boolean isMonthView, MyCalendar cal) {
    	int today = date.getDayOfMonth();
    	LocalDate first = LocalDate.of(date.getYear(), date.getMonth(), 1);
        
    	int dayOfWeek = first.getDayOfWeek().getValue();
        int numDaysInMonth = date.lengthOfMonth();
        
        Month month = date.getMonth();
        int year = date.getYear();
        int startDay = dayOfWeek % 7;
        
    	System.out.println(month + " " + year);
        System.out.println("Su Mo Tu We Th Fr Sa");
        
        
        //prints space leading up to first day of the month
        for (int i = 0; i < startDay; i++) {
            System.out.print("   ");
        }

        LocalDate currDate = date;
        
     

        if (isMonthView) {
            for (int day = 1; day <= numDaysInMonth; day++) {
            	currDate = date.withDayOfMonth(day);
            	
                if (cal.get(currDate) == null) {
                    System.out.printf("%2d ", day);
                } else {
                	
                    System.out.print("{" + day + "} ");
                }

                // New line at the end of the week
                if ((day + startDay) % 7 == 0) {
                    System.out.println();
                }
                
            }
        } else {
            for (int day = 1; day <= numDaysInMonth; day++) {
                if (today == day) {
                    System.out.print("[" + day + "]");
                } else {
                    System.out.printf("%2d ", day);
                }

                // New line at the end of the week
                if ((day + startDay) % 7 == 0) {
                    System.out.println();
                }
            }
            
            System.out.println();
            System.out.println();
            System.out.println("Select one of the following main menu options:");
        	System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit\n");
        }
        
    }
    
    /**
     * This method takes the current scanner being used to take user input and provides Day and Month options. 
     * Based on the user's selection, the corresponding view will show and the user has the freedom to go to the 
     * next view, previous view, or go back to the main menu. 
     *  
     * @param sc - Scanner value of the scanner used to check user input.  
     */
    public void view(Scanner sc) {
		String userInput = "";
    	System.out.print("[D]ay view or [M]view ? ");
		
		while(!userInput.equals("Q")) {
        	userInput = sc.nextLine();
        	
        	//user selects D
        	if(userInput.equals("D")) {
        		LocalDate dateToDisplay = todaysDate;
        		
        		while(!userInput.equals("G")) {
        			System.out.println();
        			String formattedDate = dateToDisplay.format(formatter);
        			System.out.println(formattedDate); 
        			
        			
        			if (myCal.get(dateToDisplay) == null) {
        				System.out.println("There are no events.\n");
        				
        			} else {
        				ArrayList<Event> events = myCal.get(dateToDisplay);
        				events.sort(Comparator.comparing(e -> ((Event) e).getTimeInterval().getStartTime()));
        				
        				for (Event e : events) {
                			System.out.println(e.getName() + ": " + e.getTimeInterval().getStartTime() + " - " + e.getTimeInterval().getEndTime());
                		}
        			}
            		System.out.println("[P]revious or [N]ext or [G]o back to the main menu ? ");
        			userInput = sc.nextLine();
        			
        			if(userInput.equals("P")) {
        				dateToDisplay = dateToDisplay.minusDays(1);
        			}
        			else if (userInput.equals("N")) {
        				dateToDisplay = dateToDisplay.plusDays(1);
        			} 
        		}
        		
        		System.out.println("Select one of the following main menu options:");
        		System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
        		break;
        		
        	}
        	//user selects M
        	else if(userInput.equals("M")){
    			System.out.println();
        		
        		LocalDate dateToDisplay = todaysDate;
            		
            		while(!userInput.equals("G")) {
            			printCalendar(dateToDisplay, true, myCal);
            			
            			System.out.println();
                		System.out.println("[P]revious or [N]ext or [G]o back to the main menu ? ");
            			
                		userInput = sc.nextLine();
            			
            			if(userInput.equals("P")) {
            				dateToDisplay = dateToDisplay.minusMonths(1);
            			}
            			else if (userInput.equals("N")) {
            				dateToDisplay = dateToDisplay.plusMonths(1);
            			} 
            		}
            		
            		System.out.println("Select one of the following main menu options:");
            		System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
            		break;
            		
            	}
        	 
        	else {
        		System.out.println("Type either: [D]ay view or [M]view ?");
        	}

		}
		
	}
	
    /**
     * This method takes the current scanner being used to take user input and 
     * asks for event name, date, start time, and end time to create an event. 
     * Calendar data is updated through the createEvent method. 
     * 
     * @param sc - Scanner value of the scanner used to check user input.  
     */
	public void create(Scanner sc) {
		String name, date, st, et; 
		
		System.out.println("To create an event, type: ");
		
		System.out.print("Name: ");
		name = sc.nextLine();
		System.out.print("Date [MM/DD/YYYY]: ");
		date = sc.nextLine();
		System.out.print("Starting time [hh:mm]: ");
		st = sc.nextLine();
		System.out.print("Ending time [hh:mm]: ");
		et = sc.nextLine();

		myCal.createEvent(name, date, st, et);
		System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");


	}
	
	/**
	 * This method takes the current scanner being used to take user input and 
     * asks for a date to print events in that day. 
     * 
	 * @param sc - Scanner value of the scanner used to check user input.  
	 */
	public void go(Scanner sc) {
		String userInput = "";
	
		System.out.println("Enter a date in the form of MM/DD/YYYY");
		userInput = sc.nextLine();
		
		getEventsinDay(userInput.trim(), sc);
	}
	
	/**
	 * This method takes the current scanner being used to take user input and 
     * grabs the user's current calendar data. All events from the user's calendar will
     * be printed based on the recurrence of the events, the order of the date, the order of the start time,
     * and the end time. If user presses B, user returns to main menu. 
	 * 
	 * @param sc - Scanner value of the scanner used to check user input.  
	 */
	public void eventList(Scanner sc) {
	    String userInput = "";

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy"); // Make sure this exists

	    while (!userInput.equals("B")) {
	        Map<LocalDate, ArrayList<Event>> cal = myCal.getAllEvents();
	        Map<LocalDate, ArrayList<Event>> orderOneTimeEvents = new TreeMap<>(cal);
	        Set<Event> recurringEventsSet = new HashSet<>();

	        System.out.println("ONE-TIME EVENTS\n");

	        for (Map.Entry<LocalDate, ArrayList<Event>> entry : orderOneTimeEvents.entrySet()) {
	            
	        	LocalDate date = entry.getKey();
	            ArrayList<Event> events = entry.getValue();

	            ArrayList<Event> dayOneTimeEvents = new ArrayList<>();

	            for (Event e : events) {
	                if (e.isRecurring()) {
	                    recurringEventsSet.add(e);
	                } else {
	                    dayOneTimeEvents.add(e);
	                }
	            }

	            dayOneTimeEvents.sort(Comparator.comparing(e -> e.getTimeInterval().getStartTime()));

	            if (!dayOneTimeEvents.isEmpty()) {
	                for (Event e : dayOneTimeEvents) {
	                    System.out.println(date.format(dateFormatter) + "  " + e.getName() + ": " +
	                        e.getTimeInterval().getStartTime() + " - " +
	                        e.getTimeInterval().getEndTime());
	                }
	            }
	        }

	        System.out.println("RECURRING EVENTS\n");

	        ArrayList<Event> sortedRecurring = new ArrayList<>(recurringEventsSet);
	        
	        sortedRecurring.sort(Comparator.comparing(e -> e.getTimeInterval().getStartTime()));
	       
	        //find the first and last dates of each recurring event
	        
	        String name;
	        for (Event e : sortedRecurring) {
		        ArrayList<DayOfWeek> daysOfWeek = new ArrayList<>();

	        	name = e.getName();
	        	 LocalDate firstDate = null;
	        	 LocalDate lastDate = null;
	        	
	        	for (LocalDate date : new ArrayList<>(cal.keySet())) {
					ArrayList<Event> events = cal.get(date);
					if (events != null) {
						for (Event storedEvents : events) {
							if (storedEvents.getName().equals(name) && e.isRecurring()) {
								if(firstDate == null || date.isBefore(firstDate)) {
									firstDate = date;
								}
								if(lastDate == null || date.isAfter(lastDate)) {
									lastDate = date;
								}
								
								if (!daysOfWeek.contains(date.getDayOfWeek())) {
				                        daysOfWeek.add(date.getDayOfWeek());
				                }
							}
						}
					}
	        	}
	        	
	        	ArrayList<String> temp = new ArrayList<>();
	        	for (DayOfWeek d : daysOfWeek) {
	        		temp.add(getAbbrev(d));
	        	}
	 	        System.out.println(temp + " " + e.getName() + ": " + e.getTimeInterval().getStartTime() + " - " + e.getTimeInterval().getEndTime() + " " + firstDate.format(dateFormatter) + " - " + lastDate.format(dateFormatter));
	 	        
	        }
	        System.out.println();
	        System.out.println("Press [B] to go back");
	        userInput = sc.nextLine().trim();
	    }

	    System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
	}

	/**
	 * This method grabs the passed DayOfWeek value and returns the corresponding 
	 * abbreviation of that date as a String.  
	 * 
	 * @param - DayOfWeek value 
	 * @return - String value of abbreviated DayOfWeek value
	 */
	public String getAbbrev(DayOfWeek d) {
	    switch (d) {
	        case MONDAY:
	            return "M";
	        case TUESDAY:
	            return "T";
	        case WEDNESDAY:
	            return "W";
	        case THURSDAY:
	            return "R";
	        case FRIDAY:
	            return "F";
	        case SATURDAY:
	            return "A";
	        case SUNDAY:
	            return "S";
	        default:
	            return "";
	    }
	}
	
	/**
	 * 
	 * This method takes the current scanner being used to take user input and
	 * gives the user four options: Selected, All, R, and Back. Based on the selected option, 
	 * the user will be prompted to give more information on what to delete. 
	 * User selects S, user must provide the date and the name of the event. 
	 * User selects A, user must provide the date to delete all events in that date. 
	 * User selects R, user must provide the name of the recurring event. 
	 * User selects B, user goes back to main menu. 
	 * 
	 * @param sc - Scanner value of the scanner used to check user input.  
	 *
	 */
	public void delete(Scanner sc) {
		String userInput = "";
		LocalDate dateToDisplay;
		while(!userInput.equals("B")) {
			System.out.println("[S]elected  [A]ll   [R] [B]ack");
			userInput = sc.nextLine();
			
			switch(userInput) {
				
				case "S": //specific one time event
					System.out.println("Enter the date [MM/DD/YYYY]");
					userInput = sc.nextLine();
					
					try {
					       dateToDisplay = LocalDate.parse(userInput.trim(), dateFormatter);
					       System.out.println();
							String formattedDate = dateToDisplay.format(formatter);
							System.out.println(formattedDate);
							
							if (myCal.get(dateToDisplay) == null) {
								System.out.println("There are no events.\n");
								
							} else {
								ArrayList<Event> events = myCal.get(dateToDisplay);
								events.sort(Comparator.comparing(e -> ((Event) e).getTimeInterval().getStartTime()));
								System.out.println();
								
								for (Event e : events) {
				        			System.out.println(e.getName() + ": " + e.getTimeInterval().getStartTime() + " - " + e.getTimeInterval().getEndTime());
				        		}
							}
							System.out.print("Enter the name of the event to delete: ");
							userInput = sc.nextLine();
							
							myCal.remove(dateToDisplay, userInput.trim());
							
					    } catch (DateTimeParseException e) {
					    	System.out.println("Enter a date with the correct form of MM/DD/YYYY");
					    	
					    }
					break;
				case "A": //all events in a specific day
					System.out.println("Enter the date [MM/DD/YYYY]");
					userInput = sc.nextLine();
					
					System.out.println("Deleting all events on this date...");
					try {
					    dateToDisplay = LocalDate.parse(userInput.trim(), dateFormatter);
					    System.out.println();
						myCal.removeAll(dateToDisplay);
						System.out.println("All events on this date have been successfully deleted");

					} catch (DateTimeParseException e) {
					    System.out.println("Enter a date with the correct form of MM/DD/YYYY");
					    }
					break;
				case "R": //specific recurring event 
					System.out.println("Enter the exact name of a recurring event to delete it all.");
					userInput = sc.nextLine();
					myCal.removeAllRecurring(userInput.trim());
					break;
				case "B":
            		System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
					break;
				case "Q": 
					quit();
					break;
				default:
					//System.out.println("Type in one of the following options: ");
					break;
			}
			
		}
		
	}
	
	/**
	 * Based on the user's current calendar data, a new file named "output.txt" is created,
	 * and the data is written onto the file. Events are separated by one time and recurring events. 
	 * As well, the file path of "output.txt" can be found in project's root folder. 
	 */
	public void quit() {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM dd, yyyy");
	    File file = new File("output.txt");
	    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

	        Map<LocalDate, ArrayList<Event>> cal = myCal.getAllEvents(); 
	        Map<LocalDate, ArrayList<Event>> orderOneTimeEvents = new TreeMap<>(cal);
	        Set<Event> recurringEventsSet = new HashSet<>();

	        writer.println("ONE-TIME EVENTS\n");

	        for (Map.Entry<LocalDate, ArrayList<Event>> entry : orderOneTimeEvents.entrySet()) {
	            LocalDate date = entry.getKey();
	            ArrayList<Event> events = entry.getValue();

	            ArrayList<Event> dayOneTimeEvents = new ArrayList<>();

	            for (Event e : events) {
	                if (e.isRecurring()) {
	                    recurringEventsSet.add(e);
	                } else {
	                    dayOneTimeEvents.add(e);
	                }
	            }

	            dayOneTimeEvents.sort(Comparator.comparing(e -> e.getTimeInterval().getStartTime()));

	            for (Event e : dayOneTimeEvents) {
	                writer.println(date.format(dateFormatter) + "  " + e.getName() + ": " +
	                        e.getTimeInterval().getStartTime() + " - " +
	                        e.getTimeInterval().getEndTime());
	            }
	        }

	        writer.println("\nRECURRING EVENTS\n");
	        ArrayList<Event> sortedRecurring = new ArrayList<>(recurringEventsSet);
	        sortedRecurring.sort(Comparator.comparing(e -> e.getTimeInterval().getStartTime()));

	        for (Event e : sortedRecurring) {
	            String name = e.getName();
	            LocalDate firstDate = null;
	            LocalDate lastDate = null;

	            for (LocalDate date : cal.keySet()) {
	                ArrayList<Event> events = cal.get(date);
	                for (Event stored : events) {
	                    if (stored.isRecurring() && stored.getName().equals(name)) {
	                        if (firstDate == null || date.isBefore(firstDate)) {
	                            firstDate = date;
	                        }
	                        if (lastDate == null || date.isAfter(lastDate)) {
	                            lastDate = date;
	                        }
	                    }
	                }
	            }

	            if (firstDate != null && lastDate != null) {
	                writer.println(e.getName() + ": " + e.getTimeInterval().getStartTime() + " - " + e.getTimeInterval().getEndTime() + " " + firstDate.format(dateFormatter) + " - " + lastDate.format(dateFormatter));
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("Error: Could not create output.txt");
	    }

	    System.out.println("Good Bye");
	    System.exit(0);
	}

	/**
	 * This method grabs the user's inputted date and the scanner used to check user input
	 * and returns all the events in that date. User is free to go to the previous or next date
	 * based on the options displayed afterwards. 
	 * 
     * @param date - LocalDate value of the user's date they want to check.
	 * @param sc - Scanner value of the scanner used to check user input.  
	 */
	public void getEventsinDay(String date, Scanner sc) {
		String userInput = "";
		LocalDate dateToDisplay;
		try {
			dateToDisplay = LocalDate.parse(date, dateFormatter);
			
			while(!userInput.equals("G")) {
				System.out.println();
				String formattedDate = dateToDisplay.format(formatter);
				System.out.println(formattedDate);
				
				if (myCal.get(dateToDisplay) == null) {
					System.out.println("There are no events.\n");
					
				} else {
					ArrayList<Event> events = myCal.get(dateToDisplay);
					events.sort(Comparator.comparing(e -> ((Event) e).getTimeInterval().getStartTime()));
					
					for (Event e : events) {
	        			System.out.println(e.getName() + ": " + e.getTimeInterval().getStartTime() + " - " + e.getTimeInterval().getEndTime());
	        		}
				}
	    		System.out.println("[P]revious or [N]ext or [G]o back to the main menu ? ");
				userInput = sc.nextLine();
				
				if(userInput.equals("P")) {
					dateToDisplay = dateToDisplay.minusDays(1);
				}
				else if (userInput.equals("N")) {
					dateToDisplay = dateToDisplay.plusDays(1);
				} 
			}
		}
		catch (DateTimeParseException e){
			System.out.println("Error: Type in a date with the correct form, MM/DD/YYYY");
		}
		
		System.out.println("Select one of the following main menu options:");
		System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
	}
}
