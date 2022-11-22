package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyRequestModel implements Serializable {

    @SerializedName("phoneNumber")
    private String number;
    @SerializedName("otp")
    private String otp;

    public VerifyRequestModel(String number, String otp) {
        this.number = number;
        this.otp = otp;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
