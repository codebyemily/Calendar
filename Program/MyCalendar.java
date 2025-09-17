/**
 * CS 151 HW1 Solution 
 * @author Emily Thach
 * @version 1.0 9/4/2025
 */

import java.time.*;
import java.time.format.*;
import java.io.*;
import java.util.*;

/**
 * This class stores the logic and data of the user's calendar, managing creation, removal, and 
 * retrieval of events, including recurring and one-time events.
 * Events are stored in a map where dates are keys and an ArrayList of events as values.
 */
public class MyCalendar
{
	private Map<LocalDate, ArrayList<Event>> cal = new HashMap<>();
	private Queue<String> q;
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

	/**
	 * This constructor creates a new MyCalendar object 
	 * and initializes it by loading events from "events.txt". 
	 * One time events and Recurring events are loaded differently based on the 
	 * information provided. One time events are just added to one day while recurring 
	 * events are loaded based on the day of the week, start date, and end date. 
	 * 
	 */
	public MyCalendar() {
		q = getInitialEvents();
		String title;
		TimeInterval ti;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

		int qSize = q.size();
		
		for (int i = 0; i < qSize/2; i++) {
			
			//first title line 
			title = q.remove();

			//second time info line 
				String timeInfo = q.remove();

				String[] timeInfoList = timeInfo.trim().split("\\s+");
				
				//Repeating events
				if (timeInfoList.length == 5) {
					//grabs days
					
					char[] days = timeInfoList[0].toCharArray();
					
					//turn day abbrevs into days of the week Ex: 'M' --> "Monday"
					ArrayList<String> daysList = getDaysList(days);
					
					LocalDate startDate = LocalDate.parse(timeInfoList[3], formatter);
					LocalDate endDate = LocalDate.parse(timeInfoList[4], formatter);
					
					ti = new TimeInterval(LocalTime.parse(timeInfoList[1], timeFormatter), LocalTime.parse(timeInfoList[2], timeFormatter));
					Event event = new Event(title, ti, true);
					
					//adds event to each day, Ex: (TR) first all Tues, then all Thurs, etc.
					for(int j = 0; j < daysList.size(); j++) {
						LocalDate firstDateOfDay = startDate; 
						
						while(firstDateOfDay.getDayOfWeek().toString() != daysList.get(j)) {
							firstDateOfDay = firstDateOfDay.plusDays(1);
						}
						
						//renaming variable
						LocalDate currentDate = firstDateOfDay; 
						
						while (currentDate.isBefore(endDate)) {
							
							if (!cal.containsKey(currentDate)) {
								cal.put(currentDate, new ArrayList<Event>(Arrays.asList(event)));
							}
							else {
								cal.get(currentDate).add(event);

							}
							currentDate = currentDate.plusWeeks(1);
						}
						
					}
					
				//one time events
				} else {
					
					ti = new TimeInterval(LocalTime.parse(timeInfoList[1], timeFormatter), LocalTime.parse(timeInfoList[2], timeFormatter));
					Event event = new Event(title, ti, false);

					LocalDate date = LocalDate.parse(timeInfoList[0], formatter);
					
					if(!cal.containsKey(date)) {
						cal.put(date, new ArrayList<Event>(Arrays.asList(event)));
					}
					else {
						cal.get(date).add(event);
					}
			}
		}
	}

	/**
	 * This method checks if the specific date has events, then retrieves the list of events scheduled on the specified date.
	 * 
	 * 
     * @param date - LocalDate value of the user's current date
     * @return - An ArrayList of events on the specified date or null if no events are found.
	 */
	public ArrayList<Event> get(LocalDate date) {
		
		if (cal.get(date) == null) {
			return null;
		}
		else {
			return cal.get(date);
		}
		
	}
	
	/**
     * This method converts an array of day abbreviations to their corresponding complete day names based on days list.
	 * 
	 * @param days - An array of characters that are the abbreviations of the days of the week. 
	 * @return - An ArrayList of strings that represent the long forms of the abbreviated days of the week. 
	 */
	public ArrayList<String> getDaysList(char[] days) {
		ArrayList<String> daysList = new ArrayList<>();
		
		for(int j = 0; j< days.length; j++) {
			switch(days[j]) {
				case 'M':
					daysList.add("MONDAY");
					break;
				case 'T':
					daysList.add("TUESDAY");
					break;
				case 'W':
					daysList.add("WEDNESDAY");
					break;
				case 'R':
					daysList.add("THURSDAY");
					break;
				case 'F':
					daysList.add("FRIDAY");
					break;
				case 'A':
					daysList.add("SATURDAY");
					break;
				case 'S':
					daysList.add("SUNDAY");
					break;
				default:
					System.out.println(days[j] + "is not one of the abbrevs for day of the week.");
					break;
			}
			
		}
		return daysList;
	}
	
	/**
     * This method reads initial event data from "events.txt" and returns a queue of event lines.
     * Catches possible errors where events.txt is missing and the formatting in the file is wrong.
     *
     * @return A queue of strings containing the lines from the events.txt file.
     */
	public Queue<String> getInitialEvents(){
		Queue<String> q = new LinkedList<>();
		
		try {
			FileReader fr = new FileReader("events.txt");
			BufferedReader reader = new BufferedReader(fr);
			
			String line;
			while ((line = reader.readLine()) != null) {
				q.add(line);
				
				}
			reader.close();
			System.out.println("Loading is done!\n");
		
			} catch (FileNotFoundException e) {
		        System.out.println("Error: Events.txt not found. Please check the file location.\n");
		        
		    } catch (IOException e) {
		        System.out.println("Error: Error reading events.txt\n");
		    }
		
			return q;
		}
	
	/**
     * This method removes an event by name based on inputted date.
     * Catches an error where the event name user inputted does not 
     * exist in the date. 
     *
     * @param date - LocalDate value of the event to remove.
     * @param name - String value of the event to remove.
     */
	public void remove(LocalDate date, String name) {
		ArrayList<Event> events = cal.get(date);

		if (events != null) {
			boolean removed = events.removeIf(e -> {
			    return e.getName().equals(name);
			});
		   
		    if (removed) {
		        System.out.println("Deleted successfully");

		        if (events.isEmpty()) {
		            cal.remove(date);
		        }
		    }
		    else {
			    System.out.println("Error: No event found with the specified name on this date.");
		    }
		} else {
		    System.out.println("Error: No event found with the specified name on this date.");
		}
	}
	
	/**
	 * This method removes all the events on a specific date. Since the date is empty,
	 * there are no longer any events on that date and the corresponding 
	 * ArrayList<Event> to that date is deleted. 
	 * 
	 * @param date - LocalDate value where all events will be removed.
	 */
	public void removeAll(LocalDate date) {
		ArrayList<Event> events = cal.get(date);
		
		if (events != null) {
	            cal.remove(date);
	     }
	}
	
	/**
	 * This method removes all recurring events with a specific name. 
	 * If any days are empty, there are no longer any events on that date and the corresponding 
	 * ArrayList<Event> to that date is deleted. 
	 * 
	 * @param name - String value of the recurring event to be deleted
	 */
	public void removeAllRecurring(String name) {
	    for (LocalDate date : new ArrayList<>(cal.keySet())) {
			ArrayList<Event> events = cal.get(date);
			if (events != null) {
				events.removeIf(e -> {
				    return e.getName().equals(name);
				});
				
		        if (events.isEmpty()) {
		            cal.remove(date);
		        }
			}
			
		}
		System.out.println("All recurring events with the name, " + name + ", have been deleted");

	}
	
	/**
	 * This method returns the current stored calendar data. 
	 * 
	 * @return A Map containing a key, LocalDate and a value ArrayList<Event>
	 */
	public Map<LocalDate, ArrayList<Event>> getAllEvents() {
		return cal;
	}
	
	/**
	 * This method only creates new one time events. Based on the user's input for 
	 * name, start time, and end time, a new event with these values are created on the 
	 * inputted date in the calendar data. As well, conflict is checked between the event
	 * the user wants to add and the current calendar events. If there is conflict, 
	 * the event was failed to be entered. 
	 * 
	 * @param name - String value of the name event will have.
	 * @param d - LocalDate value of date event will be in.
	 * @param st - LocalTime value of time event will start in.
	 * @param et - LocalTime value of time event will end in.
	 */
	public void createEvent(String name, String d, String st,  String et) {
		
		try {
			LocalDate date = LocalDate.parse(d, formatter);
			LocalTime startTime = LocalTime.parse(st, timeFormatter);
			LocalTime endTime = LocalTime.parse(et, timeFormatter);
			TimeInterval ti = new TimeInterval(startTime, endTime);
			
			Event newEvent = new Event(name, ti, false);
			
			//check conflict
			boolean hasConflict = hasConflict(date, startTime, endTime);
			
			if (!hasConflict) {
				if(cal.get(date) == null) {
					cal.put(date, new ArrayList<>(Arrays.asList(newEvent)));
				}
				else {
					cal.get(date).add(newEvent);
				}
				
				System.out.println("Event successfully created!");
				
			} else {
				System.out.println("There is a conflict with an existing event. Please try again.");
			}
		}
		catch (DateTimeParseException e){
			System.out.println("Invalid date or time format. Please use MM/DD/YYYY and hh:mm in military time.");
		}
		
	}
		
	/**
	 * This method checks for a time conflict in the date between the stored events, 
	 * start time and end time, and the event start time and end time user wants to add. 
	 * If there are no events on the specific date, there can not be a conflict, so 
	 * no conflict is automatically returned. 
	 * 
	 * @param date - LocalDate value of date to check for conflicts
	 * @param st - LocalTime value of event start time
	 * @param et - LocalTime value of event end time
	 * @return A boolean value of whether there is a conflict or not. 
	 */
	public boolean hasConflict(LocalDate date, LocalTime st, LocalTime et) {
		if(cal.get(date) == null) {
			return false;
		}
		
		for(Event storedEvent : cal.get(date)) {
			LocalTime storedStart = storedEvent.getTimeInterval().getStartTime();
			LocalTime storedEnd = storedEvent.getTimeInterval().getEndTime();
			
			if (st.isBefore(storedEnd) && storedStart.isBefore(et)) {
				return true;
			}
		}
		return false;
	}
	
}

