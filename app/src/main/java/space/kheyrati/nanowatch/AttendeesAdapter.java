package space.kheyrati.nanowatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.model.AttendanceState;
import space.kheyrati.nanowatch.model.AttendeeClickCallback;
import space.kheyrati.nanowatch.model.AttendeesLog;
import space.kheyrati.nanowatch.model.AttendeesWithLogResponseModel;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {

    private List<AttendeesWithLogResponseModel> dataSet;
    private AttendeeClickCallback callback;
    private int width = -1;
    private int mode = MODE_ALL;

    public static final int MODE_ALL = 0;
    public static final int MODE_PRESENT = 1;
    public static final int MODE_ABSENT = 2;

    private void setMode(int mode){
        this.mode = mode;
    }

    public AttendeesAdapter(AttendeeClickCallback callback) {
        this.callback = callback;
    }

    public void setDataSet(List<AttendeesWithLogResponseModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendee, parent, false));
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeesAdapter.ViewHolder holder, int position) {
        if (position < 0) return;
        AttendeesWithLogResponseModel item = dataSet.get(position);
        holder.llProgress.removeAllViews();
        if (width == -1)
            width = dpToPx(300);
        holder.tvName.setText(item.getFirstName() + " " + item.getLastname());
        if (item.getLastState().equalsIgnoreCase("enter")) {
            holder.tvStatus.setText("حاضر");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.green_darker));
            View view = new View(holder.itemView.getContext());
            view.setBackgroundColor(holder.itemView.getContext().getColor(R.color.green_sharp));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            holder.llProgress.addView(view);
            holder.rootCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.green_light));
        } else if (item.getLastState().equalsIgnoreCase("absent")) {
            holder.tvStatus.setText("غایب");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.red));
            View view = new View(holder.itemView.getContext());
            view.setBackgroundColor(holder.itemView.getContext().getColor(R.color.red));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            holder.llProgress.addView(view);
            holder.rootCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.grey_de));
        } else {
            holder.tvStatus.setText("خارج شده");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.grey_6));
            View view = new View(holder.itemView.getContext());
            view.setBackgroundColor(holder.itemView.getContext().getColor(R.color.grey_6));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            holder.llProgress.addView(view);
            holder.rootCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.grey_de));
        }
        List<AttendeesLog> logs = item.getLogs();
        List<AttendanceState> ranges = getRangesAndApplySeparators(holder.itemView.getContext(), logs, holder.meterView, holder.tvStatus);
        if (ranges.size() > 0) {
            holder.llProgress.removeAllViews();
        }
        for (int i = 0; i < ranges.size(); i++) {
            AttendanceState range = ranges.get(i);
            View view = new View(holder.itemView.getContext());
            view.setBackgroundColor(range.getColor());
            view.setLayoutParams(new ViewGroup.LayoutParams(width * range.getPercent() / 100, 30));
            holder.llProgress.addView(view);
        }

        holder.rootCardView.setOnClickListener(view -> callback.onClick(dataSet.get(holder.getAdapterPosition())));
    }

    private List<AttendanceState> getRangesAndApplySeparators(Context context, List<AttendeesLog> logs, View meterView, TextView name) {
        List<AttendanceState> state = new ArrayList<>();
        long lastTime = 0;
        long lastEnter = 0;
        long firstTime = Long.MAX_VALUE;
        for (int i = 0; i < logs.size(); i++) {
            if (logs.get(i).getType().equalsIgnoreCase("enter") && logs.get(i).getDate() > lastEnter)
                lastEnter = logs.get(i).getDate();
            if (logs.get(i).getDate() > lastTime) {
                lastTime = logs.get(i).getDate();
            }
            if (logs.get(i).getDate() < firstTime) {
                firstTime = logs.get(i).getDate();
            }
        }
        long totalRange;
        if (logs.size() == 1) {
            totalRange = System.currentTimeMillis() - firstTime;
        } else {
            totalRange = lastTime - firstTime;
        }
        String type = null;
        int lastChange = 0;
        for (int i = 0; i < logs.size(); i++) {
            if (type == null) {
                type = logs.get(i).getType();
            } else {
                if (!logs.get(i).getType().equalsIgnoreCase(type)) {
                    state.add(new AttendanceState(
                            type.equalsIgnoreCase("enter") ? context.getColor(R.color.green_sharp) : context.getColor(R.color.red),
                            (int) ((logs.get(i).getDate() - logs.get(lastChange).getDate()) * 100 / totalRange)));
                    lastChange = i;
                    type = logs.get(i).getType();
                }
            }
        }
        int total = 0;
        for (int i = 0; i < state.size(); i++) {
            total += state.get(i).getPercent();
        }
        if(total < 100 && state.size() > 0){
            AttendanceState state1 = state.get(state.size() - 1);
            state.remove(state1);
            AttendanceState state2 = new AttendanceState();
            state2.setColor(state1.getColor());
            state2.setPercent(100 - total + state1.getPercent());
            state.add(state2);
        }
        int dashes = (int) Math.ceil(totalRange / (float) (1000 * 3600));
        if (dashes > 24 || dashes < 0)
            dashes = 0;
        if (dashes > 0) {
            meterView.setVisibility(View.VISIBLE);
            Drawable drawable = context.getDrawable(R.drawable.timeline_background);
            ((GradientDrawable) drawable).setStroke(dpToPx(6), context.getColor(R.color.white), dpToPx(2), (width / ((float) dashes)));
            meterView.setBackground(drawable);
        } else {
            meterView.setVisibility(View.GONE);
        }
        if (lastEnter > 0 && lastTime == lastEnter) {
            name.setText(name.getText().toString() + " از " + new PersianCalendar(lastEnter).getPersianShortDateTime().substring(new PersianCalendar(lastEnter).getPersianShortDateTime().indexOf(" ")));
        }
        return state;
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public List<AttendeesWithLogResponseModel> getItems() {
        return dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvStatus;
        private final LinearLayout llProgress;
        private final View meterView;
        private final CardView rootCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            llProgress = itemView.findViewById(R.id.progressLinearLayout);
            meterView = itemView.findViewById(R.id.meterView);
            rootCardView = itemView.findViewById(R.id.rootCardView);
        }
    }
}
