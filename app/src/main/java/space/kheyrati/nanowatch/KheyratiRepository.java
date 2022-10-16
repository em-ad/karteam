package space.kheyrati.nanowatch;

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
                            apiCallback.apiFailed(new Throwable(response.message()));
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
                .enqueue(new Callback<VerifyResponseModel>() {
                    @Override
                    public void onResponse(Call<VerifyResponseModel> call, Response<VerifyResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<VerifyResponseModel> call, Throwable t) {
                        apiCallback.apiFailed(t);
                    }
                });
    }

    public void enter(String headerToken, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .enter(headerToken)
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

    public void exit(String headerToken, ApiCallback apiCallback){
        RetrofitClient.getInstance().getKheyratiApi()
                .exit(headerToken)
                .enqueue(new Callback<ExitResponseModel>() {
                    @Override
                    public void onResponse(Call<ExitResponseModel> call, Response<ExitResponseModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            apiCallback.apiSucceeded(response.body());
                        } else
                            apiCallback.apiFailed(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Call<ExitResponseModel> call, Throwable t) {
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

                    }

                    @Override
                    public void onFailure(Call<FcmResponseModel> call, Throwable t) {

                    }
                });
    }
}
