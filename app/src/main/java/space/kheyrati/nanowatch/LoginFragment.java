package space.kheyrati.nanowatch;

import android.os.Bundle;
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

import space.kheyrati.nanowatch.model.SigninResponseModel;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView tvEnter;
    private TextInputEditText loginEt;
    private ProgressBar progress;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(LoginViewModel.class);
        tvEnter = view.findViewById(R.id.tvEnter);
        progress = view.findViewById(R.id.progress);
        loginEt = view.findViewById(R.id.edittext);
        loginEt.setHint("شماره موبایلت رو وارد کن");
        tvEnter.setOnClickListener(view1 -> {
            if (loginEt.getText().toString().trim().length() < 10) {
                MAlerter.show(getActivity(), "خطا در ورود", "شماره موبایل را به صورت صحیح وارد کنید");
                return;
            }
            progress.setVisibility(View.VISIBLE);
            loginViewModel.login(loginEt.getText().toString().trim(), new ApiCallback() {
                @Override
                public void apiFailed(Object o) {
                    progress.setVisibility(View.GONE);
                    if ((o instanceof SigninResponseModel && ((SigninResponseModel) o).getStatusCode().equals("404")) ||
                            (o instanceof Throwable && ((Throwable) o).getMessage().equals("Not Found"))) {
                        MAlerter.show(getActivity(), "کاربر پیدا نشد", "برای ثبت نام با ادمین تماس بگیرید");
                    } else {
                        MAlerter.show(getActivity(), "خطا در لاگین", "خطایی در ورود به اپ رخ داد");
                    }
                }

                @Override
                public void apiSucceeded(Object o) {
                    progress.setVisibility(View.GONE);
                }
            });
        });
    }
}