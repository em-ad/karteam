package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class RequestFragment extends Fragment {

    private FloatingActionButton fabDone;
    private TextInputLayout etDate;
    private TextInputEditText etDateEt;
    private TextInputLayout etDays;
    private TextInputEditText etDaysEt;
    private TextInputLayout etType;
    private TextInputEditText etTypeEt;
    private TextInputLayout etDescription;
    private TextInputEditText etDescriptionEt;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
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
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupEditTexts();
    }

    private void setupEditTexts() {
        etDate.setHint("تاریخ شروع مرخصی");
        etDays.setHint("مدت مرخصی (روز)");
        etType.setHint("نوع مرخصی");
        etDescription.setHint("توضیحات");

        etDaysEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        etDaysEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String rawNum = editable.toString()
                        .replace("ر", "")
                        .replace("و", "")
                        .replace("ز", "")
                        .replace(" ", "");
                    if(!editable.toString().endsWith(" روز")) {
                    String newString = rawNum + " روز";
                    etDaysEt.setText(newString);
                    etDaysEt.setSelection(newString.length() - 4);
                }
            }
        });

        etDate.findViewById(R.id.edittext).setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) return false;
            if(getContext() == null) return false;
            new PersianDatePickerDialog(getContext())
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            ((TextInputEditText) etDate.findViewById(R.id.edittext))
                                    .setText(persianPickerDate.getPersianLongDate());
                        }

                        @Override
                        public void onDismissed() {}
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(getContext(), R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .show();
            view.performClick();
            return true;
        });
    }

    private void findViews(View view) {
        view.findViewById(R.id.arrow).setOnClickListener(v ->
                getParentFragmentManager().popBackStackImmediate());
        fabDone = view.findViewById(R.id.fabDone);
        etDate = view.findViewById(R.id.etDate);
        etDateEt = etDate.findViewById(R.id.edittext);
        etDays = view.findViewById(R.id.etDays);
        etDaysEt = etDays.findViewById(R.id.edittext);
        etType = view.findViewById(R.id.etType);
        etTypeEt = etType.findViewById(R.id.edittext);
        etDescription = view.findViewById(R.id.etDescription);
        etDescriptionEt = etDescription.findViewById(R.id.edittext);
    }
}