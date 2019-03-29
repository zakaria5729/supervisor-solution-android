package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.SupervisorList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class SupervisorAdapter extends RecyclerView.Adapter<SupervisorAdapter.SupervisorViewHolder> {

    private Context context;
    private List<SupervisorList.Supervisor> supervisorList;
    private OnMyClickListener onMyClickListener;

    public SupervisorAdapter(Context context, List<SupervisorList.Supervisor> supervisorList) {
        this.context = context;
        this.supervisorList = supervisorList;
    }

    @NonNull
    @Override
    public SupervisorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supervisor, parent, false);
        return new SupervisorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupervisorViewHolder holder, int position) {
        String designation = supervisorList.get(position).getDesignation() + ",";

        holder.supervisorName.setText(supervisorList.get(position).getSupervisorName());
        holder.supervisorDesignation.setText(designation);
        holder.supervisorInitial.setText(supervisorList.get(position).getSupervisorInitial());

        if (!TextUtils.isEmpty(supervisorList.get(position).getSupervisorImage())) {
            Glide.with(context)
                    .load(supervisorList.get(position).getSupervisorImage())
                    .into(holder.supervisorImage);
        }
    }

    @Override
    public int getItemCount() {
        return supervisorList.size();
    }

    public void setOnMyClickListener(OnMyClickListener myClickListener) {
        onMyClickListener = myClickListener;
    }

    class SupervisorViewHolder extends RecyclerView.ViewHolder {
        private ImageView supervisorImage;
        private AppCompatTextView supervisorName, supervisorDesignation, supervisorInitial;

        SupervisorViewHolder(@NonNull View itemView) {
            super(itemView);

            supervisorImage = itemView.findViewById(R.id.ivSupervisor);
            supervisorName = itemView.findViewById(R.id.tvSupervisorName);
            supervisorDesignation = itemView.findViewById(R.id.tvDesignation);
            supervisorInitial = itemView.findViewById(R.id.tvInitial);

            supervisorImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMyClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            onMyClickListener.onMyClick(position);
                        }
                    }
                }
            });
        }
    }
}
