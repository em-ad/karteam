package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtpFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView tvOtp;

    public OtpFragment() {}

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
        loginViewModel = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(LoginViewModel.class);
        tvOtp = view.findViewById(R.id.tvOtp);
        tvOtp.setOnClickListener(view1 -> loginViewModel.verify(""));
    }
}