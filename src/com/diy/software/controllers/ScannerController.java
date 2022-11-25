package com.diy.software.controllers;


import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodeScannerListener;

import java.util.ArrayList;
import java.util.List;

import static com.diy.software.DoItYourselfStationLogic.Status.READY;

public class ScannerController implements BarcodeScannerListener {
    private ArrayList<BarcodedProduct> scannedItems = new ArrayList();
    private double total = 0;
    private DoItYourselfStationLogic stationLogic;

    /**
     * Basic Constructor
     *
     * @param stationLogic Logic instance that is using this controller
     */
    public ScannerController(DoItYourselfStationLogic stationLogic) {
        this.stationLogic = stationLogic;
    }

    /**
     * Barcode scanned listener
     *
     * @param barcodeScanner The barcode scanner used to scan the item
     * @param barcode        The barcode of the scanned item
     */
    @Override
    public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
        // Ignore when there system is not ready
        if (stationLogic.getStatus() != READY)
            return;

        // Ignore when there is no product associated with the barcode
        if (!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode))
            return;

        var product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

        scannedItems.add(product);
        stationLogic.scaleController.addExpectedWeight(product.getExpectedWeight());
        boolean perUnit = product.isPerUnit();
        double price;
        if (perUnit)
            price = product.getPrice();
        else
            price = product.getExpectedWeight() * product.getPrice();
        total += price;
    }

    /**
     * Obtains the total value of the items scanned with this machine
     *
     * @return The total value of items scanned during the current transaction.
     */
    public double getTotal() {
        return total;
    }

    public void setTotal(double newval) {
        total = newval;
    } //This function will be used when adding items after a partial payment

    /**
     * Obtains the list of items scanned with this machine
     *
     * @return The total list of items scanned during the current transaction.
     */
    public List<BarcodedProduct> getScannedItems() {
        return scannedItems;
    }

    /**
     * Clears the current list of items scanned with this machine and reset the total cost
     */
    public void reset() {
        scannedItems.clear();
        total = 0;
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
