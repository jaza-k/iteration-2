package com.diy.software.gui;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.print.ReceiptPrinterStub;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.abagnale.IReceiptPrinter;

public class
ReceiptPrinterListenerStub extends AbstractDevice<com.jimmyselectronics.abagnale.ReceiptPrinterListener> implements   com.jimmyselectronics.abagnale.ReceiptPrinterListener {

    DoItYourselfStationAR doItYourselfStationAR;
    ReceiptPrinterStub receiptPrinterStub;

    public ReceiptPrinterListenerStub(){};

    @Override
    public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Printer is enabled");
    }

    @Override
    public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Printer is disabled");
    }

    @Override
    public void turnedOn(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Printer is turned on");
    }

    @Override
    public void turnedOff(AbstractDevice<? extends AbstractDeviceListener> device) {
        System.out.println("Printer is turned off");
    }

    @Override
    public void outOfPaper(IReceiptPrinter printer) {
//        printer.print();
    }

    @Override
    public void outOfInk(IReceiptPrinter printer) {

    }

    @Override
    public void lowInk(IReceiptPrinter printer) {
        boolean lowInk = true;
        //disable the printer
        receiptPrinterStub.disable();
        // block the system (should it)?
        doItYourselfStationAR.scanner.disable();
        doItYourselfStationAR.cardReader.disable();
        doItYourselfStationAR.coinSlot.disable();
        System.out.println("Printer is low on ink");
        // notify the attendant

    }

    @Override
    public void lowPaper(IReceiptPrinter printer) {
        boolean lowPaper = true;
        //disable the printer
        doItYourselfStationAR.printer.disable();
        // block the system (should it)?
        doItYourselfStationAR.scanner.disable();
        doItYourselfStationAR.cardReader.disable();
        doItYourselfStationAR.coinSlot.disable();
        System.out.println("Printer is low on paper");


    }
    @Override
    public void paperAdded(IReceiptPrinter printer) {

    }

    @Override
    public void inkAdded(IReceiptPrinter printer) {

    }
}
