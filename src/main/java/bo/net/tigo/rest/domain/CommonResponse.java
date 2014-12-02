package bo.net.tigo.rest.domain;

/**
 * Created by aralco on 11/5/14.
 */
public class CommonResponse {
    private String message;

    public CommonResponse(String message) {
        this.message = message;
    }

    public CommonResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
