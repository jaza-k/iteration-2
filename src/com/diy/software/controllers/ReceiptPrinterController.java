<<<<<<< HEAD
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

    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void turnedOn(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void turnedOff(AbstractDevice<? extends AbstractDeviceListener> device) {
    }
}
=======
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

    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void turnedOn(AbstractDevice<? extends AbstractDeviceListener> device) {
    }

    @Override
    public void turnedOff(AbstractDevice<? extends AbstractDeviceListener> device) {
    }
}
>>>>>>> main
