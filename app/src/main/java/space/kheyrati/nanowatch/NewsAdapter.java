package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsResponseModel> data;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0) return;
        NewsResponseModel item = data.get(position);
        holder.text.setText(item.getText());
        if (item.getUser() != null)
            holder.sender.setText("از " + item.getUser().getFirstName() + " " + item.getUser().getLastName());
        else
            holder.sender.setText("از طرف مدیر سیستم");
        if(item.getDate() != null && item.getDate() != 0){
            holder.date.setText(new PersianCalendar(item.getDate()).getPersianShortDate());
        } else {
            holder.date.setText("نامشخص");
        }
    }

    public void setData(List<NewsResponseModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView sender;
        private TextView text;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            sender = itemView.findViewById(R.id.sender);
            date = itemView.findViewById(R.id.date);
        }
    }
}
