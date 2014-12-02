package bo.net.tigo.exception;

import java.util.Collections;
import java.util.List;

/**
 * Created by aralco on 11/10/14.
 */
public class ErrorMessage {
    private List<String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(List<String> errors) {
        this.errors = errors;
    }

    public ErrorMessage(String error) {
        this(Collections.singletonList(error));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
