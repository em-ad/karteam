package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendeesRequestModel implements Serializable {

   @SerializedName("date")
   private long date;
   @SerializedName("company")
   private String company;

   public long getDate() {
      return date;
   }

   public void setDate(long date) {
      this.date = date;
   }

   public String getCompany() {
      return company;
   }

   public void setCompany(String company) {
      this.company = company;
   }
}
