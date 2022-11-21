package com.diy.software.gui;

import com.diy.hardware.DoItYourselfStationAR;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;

public class CheckOutSystem {
	private DoItYourselfStationAR checkOutSystem;
	private Customer customer;
	private CRListener crListener;
	private BSListener bsListener;
	private TouchScreen screen;
	
	public CheckOutSystem(Customer inpuCustomer) {
		customer = inpuCustomer;
		setup();
	}
	
	
	
	private void setup() {
		screen = new TouchScreen(); // customer station no longer has a touch screen? Making touch screen object here instead then
		screen.plugIn();
		screen.turnOn();
		
		checkOutSystem = new DoItYourselfStationAR();
		checkOutSystem.plugIn();
		checkOutSystem.turnOn();
		checkOutSystem.cardReader.plugIn();
		checkOutSystem.cardReader.turnOn();
		bsListener = new BSListener();
		crListener = new CRListener();
		
		checkOutSystem.scanner.register(bsListener);
		checkOutSystem.cardReader.register(crListener);
		
		customer.useStation(checkOutSystem);
		// At this point we need to setup the customer's data
		DataSetup customerData = new DataSetup(customer);
		CardIssuer bank = customerData.getBank();
		GUI customerGUI = new GUI(checkOutSystem, customer, bank, screen);
	}
}