package com.jahanbabu.AndroidAppSkeleton.eBus;

/**
 * Created by JK on 11/28/16.
 */

public class ReportBackPressEvent {
    boolean isClicked;

    public ReportBackPressEvent(boolean s) {
        isClicked = s;
    }

    public boolean getIsClicked() {
        return isClicked;
    }
}
