package com.diy.software.gui;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.diy.simulation.Customer;
//import com.diy.software.payment.CashPaymentPanel;
//import com.diy.software.payment.DebitPaymentPanel;
//import com.diy.software.scansoft.AddItemScanned;

public class BasicTest {

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
		DoItYourselfStationAR s1 = new DoItYourselfStationAR();
		DoItYourselfStationAR s2 = new DoItYourselfStationAR();
		
		DoItYourselfStationAR[] stations = {s1,s2};
		
		// BELOW LINE JUST FOR NOW
		if(stations.length > 6) {
			System.out.print("\nToo many stations loaded!\n");
			System.exit(-1);
		}
		
		// START
		AttendantGUI att = new AttendantGUI(stations);
	}
	
	public static void main(String[] args) {
		attnTest();
		//custTest();	
	}

}
