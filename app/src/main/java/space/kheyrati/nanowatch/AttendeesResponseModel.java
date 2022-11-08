package space.kheyrati.nanowatch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendeesResponseModel implements Serializable {

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("enter")
    private long enter;
    @SerializedName("exit")
    private long exit;

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
