package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.software.controllers.ReceiptPrinterController;
import com.diy.software.controllers.ScaleController;
import com.diy.software.controllers.ScannerController;

public class DoItYourselfStationLogic {
	
	// Can pass this to Attendant Station & check if -1
	private int issue = -1;	// -1 is default (no issue)
	
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

    public void receiptPrinterLowInk(){
        AttendantStationLogic.getInstance().notifyReceiptPrinterLowInk();
    }

    public void receiptPrinterLowPaper(){
        AttendantStationLogic.getInstance().notifyReceiptPrinterLowPaper();
    }
}
