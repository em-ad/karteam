package space.kheyrati.nanowatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginState> stateLiveData = new MutableLiveData<>(LoginState.LOGIN);

    private String phoneNumber;

    public LiveData<LoginState> getStateLiveData() {
        return stateLiveData;
    }

    enum LoginState {
        LOGIN,
        OTP,
        DONE
    }

    public void login(String phone){
        stateLiveData.postValue(LoginState.OTP);
        this.phoneNumber = phone;
    }

    public void verify(String otp){
        if(this.phoneNumber == null || this.phoneNumber.isEmpty())
            return;
        stateLiveData.postValue(LoginState.DONE);
    }

}
