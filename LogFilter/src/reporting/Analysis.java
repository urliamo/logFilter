package reporting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;

import beans.DeviceMeasurements;
import beans.IrrSumAnalysis;
import beans.catalinaEvent;
import beans.customEvent;
import filters.Catalina;
import filters.Custom;
import filters.IrrigationSum;

public class Analysis {

	
	public static ArrayList<IrrSumAnalysis> irrSumAnalysis= new ArrayList<>();
	
	public static void analyzeIrrSum (JTextArea analyzeText, HashMap<String, DeviceMeasurements> measurements) {
		if (!measurements.isEmpty())
		{
			analyzeText.append("------------------Irrigation Sum analysis---------------"+"\n");
			for (DeviceMeasurements m : measurements.values()) {
				 System.out.println(IrrigationSum.devices.size());
				 float totalTickVolumeCounter = m.getTickValue()*(m.getEndTickCount()-m.getStartTickCount());
				 float totalTickVolumeEvent = m.getTickValue()*m.getTickEventcounter();
				 Duration tickDuration = Duration.between(m.firstTick, m.lastTick);
				 Duration measureDuration = Duration.between(m.firstMeasurement, m.lastMeasurement);
				 IrrSumAnalysis sumAnalysis = new IrrSumAnalysis(m.uuid, m.name, totalTickVolumeCounter,totalTickVolumeEvent, tickDuration, measureDuration, m.totalFlow, m.tickEventcounter, (m.getEndTickCount()-m.getStartTickCount()), m.tickValue);
				 irrSumAnalysis.add(sumAnalysis);		
			}
			for (IrrSumAnalysis sumAnalysis : irrSumAnalysis) {
				analyzeText.append("Device Name: "+ sumAnalysis.name+"\n");
				analyzeText.append("Device uuid: "+ sumAnalysis.uuid+"\n");
				analyzeText.append("Device TickEvent Count: "+ sumAnalysis.eventTickCount+"\n");
				analyzeText.append("Device Tick Counter (last-first): "+ sumAnalysis.tickCounter+"\n");
				analyzeText.append("Device Tick Value: "+ sumAnalysis.tickValue+"\n");
				analyzeText.append("Device total Tick Volume (by Tick Counter/Event count): "+ sumAnalysis.totalTickVolumeCounter+"/"+sumAnalysis.totalTickVolumeEvent+"\n");
				analyzeText.append("Device tickPeriod: "+ sumAnalysis.tickPeriod+"\n");
				analyzeText.append("Device Total Measurement: "+ sumAnalysis.totalMeasurement+"\n");
				analyzeText.append("Device Measurement Period: "+ sumAnalysis.measurePeriod+"\n");
				analyzeText.append("--------------------------------------------------------"+"\n");
			}
		 IrrigationSum.devices.clear();
		 System.out.println(IrrigationSum.devices.size());
		}
	}
	
	public static void analyzeCat (JTextArea analyzeText, ArrayList<catalinaEvent> events)
	{
		if (!events.isEmpty())
		{
			analyzeText.append("--------------------Catalina Analysis-------------------"+"\n");
			for (catalinaEvent e : events) {
				 LocalDateTime eventTime = e.getEventTime();
				 String eventText = e.getEventText();
				 analyzeText.append("Event Time: "+ eventTime+"\n");
				 analyzeText.append("Event Text: "+ eventText+"\n");
				 analyzeText.append("--------------------------------------------------------"+"\n");
			}
		}
		Catalina.events.clear();
	}
	
	public static void analyzeCustom (JTextArea analyzeText, HashMap<String, ArrayList<customEvent>> events)
	{
		if (!events.isEmpty())
		{
			for (String filter : events.keySet()) {
				analyzeText.append("--------------------"+filter+"-------------------"+"\n");

				for (customEvent e: events.get(filter)) 
				{
				 analyzeText.append("found in line: "+e.getLineNum()+" - "+e.getEventText()+"\n");
			}
				 analyzeText.append("--------------------------------------------------------"+"\n");

			}
		}
		Custom.events.clear();
	}
}
