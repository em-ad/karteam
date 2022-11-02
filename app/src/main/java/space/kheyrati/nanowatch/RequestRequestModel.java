package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestRequestModel implements Serializable {

    @SerializedName("user")
    private String user;
    @SerializedName("company")
    private String company;
    @SerializedName("start")
    private long start;
    @SerializedName("end")
    private long end;
    @SerializedName("status")
    private String status = "Pending";
    @SerializedName("type")
    private String type;
    @SerializedName("description")
    private String description;

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValid() {
        if(type == null) return false;
        if(company == null) return false;
        if(start == 0) return false;
        if(user == null) return false;
        return true;
    }
}
