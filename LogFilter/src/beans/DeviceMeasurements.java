package beans;

import java.time.LocalDateTime;

public class DeviceMeasurements {

	public float tickValue;
	public String uuid;
	public String name;
	public LocalDateTime firstMeasurement = LocalDateTime.MIN;
	public LocalDateTime lastMeasurement = LocalDateTime.MIN;
	public LocalDateTime firstTick;
	public LocalDateTime lastTick;
	public int tickEventcounter = 0;
	public int startTickCount;
	public int endTickCount;
	public float totalFlow;
	
	
	
	public DeviceMeasurements(String uuid, int tickCount, float tickValue, LocalDateTime tickTime) {
		super();
		this.tickValue = tickValue;
		this.firstTick = tickTime;
		this.uuid = uuid;
		this.startTickCount = tickCount;
		this.totalFlow = 0;
		this.tickEventcounter = 1;
	}


	public DeviceMeasurements(String uuid, String name, float quantity, LocalDateTime measurementTime) {
		super();
		this.firstMeasurement = measurementTime;
		this.uuid = uuid;
		this.name = name;
		this.totalFlow = quantity;
	}
	
	
	
	
	public int getTickEventcounter() {
		return tickEventcounter;
	}


	public void setTickEventcounter(int tickEventcounter) {
		this.tickEventcounter = tickEventcounter;
	}


	public float getTickValue() {
		return tickValue;
	}


	public void setTickValue(float tickValue) {
		this.tickValue = tickValue;
	}


	public LocalDateTime getFirstTick() {
		return firstTick;
	}


	public void setFirstTick(LocalDateTime firstTick) {
		this.firstTick = firstTick;
	}


	public LocalDateTime getLastTick() {
		return lastTick;
	}


	public void setLastTick(LocalDateTime lastTick) {
		this.lastTick = lastTick;
	}


	public float getTotalFlow() {
		return totalFlow;
	}


	public void setTotalFlow(float totalFlow) {
		this.totalFlow = totalFlow;
	}


	public int getTickCount() {
		return startTickCount;
	}


	public void setTickCount(int tickCount) {
		this.startTickCount = tickCount;
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
	public float getQuantity() {
		return totalFlow;
	}
	public void setQuantity(float quantity) {
		this.totalFlow = quantity;
	}
	
	public LocalDateTime getFirstMeasurement() {
		return firstMeasurement;
	}


	public void setFirstMeasurement(LocalDateTime firstMeasurement) {
		this.firstMeasurement = firstMeasurement;
	}


	public LocalDateTime getLastMeasurement() {
		return lastMeasurement;
	}


	public void setLastMeasurement(LocalDateTime lastMeasurement) {
		this.lastMeasurement = lastMeasurement;
	}


	public int getStartTickCount() {
		return startTickCount;
	}


	public void setStartTickCount(int startTickCount) {
		this.startTickCount = startTickCount;
	}


	public int getEndTickCount() {
		return endTickCount;
	}


	public void setEndTickCount(int endTickCount) {
		this.endTickCount = endTickCount;
	}
}
