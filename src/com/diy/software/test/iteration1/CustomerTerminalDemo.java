package com.diy.software.test.iteration1;

import com.diy.simulation.Customer;
import com.diy.software.gui.CheckOutSystem;

public class CustomerTerminalDemo {
    public static void main(String[] args) {
        Customer customer = new Customer();
        CheckOutSystem system = new CheckOutSystem(customer);
    }
}
