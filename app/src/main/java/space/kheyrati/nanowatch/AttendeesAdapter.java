package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

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
        holder.tvStatus.setText(item.getEnter() == 0 ? "غایب" : item.getExit() == 0 ? "حاضر" : "خارج شده");
        holder.tvStatus.setTextColor(item.getExit() == 0 ? item.getEnter() > 0 ? holder.itemView.getContext().getColor(R.color.green_sharp) : holder.itemView.getContext().getColor(R.color.red) : holder.itemView.getContext().getColor(R.color.red));
        if(item.getExit() == 0){
            holder.tvExit.setText("خارج نشده");
        } else holder.tvExit.setText("خروج " + new PersianCalendar(item.getExit()).getPersianShortDateTime().substring(new PersianCalendar(item.getExit()).getPersianShortDateTime().indexOf(" ") + 1));
        if(item.getEnter() == 0){
          holder.tvEnter.setText("وارد نشده");
        } else holder.tvEnter.setText("ورود " + new PersianCalendar(item.getEnter()).getPersianShortDateTime().substring(new PersianCalendar(item.getEnter()).getPersianShortDateTime().indexOf(" ") + 1));
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
