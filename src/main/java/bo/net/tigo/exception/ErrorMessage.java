package bo.net.tigo.exception;

/**
 * Created by aralco on 11/10/14.
 */
public class ErrorMessage {
    private String error;
    private String error_description;

    public ErrorMessage() {
    }

    public ErrorMessage(String error, String error_description) {
        this.error = error;
        this.error_description = error_description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
