package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

    public class CompanyLocationResponseModel implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("lat")
    private float lat;
    @SerializedName("long")
    private float lon;
    @SerializedName("radius")
    private long radius;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }
}
