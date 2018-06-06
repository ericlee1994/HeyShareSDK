package com.shgbit.android.hsaddress.callback;


import com.shgbit.android.hsaddress.bean.Favorite;
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.User;
import com.shgbit.android.hsaddress.bean.UserOrganization;

import java.util.List;

/**
 * Created by Administrator on 2018/2/13 0013.
 */

public interface InternalCallBack {
    void onPersonalAddressFragment(UserOrganization pInformatinal);
    void onPostContactsUser(Favorite mContacts, Favorite deleteFP);
    void onPersonalMeeting(User[] users, boolean isPerson);
    void onGroupAddMember(Group group1);
    void onGroupUsers();
    void onUpdataGroup();
    void onDesFragment();
    void onInvitedUsers(User[] users, boolean isPersonal);
    void onGroupFragment(Group group, List<UserOrganization> userOrganizations, boolean isMeeting, List<UserOrganization> selectUsers, String type);
}
