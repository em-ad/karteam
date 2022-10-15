package space.kheyrati.nanowatch;

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
}
