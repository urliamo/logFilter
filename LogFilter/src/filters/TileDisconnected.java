package filters;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.DeviceMeasurements;

public class TileDisconnected {
	public static boolean activeFilter = true;
	public static HashMap<String, DeviceMeasurements> devices = new HashMap<>();
	public static String filterMeasure = "EventMeasurementProcessed";
	public static String filterTickEvent = "TickEvent";
	private static DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	
    public static void filterTrace(String line) throws ParseException {
    	String requestPattern;
    	Pattern pattern;
    	Matcher matcher;
        if (line.contains(filterTickEvent) ) {
        	requestPattern = "((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?(?=::)";
        	pattern = Pattern.compile(requestPattern);
        	matcher = pattern.matcher(line);
        	matcher.find();
        	LocalDateTime measurementTime = LocalDateTime.parse(matcher.group(0), format);
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
