package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendeesResponseModel implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("enter")
    private long enter;
    @SerializedName("exit")
    private long exit;
    @SerializedName("lastState")
    private String lastState;

    public String getLastState() {
        return lastState;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }

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

    public long getEnter() {
        return enter;
    }

    public void setEnter(long enter) {
        this.enter = enter;
    }

    public long getExit() {
        return exit;
    }

    public void setExit(long exit) {
        this.exit = exit;
    }
}
