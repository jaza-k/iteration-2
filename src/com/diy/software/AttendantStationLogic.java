package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.gui.AttendantGUI;
//import java.util.ArrayList;
//import java.util.List;

public class AttendantStationLogic {
    //private DoItYourselfStationLogic stationLogic;
	
    
	private static DoItYourselfStationLogic[] stations;
    
    private AttendantGUI attendantGUI;
    private static final AttendantStationLogic instance = new AttendantStationLogic(stations);
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
    
    
    public AttendantStationLogic(DoItYourselfStationLogic[] sts) {
        // add the stationLogic to the list of watched station by the attendant
    	
    	quantizeStations(sts);
    	
    	// TBH multithread?
    	
    	//while(numStations == 0) {System.out.print("\nBlah\n");}
    	//if (numStations == 0) wait();
    	
    	System.out.print("\nWe made it!\n");
    	//attendantGUI = new AttendantGUI();
    }


    public void quantizeStations (DoItYourselfStationLogic[] sts) {
    	stations = sts;
    	numStations = sts.length;
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
