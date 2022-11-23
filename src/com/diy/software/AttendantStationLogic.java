package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.gui.AttendantGUI;

public class AttendantStationLogic {
    private DoItYourselfStationLogic stationLogic;
    private AttendantGUI attendantGUI;
    private static final AttendantStationLogic instance = new AttendantStationLogic();

    public AttendantStationLogic() {
        stationLogic = new DoItYourselfStationLogic(new DoItYourselfStationAR());

        // add the stationLogic to the list of watched station by the attendant
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
