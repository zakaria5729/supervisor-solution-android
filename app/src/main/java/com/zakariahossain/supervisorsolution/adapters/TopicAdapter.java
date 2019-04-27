package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.TopicList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private Context context;
    private List<TopicList.Topic> topicList;
    private OnMyClickListener onMyClickListener;

    public TopicAdapter(Context context, List<TopicList.Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        holder.topicName.setText(topicList.get(position).getTopicName());
        if (!TextUtils.isEmpty(topicList.get(position).getImagePath())) {
            Glide.with(context)
                    .load(topicList.get(position).getImagePath())
                    .into(holder.topicImage);
        }
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setOnMyClickListener(OnMyClickListener myClickListener) {
        onMyClickListener = myClickListener;
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView topicImage;
        private AppCompatTextView topicName;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            topicImage = itemView.findViewById(R.id.ivTopicImage);
            topicName = itemView.findViewById(R.id.tvItemTopicName);

            topicImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
            topicName.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));

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
