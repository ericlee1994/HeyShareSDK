package com.shgbit.hssdk.callback;

public interface HeyshareAdrsCallback {
    void onDataUpdate();
    void onPostContactUser(boolean success, String error);
    void onCreateGroup(boolean success, String error);
    void onDeleteGroup(boolean success, String error);
    void onUpdateGroup(boolean success, String error);
    void onAddToGroup(boolean success, String error);
    void onDeleFrmGroup(boolean success, String error);
}
