package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SigninRequestModel implements Serializable {

    @SerializedName("phoneNumber")
    private String number;

    public SigninRequestModel(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
