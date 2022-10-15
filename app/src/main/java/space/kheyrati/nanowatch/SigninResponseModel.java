package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SigninResponseModel implements Serializable {

   @SerializedName("otp")
   private String otp;

   public String getOtp() {
      return otp;
   }

   public void setOtp(String otp) {
      this.otp = otp;
   }
}
