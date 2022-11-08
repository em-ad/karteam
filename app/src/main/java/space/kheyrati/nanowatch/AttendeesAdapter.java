package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {

    List<AttendeesResponseModel> dataSet;

    public void setDataSet(List<AttendeesResponseModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeesAdapter.ViewHolder holder, int position) {
        if(position < 0) return;
        AttendeesResponseModel item = dataSet.get(position);
        holder.tvName.setText(item.getFirstName() + " " + item.getLastname());
        holder.tvStatus.setText(item.getExit() == 0 ? "حاضر" : "خارج شده");
        holder.tvStatus.setTextColor(item.getExit() == 0 ? holder.itemView.getContext().getColor(R.color.green_sharp) : holder.itemView.getContext().getColor(R.color.red));
        holder.tvEnter.setText(String.valueOf(item.getEnter()));
        holder.tvExit.setText(String.valueOf(item.getExit()));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvEnter;
        private final TextView tvExit;
        private final TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEnter = itemView.findViewById(R.id.tvEnter);
            tvExit = itemView.findViewById(R.id.tvExit);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
