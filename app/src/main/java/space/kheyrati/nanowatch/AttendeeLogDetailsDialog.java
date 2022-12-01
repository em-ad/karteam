package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.model.AttendeesWithLogResponseModel;
import space.kheyrati.nanowatch.model.UserLogResponseModel;

public class AttendeeLogDetailsDialog extends Dialog {

    private AttendeesWithLogResponseModel model;
    private TextView tvName;
    private TextView tvDate;
    private RecyclerView recycler;
    private TrafficAdapter adapter;

    public AttendeeLogDetailsDialog(@NonNull Context context, AttendeesWithLogResponseModel model) {
        super(context);
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attendees_log);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        recycler = findViewById(R.id.recycler);
        adapter = new TrafficAdapter();
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        tvName.setText(model.getFirstName() + " " + model.getLastname());
        tvDate.setText(new PersianCalendar(System.currentTimeMillis()).getPersianShortDate());
        adapter.setDataset(new ArrayList<>(UserLogResponseModel.fromAttendeesLog(model.getLogs())));
        if (model.getLogs().size() == 0) {
            findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tvEmpty).setVisibility(View.GONE);
        }
    }
}
