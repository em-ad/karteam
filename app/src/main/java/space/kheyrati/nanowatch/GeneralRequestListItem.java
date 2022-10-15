package space.kheyrati.nanowatch;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GeneralRequestListItem implements Serializable {

   public enum RequestType {
      VACATION,
      TRAFFIC,
      MISSION
   }

   public enum RequestStatus{
      PENDING,
      SUCCESS,
      FAIL
   }

   private @NonNull String id = "";
   private RequestType requestType;
   private RequestStatus requestStatus;
   private String title;
   private String date;
   private String time;

   public RequestType getRequestType() {
      return requestType;
   }

   public void setRequestType(RequestType requestType) {
      this.requestType = requestType;
   }

   public RequestStatus getRequestStatus() {
      return requestStatus;
   }

   public void setRequestStatus(RequestStatus requestStatus) {
      this.requestStatus = requestStatus;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public @NonNull String getId() {
      return id;
   }

   @SuppressWarnings("ConstantConditions")
   public void setId(@NonNull String id) {
      if(id == null) return;
      this.id = id;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      GeneralRequestListItem that = (GeneralRequestListItem) o;

      if (!id.equals(that.id)) return false;
      if (requestType != that.requestType) return false;
      if (requestStatus != that.requestStatus) return false;
      if (title != null ? !title.equals(that.title) : that.title != null) return false;
      if (date != null ? !date.equals(that.date) : that.date != null) return false;
      return time != null ? time.equals(that.time) : that.time == null;
   }
}
