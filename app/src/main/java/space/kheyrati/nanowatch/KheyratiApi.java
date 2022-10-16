package space.kheyrati.nanowatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KheyratiApi {

    @POST("user/verify")
    Call<VerifyResponseModel> verify(@Body VerifyRequestModel requestModel);

    @POST("user/signIn")
    Call<SigninResponseModel> signin(@Body SigninRequestModel requestModel);

    @POST("user/enter")
    Call<EnterResponseModel> enter(@Header("Authorization") String authHeader);

    @POST("user/exit")
    Call<ExitResponseModel> exit(@Header("Authorization") String authHeader);

    @GET("company/logs/{userId}")
    Call<List<UserLogResponseItem>> getMyLogs(@Header("Authorization") String authHeader,
                                              @Path("userId") String customerId);

    @POST("user/updateFCM")
    Call<FcmResponseModel> sendFcmToken(@Header("Authorization") String authHeader,
                                @Body FcmRequestModel fcmRequestModel );

}
