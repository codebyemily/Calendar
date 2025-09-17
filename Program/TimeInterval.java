/**
 * CS 151 HW1 Solution 
 * @author Emily Thach
 * @version 1.0 9/4/2025
 */
import java.time.LocalTime;

/**
 * This class is used for time interval data, and contains information on the
 * start time and end time. 
 */
public class TimeInterval {
	
	//instance variables
	private LocalTime startTime; 
	private LocalTime endTime; 
	
	/**
	 * This constructor initializes a time interval based on user's inputs: 
	 * startTime and endTime, and sets the time interval data equal to these values passed. 
	 * 
	 * @param startTime - LocalTime value of start time of time interval.
	 * @param endTime - LocalTime value of end time of time interval.
	 */
	public TimeInterval(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime; 
		this.endTime = endTime; 
	}
	
	/**
	 * 
	 * This method will return the current start time value of the time interval. 
	 * @return startTime - LocalTime value for start time
	 */
	public LocalTime getStartTime() {
		return startTime;
	}
	
	/**
	 * This method will return the current end time value of the time interval. 
	 * @return endTime - LocalTime value for end time
	 */
	public LocalTime getEndTime() {
		return endTime;
	}
}