package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FcmRequestModel implements Serializable {
   
   @SerializedName("fcmID")
   private String id;

   public FcmRequestModel(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
