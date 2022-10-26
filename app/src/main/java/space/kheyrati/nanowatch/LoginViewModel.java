package space.kheyrati.nanowatch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<LoginState> stateLiveData = new MutableLiveData<>(LoginState.LOGIN);
    private final KheyratiRepository kheyratiRepository;
    private String otp;
    private String phoneNumber;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.kheyratiRepository = new KheyratiRepository();
    }

    public LiveData<LoginState> getStateLiveData() {
        return stateLiveData;
    }

    enum LoginState {
        LOGIN,
        OTP,
        DONE
    }

    public String getOtp() {
        return otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void login(String phone, ApiCallback apiCallback) {
        this.phoneNumber = phone;
        kheyratiRepository.login(phone, new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                apiCallback.apiFailed(o);
            }

            @Override
            public void apiSucceeded(Object o) {
                if (o instanceof SigninResponseModel) {
                    SigninResponseModel responseModel = ((SigninResponseModel) o);
                    otp = responseModel.getOtp();
                    stateLiveData.postValue(LoginState.OTP);
                    apiCallback.apiSucceeded(o);
                }
            }
        });
    }

    public void verify(String otp, ApiCallback apiCallback) {
        if (this.phoneNumber == null || this.phoneNumber.isEmpty())
            return;
        kheyratiRepository.verify(phoneNumber, otp, new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                apiCallback.apiFailed(o);
            }

            @Override
            public void apiSucceeded(Object o) {
                if (o instanceof TokenModel) {
                    TokenModel responseModel = ((TokenModel) o);
                    MSharedPreferences.getInstance().saveToken(getApplication(), responseModel.getToken());
                    stateLiveData.postValue(LoginState.DONE);
                    apiCallback.apiSucceeded(o);
                } else apiCallback.apiFailed(o);
            }
        });
    }

}
