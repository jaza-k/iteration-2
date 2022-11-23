package com.diy.software.gui;

import java.util.ArrayList;
import java.util.List;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.diy.simulation.Customer;

// NEW RE: STATION LOGICS, DEMO, CONTROLLERS
import com.diy.software.*;

public class BasicTests {

	public static DoItYourselfStationLogic[] stationsGen(int n){//List<DoItYourselfStationLogic> stationsGen(int n) {
		// Init List
		//List<DoItYourselfStationLogic> sts = new ArrayList<DoItYourselfStationLogic>();
		DoItYourselfStationLogic[] sts = {};
		
		for(int i = 0; i < n; i++) {
			//String sName = "Station " + Integer.toString(i);
			//System.out.print("\n" + sName + "\n");
			
			DoItYourselfStationAR stat = new DoItYourselfStationAR();
			DoItYourselfStationLogic newStation = new DoItYourselfStationLogic(stat);
			
			sts[i] = newStation;
		}
		
		return sts;
	}
	
	public static void custTest() {
		// SETUP
		DoItYourselfStationAR doit = new DoItYourselfStationAR();
		Customer cust = new Customer();
		CardIssuer bank = new CardIssuer("Paul",12);
		
		TouchScreen screen = new TouchScreen();
		screen.plugIn();
		screen.turnOn();
		
		// START
		//GUI gui = new GUI(doit,cust,bank,screen);
	}
	
	public static void attnTest() {
		
		// TEST
		DoItYourselfStationAR r1 = new DoItYourselfStationAR();
		DoItYourselfStationAR r2 = new DoItYourselfStationAR();
		DoItYourselfStationLogic s1 = new DoItYourselfStationLogic(r1);
		DoItYourselfStationLogic s2 = new DoItYourselfStationLogic(r2);
		
		DoItYourselfStationLogic[] stations = {s1,s2};
		
		// BELOW LINE JUST FOR NOW
		if(stations.length > 6) {
			System.out.print("\nToo many stations loaded!\n");
			System.exit(-1);
		}
		
		// START
		AttendantGUI att = new AttendantGUI(stations);
	}
	
	public static void main(String[] args) {
		
		// CHEAP TEST FOR @ DEMO STEPS
		DoItYourselfStationLogic[] sts = stationsGen(5);
		System.out.print(sts.length);
		
		// ~DEPRECATED, NOW @ DEMO
		//attnTest();
		//custTest();
	}

}
