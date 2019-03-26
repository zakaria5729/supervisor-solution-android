package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.Topic;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class TrialAdapter extends RecyclerView.Adapter<TrialAdapter.TrialViewHolder> {

    private Context context;
    private List<Topic> topicList;
    private List<Supervisor> supervisorList;
    private static int adapterNumber;
    private View view;
    private static OnMyClickListener onMyClickListener;

    public TrialAdapter(Context context, List<Topic> topicList, int adapterNumber) {
        this.context = context;
        this.topicList = topicList;
        TrialAdapter.adapterNumber = adapterNumber;
    }

    @NonNull
    @Override
    public TrialAdapter.TrialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adapterNumber == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        } else if (adapterNumber == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_supervisor, parent, false);
        }

        return new TrialAdapter.TrialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrialAdapter.TrialViewHolder holder, int position) {
        if (adapterNumber == 0) {
            holder.topicName.setText(topicList.get(position).getTopicName());
            Glide.with(context)
                    .load(topicList.get(position).getImagePath())
                    .into(holder.topicImage);
        } else if (adapterNumber == 1) {
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
    }

    @Override
    public int getItemCount() {
        if (adapterNumber == 0) {
            return topicList.size();
        }
        return supervisorList.size();
    }

    public void setOnMyClickListener(OnMyClickListener myClickListener) {
        onMyClickListener = myClickListener;
    }

    static class TrialViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView topicImage;
        private AppCompatTextView topicName;
        private ImageView supervisorImage;
        private AppCompatTextView supervisorName, supervisorDesignation, supervisorInitial;

        TrialViewHolder(@NonNull View itemView) {
            super(itemView);

            if (adapterNumber == 0) {
                topicImage = itemView.findViewById(R.id.ivTopicImage);
                topicName = itemView.findViewById(R.id.tvTopicName);
            } else if (adapterNumber == 1) {
                supervisorImage = itemView.findViewById(R.id.ivSupervisor);
                supervisorName = itemView.findViewById(R.id.tvSupervisorName);
                supervisorDesignation = itemView.findViewById(R.id.tvDesignation);
                supervisorInitial = itemView.findViewById(R.id.tvInitial);
            }

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
