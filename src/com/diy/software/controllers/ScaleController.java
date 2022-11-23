package com.diy.software.controllers;


import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class ScaleController implements ElectronicScaleListener {

    private DoItYourselfStationLogic diyStationLogic;

    public ScaleController(DoItYourselfStationLogic doItYourselfStationLogic) {
        this.diyStationLogic = doItYourselfStationLogic;
    }
    public enum Status {
        READY,
        WAITING_FOR_WEIGHT,
        DISCREPANCY,
        OVERLOAD
    }
    private Status status = Status.READY;
    private double expectedWeightInGrams = 0;

    /**
     * Increases the total expected weight of the items scanned with this machine
     *
     * @param weightInGrams the expected weight of a scanned item
     */
    public void addExpectedWeight(double weightInGrams) {
        expectedWeightInGrams += weightInGrams;
        status = Status.WAITING_FOR_WEIGHT;
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
     * Resets the total expected weight of the items scanned with this machine
     */
    public void resetExpectedWeight() {
        expectedWeightInGrams = 0;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public void weightChanged(ElectronicScale scale, double weightInGrams) {
        if(weightInGrams == expectedWeightInGrams)
            status = Status.READY;
        else
            status = Status.DISCREPANCY;
    }

    @Override
    public void overload(ElectronicScale scale) {
        status = Status.OVERLOAD;
    }

    @Override
    public void outOfOverload(ElectronicScale scale) {
        status = Status.READY;
    }
}
