package space.kheyrati.nanowatch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

class MyRequestListAdapter extends ListAdapter<RequestResponseModel, MyRequestListAdapter.ViewHolder> {

    RequestClickCallback callback;

    protected MyRequestListAdapter(RequestClickCallback callback) {
        super(new DiffUtil.ItemCallback<RequestResponseModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull RequestResponseModel oldItem, @NonNull RequestResponseModel newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull RequestResponseModel oldItem, @NonNull RequestResponseModel newItem) {
                return false;
            }
        });
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position < 0) return;
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvDate;
        private final TextView tvTime;
        private final AppCompatImageView ivStatus;
        private final AppCompatImageView ivType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            ivType = itemView.findViewById(R.id.ivType);
        }

        public void bind(RequestResponseModel item) {
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date startDate = new Date();
            Date endDate = new Date();
            PersianCalendar cal = null;
            long time = 0;
            long rawtime = 0;
            try{
                startDate = format.parse(item.getStart());
                endDate = format.parse(item.getEnd());
                cal = new PersianCalendar(((long) (startDate.getTime() + (3600 * 3.5 * 1000))));
                if(endDate.getTime() != startDate.getTime()) {
                    time = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if(cal != null) {
                tvDate.setText(cal.getPersianShortDate());
            }
            if(!item.getType().equals("Enter") && !item.getType().equals("Exit")) {
                tvTime.setText(time < 24 ? time + " ساعت" : (Math.ceil(((double) time) / 24) + " روز").replace(".0", ""));
                if(tvTime.getText().toString().equals("0 ساعت"))
                    tvTime.setText("کمتر از یک ساعت");
            } else {
                String rawTime = cal.getPersianShortDateTime().substring(cal.getPersianShortDateTime().indexOf(" "));
                tvTime.setText(reduceTwelveHours(rawTime));
            }

            switch (item.getStatus().toLowerCase()){
                case "pending":
                    ivStatus.setImageResource(R.drawable.ic_pending);
                    break;
                case "accept":
                    ivStatus.setImageResource(R.drawable.ic_success);
                    break;
                case "reject":
                    ivStatus.setImageResource(R.drawable.ic_failed);
                    break;
            }
            switch (item.getType().toLowerCase()){
                case "mission":
                    tvTitle.setText("ماموریت");
                    ivType.setImageResource(R.drawable.ic_mission_primary_dark);
                    break;
                case "enter":
                    tvTitle.setText("درخواست ورود");
                    ivType.setImageResource(R.drawable.ic_fingerprint_primary);

                    break;
                case "exit":
                    tvTitle.setText("درخواست خروج");
                    ivType.setImageResource(R.drawable.ic_fingerprint_primary);
                    break;
                case "vacation":
                    tvTitle.setText("درخواست مرخصی");
                    ivType.setImageResource(R.drawable.ic_vacation_primary_dark);
                    break;
            }
            itemView.setOnClickListener(view -> callback.itemClicked(item));
        }

        private String reduceTwelveHours(String rawTime) {
//            String hour = rawTime.substring(0, rawTime.indexOf(":")).trim();
//            String rest = rawTime.substring(rawTime.indexOf(":") + 1).trim();
//            Log.e("TAG", "reduceTwelveHours: " + hour );
//            int h = Integer.parseInt(hour);
//            h -= 12;
//            if(h < 0)
//                h += 24;
//            return String.valueOf(h) + ":" + rest;
            return rawTime;
        }
    }
}
