/**
 * CS 151 HW1 Solution 
 * @author Emily Thach
 * @version 1.0 9/4/2025
 */

/**
 * This class is used for Event data, and contains information on the
 * event name, the time interval, and its recurrence
 */
public class Event {
	
	//instance variables
	private String name;
	private TimeInterval timeInterval; 
	private boolean isRecurring;
 	
	/**
	 * This constructor initializes an event based on user's inputs: 
	 * name, time interval, and whether it recurs, and sets the Event data equal 
	 * to these values passed. 
	 * 
	 * @param name - String value for event name
	 * @param timeInterval - Time interval value for event's time interval
	 * @param isRecurring - boolean value if event is recurring
	 */
	public Event(String name, TimeInterval timeInterval, boolean isRecurring) {
		this.name = name;
		this.timeInterval = timeInterval;
		this.isRecurring = isRecurring;
	}
	
	/**
	 * This method will return the current String value of the Event's name. 
	 * 
	 * @return String value for current Event name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method will return the current TimeInterval value of the Event. 
	 * 
	 * @return TimeInterval value of current Event time interval.
	 */
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}
	
	/**
	 * This method will return the current recurrence status value of the Event.
	 * @return boolean value of current Event recurrence status. 
	 */
	public boolean isRecurring() {
		return isRecurring;
	}
}