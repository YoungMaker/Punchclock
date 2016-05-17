package main;

import java.util.Calendar;

public class Run {
static TimeHandler time = null;


	public static void main(String[] args) {
		time = new TimeHandler(Calendar.getInstance());
		StorageFactory.getInstance();
		gui view =  gui.getInstance();
		view.setupGUI();

	}
	
	public static TimeHandler getTimeHandler() {
		return time;
	}
	
}
