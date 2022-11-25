<<<<<<< HEAD
package com.diy.software.test.iteration2;

import ca.powerutility.NoPowerException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.OutOfCashException;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BankCoinHardwareTest {

    private static class NoteDispenserObserver implements BanknoteDispenserObserver {
        String name;

        public NoteDispenserObserver(String newname) {
            name = newname;
        }

        @Override
        public void moneyFull(IBanknoteDispenser dispenser) {
            System.out.println(name + ": Dispenser full.");
        }

        @Override
        public void banknotesEmpty(IBanknoteDispenser dispenser) {
            System.out.println(name + ": Dispenser empty.");
        }

        @Override
        public void billAdded(IBanknoteDispenser dispenser, Banknote banknote) {
            System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill added");
        }

        @Override
        public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
            System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill removed.");
        }

        @Override
        public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
            System.out.println(name + ": banknotes loaded.");
        }

        @Override
        public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
            System.out.println(name + ": banknotes unloaded");
        }
    }

    private static class NoteStorageObserver implements BanknoteStorageUnitObserver {
        String name;

        public NoteStorageObserver(String newname) {
            name = newname;
        }

        @Override
        public void banknotesFull(BanknoteStorageUnit unit) {
            System.out.println(name + ": Banknote storage unit full.");
        }

        @Override
        public void banknoteAdded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknote added to storage. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }

        @Override
        public void banknotesLoaded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknotes loaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }

        @Override
        public void banknotesUnloaded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknotes unloaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }
    }

    private static class NoteValidObserver implements BanknoteValidatorObserver {
        String name;

        public NoteValidObserver(String newname) {
            name = newname;
        }

        @Override
        public void validBanknoteDetected(BanknoteValidator validator, Currency currency, long value) {
            System.out.println(name + ": Valid " + currency.getDisplayName() + " bill detected. Value == " + Long.toString(value));
        }

        @Override
        public void invalidBanknoteDetected(BanknoteValidator validator) {
            System.out.println(name + ": Invalid bill detected.");
        }
    }

    private static class MySlotObserver implements BanknoteSlotRObserver {
        String name;

        public MySlotObserver(String newname) {
            name = newname;
        }

        @Override
        public void banknoteInserted(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote inserted.");
        }

        /**
         * An event announcing that a banknote has been returned to the user, dangling
         * from the slot.
         *
         * @param slot The device on which the event occurred.
         */
        @Override
        public void banknoteEjected(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote ejected.");
        }

        /**
         * An event announcing that a dangling banknote has been removed by the user.
         *
         * @param slot The device on which the event occurred.
         */
        @Override
        public void banknoteRemoved(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote removed from slot.");
        }
    }

    MySlotObserver observer = new MySlotObserver("Observer");
    MySlotObserver observer2 = new MySlotObserver("Observer2");
    MySlotObserver output1 = new MySlotObserver("Output1");
    MySlotObserver output2 = new MySlotObserver("Output2");
    NoteValidObserver noteobs1 = new NoteValidObserver("Validator1");
    NoteValidObserver noteobs2 = new NoteValidObserver("Validator2");
    NoteStorageObserver storeobs1 = new NoteStorageObserver("Storage1");
    NoteStorageObserver storeobs2 = new NoteStorageObserver("Storage2");
    NoteDispenserObserver dispobs1 = new NoteDispenserObserver("Dispenser1");
    NoteDispenserObserver dispobs2 = new NoteDispenserObserver("Dispenser2");
    DoItYourselfStationAR station;
    DoItYourselfStationAR station2;
    Banknote twentybill, onebill, tenbill;
    boolean flag;
    int[] denominations;

    @Before
    public void setUp() throws Exception {
        DoItYourselfStationAR.resetConfigurationToDefaults();
        station = new DoItYourselfStationAR();
        station.plugIn();
        station.turnOn();
        denominations = new int[]{5, 10, 20, 50, 100};

        DoItYourselfStationAR.configureBanknoteDenominations(denominations); //Note that the static call to configure banknote denominations
        //is done BEFORE constructing a station
        DoItYourselfStationAR.configureBanknoteStorageUnitCapacity(1); //Only one bill can fit in station2's storage unit

        station2 = new DoItYourselfStationAR();
        station2.plugIn();
        station2.turnOn();

        twentybill = new Banknote(Currency.getInstance("CAD"), 20);
        onebill = new Banknote(Currency.getInstance("CAD"), 1);
        tenbill = new Banknote(Currency.getInstance("CAD"), 10);
        station.banknoteInput.attach(observer);
        station2.banknoteInput.attach(observer2);
        station.banknoteValidator.attach(noteobs1);
        station2.banknoteValidator.attach(noteobs2);
        station.banknoteStorage.attach(storeobs1);
        station2.banknoteStorage.attach(storeobs2);
        station.banknoteOutput.attach(output1);
        station2.banknoteOutput.attach(output2);
        station.banknoteDispensers.get(1).attach(dispobs1);
        for (int denomination : denominations) {
            station2.banknoteDispensers.get(denomination).attach(dispobs2);
        }
    }

    @After
    public void tearDown() throws Exception {
        station.banknoteStorage.unload();
        station2.banknoteStorage.unload();
    }

    @Test
    public void ReceiveNullNote() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECEIVE NULL NOTE\n");
        flag = false;
        try {
            station.banknoteInput.receive(null);
        } catch (NullPointerSimulationException ex) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DisactiveBanknoteInput() throws DisabledException, TooMuchCashException {
        //Tests that the receive() throws the correct exception when the input's deactivated
        System.out.println("\nDISACTIVE BANKNOTE INPUT\n");
        station.banknoteInput.disactivate();
        flag = false;
        try {
            station.banknoteInput.receive(onebill); //The bill should be a valid twenty dollar canadian bill, but receive will fail.
        } catch (NoPowerException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DisabledBanknoteInput() throws TooMuchCashException {
        System.out.println("\nDISABLED BANKNOTE INPUT\n");
        station.banknoteInput.disable();
        flag = false;
        try {
            station.banknoteInput.receive(onebill);
        } catch (DisabledException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DenominationNotAccepted() throws DisabledException, TooMuchCashException {
        System.out.println("\nDENOMINATION NOT ACCEPTED\n");
        station.banknoteInput.receive(twentybill);
        assertTrue(station.banknoteInput.hasDanglingBanknote());
        station.banknoteInput.removeDanglingBanknote();
    }

    @Test
    public void NormalReceive() throws DisabledException, TooMuchCashException {
        System.out.println("\nNORMAL RECEIVE\n");

        station.banknoteInput.receive(onebill);
        assertFalse(station.banknoteInput.hasDanglingBanknote());
    }

    @Test
    public void ReconfigureDenominations() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECONFIGURE DENOMINATIONS\n");

        //By default, the bankslot in the diy station will only receive one dollar bills. of course, canadian one dollar bills haven't been
        //minted since 1989, so we should try using station2 which has been configured to accept more commonly circulated bills.
        station2.banknoteInput.receive(onebill);
        assertTrue(station2.banknoteInput.hasDanglingBanknote());
        station2.banknoteInput.removeDanglingBanknote();
        station2.banknoteInput.receive(twentybill);
        assertFalse(station.banknoteInput.hasDanglingBanknote());
    }

    @Test
    public void DoubleReceive() throws DisabledException, TooMuchCashException {
        System.out.println("\nDOUBLE RECEIVE\n");
        flag = false;
        station.banknoteInput.receive(twentybill); //We know from the previous test that station will reject this bill and leave it dangling
        try {
            station.banknoteInput.receive(onebill);
        } catch (TooMuchCashException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void TwoValidReceives() throws DisabledException, TooMuchCashException {
        System.out.println("\nTWO VALID RECEIVES\n");
        flag = true;
        station.banknoteInput.receive(onebill);
        station.banknoteInput.receive(onebill);
        flag = station.banknoteInput.hasDanglingBanknote();
        assertFalse(flag);
    }

    @Test
    public void OverloadStorage() throws DisabledException, TooMuchCashException {
        System.out.println("\nOVERLOAD STORAGE\n");

        flag = false;
        station2.banknoteInput.receive(twentybill);
        station2.banknoteDispensers.get((int) twentybill.getValue()).receive(twentybill);
        station2.banknoteInput.receive(tenbill);
        flag = station2.banknoteInput.hasDanglingBanknote();
        assertTrue(flag);
    }

    @Test
    public void ReceiveAndDispense() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECEIVE AND DISPENSE\n");

        station2.banknoteInput.receive(twentybill);
        List<Banknote> notelist = station2.banknoteStorage.unload();
        assertTrue(notelist.size() == 1);
        station2.banknoteOutput.emit(notelist.get(0));
        assertTrue(station2.banknoteOutput.hasDanglingBanknote());
        station2.banknoteOutput.removeDanglingBanknote();
        assertFalse(station2.banknoteOutput.hasDanglingBanknote());
        assertTrue(station2.banknoteStorage.getBanknoteCount() == 0);
    }

    @Test
    public void LongValue() {
        System.out.println("\nLONG VALUE\n");
        double temp = 1.25;
        long dollarval = (long) Math.floor(temp);
        temp -= dollarval;
        temp *= 100;
        long centval = (long) Math.floor(temp);
        System.out.println(Long.toString(dollarval) + " dollars and " + Long.toString(centval) + " cents");
    }

    @Test
    public void DispenserEmit() throws TooMuchCashException, DisabledException, OutOfCashException {
        System.out.println("\nDISPENSER EMIT\n");
        station.banknoteDispensers.get(1).load(onebill);
        assertTrue(station.banknoteStorage.getBanknoteCount() == 0); //The storage and the dispensers are completely separate
        station.banknoteDispensers.get(1).emit();
        assertTrue(station.banknoteOutput.hasDanglingBanknote());
        station.banknoteOutput.removeDanglingBanknote();
    }
}
=======
package com.diy.software.test.iteration2;

import ca.powerutility.NoPowerException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.OutOfCashException;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BankCoinHardwareTest {

    private static class NoteDispenserObserver implements BanknoteDispenserObserver {
        String name;

        public NoteDispenserObserver(String newname) {
            name = newname;
        }

        @Override
        public void moneyFull(IBanknoteDispenser dispenser) {
            System.out.println(name + ": Dispenser full.");
        }

        @Override
        public void banknotesEmpty(IBanknoteDispenser dispenser) {
            System.out.println(name + ": Dispenser empty.");
        }

        @Override
        public void billAdded(IBanknoteDispenser dispenser, Banknote banknote) {
            System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill added");
        }

        @Override
        public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
            System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill removed.");
        }

        @Override
        public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
            System.out.println(name + ": banknotes loaded.");
        }

        @Override
        public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
            System.out.println(name + ": banknotes unloaded");
        }
    }

    private static class NoteStorageObserver implements BanknoteStorageUnitObserver {
        String name;

        public NoteStorageObserver(String newname) {
            name = newname;
        }

        @Override
        public void banknotesFull(BanknoteStorageUnit unit) {
            System.out.println(name + ": Banknote storage unit full.");
        }

        @Override
        public void banknoteAdded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknote added to storage. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }

        @Override
        public void banknotesLoaded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknotes loaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }

        @Override
        public void banknotesUnloaded(BanknoteStorageUnit unit) {
            System.out.println(name + ": banknotes unloaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
        }
    }

    private static class NoteValidObserver implements BanknoteValidatorObserver {
        String name;

        public NoteValidObserver(String newname) {
            name = newname;
        }

        @Override
        public void validBanknoteDetected(BanknoteValidator validator, Currency currency, long value) {
            System.out.println(name + ": Valid " + currency.getDisplayName() + " bill detected. Value == " + Long.toString(value));
        }

        @Override
        public void invalidBanknoteDetected(BanknoteValidator validator) {
            System.out.println(name + ": Invalid bill detected.");
        }
    }

    private static class MySlotObserver implements BanknoteSlotRObserver {
        String name;

        public MySlotObserver(String newname) {
            name = newname;
        }

        @Override
        public void banknoteInserted(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote inserted.");
        }

        /**
         * An event announcing that a banknote has been returned to the user, dangling
         * from the slot.
         *
         * @param slot The device on which the event occurred.
         */
        @Override
        public void banknoteEjected(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote ejected.");
        }

        /**
         * An event announcing that a dangling banknote has been removed by the user.
         *
         * @param slot The device on which the event occurred.
         */
        @Override
        public void banknoteRemoved(BanknoteSlotR slot) {
            System.out.println(name + ": Banknote removed from slot.");
        }
    }

    MySlotObserver observer = new MySlotObserver("Observer");
    MySlotObserver observer2 = new MySlotObserver("Observer2");
    MySlotObserver output1 = new MySlotObserver("Output1");
    MySlotObserver output2 = new MySlotObserver("Output2");
    NoteValidObserver noteobs1 = new NoteValidObserver("Validator1");
    NoteValidObserver noteobs2 = new NoteValidObserver("Validator2");
    NoteStorageObserver storeobs1 = new NoteStorageObserver("Storage1");
    NoteStorageObserver storeobs2 = new NoteStorageObserver("Storage2");
    NoteDispenserObserver dispobs1 = new NoteDispenserObserver("Dispenser1");
    NoteDispenserObserver dispobs2 = new NoteDispenserObserver("Dispenser2");
    DoItYourselfStationAR station;
    DoItYourselfStationAR station2;
    Banknote twentybill, onebill, tenbill;
    boolean flag;
    int[] denominations;

    @Before
    public void setUp() throws Exception {
        DoItYourselfStationAR.resetConfigurationToDefaults();
        station = new DoItYourselfStationAR();
        station.plugIn();
        station.turnOn();
        denominations = new int[]{5, 10, 20, 50, 100};

        DoItYourselfStationAR.configureBanknoteDenominations(denominations); //Note that the static call to configure banknote denominations
        //is done BEFORE constructing a station
        DoItYourselfStationAR.configureBanknoteStorageUnitCapacity(1); //Only one bill can fit in station2's storage unit

        station2 = new DoItYourselfStationAR();
        station2.plugIn();
        station2.turnOn();

        twentybill = new Banknote(Currency.getInstance("CAD"), 20);
        onebill = new Banknote(Currency.getInstance("CAD"), 1);
        tenbill = new Banknote(Currency.getInstance("CAD"), 10);
        station.banknoteInput.attach(observer);
        station2.banknoteInput.attach(observer2);
        station.banknoteValidator.attach(noteobs1);
        station2.banknoteValidator.attach(noteobs2);
        station.banknoteStorage.attach(storeobs1);
        station2.banknoteStorage.attach(storeobs2);
        station.banknoteOutput.attach(output1);
        station2.banknoteOutput.attach(output2);
        station.banknoteDispensers.get(1).attach(dispobs1);
        for (int denomination : denominations) {
            station2.banknoteDispensers.get(denomination).attach(dispobs2);
        }
    }

    @After
    public void tearDown() throws Exception {
        station.banknoteStorage.unload();
        station2.banknoteStorage.unload();
    }

    @Test
    public void ReceiveNullNote() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECEIVE NULL NOTE\n");
        flag = false;
        try {
            station.banknoteInput.receive(null);
        } catch (NullPointerSimulationException ex) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DisactiveBanknoteInput() throws DisabledException, TooMuchCashException {
        //Tests that the receive() throws the correct exception when the input's deactivated
        System.out.println("\nDISACTIVE BANKNOTE INPUT\n");
        station.banknoteInput.disactivate();
        flag = false;
        try {
            station.banknoteInput.receive(onebill); //The bill should be a valid twenty dollar canadian bill, but receive will fail.
        } catch (NoPowerException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DisabledBanknoteInput() throws TooMuchCashException {
        System.out.println("\nDISABLED BANKNOTE INPUT\n");
        station.banknoteInput.disable();
        flag = false;
        try {
            station.banknoteInput.receive(onebill);
        } catch (DisabledException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void DenominationNotAccepted() throws DisabledException, TooMuchCashException {
        System.out.println("\nDENOMINATION NOT ACCEPTED\n");
        station.banknoteInput.receive(twentybill);
        assertTrue(station.banknoteInput.hasDanglingBanknote());
        station.banknoteInput.removeDanglingBanknote();
    }

    @Test
    public void NormalReceive() throws DisabledException, TooMuchCashException {
        System.out.println("\nNORMAL RECEIVE\n");

        station.banknoteInput.receive(onebill);
        assertFalse(station.banknoteInput.hasDanglingBanknote());
    }

    @Test
    public void ReconfigureDenominations() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECONFIGURE DENOMINATIONS\n");

        //By default, the bankslot in the diy station will only receive one dollar bills. of course, canadian one dollar bills haven't been
        //minted since 1989, so we should try using station2 which has been configured to accept more commonly circulated bills.
        station2.banknoteInput.receive(onebill);
        assertTrue(station2.banknoteInput.hasDanglingBanknote());
        station2.banknoteInput.removeDanglingBanknote();
        station2.banknoteInput.receive(twentybill);
        assertFalse(station.banknoteInput.hasDanglingBanknote());
    }

    @Test
    public void DoubleReceive() throws DisabledException, TooMuchCashException {
        System.out.println("\nDOUBLE RECEIVE\n");
        flag = false;
        station.banknoteInput.receive(twentybill); //We know from the previous test that station will reject this bill and leave it dangling
        try {
            station.banknoteInput.receive(onebill);
        } catch (TooMuchCashException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void TwoValidReceives() throws DisabledException, TooMuchCashException {
        System.out.println("\nTWO VALID RECEIVES\n");
        flag = true;
        station.banknoteInput.receive(onebill);
        station.banknoteInput.receive(onebill);
        flag = station.banknoteInput.hasDanglingBanknote();
        assertFalse(flag);
    }

    @Test
    public void OverloadStorage() throws DisabledException, TooMuchCashException {
        System.out.println("\nOVERLOAD STORAGE\n");

        flag = false;
        station2.banknoteInput.receive(twentybill);
        station2.banknoteDispensers.get((int) twentybill.getValue()).receive(twentybill);
        station2.banknoteInput.receive(tenbill);
        flag = station2.banknoteInput.hasDanglingBanknote();
        assertTrue(flag);
    }

    @Test
    public void ReceiveAndDispense() throws DisabledException, TooMuchCashException {
        System.out.println("\nRECEIVE AND DISPENSE\n");

        station2.banknoteInput.receive(twentybill);
        List<Banknote> notelist = station2.banknoteStorage.unload();
        assertTrue(notelist.size() == 1);
        station2.banknoteOutput.emit(notelist.get(0));
        assertTrue(station2.banknoteOutput.hasDanglingBanknote());
        station2.banknoteOutput.removeDanglingBanknote();
        assertFalse(station2.banknoteOutput.hasDanglingBanknote());
        assertTrue(station2.banknoteStorage.getBanknoteCount() == 0);
    }

    @Test
    public void LongValue() {
        System.out.println("\nLONG VALUE\n");
        double temp = 1.25;
        long dollarval = (long) Math.floor(temp);
        temp -= dollarval;
        temp *= 100;
        long centval = (long) Math.floor(temp);
        System.out.println(Long.toString(dollarval) + " dollars and " + Long.toString(centval) + " cents");
    }

    @Test
    public void DispenserEmit() throws TooMuchCashException, DisabledException, OutOfCashException {
        System.out.println("\nDISPENSER EMIT\n");
        station.banknoteDispensers.get(1).load(onebill);
        assertTrue(station.banknoteStorage.getBanknoteCount() == 0); //The storage and the dispensers are completely separate
        station.banknoteDispensers.get(1).emit();
        assertTrue(station.banknoteOutput.hasDanglingBanknote());
        station.banknoteOutput.removeDanglingBanknote();
    }
}
>>>>>>> main
