package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.RequestedOrAcceptedGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class AcceptedGroupAdapter extends RecyclerView.Adapter<AcceptedGroupAdapter.AcceptedGroupViewHolder> {

    private Context context;
    private List<List<RequestedOrAcceptedGroup>> acceptedGroupList;
    private static OnMyClickListener onMyClickListener;

    public AcceptedGroupAdapter(Context context, List<List<RequestedOrAcceptedGroup>> acceptedGroupList) {
        this.context = context;
        this.acceptedGroupList = acceptedGroupList;
    }

    @NonNull
    @Override
    public AcceptedGroupAdapter.AcceptedGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requested_and_accepted_and_group_status, parent, false);
        return new AcceptedGroupAdapter.AcceptedGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedGroupAdapter.AcceptedGroupViewHolder holder, int position) {
        List<RequestedOrAcceptedGroup> groupList = acceptedGroupList.get(position);

        holder.acceptedTextView.setText("Accepted");
        holder.acceptedTextView.setTextColor(context.getResources().getColor(R.color.colorGreen));
        holder.acceptedGroupNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_accepted, 0, 0, 0);

        holder.acceptedGroupNameTextView.setCompoundDrawablePadding(10);
        holder.acceptedGroupNameTextView.setText(groupList.get(0).getName());
        holder.acceptedGroupEmailTextView.setText(groupList.get(0).getEmail());
    }

    @Override
    public int getItemCount() {
        return acceptedGroupList.size();
    }

    public void setOnMyClickListener(OnMyClickListener myClickListener) {
        onMyClickListener = myClickListener;
    }

    static class AcceptedGroupViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView acceptedGroupNameTextView, acceptedGroupEmailTextView, acceptedTextView;

        AcceptedGroupViewHolder(@NonNull View itemView) {
            super(itemView);

            acceptedGroupNameTextView = itemView.findViewById(R.id.tvRequestedOrAcceptedGroupName);
            acceptedGroupEmailTextView = itemView.findViewById(R.id.tvRequestedOrAcceptedGroupEmail);
            acceptedTextView = itemView.findViewById(R.id.tvPendingOrAccept);
            acceptedGroupEmailTextView.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMyClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            onMyClickListener.onMyClickRequestOrAcceptListener(position, 1);
                        }
                    }
                }
            });
        }
    }
}
