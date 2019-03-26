package com.zakariahossain.supervisorsolution.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_initial_chip, parent, false);
        return new InitialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InitialViewHolder holder, final int position) {
        holder.initialChip.setText(supervisorInitialList[position]);

        holder.initialChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied: "+supervisorInitialList[position], supervisorInitialList[position]);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copied: "+supervisorInitialList[position], Toast.LENGTH_SHORT).show();
            }
        });
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
