package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsRequestModel implements Serializable {

    @SerializedName("text")
    private String text;
    @SerializedName("company")
    private String company;
    @SerializedName("user")
    private String user;

    public NewsRequestModel(String text, String company) {
        this.text = text;
        this.company = company;
    }

    public NewsRequestModel(String text, String company, String user) {
        this.text = text;
        this.company = company;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
