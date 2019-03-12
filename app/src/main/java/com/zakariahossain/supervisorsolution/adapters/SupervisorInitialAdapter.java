package com.zakariahossain.supervisorsolution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.zakariahossain.supervisorsolution.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SupervisorInitialAdapter extends RecyclerView.Adapter<SupervisorInitialAdapter.InitialViewHolder> {

    private Context context;
    private String[] supervisorInitialList;

    public SupervisorInitialAdapter(Context context, String[] supervisorInitialList) {
        this.context = context;
        this.supervisorInitialList = supervisorInitialList;
    }

    @NonNull
    @Override
    public InitialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_inital_chip, parent, false);
        return new InitialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InitialViewHolder holder, int position) {
        holder.initialChip.setText(supervisorInitialList[position]);
    }

    @Override
    public int getItemCount() {
        return supervisorInitialList.length;
    }

    static class InitialViewHolder extends RecyclerView.ViewHolder {
        private Chip initialChip;

        InitialViewHolder(@NonNull View itemView) {
            super(itemView);
            initialChip = itemView.findViewById(R.id.chipSupervisorInitial);
        }
    }
}
