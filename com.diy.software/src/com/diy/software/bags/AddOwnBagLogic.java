package com.diy.software.bags;

public class AddOwnBagLogic {

    private AddOwnBagListener addOwnBagListener;

    public void addUpTotalWeight() {
        // listen for the weight change until the customer indicates that they are finished adding bags
        // take the weight change and add it to the total expected weight
    }

    public void blockStation() {
        // block the station so the customer cannot perform any actions
    }

    public void unblockStation() {
        // unblock the station from the back-end
    }

    public void customerCanContinue() {
        // signal to GUI that the customer is unblocked (front-end)
    }

    public void approveAddedBags() {
        // prompts the attendant station to approve the added bags
    }
}
