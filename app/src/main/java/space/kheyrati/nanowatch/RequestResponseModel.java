package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestResponseModel implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("user")
    private String user;
    @SerializedName("company")
    private String company;
    @SerializedName("start")
    private String start;
    @SerializedName("end")
    private String end;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestResponseModel that = (RequestResponseModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (end != null ? !end.equals(that.end) : that.end != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

}
