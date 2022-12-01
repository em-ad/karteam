package space.kheyrati.nanowatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IntroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new IntroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro_1, parent, false));
            case 1:
                return new IntroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro_2, parent, false));
            case 2:
                return new IntroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro_3, parent, false));
            case 3:
                return new IntroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro_4, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //avalin adapterie ke badaz N sal minevisam o bind nadare lol
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class IntroViewHolder extends RecyclerView.ViewHolder {
        public IntroViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
