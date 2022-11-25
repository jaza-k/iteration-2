package com.diy.software.test.iteration2;

import ca.powerutility.NoPowerException;
import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.AttendantStationLogic;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.abs;
import static org.junit.Assert.assertTrue;


public class AddOwnBagTest {

    DoItYourselfStationAR station;
    DoItYourselfStationLogic stationLogic;
    Customer customer;


    Barcode barcode1 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem ownBags = new BarcodedItem(barcode1, 10);

//    Barcode barcode2 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 2)});
//    BarcodedItem suspiciousItem = new BarcodedItem(barcode2, 500);

    Barcode barcode3 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 3)});
    BarcodedItem item = new BarcodedItem(barcode3, 50);
    BarcodedProduct product = new BarcodedProduct(barcode3, "Test product 1", 10, 50);


    Double weightFromScale;

    Boolean flag = false;

    @Before
    public void setUp() {
        station = new DoItYourselfStationAR();
        stationLogic = new DoItYourselfStationLogic(station);
        AttendantStationLogic.getInstance().quantizeStations(new DoItYourselfStationLogic[]{stationLogic});
        customer = new Customer();
        customer.useStation(station);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, product);
        customer.shoppingCart.add(item);
        customer.selectNextItem();

    }

    @Test
    public void addOwnBagsTest() {
        stationLogic.bagApproval(ownBags);
        try {
            weightFromScale = station.scale.getCurrentWeight();
        }
        catch (OverloadException e) {
            System.out.println("OverloadException thrown");
        }
        if (station.scale.getSensitivity() > abs(weightFromScale - ownBags.getWeight()) && !station.scanner.isPoweredUp()) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void addOwnBagsUnblockTest() {
        stationLogic.bagApproval(ownBags);
        try {
            weightFromScale = station.scale.getCurrentWeight();
        }
        catch (OverloadException e) {
            System.out.println("OverloadException thrown");
        }
        if (station.scale.getSensitivity() > abs(weightFromScale - ownBags.getWeight()) && !station.scanner.isPoweredUp()) {
            stationLogic.unblock(station);
            if (station.scanner.isPoweredUp()) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void tryScanItemWhileBaggingTest() {
        stationLogic.bagApproval(ownBags);
        try {
            customer.scanItem();
        }
        catch (NoPowerException e) {
            flag = true;
        }
        assertTrue(flag);
    }
//    @Test
//    public void blockSuspiciousItem() {
//        System.out.println(station.scanner.isPoweredUp());
//        stationLogic.bagApproval(suspiciousItem);
//        System.out.println(station.scanner.isPoweredUp());
//        try {
//            weightFromScale = station.scale.getCurrentWeight();
//            // simulate blocking a station from the attendant side
//            // for some reason this block isn't working
////            System.out.println(station.scanner.isPoweredUp());
//            stationLogic.block(station);
////            System.out.println(station.scanner.isPoweredUp());
//            station.scanner.unplug();
////            System.out.println(station.scanner.isPluggedIn());
//        }
//        catch (OverloadException e) {
//            System.out.println("OverloadException thrown");
//        }
//        if (!station.scanner.isPluggedIn()) {
//            flag = true;
//        }
//        assertTrue(flag);
//    }

}

