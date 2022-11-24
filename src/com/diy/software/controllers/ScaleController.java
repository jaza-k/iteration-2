package com.diy.software.controllers;


import com.diy.software.DoItYourselfStationLogic;
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

    @Override
    public void weightChanged(ElectronicScale scale, double weightInGrams) {
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
}
