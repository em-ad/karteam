package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendeesLog implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("user")
    private AttendeesLogUser user;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private long date;

    public AttendeesLog() {
    }

    public AttendeesLog(String type, long date) {
        this.type = type;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttendeesLogUser getUser() {
        return user;
    }

    public void setUser(AttendeesLogUser user) {
        this.user = user;
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
}
