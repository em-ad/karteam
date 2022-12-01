package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AttendeesWithLogResponseModel implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("lastState")
    private String lastState;
    @SerializedName("logs")
    private List<AttendeesLog> logs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastState() {
        return lastState;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }

    public List<AttendeesLog> getLogs() {
        return logs;
    }

    public void setLogs(List<AttendeesLog> logs) {
        this.logs = logs;
    }
}
