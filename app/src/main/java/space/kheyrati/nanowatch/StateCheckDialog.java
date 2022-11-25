package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.AttendanceViewModel;
import space.kheyrati.nanowatch.model.StateResponseModel;

public class StateCheckDialog extends Dialog {

    private ProgressBar progress;
    private AppCompatActivity activity;
    private AttendanceViewModel viewModel;
    private TextView tvMessage;
    private KheyratiRepository repository;
    private LottieAnimationView lav;

    public StateCheckDialog(@NonNull AppCompatActivity context) {
        super(context);
        this.activity = context;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_api_failure);
        progress = findViewById(R.id.progress);
        tvMessage = findViewById(R.id.tvMessage);
        lav = findViewById(R.id.lav);
        repository = new KheyratiRepository();
        findViewById(R.id.tvDownload).setOnClickListener(view -> checkStatus());
        viewModel = new ViewModelProvider(activity).get(AttendanceViewModel.class);
        checkStatus();
    }

    private void checkStatus() {
        progress.setVisibility(View.VISIBLE);
        lav.setAnimation(R.raw.status_loading);
        tvMessage.setText(getContext().getString(R.string.status_loading));
        repository.getLastState(MSharedPreferences.getInstance().getTokenHeader(getContext()), MyApplication.company.getCompany().getId(), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                progress.setVisibility(View.GONE);
                lav.setAnimation(R.raw.error);
                tvMessage.setText(getContext().getString(R.string.status_error));
            }

            @Override
            public void apiSucceeded(Object o) {
                StateResponseModel responseModel = ((StateResponseModel) o);
                if (responseModel.getType().equalsIgnoreCase("enter")) {
                    viewModel.enterTime = responseModel.getDate();
                    viewModel.isIn.postValue(true);
                } else {
                    viewModel.isIn.postValue(false);
                }
                dismiss();
            }
        });
    }
}
