package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDeleteResponseModel implements Serializable {

    @SerializedName("acknowledged")
    private boolean acknowledged;
    @SerializedName("deletedCount")
    private int deletedCount;

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
}
