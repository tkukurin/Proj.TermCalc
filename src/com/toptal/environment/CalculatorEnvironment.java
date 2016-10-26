package com.toptal.environment;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.Scanner;

public class CalculatorEnvironment {

    private String inputPointer;

    private final Scanner inputStreamScanner;
    private final OutputStreamWriter outputStreamWriter;

    public CalculatorEnvironment(String inputPointer,
                                 Scanner inputStreamScanner,
                                 OutputStreamWriter outputStreamWriter) {
        this.inputPointer = inputPointer;
        this.inputStreamScanner = inputStreamScanner;
        this.outputStreamWriter = outputStreamWriter;
    }

    public String requestUserInput() throws IOException {
        this.output(this.inputPointer);
        this.output(" ");
        return inputStreamScanner.nextLine();
    }

    public void output(String s) throws IOException {
        this.outputStreamWriter.write(s);
        this.outputStreamWriter.flush();
    }

    public void outputLine(String s) throws IOException {
        this.outputStreamWriter.write(s);
        this.outputStreamWriter.write(System.lineSeparator());
        this.outputStreamWriter.flush();
    }
}
