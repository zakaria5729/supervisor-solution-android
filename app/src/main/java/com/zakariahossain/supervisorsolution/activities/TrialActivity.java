package com.zakariahossain.supervisorsolution.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.zakariahossain.supervisorsolution.R;

public class TrialActivity extends AppCompatActivity {

    AppCompatSpinner userRoleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        //userRoleSpinner = findViewById(R.id.spUserRoleaaa);

        ///seUserRoleSpinner();
    }

    /*private void seUserRoleSpinner() {
        ArrayAdapter<String> userRoleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.user_role_to_login));
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(userRoleAdapter);
    }*/
}
