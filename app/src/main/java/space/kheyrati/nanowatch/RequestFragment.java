package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class RequestFragment extends Fragment {

    private FloatingActionButton fabDone;
    private TextView etStartDate;
    private TextView etEndDate;
    private TextView etTime;
    private EditText etDescription;
    private TextView etType;
    private KheyratiRepository repository;
    private RefreshCallback callback;

    private final RequestRequestModel requestModel = new RequestRequestModel();

    public RequestFragment(RefreshCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupEditTexts();
        requestModel.setCompany(MyApplication.company.getId());
        requestModel.setUser(MSharedPreferences.getInstance().getUserIdFromToken(getContext()));
        repository = new KheyratiRepository();
    }

    private void setupEditTexts() {

        etStartDate.setOnClickListener(view -> {
            if (getContext() == null) return;
            new PersianDatePickerDialog(getContext())
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            etStartDate.setText(persianPickerDate.getPersianLongDate());
                            if(requestModel.getEnd() == 0){
                                requestModel.setEnd(persianPickerDate.getTimestamp());
                            }
                            requestModel.setStart(persianPickerDate.getTimestamp());
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(getContext(), R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .show();
        });

        etEndDate.setOnClickListener(view -> {
            if (getContext() == null) return;
            new PersianDatePickerDialog(getContext())
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            etEndDate.setText(persianPickerDate.getPersianLongDate());
                            requestModel.setEnd(persianPickerDate.getTimestamp());
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(getContext(), R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .show();
        });

        etTime.setOnClickListener(view -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("انتخاب زمان")
                    .setPositiveButtonText("تایید")
                    .setNegativeButtonText("انصراف")
                    .build();
            picker.addOnPositiveButtonClickListener(view1 -> {
                String selectedTime = String.format("%02d", picker.getHour()) + ":" + String.format("%02d", picker.getMinute());
                etTime.setText(selectedTime);
                requestModel.setTime(picker.getHour() * 3600 * 1000 + picker.getMinute() * 60 * 1000);
            });
            picker.show(getChildFragmentManager(), "picker");
        });

        etType.setOnClickListener(view -> {
            RequestTypeDialog dialog = new RequestTypeDialog(getContext(), type -> {
                requestModel.setType(type);
                switch (type) {
                    case "Enter":
                        etType.setText("درخواست ورود");
                        break;
                    case "Exit":
                        etType.setText("درخواست خروج");
                        break;
                    case "Mission":
                        etType.setText("درخواست ماموریت");
                        break;
                    case "Vacation":
                        etType.setText("درخواست مرخصی");
                        break;
                }
            });
            dialog.show();
        });

        fabDone.setOnClickListener(view -> {
            if(requestModel.isValid()){
                submitRequest();
            } else {
                MAlerter.show(getActivity(), "خطا", "همه موارد ضروری را وارد کنید");
            }
        });
    }

    private void submitRequest() {
        requestModel.setDescription(etDescription.getText().toString() + " ");
        requestModel.setStart(requestModel.getStart() + requestModel.getTime());
        requestModel.setTime(0);
        repository.submitRequest(MSharedPreferences.getInstance().getTokenHeader(getContext()), requestModel, new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(getActivity(), "خطا", "در ثبت درخواست خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                callback.refresh();
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void findViews(View view) {
        view.findViewById(R.id.arrow).setOnClickListener(v ->
                getParentFragmentManager().popBackStackImmediate());
        fabDone = view.findViewById(R.id.fabDone);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etTime = view.findViewById(R.id.etTime);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);
    }
}