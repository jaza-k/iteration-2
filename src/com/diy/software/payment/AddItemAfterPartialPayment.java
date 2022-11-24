package com.diy.software.payment;

import com.diy.software.controllers.ScannerController;

public class AddItemAfterPartialPayment
{
    public static void AddAfterPartial(Payment partial, ScannerController control)
    {
        //This function should be called from the customer gui when the customer cancels a checkout to add more items.

        if (partial.checkoutTotal >= 0)
        {
            control.setTotal(partial.checkoutTotal);
        }
    }
}
