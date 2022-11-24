package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.gui.AttendantGUI;
//import java.util.ArrayList;
//import java.util.List;

public class AttendantStationLogic {
    
	private static int[] issues;	// tracks what issue stations have - ints for now
	private static DoItYourselfStationLogic[] stations;
    
    private AttendantGUI attendantGUI;
    private static final AttendantStationLogic instance = new AttendantStationLogic();
    private int numStations = 0;	// incremented as stations are added
    private static boolean stationsBound = false;
    

    

    /*
     * 
     * WE DO THE STUFF HERE
     * 
     */
    public AttendantStationLogic() {
    	// TBH multithread?
    	//System.out.print("\nStarted an AttendantStationLogic node!\n");
    	
    	attendantGUI = new AttendantGUI(stations);
    }

    // needs to be private??? see first line in body
    public void quantizeStations (DoItYourselfStationLogic[] sts) {
    	if (this.equals(instance)) {
	    	this.stations = sts;
	    	this.issues = new int[sts.length];
	    	for(int i = 0; i < sts.length; i++) {
	    		this.issues[i] = 0;
	    	}
	    	this.numStations = sts.length;
    	}
    }
    
    public static int matchStationID (DoItYourselfStationLogic station) {
    	int id = -1;
    	if(stations != null) {
	    	//System.out.print("\nWe have " + stations.length + " stations...\n");
	    	for (int i = 0; i < stations.length; i++) {
	    		if (station.equals(stations[i])) {
	    			id = i;
	    			break;
	    		}
	    	}
    	}
    	return id;
    }
    
    // Note that all DIY Stations are now bound to the Attendant Station
    public void bind() {
    	stationsBound = true;
    }
    
    /*
    * BELOW: returns if all stations are bound yet (largely for AttendantGUI purposes)
    */
    // TO-DO: check if there are unintended interactions with clicking buttons before binding...
    // ... is only because this is the (decently sub-optimal...) way I am obtaining station IDs
    // ... has since occurred to me that this implementation is bleh and somewhat needless ._.
    public static boolean checkBound() {
    	return stationsBound;
    }
    
    // track that there is an issue with a station... should be implemented by listener
    // params: station id, problem id (int for now)
    public void notifyProblem (int sID, int pID) {
    	instance.issues[sID] = pID;
    	attendantGUI.updateIssues();
    }
    
    public int[] getIssues() {
    	return issues;
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
