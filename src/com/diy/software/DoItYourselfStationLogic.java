package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.controllers.ReceiptPrinterController;
import com.diy.software.controllers.ScaleController;
import com.diy.software.controllers.ScannerController;


// bag item
import com.jimmyselectronics.necchi.*;

public class DoItYourselfStationLogic {
	
	// Can pass this to Attendant Station & check if -1
	private int issue = -1;	// -1 is default (no issue)
	
	
	// ADD OWN BAG (GUI etc)
	private boolean freezeButtons = false;
	
	// TODO: make in demo
	public Barcode bar = new Barcode(new Numeral[] { Numeral.one });
	public BarcodedItem ownBag = new BarcodedItem(bar, 1);
	
	
	
    /**
     * The station on which the logic is installed.
     */
    public DoItYourselfStationAR station;
    /**
     * The controller that tracks the scanned products
     */
    public ScannerController scannerController;
    /**
     * The controller that tracks the electronic scale
     */
    public ScaleController scaleController;
    /**
     * The controller that tracks the receipt printer
     */
    public ReceiptPrinterController receiptPrinterController;

    /**
     * Installs an instance of the logic on the indicated station.
     *
     * @param station
     *            The station on which to install the logic.
     * @return The newly installed instance.
     * @throws NullPointerException
     *             If any argument is null.
     */
    public static DoItYourselfStationLogic installOn(DoItYourselfStationAR station) {
        return new DoItYourselfStationLogic(station);
    }

    /**
     * Basic constructor.
     *
     * @param station
     *            The station on which to install the logic.
     * @return The newly installed instance.
     * @throws NullPointerException
     *             If any argument is null.
     */
    public DoItYourselfStationLogic(DoItYourselfStationAR station) {
        this.station = station;
        // Have to plug in the station
        station.plugIn();
        station.turnOn();
        
        scannerController = new ScannerController(this);
        station.scanner.plugIn();
        station.scanner.turnOn();
        station.scanner.register(scannerController);

        scaleController = new ScaleController(this);
        station.scale.plugIn();
        station.scale.turnOn();
        station.scale.register(scaleController);

        receiptPrinterController = new ReceiptPrinterController(this);
        station.printer.plugIn();
        station.printer.turnOn();
        station.printer.register(receiptPrinterController);
        

        
        // BELOW BLOCK IS TESTS - USEFUL ONES SHOULD BE VIA GUI BUTTONS TO FUNCTION
        //System.out.print("\n" + station + "\n");
        //receiptPrinterLowInk();	// TEST
        //System.out.print("\n" + Integer.toString(getStationID()) + "\n");
        
        
    }
    
    public int getStationID() {
    	return AttendantStationLogic.matchStationID(this);	// clumsy implementation here (needless extra steps)
    }
    
    public DoItYourselfStationAR getStation() {
    	return this.station;
    }
    
    
    /*
     *  ADD OWN BAG
     */
    public void bagApproval() {
    	
    	System.out.print("\nAdding Own Bags Now...\n");
    	// notify station done adding to scale
    	
    	/*
    	try {
    		this.station.scale.getCurrentWeight();
    	} catch (Exception oe) {
    		System.out.print("\nBag Add Did Not Work\n");
    	} finally {
    		System.out.print("\ndone did\n");
    	}
    	*/
    	
    	// NO EXPECTED WEIGHT
    	this.station.scale.add(ownBag);
    	
    	// IF ATTENDANT OK, add ownBag.weight to ExceptedWeight (scale controller)
    	
    	// vet
    	//this.block();
    	
    	AttendantStationLogic.getInstance().notifyProblem(this.getStationID(),1);
    	// attendant unfreezes buttons
    }
    
    
    public void block(DoItYourselfStationAR stat) {	//int sID) {
    	//freezeButtons = true;
    	
    	//System.out.print("\nFrom Block: " + stat + "\n");
    	
    	// MAYBE MORE???
    	stat.scanner.turnOff();
    	stat.cardReader.turnOff();
    	stat.printer.turnOff();
    	stat.banknoteInput.disactivate();
    	stat.banknoteOutput.disactivate();
    	stat.coinSlot.disactivate();
    	stat.cardReader.turnOff();
    	
    	//AttendantStationLogic.getInstance().stations[sID].scanner.disable();
    	
    	// DISABLE STUFF
    	// blah blah blah
    }
    
    public void unblock(DoItYourselfStationAR stat) {
    	//freezeButtons = false;
    	
    	// MAYBE MORE???
    	stat.scanner.turnOn();
    	stat.cardReader.turnOn();
    	stat.printer.turnOn();
    	stat.banknoteInput.activate();
    	stat.banknoteOutput.activate();
    	stat.coinSlot.activate();
    	stat.cardReader.turnOn();
    }
    
    
    public void receiptPrinterLowInk(){
        AttendantStationLogic.getInstance().notifyReceiptPrinterLowInk();
    }

    public void receiptPrinterLowPaper(){
        AttendantStationLogic.getInstance().notifyReceiptPrinterLowPaper();
    }
}
