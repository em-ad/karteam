package space.kheyrati.nanowatch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import space.kheyrati.nanowatch.TokenModel;

public class VerifyResponseModel implements Serializable {
    @SerializedName("token")
    private TokenModel tokenModel;

    public TokenModel getTokenModel() {
        return tokenModel;
    }

    public void setTokenModel(TokenModel tokenModel) {
        this.tokenModel = tokenModel;
    }
}
