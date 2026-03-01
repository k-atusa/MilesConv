package com.example.ku7_milesconv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    public List<String> items;
    private OnItemActionListener listener;

    // listener interface
    public interface OnItemActionListener {
        void onItemClick(int pos); // click item
        void onDeleteClick(int pos); // click delete
    }
    public ItemAdapter(OnItemActionListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draw_listitem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String item = items.get(position);
        holder.textViewItem.setText(item);

        // item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

        // copy click
        holder.imageButtonCopy.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems() {
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        ImageButton imageButtonCopy;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.text_item);
            imageButtonCopy = itemView.findViewById(R.id.delete_button);
        }
    }
}