package space.kheyrati.nanowatch.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import space.kheyrati.nanowatch.model.AttendeesRequestModel;
import space.kheyrati.nanowatch.model.AttendeesResponseModel;
import space.kheyrati.nanowatch.model.AttendeesWithLogResponseModel;
import space.kheyrati.nanowatch.model.CompanyLocationResponseModel;
import space.kheyrati.nanowatch.model.CompanyResponseModel;
import space.kheyrati.nanowatch.model.EnterExitRequestModel;
import space.kheyrati.nanowatch.model.EnterResponseModel;
import space.kheyrati.nanowatch.model.FcmRequestModel;
import space.kheyrati.nanowatch.model.FcmResponseModel;
import space.kheyrati.nanowatch.model.NewsRequestModel;
import space.kheyrati.nanowatch.model.NewsResponseModel;
import space.kheyrati.nanowatch.model.RequestDeleteResponseModel;
import space.kheyrati.nanowatch.model.RequestRequestModel;
import space.kheyrati.nanowatch.model.RequestResponseModel;
import space.kheyrati.nanowatch.model.SigninRequestModel;
import space.kheyrati.nanowatch.model.SigninResponseModel;
import space.kheyrati.nanowatch.model.StateResponseModel;
import space.kheyrati.nanowatch.model.TokenModel;
import space.kheyrati.nanowatch.model.UserLogResponseModel;
import space.kheyrati.nanowatch.model.VerifyRequestModel;
import space.kheyrati.nanowatch.model.VersionResponseModel;

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
    Call<List<UserLogResponseModel>> getMyLogs(@Header("Authorization") String authHeader);

    @POST("user/updateFCM")
    Call<FcmResponseModel> sendFcmToken(@Header("Authorization") String authHeader,
                                        @Body FcmRequestModel fcmRequestModel);

    @GET("request/company/{companyId}")
    Call<List<RequestResponseModel>> getRequests(@Header("Authorization") String authHeader,
                                                 @Path("companyId") String companyId);

    @POST("request")
    Call<ResponseBody> submitRequest(@Header("Authorization") String authHeader,
                                     @Body RequestRequestModel fcmRequestModel);

    @PUT("request/{requestId}")
    Call<ResponseBody> submitRequestPut(@Header("Authorization") String authHeader,
                                        @Path("requestId") String requestId,
                                        @Body RequestRequestModel request);

    @GET("version")
    Call<List<VersionResponseModel>> getVersion();

    @DELETE("request/{requestId}")
    Call<RequestDeleteResponseModel> deleteRequest(@Header("Authorization") String authHeader,
                                                   @Path("requestId") String requestId);

    @POST("EnterExit/date")
    Call<List<AttendeesResponseModel>> getAttendees(@Header("Authorization") String authHeader,
                                                    @Body AttendeesRequestModel requestModel);

    @POST("EnterExit/all-date")
    Call<List<AttendeesWithLogResponseModel>> getAttendeesWithLog(@Header("Authorization") String authHeader,
                                                                  @Body AttendeesRequestModel requestModel);

    @GET("news/company/{companyId}")
    Call<List<NewsResponseModel>> getNews(@Header("Authorization") String authHeader,
                                          @Path("companyId") String companyId);

    @POST("news")
    Call<ResponseBody> sendNews(@Header("Authorization") String authHeader,
                                @Body NewsRequestModel requestModel);

    @GET("EnterExit/last/{companyId}")
    Call<StateResponseModel> getState(@Header("Authorization") String authHeader,
                                      @Path("companyId") String companyId);
}
