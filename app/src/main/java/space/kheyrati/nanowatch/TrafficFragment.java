package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class TrafficFragment extends Fragment {

    private AppCompatImageView ivFinger;
    private MediaPlayer mPlayer;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvName;
    private TextView tvEntered;
    private TextView tvLocationSearch;
    private AttendanceViewModel viewModel;
    private LottieAnimationView lav;
    private LottieAnimationView pbProgress;
    private FrameLayout flEdge;
    private volatile boolean isTimerRunning;
    private AtomicBoolean apiCalling = new AtomicBoolean(false);
    private KheyratiRepository repository;
    private String lastApi = null;

    private Handler attendanceHandler;
    private final Runnable attendanceRunnable = () -> {
        boolean isIn = MyApplication.isIn;
        if (!isIn) {
            PersianCalendar date = new PersianCalendar(System.currentTimeMillis());
            PreferencesManager.getInstance(getContext()).edit().putLong("last_enter", date.getTimeInMillis()).commit();
            vibratePhone();
            ivFinger.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }
        MyApplication.isIn = !MyApplication.isIn;
        attendanceStateUpdated(MyApplication.isIn);
    };

    private final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long untilEnd) {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(String.valueOf(untilEnd / 1000));
        }

        @Override
        public void onFinish() {
            isTimerRunning = false;
            tvTime.setVisibility(View.GONE);
        }
    };

    public TrafficFragment() {}

    public static TrafficFragment newInstance() {
        TrafficFragment fragment = new TrafficFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void startTimerFromScratch() {
        if (isTimerRunning) return;
        countDownTimer.start();
        isTimerRunning = true;
    }

    @Override
    public void onPause() {
        countDownTimer.cancel();
        isTimerRunning = false;
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traffic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initComponents();
        setTouchListener();
        viewModel = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(AttendanceViewModel.class);
        tvTitle.setText("عبور و مرور در " + MyApplication.company.getCompany().getName());
    }

    private void attendanceStateUpdated(Boolean entered) {
        if (entered == null || repository == null || getContext() == null) return;
        if (entered) {
            if (!MSharedPreferences.getInstance().whatIsLastTrafficEvent(getContext()).equals("enter")) {
                callEnter();
            } else changeUiForEnter();
        } else {
            if (!MSharedPreferences.getInstance().whatIsLastTrafficEvent(getContext()).equals("exit")) {
                callExit();
            }else changeUiForExit();
        }
    }

    private void callExit() {
        if(apiCalling.get()) return;
        apiCalling.set(true);
        EnterExitRequestModel enterExitRequestModel = new EnterExitRequestModel(MyApplication.company.getCompany().getId(), MyApplication.company.getLocation().get(0).getId(), "Exit");
        repository.enterOrExit(MSharedPreferences.getInstance().getTokenHeader(requireContext()), enterExitRequestModel, new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                apiCalling.set(false);
                MAlerter.show(getActivity(), "خطا", "خطایی در ثبت خروج پیش آمد");
            }

            @Override
            public void apiSucceeded(Object o) {
                apiCalling.set(false);
                if (getContext() != null)
                    MSharedPreferences.getInstance().saveLastTrafficEvent(getContext(), "exit");
                changeUiForExit();
                MAlerter.show(getActivity(), "خارج شدید", "خروج شما با موفقیت ثبت شد");
            }
        });
    }

    private void callEnter() {
        if(apiCalling.get()) return;
        apiCalling.set(true);
        EnterExitRequestModel enterExitRequestModel = new EnterExitRequestModel(MyApplication.company.getCompany().getId(), MyApplication.company.getLocation().get(0).getId(), "Enter");
        repository.enterOrExit(MSharedPreferences.getInstance().getTokenHeader(requireContext()), enterExitRequestModel, new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(getActivity(), "خطا", "خطایی در ثبت ورود پیش آمد");
                apiCalling.set(false);
            }

            @Override
            public void apiSucceeded(Object o) {
                apiCalling.set(false);
                if (getContext() != null)
                    MSharedPreferences.getInstance().saveLastTrafficEvent(getContext(), "enter");
                changeUiForEnter();
                MAlerter.show(getActivity(), "وارد شدید", "ورود شما با موفقیت ثبت شد");
            }
        });
    }

    private void changeUiForExit() {
        lav.setVisibility(View.GONE);
        tvEntered.setVisibility(View.GONE);
        flEdge.setVisibility(View.GONE);
        tvTime.setVisibility(View.VISIBLE);
        PreferencesManager.getInstance(getContext()).edit().remove("last_enter").commit();
    }

    private void changeUiForEnter() {
        lav.setVisibility(View.VISIBLE);
        lav.playAnimation();
        tvEntered.setVisibility(View.VISIBLE);
        flEdge.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.GONE);
        PersianCalendar date = new PersianCalendar(viewModel.enterTime);
        tvEntered.setText("شما در " + date.getPersianShortDateTime().replace(" ", " در ساعت ") + " وارد شدید" + "\n\n" + getString(R.string.hold_to_exit));
    }

    @Override
    public void onResume() {
        super.onResume();
        repository = new KheyratiRepository();
        startTimerFromScratch();
        if(tvName != null){
            tvName.setText(MSharedPreferences.getInstance().getNameFromToken(getContext()));
        }
        if (viewModel != null){
            viewModel.isIn.observe(getViewLifecycleOwner(), aBoolean -> {
                if(aBoolean == null) return;
                if(aBoolean){
                    changeUiForEnter();
                } else changeUiForExit();
            });
        }
        handleMapAccessVisibility();
    }

    private void handleMapAccessVisibility() {
        if (MyApplication.locationValid()) {
            tvLocationSearch.setVisibility(View.GONE);
        } else {
            tvLocationSearch.setVisibility(View.VISIBLE);
        }
    }

    private void initComponents() {
        attendanceHandler = new Handler();
        mPlayer = MediaPlayer.create(getContext(), R.raw.bells);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        ivFinger.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        MAlerter.show(getActivity(), "جی پی اس خاموش است", "از روشن بودن جی پی اس خود اطمینان حاصل کنید");
                        return true;
                    }
                    if (!MyApplication.locationValid()) {
                        MAlerter.show(getActivity(), "موقعیت تایید نشد", "برای ثبت ورود و خروج باید در محدوده تعیین شده شرکت باشید");
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).findUserLocation();
                        }
                        tvLocationSearch.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        tvLocationSearch.setVisibility(View.GONE);
                    }
                    initAttendance();
                    break;
                case MotionEvent.ACTION_UP:
                    stopAttendance();
                    break;
            }
            return true;
        });
    }

    private void stopAttendance() {
        mPlayer.stop();
        pbProgress.setVisibility(View.GONE);
        attendanceHandler.removeCallbacks(attendanceRunnable);
        mPlayer = MediaPlayer.create(getContext(), R.raw.bells);
    }

    private void initAttendance() {
        mPlayer.start();
        pbProgress.setVisibility(View.VISIBLE);
        pbProgress.playAnimation();
        attendanceHandler.postDelayed(attendanceRunnable, MConstants.Longs.FINGER_PRESS_MILLIS);
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        ivFinger = view.findViewById(R.id.ivFingerprint);
        pbProgress = view.findViewById(R.id.progress);
        tvTime = view.findViewById(R.id.tvTime);
        tvName = view.findViewById(R.id.tvName);
        tvEntered = view.findViewById(R.id.tvEntered);
        lav = view.findViewById(R.id.lav_thumbUp);
        flEdge = view.findViewById(R.id.flEdge);
        tvLocationSearch = view.findViewById(R.id.tvLocationSearch);
        tvLocationSearch.setOnClickListener(view1 -> {
            try {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", MyApplication.lastLocation.getLatitude(), MyApplication.lastLocation.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            } catch (Exception e) {
                MAlerter.show(getActivity(), "خطا", "در باز کردن نقشه خطایی رخ داد. از نصب بودن نقشه گوگل روی گوشی مطمئن شوید");
            }
        });

        view.findViewById(R.id.tvCompanyLocation).setOnClickListener(view12 -> {
            try {
                String uri = MyApplication.getNearestCompanyLocationUri();
                if(uri == null){
                    MAlerter.show(getActivity(), "صبر کنید", "مکان مُجازی برای شرکت پیدا نشد");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            } catch (Exception e) {
                MAlerter.show(getActivity(), "خطا", "در باز کردن نقشه خطایی رخ داد. از نصب بودن نقشه گوگل روی گوشی مطمئن شوید");
            }
        });
    }

    private void vibratePhone() {
        if (getContext() == null) return;
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }
}