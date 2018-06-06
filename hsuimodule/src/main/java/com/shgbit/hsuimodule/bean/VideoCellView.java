package com.shgbit.hsuimodule.bean;

import android.content.Context;
import android.util.AttributeSet;

import com.shgbit.hssdk.bean.VideoCell;

public class VideoCellView extends VideoCell {
    private int participantId;

    public VideoCellView(Context context) {
        super(context);
    }

    public VideoCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

}
