package space.kheyrati.nanowatch;

import android.util.Log;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KheyratiRepository {

    public void login(String phone, ApiCallback apiCallback) {
        RetrofitClient.getInstance().getKheyratiApi()
                .signin(new SigninRequestModel(phone))
                .enqueue(new Callback<SigninResponseModel>() {
                    @Override
                    public void onResponse(Call<SigninResponseModel> call, Response<SigninResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.body() == null ? response.message() : response.body().getStatusCode()));
                    }

                    @Override
                    public void onFailure(Call<SigninResponseModel> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void verify(String phone, String otp, ApiCallback apiCallback) {
        RetrofitClient.getInstance().getKheyratiApi()
                .verify(new VerifyRequestModel(phone, otp))
                .enqueue(new Callback<TokenModel>() {
                    @Override
                    public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<TokenModel> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void enterOrExit(String headerToken, EnterExitRequestModel model, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .enterOrExit(headerToken, model)
                .enqueue(new Callback<EnterResponseModel>() {
                    @Override
                    public void onResponse(Call<EnterResponseModel> call, Response<EnterResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<EnterResponseModel> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void getLogs(String headerToken, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getMyLogs(headerToken)
                .enqueue(new Callback<List<UserLogResponseItem>>() {
                    @Override
                    public void onResponse(Call<List<UserLogResponseItem>> call, Response<List<UserLogResponseItem>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<UserLogResponseItem>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void sendToken(String headerToken, String fcmToken){
        RetrofitClient.getInstance().getKheyratiApi()
                .sendFcmToken(headerToken, new FcmRequestModel(fcmToken))
                .enqueue(new Callback<FcmResponseModel>() {
                    @Override
                    public void onResponse(Call<FcmResponseModel> call, Response<FcmResponseModel> response) {
                        Log.e("TAG", "FCM TOKEN SENT? " + response.isSuccessful() );
                    }

                    @Override
                    public void onFailure(Call<FcmResponseModel> call, Throwable t) {
                        Log.e("TAG", "FCM TOKEN NOT SENT! " + t.getMessage() );
                    }
                });
    }

    public void getMyCompanies(String headerToken, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getMyCompanies(headerToken)
                .enqueue(new Callback<List<CompanyResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<CompanyResponseModel>> call, Response<List<CompanyResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<CompanyResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void getCompanyLocation(String headerToken, String companyId, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getCompanyLocations(headerToken, companyId)
                .enqueue(new Callback<List<CompanyLocationResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<CompanyLocationResponseModel>> call, Response<List<CompanyLocationResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<CompanyLocationResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void getMyRequests(String headerToken, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getRequests(headerToken, MyApplication.company.getCompany().getId())
                .enqueue(new Callback<List<RequestResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<RequestResponseModel>> call, Response<List<RequestResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<RequestResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void submitRequest(String headerToken, RequestRequestModel requestModel, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .submitRequest(headerToken, requestModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void submitRequestPut(String headerToken, RequestRequestModel requestModel, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .submitRequestPut(headerToken,requestModel.getId(),requestModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void acceptRequest(String headerToken, RequestResponseModel model, ApiCallback apiCallback) {
        RequestRequestModel requestModel = model.accept();
        RetrofitClient.getInstance().getKheyratiApi()
                .submitRequestPut(headerToken, requestModel.getId(), requestModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void rejectRequest(String headerToken, RequestResponseModel model, ApiCallback apiCallback) {
        RequestRequestModel requestModel = model.reject();
        RetrofitClient.getInstance().getKheyratiApi()
                .submitRequestPut(headerToken, requestModel.getId(), requestModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void getVersion(ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getVersion()
                .enqueue(new Callback<List<VersionResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<VersionResponseModel>> call, Response<List<VersionResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body().get(0));
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<VersionResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void deleteRequest(String headerToken, String requestId, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .deleteRequest(headerToken, requestId)
                .enqueue(new Callback<RequestDeleteResponseModel>() {
                    @Override
                    public void onResponse(Call<RequestDeleteResponseModel> call, Response<RequestDeleteResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<RequestDeleteResponseModel> call, Throwable t) {

                    }
                });
    }

    public void getAttendees(String headerToken, AttendeesRequestModel model, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getAttendees(headerToken, model)
                .enqueue(new Callback<List<AttendeesResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<AttendeesResponseModel>> call, Response<List<AttendeesResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<AttendeesResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void getNews(String headerToken, String companyId, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getNews(headerToken, companyId)
                .enqueue(new Callback<List<NewsResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<NewsResponseModel>> call, Response<List<NewsResponseModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<List<NewsResponseModel>> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void sendNews(String tokenHeader, String message, String companyId, ApiCallback apiCallback) {
        NewsRequestModel model = new NewsRequestModel(message, companyId);
        RetrofitClient.getInstance().getKheyratiApi()
                .sendNews(tokenHeader, model)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        apiCallback.apiFailed(new Throwable(t.getMessage()));
                    }
                });
    }
}
