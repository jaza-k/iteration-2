package com.diy.software.bags;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class AddOwnBagListener implements ElectronicScaleListener {

    public AbstractDevice<? extends AbstractDeviceListener> device = null;

    long totalWeight;

    public AddOwnBagListener() {
        this.totalWeight = 0;
    }

    public long getTotalWeight() {
        return this.totalWeight;
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

    @Override
    public void weightChanged(ElectronicScale scale, double weightInGrams) {

    }

    @Override
    public void overload(ElectronicScale scale) {
        // prevent the customer from doing anything
    }

    @Override
    public void outOfOverload(ElectronicScale scale) {

    }
}
