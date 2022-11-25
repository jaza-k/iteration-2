package com.diy.software.controllers;


import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

import static com.diy.software.DoItYourselfStationLogic.Status.*;
import static java.lang.Math.abs;

public class ScaleController implements ElectronicScaleListener {
    private DoItYourselfStationLogic stationLogic;

    /**
     * Basic Constructor
     *
     * @param stationLogic Logic instance that is using this controller
     */
    public ScaleController(DoItYourselfStationLogic stationLogic) {
        this.stationLogic = stationLogic;
    }

    private double actualWeightInGrams = 0;
    private double expectedWeightInGrams = 0;

    /**
     * Increases the total expected weight of the items scanned with this machine
     *
     * @param weightInGrams the expected weight of a scanned item
     */
    public void addExpectedWeight(double weightInGrams) {
        expectedWeightInGrams += weightInGrams;
        stationLogic.setStatus(WAITING_FOR_WEIGHT);
    }

    /**
     * Obtains the total expected weight of the items scanned with this machine
     *
     * @return The total expected weight of items scanned during the current transaction.
     */
    public double getExpectedWeightInGrams() {
        return expectedWeightInGrams;
    }

    /**
     * Resets the total expected weight of the items scanned with this machine and marks status as ready
     */
    public void reset() {
        expectedWeightInGrams = 0;
        stationLogic.setStatus(READY);
    }

    /**
     * Sets the expected weight of the items to the actual weight of item scanned with this machine
     */
    public void clearDiscrepancy() {
        expectedWeightInGrams = actualWeightInGrams;
    }

    @Override
    public void weightChanged(ElectronicScale scale, double weightInGrams) {
        actualWeightInGrams = weightInGrams;
        if (abs(weightInGrams - expectedWeightInGrams) < scale.getSensitivity())
            stationLogic.setStatus(READY);
        else
            stationLogic.setStatus(DISCREPANCY);
    }

    @Override
    public void overload(ElectronicScale scale) {
        stationLogic.setStatus(OVERLOAD);
    }

    @Override
    public void outOfOverload(ElectronicScale scale) {
        stationLogic.setStatus(READY);
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
