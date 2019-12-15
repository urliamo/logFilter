package filters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import beans.customEvent;
import gui.SwingGUI;

public class Custom {
	public static boolean activeFilter = true;
	public static HashMap<String, ArrayList<customEvent>> events = new HashMap<>(); //list containing all events discovered
	
	 public static void customFilters(String line, String customFilter, int lineNum) throws ParseException, IOException 
	 {
	    	String requestPattern = customFilter;
	    	Pattern pattern;
	    	Matcher matcher;
	    	pattern = Pattern.compile(requestPattern);
        	matcher = pattern.matcher(line);
	        	if ( matcher.find()) {
	        		if (SwingGUI.output) {
	        		File logFile = new File(SwingGUI.outputFolder+"\\customFilters");
	        		logFile.createNewFile();
	        		FileWriter fr = new FileWriter(logFile, true);
	        		fr.write(customFilter+" found in line: "+lineNum+" - "+line+"\n");
	        		fr.close();
	        		}
	        		if (events.get(customFilter) == null) {
	        			events.put(customFilter, new ArrayList<customEvent>());
	        		}
	        		events.get(customFilter).add(new customEvent (lineNum, line, customFilter));

	        		}

		     }
	       
	        
	   }
	        
