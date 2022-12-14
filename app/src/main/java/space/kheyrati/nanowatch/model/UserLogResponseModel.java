package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserLogResponseModel implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("company")
    private CompanyModel company;
    @SerializedName("location")
    private CompanyLocationResponseModel location;
    @SerializedName("user")
    private UserResponseModel user;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private long date;

    public static ArrayList<UserLogResponseModel> fromAttendeesLog(List<AttendeesLog> logs) {
        ArrayList<UserLogResponseModel> res = new ArrayList<>();

        for (int i = 0; i < logs.size(); i++) {
            UserLogResponseModel item = new UserLogResponseModel();
            AttendeesLog log = logs.get(i);
            item.setType(log.getType());
            item.setId(log.getId());
            item.setDate(log.getDate());
            res.add(item);
        }

        return res;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public CompanyLocationResponseModel getLocation() {
        return location;
    }

    public void setLocation(CompanyLocationResponseModel location) {
        this.location = location;
    }

    public UserResponseModel getUser() {
        return user;
    }

    public void setUser(UserResponseModel user) {
        this.user = user;
    }
}
