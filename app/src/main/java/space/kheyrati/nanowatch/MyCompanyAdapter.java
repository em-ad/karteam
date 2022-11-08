package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCompanyAdapter extends RecyclerView.Adapter<MyCompanyAdapter.ViewHolder> {

    private List<CompanyResponseModel> dataSet;
    private final CompanyCallback callback;

    public MyCompanyAdapter(CompanyCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_company, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataSet(List<CompanyResponseModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0)
            return;
        holder.companyNameTextView.setText(dataSet.get(position).getCompany().getName() + " (" + getPersianRole(dataSet.get(position).getRole()) + ")");
        holder.itemView.setOnClickListener(view -> callback.companyClicked(dataSet.get(holder.getAdapterPosition())));
    }

    private String getPersianRole(String role) {
        switch (role.toLowerCase()){
            case "admin":
                return "مدیر";
            case "employee":
                return "کارمند";
            case "manager":
                return "مدیر داخلی";
        }
        return "نامشخص";
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView companyNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyNameTextView = itemView.findViewById(R.id.tvName);
        }
    }
}
