package com.diy.software.bags;

public class AddOwnBagLogic {

    private AddOwnBagListener addOwnBagListener;

//    public double getActualWeight() {
//        // listen for the weight change until the customer indicates that they are finished adding bags
//        return 0.0;
//    }

    public void blockStation() {
        // disable the scanner, card reader, cash input, scale?
    }

    public void unblockStation() {
        // unblock the station from the back-end
    }

    public void customerCanContinue() {
        // signal to GUI that the customer is unblocked (front-end)
    }

    public boolean promptAttendantApproval() {
        // prompts the attendant station to approve the added bags
        return true;
    }

    public void addOwnBagWeight(double totalWeight) {

    }
}
