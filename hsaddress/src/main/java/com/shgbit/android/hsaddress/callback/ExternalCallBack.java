package com.shgbit.android.hsaddress.callback;

import com.shgbit.android.hsaddress.bean.AddToGroupInfo;
import com.shgbit.android.hsaddress.bean.CancelInviteInfo;
import com.shgbit.android.hsaddress.bean.CreateGroupInfo;
import com.shgbit.android.hsaddress.bean.CreateMeetingInfo;
import com.shgbit.android.hsaddress.bean.DeleFrmGroupInfo;
import com.shgbit.android.hsaddress.bean.DeleteGroupInfo;
import com.shgbit.android.hsaddress.bean.FrequentContactsPost;
import com.shgbit.android.hsaddress.bean.QuiteMeetingInfo;
import com.shgbit.android.hsaddress.bean.UpdateGroupInfo;
import com.shgbit.android.hsaddress.bean.User;

/**
 * Created by Administrator on 2018/2/13 0013.
 */

public interface ExternalCallBack {
    void onReserveUsers(User[] users);
    void onInvitedUsers(User[] users, boolean isPerson);

    void invite(User[] users);
    void onDesFragment();
    void onAddToGroup(AddToGroupInfo addToGroupInfo);
    void onDeleteGroup(DeleteGroupInfo deleteGroupInfo);
    void onCreateGroup(CreateGroupInfo createGroupInfo);
    void onUpdateGroup(UpdateGroupInfo updateGroupInfo);
    void onDeleFrmGroup(DeleFrmGroupInfo deleFrmGroupInfo);
    void onCancelMeeting(CancelInviteInfo cancelInviteInfo);
    void onQuiteMeeting(QuiteMeetingInfo quiteMeetingInfo);
    void onSyncGetMeeting(String meetingid);
    void onStartVideo();
    void onFrequentContacts(FrequentContactsPost fp);
    void onCreateMeeting(CreateMeetingInfo cl);

}
