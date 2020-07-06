package com.example.myapplication.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder> implements OnNotiItemClickListener{

    private ArrayList<NotiItem> items;
    private OnNotiItemClickListener listener;

    public NotiAdapter(ArrayList<NotiItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotiItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NotiItem item){
        items.add(item);
    }

    public void setItems(ArrayList<NotiItem> items){
        this.items = items;
    }

    public NotiItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, NotiItem item){
        items.set(position, item);
    }

    public void setOnItemLongClickListener(OnNotiItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemLongClick(NotiAdapter.ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onItemLongClick(holder, view, position);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, duration, dday;
        public ViewHolder(@NonNull View itemView, final OnNotiItemClickListener listener) {
            super(itemView);

            image = itemView.findViewById(R.id.noti_image);
            title = itemView.findViewById(R.id.noti_title);
            duration = itemView.findViewById(R.id.noti_duration);
            dday = itemView.findViewById(R.id.noti_dday);

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(listener!=null){
                    listener.onItemLongClick(ViewHolder.this, v, position);
                }
                return true;
            });

        }

        public void setItem(NotiItem item){
            image.setImageResource(item.getImageId());
            title.setText(item.getTitle());
            duration.setText(item.getDuration());
            dday.setText(item.getDday());
        }
    }
}
