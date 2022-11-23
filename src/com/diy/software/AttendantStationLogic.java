package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.gui.AttendantGUI;
//import java.util.ArrayList;
//import java.util.List;

public class AttendantStationLogic {
    
	private static DoItYourselfStationLogic[] stations;
    
    private AttendantGUI attendantGUI;
    private static final AttendantStationLogic instance = new AttendantStationLogic();
    volatile private int numStations = 0;	// incremented as stations are added

    /*
    public AttendantStationLogic() {
        stationLogic = new DoItYourselfStationLogic(new DoItYourselfStationAR());

        // add the stationLogic to the list of watched station by the attendant
    }*/
    /*    
    public void AssignStation(DoItYourselfStationLogic terminal) {
    	//stations.add(terminal);
    	System.out.print("\nWe have added a DIY Station!\n");
    }
    */
    
    public int getStationNum() {
    	return this.numStations;
    }
    
    
    public AttendantStationLogic() {
    	// TBH multithread?
    	System.out.print("\nStarted an AttendantStationLogic node!\n");
    	
    	attendantGUI = new AttendantGUI(stations);
    }


    public void quantizeStations (DoItYourselfStationLogic[] sts) {
    	this.stations = sts;
    	this.numStations = sts.length;
    }
    
    public static AttendantStationLogic getInstance() {
        return instance;
    }

    public void notifyReceiptPrinterLowPaper() {
        attendantGUI.receiptPrinterPaperLowPopUp();
    }

    public void notifyReceiptPrinterLowInk() {
        attendantGUI.receiptPrinterInkLowPopUp();
    }
}
