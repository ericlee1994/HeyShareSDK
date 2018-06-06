package com.shgbit.hssdk.callback;

public interface HeyshareRecordCallback {
    void startRecord(boolean result, String err);
    void endRecord(boolean result, String err);
}
