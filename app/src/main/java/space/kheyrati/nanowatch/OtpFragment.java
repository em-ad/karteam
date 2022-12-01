package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

public class OtpFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView tvEnter;
    private TextView resend;
    private TextInputEditText loginEt;
    private ProgressBar progress;

    public OtpFragment() {
    }

    private CountDownTimer timer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long l) {
            resend.setText("ارسال مجدد کد تایید" + " " + ((int) l / 1000));
        }

        @Override
        public void onFinish() {
            resend.setText("ارسال مجدد کد تایید");
        }
    };

    public static OtpFragment newInstance() {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEnter = view.findViewById(R.id.tvEnter);
        progress = view.findViewById(R.id.progress);
        resend = view.findViewById(R.id.resend);
        timer.start();
        resend.setOnClickListener(view12 -> {
            if (!resend.getText().toString().trim().equals("ارسال مجدد کد تایید")) {
                MAlerter.show(getActivity(), "صبر کنید", "برای درخواست مجدد کد تا پایان زمان صبر کنید");
                return;
            }
            progress.setVisibility(View.VISIBLE);
            loginViewModel.login(loginViewModel.getPhoneNumber(), new ApiCallback() {
                @Override
                public void apiFailed(Object o) {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void apiSucceeded(Object o) {
                    progress.setVisibility(View.GONE);
                    timer.start();
                }
            });
        });
        loginEt = view.findViewById(R.id.edittext);
        loginEt.setHint("کد تاییدی که پیامک شده رو وارد کن");
        loginViewModel = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(LoginViewModel.class);
        if (loginViewModel.getOtp() != null && !loginViewModel.getOtp().isEmpty()) {
            loginEt.setText(loginViewModel.getOtp().equals("0") ? "" : loginViewModel.getOtp());
        }
        tvEnter.setOnClickListener(view1 -> {
            if (loginEt.getText().toString().trim().length() <= 2) {
                MAlerter.show(getActivity(), "خطا", "کد تایید را به درستی وارد کنید");
                return;
            }
            progress.setVisibility(View.VISIBLE);
            loginViewModel.verify(loginEt.getText().toString().trim(), new ApiCallback() {
                @Override
                public void apiFailed(Object o) {
                    progress.setVisibility(View.GONE);
                    MAlerter.show(getActivity(), "خطا", "در تایید کد ورود خطایی رخ داد");
                }

                @Override
                public void apiSucceeded(Object o) {
                    progress.setVisibility(View.GONE);
                }
            });
        });
    }
}