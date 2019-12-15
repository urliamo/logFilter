package filters;


	
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.regex.*;

import javax.swing.JTextArea;

import beans.DeviceMeasurements;
import gui.SwingGUI;
	
	public class IrrigationSum {
		public static boolean activeFilter = true;
		//public static File logFile = new File("");

		public static HashMap<String, DeviceMeasurements> devices = new HashMap<>();
		public static String filterMeasure = "EventMeasurementProcessed";
		public static String filterTickEvent = "TickEvent";
    	//private static DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

		
		public static void displayDescription(JTextArea description) {
			description.setText("Irrigation Sum Filter:"+"\n");
			description.append("checks for the duration and amount of irrigation done by each device."+"\n");
			description.append("Uses the TraceToException and IOBusAction logs."+"\n");
			description.append("Analysis description:"+"\n");
			description.append("Device TickEvent Count - amount of Tick Events for this device."+"\n");
			description.append("Device Tick Counter - the difference between the first and last tick counts as counted by MCU."+"\n");
			description.append("Device Tick Value - the value of each individual Tick."+"\n");
			description.append("Device total Tick Volume - the total volume (value*count) by both event count and tick counter."+"\n");
			description.append("Device tickPeriod - the difference of time between the first and last tick for this device."+"\n");
			description.append("Device Total Measurement - the total flow of the device."+"\n");
			description.append("Device Measurement Period - the difference of tiem between the first and last measurement for this device."+"\n");
		}
		
	    public static void filterIO(String line) throws ParseException, IOException {
	    	File logFile = new File(SwingGUI.outputFolder+"\\IrrSum");
	    	String requestPattern;
	    	Pattern pattern;
	    	Matcher matcher;
	        if (line.contains(filterTickEvent) ) {
	        	requestPattern = "((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?(?=::)";
	        	pattern = Pattern.compile(requestPattern);
	        	matcher = pattern.matcher(line);
	        	matcher.find();
	        	LocalDateTime measurementTime = LocalDateTime.parse(matcher.group(0), SwingGUI.format);
		        	if (!measurementTime.isAfter(SwingGUI.endTime) && !measurementTime.isBefore(SwingGUI.startTime)) 
		        	{
		        		if (SwingGUI.output) {
		        		FileWriter fr = new FileWriter(logFile, true);
		        		fr.write(line+"\n");
		        		fr.close();
		        		}
			        	requestPattern = "(?<=\"tickCounter\":)(.*)(?=})";
			        	pattern = Pattern.compile(requestPattern);
			        	matcher = pattern.matcher(line);
			        	matcher.find();
			        	int tickCounter = Integer.parseInt(matcher.group(0));
			        	requestPattern = "(?<=uuid\":\")(.*)(?=\",\"mcu)";
			        	pattern = Pattern.compile(requestPattern);
			        	matcher = pattern.matcher(line);
			        	matcher.find();
			        	String uuid = matcher.group(0);
			        	requestPattern = "(?<=\"qValue\":)(.*)(?=,\"d)";
			        	pattern = Pattern.compile(requestPattern);
			        	matcher = pattern.matcher(line);
			        	matcher.find();
			        	float tickValue = Float.parseFloat(matcher.group(0));
			        	
			     
			        	if (devices.get(uuid) != null) {
			        		DeviceMeasurements device = devices.get(uuid);
			        			if (device.firstTick == null) {
			        				device.setFirstTick(measurementTime);
			        			}
			        			if (device.tickValue == 0) {
			        				device.setTickValue(tickValue);
			        			}
			        			device.tickEventcounter++;
			        			device.endTickCount = tickCounter;
			        			device.lastTick = measurementTime;
			        		}
			        	else	
			        	{
			        		
			        		DeviceMeasurements newMeasure = new DeviceMeasurements(uuid,tickCounter,tickValue, measurementTime);
			        		devices.put(uuid, newMeasure);
			        	}
		        	}
	        }
	    }
	        
	        public static void filterTrace(String line) throws ParseException, IOException {
		    	String requestPattern;
		    	Pattern pattern;
		    	Matcher matcher;
		        if (line.contains(filterMeasure) && !line.contains("unitOfMeasurement\":\"bit") && !line.contains("Listener")) 
		        {
		        	requestPattern = "((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?(?=::)";
		        	pattern = Pattern.compile(requestPattern);
		        	matcher = pattern.matcher(line);
		        	matcher.find();
		        	LocalDateTime measurementTime = LocalDateTime.parse(matcher.group(0), SwingGUI.format);
		        		if (!measurementTime.isAfter(SwingGUI.endTime) && !measurementTime.isBefore(SwingGUI.startTime)) 
		        		{
		        			if (SwingGUI.output) {
		    		        	File logFile = new File(SwingGUI.outputFolder+"\\IrrSum");

				        		FileWriter fr = new FileWriter(logFile, true);
				        		fr.write(line+"\n");
				        		fr.close();
				        		}
				        	requestPattern = "(?<=uuid\":\")(.{36})";
				        	pattern = Pattern.compile(requestPattern);
				        	matcher = pattern.matcher(line);
				        	matcher.find();
				        	String uuid = matcher.group(0);
				        	requestPattern = "(?<=sourceDeviceName\":\")(.*)(?=\",\"value\")";
				        	pattern = Pattern.compile(requestPattern);
				        	matcher = pattern.matcher(line);
				        	matcher.find();
				        	String name = matcher.group(0); 
				        	requestPattern = "(?<=value\":)(.*)(?=,\"date\")";
				        	pattern = Pattern.compile(requestPattern);
				        	matcher = pattern.matcher(line);
				        	matcher.find();
				        	float measurementValue = Float.parseFloat(matcher.group(0));
				        	if (devices.get(uuid) != null) {
				        		DeviceMeasurements device = devices.get(uuid);
				        		if (device.firstMeasurement == null) {
				        			device.setFirstMeasurement(measurementTime);
			        			}
				        		if (device.name == null) {
				        			device.setName(name);
			        			}
				        		device.totalFlow += measurementValue;
			        			device.lastMeasurement = measurementTime;
				        	}
				        	else{
				        		DeviceMeasurements newMeasure = new DeviceMeasurements(uuid, name, measurementValue, measurementTime);
				        		devices.put(uuid, newMeasure);
				        	}
				        } 
		        }
	    }
	}


