package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.RequestRequestModel;
import space.kheyrati.nanowatch.model.RequestResponseModel;

public class RequestFragment extends Fragment {

    private FloatingActionButton fabDone;
    private TextView etStartDate;
    private TextView etEndDate;
    private TextView etVacationType;
    private TextView etStartTime;
    private TextView etEndTime;
    private EditText etDescription;
    private TextView etType;
    private TextView title;
    private AppCompatImageView delete;
    private KheyratiRepository repository;
    private RefreshCallback callback;
    private RequestResponseModel item = null;
    private boolean isEditMode = false;

    private String type;

    private final RequestRequestModel requestModel = new RequestRequestModel();

    public RequestFragment(RefreshCallback callback, String type) {
        this.callback = callback;
        this.type = type;
    }

    public RequestFragment(RefreshCallback callback, RequestResponseModel item) {
        this.callback = callback;
        this.item = item;
    }

    private void handleRequestItem(RequestResponseModel item) {
        handleType(item.getType());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        long start = 0;
        long end = 0;
        try {
            Date startDate = format.parse(item.getStart());
            Date endDate = format.parse(item.getEnd());
            start = startDate.getTime();
            end = endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        etStartDate.setText(new PersianCalendar(start).getPersianLongDate());
        etEndDate.setText(new PersianCalendar(end).getPersianLongDate());
        etDescription.setText(item.getDescription());

        requestModel.setId(item.getId());
        requestModel.setEnd(end);
        requestModel.setStart(start);
        requestModel.setStatus(item.getStatus());
        requestModel.setDescription(item.getDescription());
        requestModel.setType(item.getType());
        requestModel.setCompany(item.getCompany() != null ? item.getCompany().getId() : "");
        requestModel.setUser(item.getUser().getId());

        if (item.getId() != null && !item.getId().isEmpty() && getContext() != null) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(view -> new AreYouShortDialog(getContext(), "آیا از حذف درخواست خود اطمینان دارید؟", null, null, new AreYouShortCallback() {
                @Override
                public void accept() {
                    repository.deleteRequest(MSharedPreferences.getInstance().getTokenHeader(getContext()), item.getId(), new ApiCallback() {
                        @Override
                        public void apiFailed(Object o) {
                            MAlerter.show(getActivity(), "خطا", "در حذف درخواست خطایی رخ داد");
                        }

                        @Override
                        public void apiSucceeded(Object o) {
                            callback.refresh();
                            getParentFragmentManager().popBackStackImmediate();
                        }
                    });
                }

                @Override
                public void reject() {

                }

                @Override
                public void dismiss() {

                }
            }).show());
        }
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
        requestModel.setCompany(MyApplication.company.getCompany().getId());
        requestModel.setUser(MSharedPreferences.getInstance().getUserIdFromToken(getContext()));
        repository = new KheyratiRepository();
        if (item != null) {
            isEditMode = true;
            title.setText("ویرایش درخواست");
            handleRequestItem(item);
        } else {
            title.setText("ثبت درخواست");
        }
    }

    private void setupEditTexts() {

        etVacationType.setOnClickListener(view -> {
            VacationTypeDialog dialog = new VacationTypeDialog(getContext(), RequestFragment.this::handleVacationType);
            dialog.show();
        });

        etStartDate.setOnClickListener(view -> {
            if (getContext() == null) return;
            new PersianDatePickerDialog(getContext())
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            etStartDate.setText(persianPickerDate.getPersianLongDate());
                            PersianCalendar cal = new PersianCalendar();
                            cal.setPersianDate(persianPickerDate.getPersianYear(), persianPickerDate.getPersianMonth(), persianPickerDate.getPersianDay());
                            cal.set(PersianCalendar.HOUR_OF_DAY, 0);
                            cal.set(PersianCalendar.MINUTE, 0);
                            cal.set(PersianCalendar.SECOND, 0);
                            cal.set(PersianCalendar.MILLISECOND, 0);
                            requestModel.setStart(cal.getTimeInMillis());
                            if (etEndDate.getVisibility() == View.GONE) {
                                requestModel.setEnd(requestModel.getStart());
                            }
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(getContext(), R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .setMinYear(1400)
                    .show();
        });

        etEndDate.setOnClickListener(view -> {
            if (getContext() == null) return;
            new PersianDatePickerDialog(getContext())
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            etEndDate.setText(persianPickerDate.getPersianLongDate());
                            PersianCalendar cal = new PersianCalendar();
                            cal.setPersianDate(persianPickerDate.getPersianYear(), persianPickerDate.getPersianMonth(), persianPickerDate.getPersianDay());
                            cal.set(PersianCalendar.HOUR_OF_DAY, 0);
                            cal.set(PersianCalendar.MINUTE, 0);
                            cal.set(PersianCalendar.SECOND, 0);
                            cal.set(PersianCalendar.MILLISECOND, 0);
                            requestModel.setEnd(cal.getTimeInMillis());
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(getContext(), R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .setMinYear(1400)
                    .show();
        });

        etStartTime.setOnClickListener(view -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("انتخاب زمان")
                    .setPositiveButtonText("تایید")
                    .setNegativeButtonText("انصراف")
                    .build();
            picker.addOnPositiveButtonClickListener(view1 -> {
                String selectedTime = String.format("%02d", picker.getHour()) + ":" + String.format("%02d", picker.getMinute());
                etStartTime.setText(selectedTime);
                requestModel.setStartTime(picker.getHour() * 3600 * 1000 + picker.getMinute() * 60 * 1000);
            });
            picker.show(getChildFragmentManager(), "picker");
        });

        etEndTime.setOnClickListener(view -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("انتخاب زمان")
                    .setPositiveButtonText("تایید")
                    .setNegativeButtonText("انصراف")
                    .build();
            picker.addOnPositiveButtonClickListener(view1 -> {
                String selectedTime = String.format("%02d", picker.getHour()) + ":" + String.format("%02d", picker.getMinute());
                etEndTime.setText(selectedTime);
                requestModel.setEndTime(picker.getHour() * 3600 * 1000 + picker.getMinute() * 60 * 1000);
            });
            picker.show(getChildFragmentManager(), "picker");
        });

        etType.setOnClickListener(view -> {
            RequestTypeDialog dialog = new RequestTypeDialog(getContext(), this::handleType);
            dialog.show();
        });

        fabDone.setOnClickListener(view -> {
            if (requestModel.isValid()) {
                submitRequest();
            } else {
                MAlerter.show(getActivity(), "خطا", "همه موارد ضروری را وارد کنید");
            }
        });

        if (type != null) {
            switch (type) {
                case "mission":
                    handleType("Mission");
                    break;
                case "vacation":
                    handleType("Vacation");
                    break;
                case "traffic":
                    handleType("Enter");
                    break;
            }
        }
    }

    private void handleVacationType(String s) {
        requestModel.setVacationType(s);
        etVacationType.setText(s);
    }

    private void handleType(String type) {
        requestModel.setType(type);
        etEndDate.setVisibility(View.VISIBLE);
        etEndTime.setVisibility(View.VISIBLE);
        etStartDate.setHint("تاریخ شروع را انتخاب کنید");
        etVacationType.setVisibility(View.GONE);
        switch (type) {
            case "Enter":
                etEndDate.setVisibility(View.GONE);
                etEndTime.setVisibility(View.GONE);
                etStartDate.setHint("تاریخ ورود را انتخاب کنید");
                etType.setText("درخواست ورود");
                break;
            case "Exit":
                etEndDate.setVisibility(View.GONE);
                etEndTime.setVisibility(View.GONE);
                etStartDate.setHint("تاریخ خروج را انتخاب کنید");
                etType.setText("درخواست خروج");
                break;
            case "Mission":
                etType.setText("درخواست ماموریت");
                break;
            case "Vacation":
                etType.setText("درخواست مرخصی");
                etVacationType.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void submitRequest() {
        requestModel.setDescription(requestModel.getVacationType() + " " + etDescription.getText().toString());
        requestModel.setStart(requestModel.getStart() + requestModel.getStartTime());
        requestModel.setEnd(requestModel.getEnd() + requestModel.getEndTime());
        if (etEndDate.getVisibility() == View.GONE) {
            requestModel.setEnd(requestModel.getStart());
        }
//        if(requestModel.getEnd() < requestModel.getStart()){
//            MAlerter.show(getActivity(), "خطا", "انتهای بازه باید پس از شروع آن باشد");
//            return;
//        }
        if (requestModel.getId() != null && !requestModel.getId().isEmpty()) {
            repository.submitRequestPut(MSharedPreferences.getInstance().getTokenHeader(getContext()), requestModel, new ApiCallback() {
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
        } else {
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
    }

    private void findViews(View view) {
        view.findViewById(R.id.arrow).setOnClickListener(v ->
                getParentFragmentManager().popBackStackImmediate());
        fabDone = view.findViewById(R.id.fabDone);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etVacationType = view.findViewById(R.id.etVacationType);
        etStartTime = view.findViewById(R.id.etStartTime);
        etEndTime = view.findViewById(R.id.etEndTime);
        delete = view.findViewById(R.id.delete);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);
        title = view.findViewById(R.id.title);
    }
}