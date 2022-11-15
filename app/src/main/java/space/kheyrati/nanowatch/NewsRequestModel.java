package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsRequestModel implements Serializable {

   @SerializedName("text")
   private String text;
   @SerializedName("company")
   private String company;

   public NewsRequestModel(String text, String company) {
      this.text = text;
      this.company = company;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getCompany() {
      return company;
   }

   public void setCompany(String company) {
      this.company = company;
   }
}
