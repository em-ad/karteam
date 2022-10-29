package space.kheyrati.nanowatch;

import java.util.List;

import okhttp3.ResponseBody;
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

    @GET("companyUser")
    Call<List<CompanyResponseModel>> getMyCompanies(@Header("Authorization") String authHeader);

    @GET("companyLocation/company/{companyId}")
    Call<List<CompanyLocationResponseModel>> getCompanyLocations(@Header("Authorization") String authHeader,
                                                           @Path("companyId") String companyId);

    @POST("EnterExit")
    Call<EnterResponseModel> enterOrExit(@Header("Authorization") String authHeader,
                                         @Body EnterExitRequestModel requestModel);

    @GET("EnterExit")
    Call<List<UserLogResponseItem>> getMyLogs(@Header("Authorization") String authHeader);

    @POST("user/updateFCM")
    Call<FcmResponseModel> sendFcmToken(@Header("Authorization") String authHeader,
                                @Body FcmRequestModel fcmRequestModel );

    @GET("request")
    Call<List<RequestResponseModel>> getRequests(@Header("Authorization") String authHeader);

    @POST("request")
    Call<ResponseBody> submitRequest(@Header("Authorization") String authHeader,
                                    @Body RequestRequestModel fcmRequestModel);

}
