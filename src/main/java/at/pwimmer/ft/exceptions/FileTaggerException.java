package at.pwimmer.ft.exceptions;

import java.io.IOException;

public class FileTaggerException extends Throwable {

    public FileTaggerException(String message) {
        super(message);
    }

    public FileTaggerException(Throwable cause) {
        super(cause);
    }

    public FileTaggerException(String message, Throwable cause) {
        super(message, cause);
    }
}
