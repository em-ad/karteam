package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

class RequestListAdapter extends ListAdapter<GeneralRequestListItem, RequestListAdapter.ViewHolder> {

    protected RequestListAdapter() {
        super(new DiffUtil.ItemCallback<GeneralRequestListItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull GeneralRequestListItem oldItem, @NonNull GeneralRequestListItem newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull GeneralRequestListItem oldItem, @NonNull GeneralRequestListItem newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false));
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

        public void bind(GeneralRequestListItem item) {
            tvTitle.setText(item.getTitle());
            tvDate.setText(item.getDate());
            tvTime.setText(item.getTime());
            switch (item.getRequestStatus()){
                case PENDING:
                    ivStatus.setImageResource(R.drawable.ic_pending);
                    break;
                case SUCCESS:
                    ivStatus.setImageResource(R.drawable.ic_success);
                    break;
                case FAIL:
                    ivStatus.setImageResource(R.drawable.ic_failed);
                    break;
            }
            switch (item.getRequestType()){
                case MISSION:
                    ivType.setImageResource(R.drawable.ic_mission_primary_dark);
                    break;
                case TRAFFIC:
                    ivType.setImageResource(R.drawable.ic_fingerprint_primary);
                    break;
                case VACATION:
                    ivType.setImageResource(R.drawable.ic_vacation_primary_dark);
                    break;
            }
        }
    }
}
