package com.shgbit.hssdk.view;


import android.content.Context;
import android.view.ViewGroup;

public class VideoCellView extends VideoView {
    private int participantId;
    public VideoCellView (Context context) {
        super(context);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    public void setSourceID(String s) {
        super.setSourceID(s);
    }

    @Override
    public void setContent(boolean b) {
        super.setContent(b);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
}
