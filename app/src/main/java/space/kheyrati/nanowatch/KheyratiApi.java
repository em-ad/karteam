package space.kheyrati.nanowatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KheyratiApi {

    @POST("auth/verify")
    Call<TokenModel> verify(@Body VerifyRequestModel requestModel);

    @POST("auth/signIn")
    Call<SigninResponseModel> signin(@Body SigninRequestModel requestModel);

    @GET("company")
    Call<List<CompanyResponseModel>> getMyCompanies(@Header("Authorization") String authHeader);

    @GET("companyLocation/company/{companyId}")
    Call<List<CompanyLocationResponseModel>> getCompanyLocations(@Header("Authorization") String authHeader,
                                                           @Path("companyId") String companyId);

    @POST("EnterExit")
    Call<EnterResponseModel> enterOrExit(@Header("Authorization") String authHeader,
                                         @Body EnterExitRequestModel requestModel);

    @GET("company/logs/{userId}")
    Call<List<UserLogResponseItem>> getMyLogs(@Header("Authorization") String authHeader,
                                              @Path("userId") String customerId);

    @POST("user/updateFCM")
    Call<FcmResponseModel> sendFcmToken(@Header("Authorization") String authHeader,
                                @Body FcmRequestModel fcmRequestModel );

    @GET("request")
    Call<List<RequestResponseModel>> getMyRequests(@Header("Authorization") String authHeader);

}
