package com.diy.software.print;

import ca.powerutility.NoPowerException;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.IReceiptPrinter;
import com.jimmyselectronics.AbstractDevice;
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;

public class ReceiptPrinterStub extends AbstractDevice<ReceiptPrinterListener> implements IReceiptPrinter {
    /**
     * Represents the maximum amount of ink that the printer can hold, measured in
     * printable-character units.
     */
    public static final int MAXIMUM_INK = 1 << 20;
    /**
     * Represents the maximum amount of paper that the printer can hold, measured in
     * lines.
     */
    public static final int MAXIMUM_PAPER = 1 << 10;
    private int charactersOfInkRemaining = 0;
    private int linesOfPaperRemaining = 0;
    private StringBuilder sb = new StringBuilder();
    private int charactersOnCurrentLine = 0;

    /**
     * Represents the maximum number of characters that can fit on one line of the
     * receipt. This is a simulation, so the font is assumed monospaced and of fixed
     * size.
     */
    public final static int CHARACTERS_PER_LINE = 60;

    /**
     * Creates a receipt printer.
     */
    public ReceiptPrinterStub() {}

    /**
     * Prints a single character to the receipt. Whitespace characters are ignored,
     * with the exception of ' ' (blank) and '\n', which signals to move to the
     * start of the next line. If the printer is out of ink, announces "outOfInk"
     * event; otherwise, if it has less than 10% of its maximum ink, announces
     * "lowInk" event. If the printer is out of paper, announces "outOfPaper" event;
     * otherwise, if it has less than 10% of its maximum paper, announces "lowPaper"
     * event. Requires power.
     *
     * @param c
     *            The character to print.
     * @throws EmptyException
     *             If there is no ink or no paper in the printer.
     * @throws OverloadException
     *             If the extra character would spill off the end of the line.
     */
    @Override
    public void print(char c) throws EmptyException, OverloadException {
        if(!isPoweredUp())
            throw new NoPowerException();

        if(c == '\n') {
            --linesOfPaperRemaining;
            charactersOnCurrentLine = 0;
        }
        else if(c != ' ' && Character.isWhitespace(c))
            return;
        else if(charactersOnCurrentLine == CHARACTERS_PER_LINE)
            throw new OverloadException("The line is too long. Add a newline");
        else if(linesOfPaperRemaining == 0)
            throw new EmptyException("There is no paper in the printer.");
        else
            charactersOnCurrentLine++;

        if(!Character.isWhitespace(c)) {
            if(charactersOfInkRemaining == 0)
                throw new EmptyException("There is no ink in the printer");

            charactersOfInkRemaining--;
        }

        sb.append(c);

        if(charactersOfInkRemaining == 0)
            notifyOutOfInk();
        else if(charactersOfInkRemaining <= MAXIMUM_INK * 0.1)
            notifyLowInk();

        if(linesOfPaperRemaining == 0)
            notifyOutOfPaper();
        else if(linesOfPaperRemaining <= MAXIMUM_PAPER * 0.1)
            notifyLowPaper();
    }

    @Override
    public void cutPaper() {

    }

    @Override
    public String removeReceipt() {
        return null;
    }

    @Override
    public void addInk(int quantity) throws OverloadException {

    }

    @Override
    public void addPaper(int units) throws OverloadException {

    }
    private void notifyOutOfInk() {
        for(com.jimmyselectronics.abagnale.ReceiptPrinterListener l : listeners())
            l.outOfInk(this);
    }

    private void notifyInkAdded() {
        for(com.jimmyselectronics.abagnale.ReceiptPrinterListener l : listeners())
            l.inkAdded(this);
    }

    private void notifyOutOfPaper() {
        for(com.jimmyselectronics.abagnale.ReceiptPrinterListener l : listeners())
            l.outOfPaper(this);
    }

    private void notifyPaperAdded() {
        for(com.jimmyselectronics.abagnale.ReceiptPrinterListener l : listeners())
            l.paperAdded(this);
    }

    private void notifyLowInk() {
        for(com.jimmyselectronics.abagnale.ReceiptPrinterListener l : listeners())
            l.lowInk(this);
    }

    private void notifyLowPaper() {
        for(ReceiptPrinterListener l : listeners())
            l.lowPaper(this);
    }
}
