package main;

import java.util.Calendar;

public class TimeHandler {
	private Calendar lastClockIN = null;
//	private Calendar lastLaunch = null;
	private boolean state = false;

	public TimeHandler(Calendar now) {
		
//		lastLaunch = Calendar.getInstance();
//		lastLaunch.setTimeInMillis(Long.parseLong(StorageFactory.getInstance().getProperty("timestamp")));
		StorageFactory.getInstance().addProperty("timestamp", "" + now.getTimeInMillis());
		StorageFactory.getInstance().saveProperties();
	}
	
	private void clockIn() {
		lastClockIN = Calendar.getInstance();
	}
	
	private void clockOut() {
		saveHours();
		lastClockIN = null;
		
	}
	
	public void saveHours() {
		StorageFactory fact = StorageFactory.getInstance();
		if(fact.getProperty("totalHours") != null){
			fact.addProperty("totalHours", "" + (Long.parseLong(fact.getProperty("totalHours")) + getMillisIN()));
			//TODO: FIX, this adds hours multiple times, resulting in a huge windup
		}
		else {
			fact.addProperty("totalHours", "" + getMillisIN());
		}
		fact.saveProperties();
	}
	private long getTotalMillis() {
		StorageFactory fact = StorageFactory.getInstance();
		if(fact.getProperty("totalHours") != null){
			//System.out.println(fact.getProperty("totalHours"));
			return (Long.parseLong(fact.getProperty("totalHours")) + getMillisIN());
		}
		else {
			return getMillisIN();
		}
	}
	
	public String getTimeIN() {
		return millisToString(getMillisIN());
	}
	
	public String getTotalTime() {
		//System.out.println(getTotalMillis());
		return millisToString(getTotalMillis());
	}
	
	public void ResetTime() {
		StorageFactory.getInstance().addProperty("totalHours","0");
		StorageFactory.getInstance().saveProperties();
	}
	
	private String millisToString(long millis) {
		int seconds = (int) (millis / 1000) % 60 ;
		int minutes = (int) ((millis/ (1000*60)) % 60);
		int hours   = (int) ((millis / (1000*60*60))); //BUG FIXED, hours should stack higher than 24
		return "" + hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
	}
	
	private long getMillisIN() {
		if(lastClockIN != null) {
			return Calendar.getInstance().getTimeInMillis() - lastClockIN.getTimeInMillis();
		}
		return 0;
	}
	
	public void flipState() {
		state = !state;
		if(state){
			clockIn();
		}
		else {
			clockOut();
		}
	}

	public boolean getState() {
		return state;
	}
	

}
