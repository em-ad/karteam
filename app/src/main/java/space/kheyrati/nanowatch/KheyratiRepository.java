package space.kheyrati.nanowatch;

import android.util.Log;

import java.util.List;

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

    public void getLogs(String headerToken, String myUserId, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .getMyLogs(headerToken, myUserId)
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
                        Log.e("TAG", "onResponse: " + response.isSuccessful() );
                    }

                    @Override
                    public void onFailure(Call<FcmResponseModel> call, Throwable t) {

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
                .getMyRequests(headerToken)
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
}
