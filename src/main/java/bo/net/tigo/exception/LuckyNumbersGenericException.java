package bo.net.tigo.exception;

/**
 * Created by aralco on 11/10/14.
 */
public class LuckyNumbersGenericException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public LuckyNumbersGenericException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
