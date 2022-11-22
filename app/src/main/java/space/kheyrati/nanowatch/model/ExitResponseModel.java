package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExitResponseModel implements Serializable {

   @SerializedName("exit")
   private TrafficResponseModel exit;

   public TrafficResponseModel getExit() {
      return exit;
   }

   public void setExit(TrafficResponseModel exit) {
      this.exit = exit;
   }
}