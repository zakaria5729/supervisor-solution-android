package com.zakariahossain.supervisorsolution.utils;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.zakariahossain.supervisorsolution.activities.TopicSupervisorDetailActivity;

public class PermissionListener implements com.karumi.dexter.listener.single.PermissionListener {
    private TopicSupervisorDetailActivity detailActivity;

    public PermissionListener(TopicSupervisorDetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        detailActivity.showPermissionGranted(response.getPermissionName());
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        if (response.isPermanentlyDenied()) {
            detailActivity.handlePermanentDeniedPermissions(response.getPermissionName());
            return;
        }
        detailActivity.showPermissionDenied(response.getPermissionName());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        detailActivity.showPermissionRationale(token);
    }
}
