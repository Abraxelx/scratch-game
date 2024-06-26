package com.halilsahin.scratch;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class CustomConsoleHandler extends ConsoleHandler {

    private PrintStream ps;

    public CustomConsoleHandler(OutputStream os) {
        this.ps = new PrintStream(os);
    }

    @Override
    public void publish(LogRecord record) {
        ps.println(getFormatter().format(record));
    }
}
