package space.kheyrati.nanowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getStateLiveData().observe(this, loginState -> {
            switch (loginState){
                case LOGIN:
                    getSupportFragmentManager().beginTransaction().replace(R.id.root,
                            LoginFragment.newInstance()).commit();
                    break;
                case OTP:
                    getSupportFragmentManager().beginTransaction().add(R.id.root,
                            OtpFragment.newInstance()).commit();
                    break;
                case DONE:
                    startActivity(new Intent(LoginActivity.this, CompanyChooserActivity.class));
                    finish();
                    break;
            }
        });
    }
}