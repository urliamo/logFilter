package beans;

import java.time.LocalDateTime;
public class catalinaEvent {
	
	private LocalDateTime eventTime = LocalDateTime.MIN;
	private String eventText = "";
	
	
	
	public catalinaEvent(LocalDateTime eventTime, String eventText) {
		super();
		this.eventTime = eventTime;
		this.eventText = eventText;
	}
	public LocalDateTime getEventTime() {
		return eventTime;
	}
	public void setEventTime(LocalDateTime eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventText() {
		return eventText;
	}
	public void setEventText(String eventText) {
		this.eventText = eventText;
	}
	
	

}
