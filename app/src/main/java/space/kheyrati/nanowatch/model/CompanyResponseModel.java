package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import space.kheyrati.nanowatch.CompanyModel;

public class CompanyResponseModel implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("company")
    private CompanyModel company;
    @SerializedName("role")
    private String role;

    private List<CompanyLocationResponseModel> location;

    public List<CompanyLocationResponseModel> getLocation() {
        return location;
    }

    public void setLocation(List<CompanyLocationResponseModel> location) {
        this.location = location;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
