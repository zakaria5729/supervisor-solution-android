package com.zakariahossain.supervisorsolution.utils;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

public class PermissionListener implements com.karumi.dexter.listener.single.PermissionListener {
    private OthersUtil activity;

    PermissionListener(OthersUtil activity) {
        this.activity = activity;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        activity.showPermissionGranted(response.getPermissionName());
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        if (response.isPermanentlyDenied()) {
            activity.handlePermanentDeniedPermissions(response.getPermissionName());
            return;
        }
        activity.showPermissionDenied(response.getPermissionName());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        activity.showPermissionRationale(token);
    }
}
