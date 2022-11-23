package com.diy.software.controllers;

import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.abagnale.IReceiptPrinter;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;

public class ReceiptPrinterController implements ReceiptPrinterListener {
    private DoItYourselfStationLogic stationLogic;

    /**
     * Basic Constructor
     *
     * @param stationLogic Logic instance that is using this controller
     */
    public ReceiptPrinterController(DoItYourselfStationLogic stationLogic) {
        this.stationLogic = stationLogic;
    }

    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Receipt printer is enabled");
    }

    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Receipt printer is disabled");
    }

    @Override
    public void outOfPaper(IReceiptPrinter printer) {

    }

    @Override
    public void outOfInk(IReceiptPrinter printer) {

    }

    @Override
    public void lowInk(IReceiptPrinter printer) {
        System.out.println("Receipt printer is low on ink");
        stationLogic.receiptPrinterLowInk();
    }

    @Override
    public void lowPaper(IReceiptPrinter printer) {
        System.out.println("Receipt printer is low on paper");
        stationLogic.receiptPrinterLowPaper();
    }

    @Override
    public void paperAdded(IReceiptPrinter printer) {

    }

    @Override
    public void inkAdded(IReceiptPrinter printer) {

    }
}
