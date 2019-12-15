package filters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import beans.catalinaEvent;
import gui.SwingGUI;


//filter for Catalina files events
public class Catalina {

	public static boolean activeFilter = true;
	public static ArrayList<catalinaEvent> events = new ArrayList<>(); //list containing all events discovered
	public static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS", Locale.ENGLISH);//timestamp format for this log

	public static void displayDescription(JTextArea description) {
		description.setText("Catalina Event Filter:"+"\n");
		description.append("checks for errors and startup messages in catalina logs."+"\n");
		description.append("Uses only catalina logs."+"\n");
		description.append("Analysis description:"+"\n");
		description.append("Event Time - local dateTime the event took place."+"\n");
		description.append("Event Text - the entire log line of the event."+"\n");
	}
	 public static void filterCat(String line) throws ParseException, IOException 
	 {
	    	String requestPattern;
	    	Pattern pattern;
	    	Matcher matcher;
	        if (line.contains("ERROR") ) 
	        {
	        	if (SwingGUI.output) {
		        	File logFile = new File(SwingGUI.outputFolder+"\\catLog");

	        		FileWriter fr = new FileWriter(logFile, true);
	        		fr.write(line+"\n");
	        		fr.close();
	        		}
	        	requestPattern = "(0[1-9]|[12][0-9]|3[01])-\\D{3}-((19|2[0-9])[0-9]{2})\\s([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?(?= INFO)";
	        	pattern = Pattern.compile(requestPattern);
	        	matcher = pattern.matcher(line);
	        	matcher.find();
	        	LocalDateTime eventTime = LocalDateTime.parse(matcher.group(), format);
	        	if (!eventTime.isAfter(SwingGUI.endTime) && !eventTime.isBefore(SwingGUI.startTime)) 
		        	{
		        		String errorLine = line;
		        		events.add(new catalinaEvent (eventTime, errorLine));
		        	}
	        	
  	
		     }
	        if (line.contains("Server startup") ) 
	        {
	        	if (SwingGUI.output) {
		        	File logFile = new File(SwingGUI.outputFolder+"\\catLog");
	        		FileWriter fr = new FileWriter(logFile, true);
	        		fr.write(line+"\n");
	        		fr.close();
	        		}
	        	requestPattern = "(0[1-9]|[12][0-9]|3[01])-\\D{3}-((19|2[0-9])[0-9]{2})\\s([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?(?= INFO)";
	        	pattern = Pattern.compile(requestPattern);
	        	matcher = pattern.matcher(line);
	        	matcher.find();
	        	LocalDateTime eventTime = LocalDateTime.parse(matcher.group(), format);
	        	if (!eventTime.isAfter(SwingGUI.endTime) && !eventTime.isBefore(SwingGUI.startTime)) 
	        	{
	        	   requestPattern = "(?<=org.apache.catalina.startup.Catalina.start Server startup in )(.*)(?= ms)";
			       pattern = Pattern.compile(requestPattern);
			       matcher = pattern.matcher(line);
			       matcher.find();
			       String startupTime = matcher.group(0);
	        		events.add(new catalinaEvent (eventTime, startupTime));
	        	}
	        }
	        
	   }
	        
	 }

