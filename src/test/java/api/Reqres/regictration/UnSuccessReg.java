package api.Reqres.regictration;

public class UnSuccessReg {
     private String error;

    public UnSuccessReg() {
    }

    public UnSuccessReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String errorMessage) {
        this.error = errorMessage;
    }
}
