package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

public class EnterExitRequestModel {

    @SerializedName("company")
    private String company;
    @SerializedName("location")
    private String location;
    @SerializedName("type")
    private String type;

    public EnterExitRequestModel(String company, String location, String type) {
        this.company = company;
        this.location = location;
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
