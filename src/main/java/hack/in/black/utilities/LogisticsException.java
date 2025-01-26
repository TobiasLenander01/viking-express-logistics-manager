/*
 * This class represents a logistics exception that can be thrown by the LogisticsManager class.
 * It extends the Exception class. It provides constructors to create a new LogisticsException object.
 */

package hack.in.black.utilities;

public class LogisticsException extends Exception {
    // Default constructor
    public LogisticsException() {
        super();
    }

    // Constructor that accepts a message
    public LogisticsException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public LogisticsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public LogisticsException(Throwable cause) {
        super(cause);
    }
}