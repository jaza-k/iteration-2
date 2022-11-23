package com.diy.software.gui;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.bags.AddOwnBagListener;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.disenchantment.TouchScreenListener;

public class CheckOutSystem {
	private DoItYourselfStationAR checkOutSystem;
	private Customer customer;
	private CRListener crListener;
	private BSListener bsListener;

	private AddOwnBagListener addOwnBagListener;
	private TouchScreen customerScreen;

	public CheckOutSystem(Customer inpuCustomer) {
		customer = inpuCustomer;
		setup();
	}

	private void setup() {
		checkOutSystem = new DoItYourselfStationAR();
		checkOutSystem.plugIn();
		checkOutSystem.turnOn();
		checkOutSystem.cardReader.plugIn();
		checkOutSystem.cardReader.turnOn();
		//Creating a touch screen - for the customer
		customerScreen = new TouchScreen();
		customerScreen.plugIn();
		customerScreen.turnOn();

		//TODO Create Touch Screen Listener and register it to the corresponding touch-screen

		bsListener = new BSListener();
		crListener = new CRListener();

		checkOutSystem.scanner.register(bsListener);
		checkOutSystem.cardReader.register(crListener);

		customer.useStation(checkOutSystem);
		// At this point we need to setup the customer's data
		DataSetup customerData = new DataSetup(customer);
		CardIssuer bank = customerData.getBank();
		GUI customerGUI = new GUI(checkOutSystem, customerScreen, customer, bank);
	}
}