package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.model.RequestResponseModel;

class OthersRequestListAdapter extends ListAdapter<RequestResponseModel, OthersRequestListAdapter.ViewHolder> {

    private final RequestCallback callback;

    protected OthersRequestListAdapter(RequestCallback callback) {
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_others_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0) return;
        holder.bind(getItem(position));
    }

    @Override
    public void submitList(@Nullable List<RequestResponseModel> list) {
        super.submitList(removeNotPendings(list));
    }

    private List<RequestResponseModel> removeNotPendings(List<RequestResponseModel> list) {
        List<RequestResponseModel> res = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() != null && list.get(i).getStatus().equalsIgnoreCase("pending"))
                    res.add(list.get(i));
            }
        }
        Collections.reverse(res);
        return res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvDescription;
        private final AppCompatImageView ivAccept;
        private final AppCompatImageView ivReject;
        private final AppCompatImageView ivType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivAccept = itemView.findViewById(R.id.ivAccept);
            ivReject = itemView.findViewById(R.id.ivReject);
            ivType = itemView.findViewById(R.id.ivType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(RequestResponseModel item) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date lastUpdateDate = null;
            if(item.getLastUpdate() != null && !item.getLastUpdate().isEmpty()){
                try {
                    lastUpdateDate = format.parse(item.getLastUpdate());
                } catch (Exception ignored){}
            }
            Date startDate = new Date();
            Date endDate = new Date();
            PersianCalendar cal = null;
            long time = 0;
            try {
                startDate = format.parse(item.getStart());
                endDate = format.parse(item.getEnd());
                cal = new PersianCalendar(((long) (startDate.getTime())));
                if (endDate.getTime() != startDate.getTime()) {
                    time = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cal != null) {
                tvDate.setText("درخواست برای " + cal.getPersianShortDateTime().replace(" ", " | "));
            }
            if (!item.getType().equals("Enter") && !item.getType().equals("Exit")) {
                tvTime.setText("مدت: " + (time < 24 ? time + " ساعت" : (Math.ceil(((double) time) / 24) + " روز").replace(".0", "")));
                if (tvTime.getText().toString().equals("0 ساعت"))
                    tvTime.setText("به مدت کمتر از یک ساعت");
            } else {
                tvTime.setText(cal.getPersianShortDateTime().substring(cal.getPersianShortDateTime().indexOf(" ")));
            }
            switch (item.getType().toLowerCase()) {
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
                    tvTitle.setText("مرخصی");
                    ivType.setImageResource(R.drawable.ic_vacation_primary_dark);
                    break;

            }
            if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(item.getDescription());
            } else {
                tvDescription.setVisibility(View.GONE);
            }
            if (item.getUser() != null)
                tvTitle.setText(tvTitle.getText() + " از " + item.getUser().getFirstName() + " " + item.getUser().getLastName());
            ivAccept.setOnClickListener(view -> callback.onAccept(getItem(getAdapterPosition())));
            ivReject.setOnClickListener(view -> callback.onReject(getItem(getAdapterPosition())));
            if(lastUpdateDate != null){
                tvTitle.setText(tvTitle.getText() + " - زمان ثبت درخواست: " + new PersianCalendar(lastUpdateDate.getTime()).getPersianShortDateTime().replace(" ", " | "));
            }
        }
    }
}
