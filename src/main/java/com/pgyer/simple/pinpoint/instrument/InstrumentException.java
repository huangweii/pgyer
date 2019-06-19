package com.pgyer.simple.pinpoint.instrument;

public class InstrumentException extends Exception {

    public InstrumentException() {
    }

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentException(Throwable cause) {
        super(cause);
    }
}
