package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.GroupStatusList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class GroupStatusAdapter extends RecyclerView.Adapter<GroupStatusAdapter.GroupListStatusHolder> {

    private Context context;
    private List<GroupStatusList.GroupStatus> groupStatusList;
    private OnMyClickListener onMyClickListener;

    public GroupStatusAdapter(Context context, List<GroupStatusList.GroupStatus> groupStatusList) {
        this.context = context;
        this.groupStatusList = groupStatusList;
    }

    @NonNull
    @Override
    public GroupListStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requested_and_accepted_and_group_status, parent, false);
        return new GroupListStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListStatusHolder holder, int position) {
        if (groupStatusList.get(position).getIsAccepted() == 0) {
            holder.pendingOrAcceptTextView.setText("Pending");
            holder.pendingOrAcceptTextView.setTextColor(context.getResources().getColor(R.color.colorPink));
            holder.supervisorEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pending, 0, 0, 0);
        } else {
            holder.pendingOrAcceptTextView.setText("Accepted");
            holder.pendingOrAcceptTextView.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.supervisorEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_accepted, 0, 0, 0);
        }

        holder.supervisorEmailTextView.setText(groupStatusList.get(position).getSupervisorEmail());
        holder.supervisorEmailTextView.setCompoundDrawablePadding(10);
    }

    @Override
    public int getItemCount() {
        return groupStatusList.size();
    }

    public void setOnMyClickListener(OnMyClickListener listener) {
        onMyClickListener = listener;
    }

    class GroupListStatusHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView supervisorEmailTextView, pendingOrAcceptTextView;

        GroupListStatusHolder(@NonNull View itemView) {
            super(itemView);
            supervisorEmailTextView = itemView.findViewById(R.id.tvRequestedOrAcceptedGroupName);
            pendingOrAcceptTextView = itemView.findViewById(R.id.tvPendingOrAccept);

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
