package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SigninResponseModel implements Serializable {

   @SerializedName("otp")
   private String otp;
   @SerializedName("statusCode")
   private String statusCode;

   public String getOtp() {
      return otp;
   }

   public void setOtp(String otp) {
      this.otp = otp;
   }

   public String getStatusCode() {
      return statusCode;
   }

   public void setStatusCode(String statusCode) {
      this.statusCode = statusCode;
   }
}
