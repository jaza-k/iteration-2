package com.diy.software.bags;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class AddOwnBagListener implements ElectronicScaleListener {

    private final AddOwnBagLogic addOwnBagLogic;

    private ElectronicScale scale;

    //creating instanced values for testing scale, don't use this for final commit
    double weightLimitInGrams;
    double sensitivity;

    private boolean attendantApproval;

    public AbstractDevice<? extends AbstractDeviceListener> device = null;

    double totalWeight;

    public AddOwnBagListener(AddOwnBagLogic addOwnBagLogic) {
        this.addOwnBagLogic = addOwnBagLogic;
        this.weightLimitInGrams = 1000.0;
        this.sensitivity = 1.0;
        this.scale = new ElectronicScale(this.weightLimitInGrams, this.sensitivity);
        this.totalWeight = 0.0;
        this.attendantApproval = false;

    }

    public double getTotalWeight() {
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

        try {
            // get the actual weight from the scale
            // are there two scales? one in the bagging area and one in the scanning area?
            System.out.println("totalWeight before adding own bags: " + totalWeight + "\n");
            Barcode barcode = new Barcode(new Numeral[] { Numeral.six, Numeral.nine, Numeral.six, Numeral.nine });
            BarcodedItem ownBag = new BarcodedItem(barcode, weightInGrams);
            scale.add(ownBag);
            totalWeight = scale.getCurrentWeight();
            System.out.println("totalWeight after adding own bags: " + totalWeight + "\n");
            // wait for Attendant approval before adding own bags weight to the total expected weight
            attendantApproval = addOwnBagLogic.promptAttendantApproval();
            // add it to the expected weight if Attendant approves
            // IS THIS the right way to allow the customer bag weight?
            if (attendantApproval) {
                addOwnBagLogic.addOwnBagWeight(totalWeight);
            } else {
                block();
            }
            // block if Attendant doesn't approve
            //
        }
        catch (OverloadException e) {
            System.out.println("OverloadException thrown. Weight limit exceeded.");
        }
    }

    @Override
    public void overload(ElectronicScale scale) {
        // prevent the customer from doing anything, weight has exceeded the limit
        block();
    }

    @Override
    public void outOfOverload(ElectronicScale scale) {
        // unblock the station
        unblock();
    }

    public void block() {
        addOwnBagLogic.blockStation();
    }

    public void unblock() {
        addOwnBagLogic.unblockStation();
    }

    // System: Indicates that the customer should add their own bags now.
    public void notifyOwnBagPlacement() {

    }

    public static void main(String[] args) throws OverloadException {
        double weightLimitInGrams = 1000.0;
        double sensitivity = 1.0;
        double weightOfOwnBags = 2.5;
        ElectronicScale scale = new ElectronicScale(weightLimitInGrams, sensitivity);
        scale.plugIn();
        scale.turnOn();
        scale.enable();
        AddOwnBagLogic addOwnBagLogic = new AddOwnBagLogic();
        AddOwnBagListener addOwnBagListener = new AddOwnBagListener(addOwnBagLogic);
        addOwnBagListener.weightChanged(scale, weightOfOwnBags);
    }

}
