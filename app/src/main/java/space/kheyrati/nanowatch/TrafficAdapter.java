package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.hamsaa.persiandatepicker.util.PersianDateParser;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class TrafficAdapter extends RecyclerView.Adapter<TrafficAdapter.ViewHolder> {

    ArrayList<UserLogResponseItem> dataset;

    public void setDataset(ArrayList<UserLogResponseItem> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_traffic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == -1) return;
        UserLogResponseItem item = dataset.get(position);
        if(item.getType().equalsIgnoreCase("enter")){
            holder.ivStatus.setImageResource(R.drawable.ic_sign_in_alt);
        } else if(item.getType().equalsIgnoreCase("exit")){
            holder.ivStatus.setImageResource(R.drawable.ic_sign_out_alt);
        }
        PersianCalendar cal = new PersianCalendar(item.getDate());
        PersianDateImpl date = new PersianDateImpl();
        date.setDate(item.getDate());
        holder.tvDate.setText(date.getPersianYear() + "/" + date.getPersianMonth() + "/" + date.getPersianDay());
        holder.tvTime.setText(cal.getPersianShortDateTime().substring(cal.getPersianShortDateTime().indexOf(" ")));
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDate;
        private final TextView tvTime;
        private final AppCompatImageView ivStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
