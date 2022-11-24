package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.gui.AttendantGUI;
//import java.util.ArrayList;
//import java.util.List;

public class AttendantStationLogic {

	private static DoItYourselfStationLogic[] stations;
	
	private static int[] issues;	// tracks what issue stations have - ints for now
	public String[] probDesc = {
			"Weight Discrepancy",		// 0
			"Bag Approval Required",	// 1
			"Out of Bags",				// 2
			"Insufficient Change",		// 3
			"",	// 4
			"",	// 5
			"",	// 6
			"",	// 7
			"",	// 8
			""	// 9
	};
    
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
    	
    	//System.out.print("\nHello\n");
    	// block only in some cases except maybe all of them
    	
    	//System.out.print("\nFrom Notify: " + instance.stations[sID].getStation() + "\n");
    	
    	instance.stations[sID].block(instance.stations[sID].getStation());
    	//instance.stations[sID].block(instance.stations[sID]);
    	//System.out.print("\nGoodbye\n");
    	
    	attendantGUI.updateIssues();	// updates list on GUI
    }
    
    
    public void attendantDecision(int sID, boolean choice) {
    	if (choice) {
    		instance.stations[sID].unblock(instance.stations[sID].getStation());
    		instance.getStationLogic(sID).setStatus(DoItYourselfStationLogic.Status.READY);
    		
    		// update customer GUI that is OK
    		instance.getStationLogic(sID).getCustomerGUI().setTabbedFocus(0);
    		
    		attendantGUI.updateIssues();	// repaint issues
    	} else notifyProblem(sID, instance.issues[sID]);
    	// also provide opportunity to cancel IN SOME CASES
    }
    
    /*
    public void notifyAttendantOwnBag() {
    	
    }
    */
    
    public int[] getIssues() {
    	return issues;
    }
    
    public static DoItYourselfStationLogic getStationLogic(int sID) {
    	return instance.stations[sID];
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
