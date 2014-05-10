package com.hwaipy.apple.iso.backup.old;

/**
 *
 * @author Hwaipy
 */
public class MBDBParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public MBDBParseException() {
    }

    public MBDBParseException(String message) {
        super(message);
    }

    public MBDBParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MBDBParseException(Throwable cause) {
        super(cause);
    }

    public MBDBParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
