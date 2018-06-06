package com.shgbit.hshttplibrary.callback;


import com.shgbit.hshttplibrary.json.Favorite;
import com.shgbit.hshttplibrary.json.Group;
import com.shgbit.hshttplibrary.json.OnlineUser;
import com.shgbit.hshttplibrary.json.Organization;
import com.shgbit.hshttplibrary.json.UserInfo;

/**
 * Created by Administrator on 2018/2/13.
 */

public interface ServerAddressCallback {
//    void onOnlineChanged(OnlineUser[] onlineUsers);
    void onPostContactUser(boolean success, String error);
//    void onGetContactUser(Favorite[] favs);
    void onCreateGroup(boolean success, String error);
    void onDeleteGroup(boolean success, String error);
    void onUpdateGroup(boolean success, String error);
//    void onQueryGroup(boolean success, String error, Group[] groups);
    void onAddToGroup(boolean success, String error);
    void onDeleFrmGroup(boolean success, String error);
}
