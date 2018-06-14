package com.shgbit.hssdk.view;

import android.content.Context;
import android.view.ViewGroup;

import com.ainemo.sdk.otf.OpenGLTextureView;

public class VideoView extends OpenGLTextureView{
    protected VideoView (Context context) {
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

    @Override
    public void requestRender() {
        super.requestRender();
    }
}
