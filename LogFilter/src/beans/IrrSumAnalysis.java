package beans;

import java.time.Duration;

public class IrrSumAnalysis {

	public String uuid;
	public String name;
	public float totalTickVolumeCounter ;
	public float totalTickVolumeEvent ;

	public Duration tickPeriod ;
	public Duration measurePeriod ;
	public float totalMeasurement;
	public int eventTickCount;
	public int tickCounter;
	public float tickValue;


	
	
	public IrrSumAnalysis(String uuid, String name, float totalTickVolumeCounter, float totalTickVolumeEvent, Duration tickPeriod, Duration measurePeriod,
			float totalMeasurement, int eventTickCount, int tickCounter, float tickValue) {
		super();
		this.eventTickCount = eventTickCount;
		this.uuid = uuid;
		this.name = name;
		this.totalTickVolumeCounter = totalTickVolumeCounter;
		this.totalTickVolumeEvent = totalTickVolumeEvent;
		this.tickPeriod = tickPeriod;
		this.measurePeriod = measurePeriod;
		this.totalMeasurement = totalMeasurement;
		this.tickCounter = tickCounter;
		this.tickValue = tickValue;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getTotalTickVolume() {
		return totalTickVolumeCounter;
	}
	public void setTotalTickVolume(float totalTickVolumeCounter) {
		this.totalTickVolumeCounter = totalTickVolumeCounter;
	}
	public Duration getTickPeriod() {
		return tickPeriod;
	}
	public void setTickPeriod(Duration tickPeriod) {
		this.tickPeriod = tickPeriod;
	}
	public Duration getMeasurePeriod() {
		return measurePeriod;
	}
	public void setMeasurePeriod(Duration measurePeriod) {
		this.measurePeriod = measurePeriod;
	}
	public float getTotalMeasurement() {
		return totalMeasurement;
	}
	public void setTotalMeasurement(float totalMeasurement) {
		this.totalMeasurement = totalMeasurement;
	}
	
	
}
