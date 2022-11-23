package com.diy.software;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.software.controllers.ScaleController;
import com.diy.software.controllers.ScannerController;

public class DoItYourselfStationLogic {
    /**
     * The station on which the logic is installed.
     */
    public DoItYourselfStationAR station;
    /**
     * The controller that tracks the scanned products
     */
    public ScannerController scannerController;
    /**
     * The controller that tracks the electronic scale
     */
    public ScaleController scaleController;

    /**
     * Installs an instance of the logic on the indicated station.
     *
     * @param station
     *            The station on which to install the logic.
     * @param creditIssuer
     *            The credit issuer to by used for payment.
     * @return The newly installed instance.
     * @throws NullPointerException
     *             If any argument is null.
     */
    public static DoItYourselfStationLogic installOn(DoItYourselfStationAR station, CardIssuer creditIssuer) {
        return new DoItYourselfStationLogic(station, creditIssuer);
    }

    /**
     * Basic constructor.
     *
     * @param station
     *            The station on which to install the logic.
     * @param creditIssuer
     *            The credit issuer to by used for payment.
     * @return The newly installed instance.
     * @throws NullPointerException
     *             If any argument is null.
     */
    public DoItYourselfStationLogic(DoItYourselfStationAR station, CardIssuer creditIssuer) {
        this.station = station;

        scannerController = new ScannerController(this);
        station.scanner.register(scannerController);

        scaleController = new ScaleController(this);
        station.scale.register(scaleController);
    }
}