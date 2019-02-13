package com.zakariahossain.supervisorsolution.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zakariahossain.supervisorsolution.R;

import androidx.appcompat.app.AlertDialog;

public class CircularProgressBar {
    private Context context;
    private AlertDialog alertDialog;

    public CircularProgressBar(Context context) {
        this.context = context;
    }

    public AlertDialog setCircularProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_loader, null);
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }

        return alertDialog;
    }
}
