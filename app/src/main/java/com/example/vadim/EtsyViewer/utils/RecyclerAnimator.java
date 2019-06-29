package com.example.vadim.EtsyViewer.utils;

import android.support.v7.widget.RecyclerView;

public class RecyclerAnimator {
    public void setDeleteAnimationSpeed(RecyclerView recyclerView ,int speed) {
        recyclerView.getItemAnimator().setRemoveDuration(speed);
        recyclerView.getItemAnimator().setMoveDuration(speed);
        recyclerView.getItemAnimator().setChangeDuration(speed);
        recyclerView.getItemAnimator().setAddDuration(speed);
    }
}
