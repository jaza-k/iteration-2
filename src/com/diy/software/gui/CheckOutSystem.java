package com.diy.software.gui;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;

public class CheckOutSystem {
    private DoItYourselfStationAR checkOutSystem;
    private Customer customer;
    private CRListener crListener;
    private BSListener bsListener;

    /**
     * Basic constructor for CheckOutSystem
     *
     * @param inputCustomer
     */
    public CheckOutSystem(Customer inputCustomer) {
        customer = inputCustomer;
        setup();
    }

    /**
     * Sets up the checkout system to make it ready for use
     */
    private void setup() {
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
        //GUI customerGUI = new GUI(checkOutSystem, customer, bank);
    }
}