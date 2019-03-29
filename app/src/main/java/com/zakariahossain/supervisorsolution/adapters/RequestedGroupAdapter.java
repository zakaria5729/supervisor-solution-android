package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.Student;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class RequestedGroupAdapter extends RecyclerView.Adapter<RequestedGroupAdapter.RequestedGroupViewHolder> {

    private Context context;
    private List<List<Student>> requestedGroupList;
    private static OnMyClickListener onMyClickListener;

    public RequestedGroupAdapter(Context context, List<List<Student>> requestedGroupList) {
        this.context = context;
        this.requestedGroupList = requestedGroupList;
    }

    @NonNull
    @Override
    public RequestedGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requested_and_accepted_and_group_status, parent, false);
        return new RequestedGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedGroupViewHolder holder, int position) {
        List<Student> groupList = requestedGroupList.get(position);

            holder.pendingTextView.setText("Pending");
            holder.pendingTextView.setTextColor(context.getResources().getColor(R.color.colorPink));
            holder.requestedGroupNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pending, 0, 0, 0);

        holder.requestedGroupNameTextView.setCompoundDrawablePadding(10);
        holder.requestedGroupNameTextView.setText(groupList.get(0).getName());
        holder.requestedGroupEmailTextView.setText(groupList.get(0).getEmail());
    }

    @Override
    public int getItemCount() {
        return requestedGroupList.size();
    }

    public void setOnMyClickListener(OnMyClickListener myClickListener) {
        onMyClickListener = myClickListener;
    }

    static class RequestedGroupViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView requestedGroupNameTextView, requestedGroupEmailTextView, pendingTextView;

        RequestedGroupViewHolder(@NonNull View itemView) {
            super(itemView);

            requestedGroupNameTextView = itemView.findViewById(R.id.tvRequestedOrAcceptedGroupName);
            requestedGroupEmailTextView = itemView.findViewById(R.id.tvRequestedOrAcceptedGroupEmail);
            pendingTextView = itemView.findViewById(R.id.tvPendingOrAccept);
            requestedGroupEmailTextView.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMyClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            onMyClickListener.onMyClickRequestOrAcceptListener(position, 0);
                        }
                    }
                }
            });
        }
    }
}
