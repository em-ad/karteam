package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class TrafficFragment extends Fragment {

    private AppCompatImageView ivFinger;
    private MediaPlayer mPlayer;
    private TextView tvTime;
    private TextView tvEntered;
    private TextView tvLocationSearch;
    private AttendanceViewModel viewModel;
    private LottieAnimationView lav;
    private LottieAnimationView pbProgress;
    private FrameLayout flEdge;
    private volatile boolean isTimerRunning;
    private KheyratiRepository repository;

    private Handler attendanceHandler;
    private final Runnable attendanceRunnable = () -> {
        boolean isIn = viewModel.isIn.getValue() != null && viewModel.isIn.getValue();
        if (isIn) {
            viewModel.isIn.postValue(false);
        } else {
            PersianCalendar date = new PersianCalendar(System.currentTimeMillis());
            PreferencesManager.getInstance(getContext()).edit().putLong("last_enter", date.getTimeInMillis()).commit();
            vibratePhone();
            ivFinger.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            viewModel.isIn.postValue(true);
        }
    };

    private final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long untilEnd) {
            tvTime.setText(String.valueOf(untilEnd / 1000));
        }

        @Override
        public void onFinish() {
            isTimerRunning = false;
        }
    };

    public TrafficFragment() {
    }

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
        viewModel.isIn.observe(getViewLifecycleOwner(), this::attendanceStateChanged);
    }

    private void attendanceStateChanged(Boolean entered) {
        if (entered == null || repository == null || getContext() == null) return;
        if (entered) {
            if (!MSharedPreferences.getInstance().whatIsLastTrafficEvent(getContext()).equals("enter"))
                callEnter();
            else changeUiForEnter();
        } else {
            if (!MSharedPreferences.getInstance().whatIsLastTrafficEvent(getContext()).equals("exit"))
                callExit();
            else changeUiForExit();
        }
    }

    private void callExit() {
        repository.exit(MSharedPreferences.getInstance().getTokenHeader(requireContext()), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(getActivity(), "خطا", "خطایی در ثبت خروج پیش آمد");
            }

            @Override
            public void apiSucceeded(Object o) {
                if (getContext() != null)
                    MSharedPreferences.getInstance().saveLastTrafficEvent(getContext(), "exit");
                changeUiForExit();
            }
        });
    }

    private void callEnter() {
        repository.enter(MSharedPreferences.getInstance().getTokenHeader(requireContext()), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(getActivity(), "خطا", "خطایی در ثبت ورود پیش آمد");
            }

            @Override
            public void apiSucceeded(Object o) {
                if (getContext() != null)
                    MSharedPreferences.getInstance().saveLastTrafficEvent(getContext(), "enter");
                changeUiForEnter();
            }
        });
    }

    private void changeUiForExit() {
        lav.setVisibility(View.INVISIBLE);
        tvEntered.setVisibility(View.INVISIBLE);
        flEdge.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        PreferencesManager.getInstance(getContext()).edit().remove("last_enter").commit();
    }

    private void changeUiForEnter() {
        lav.setVisibility(View.VISIBLE);
        lav.playAnimation();
        tvEntered.setVisibility(View.VISIBLE);
        flEdge.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.GONE);
        PersianCalendar date = new PersianCalendar(PreferencesManager
                .getInstance(getContext()).getLong("last_enter", System.currentTimeMillis()));
        tvEntered.setText("شما در " + date.getPersianShortDateTime().replace(" ", " در ساعت ") + " وارد شدید" + "\n\n" + getString(R.string.hold_to_exit));
    }

    @Override
    public void onResume() {
        super.onResume();
        repository = new KheyratiRepository();
        startTimerFromScratch();
        if (viewModel != null)
            attendanceStateChanged(viewModel.isIn.getValue());
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
                    if (!MyApplication.locationValid()) {
                        MAlerter.show(getActivity(), "در حال جستجوی لوکیشن", "برای ثبت ورود و خروج باید در محدوده دانشگاه باشید");
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
        ivFinger = view.findViewById(R.id.ivFingerprint);
        pbProgress = view.findViewById(R.id.progress);
        tvTime = view.findViewById(R.id.tvTime);
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