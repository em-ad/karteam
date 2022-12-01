package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EnterResponseModel implements Serializable {

    @SerializedName("enter")
    private TrafficResponseModel enter;

    public TrafficResponseModel getEnter() {
        return enter;
    }

    public void setEnter(TrafficResponseModel enter) {
        this.enter = enter;
    }
}
