package beans;


public class customEvent {
	private int lineNum;
	private String filter = "";
	private String eventText = "";
	
	
	
	public customEvent(int num, String eventText, String filter) {
		super();
		this.lineNum = num;
		this.eventText = eventText;
		this.filter = filter;
	}
	
	
	public String getFilter() {
		return filter;
	}


	public void setFilter(String filter) {
		this.filter = filter;
	}


	public int getLineNum() {
		return lineNum;
	}
	public void setlineNum(int num) {
		this.lineNum = num;
	}
	public String getEventText() {
		return eventText;
	}
	public void setEventText(String eventText) {
		this.eventText = eventText;
	}
}
