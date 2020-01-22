package com.eekrain.amikomparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    ArrayList<String> plat, date;
    ArrayList<Boolean> status;
    int[] status_images;
    Context context;

    public HistoryAdapter(Context ct, ArrayList<String> input_plat, ArrayList<String> input_date, ArrayList<Boolean> input_status, int[] input_img) {
        context = ct;
        plat = input_plat;
        date = input_date;
        status = input_status;
        status_images = input_img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtPlat.setText(plat.get(position));
        holder.txtTime.setText(date.get(position));
        Boolean status_ref = status.get(position);

        if (status_ref) {
            holder.imgStatus.setImageResource(status_images[0]);
        } else {
            holder.imgStatus.setImageResource(status_images[1]);
        }
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPlat, txtTime;
        ImageView imgStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPlat = itemView.findViewById(R.id.plat_history);
            txtTime = itemView.findViewById(R.id.time_history);
            imgStatus = itemView.findViewById(R.id.status_history);
        }
    }
}
