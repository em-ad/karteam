package space.kheyrati.nanowatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface KheyratiApi {

    @POST("user/verify")
    Call<VerifyResponseModel> verify(@Body VerifyRequestModel requestModel);

    @POST("user/signIn")
    Call<SigninResponseModel> signin(@Body SigninRequestModel requestModel);

    @POST("user/enter")
    Call<EnterResponseModel> enter(@Header("Authorization") String authHeader);

    @POST("user/exit")
    Call<ExitResponseModel> exit(@Header("Authorization") String authHeader);

//    @GET("/license/myRequests")
//    Call<List<RequestItem>> getMyRequests(@Header("Authorization") String authHeader);
//
//    @GET("/license/requests")
//    Call<List<RequestResponseModel>> getRequests(@Header("Authorization") String authHeader);
//
//    @POST("/license/create")
//    Call<Void> createRequest(@Header("Authorization") String authHeader,
//                             @Body CreateRequestModel createRequestModel);
//
//    @POST("/license/changeRequestStatus")
//    Call<Void> changeRequestStatus(@Header("Authorization") String authHeader,
//                             @Body ChangeStatusModel model);
//
//    @GET("/notification/list")
//    Call<List<NotificationModel>> getNotifications(@Header("Authorization") String authHeader);
//
//    @POST("/notification/create")
//    Call<Void> createNotification(@Header("Authorization") String authHeader,
//                             @Body CreateNotificationModel model);
}
