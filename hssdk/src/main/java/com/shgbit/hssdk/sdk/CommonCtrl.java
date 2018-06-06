package com.shgbit.hssdk.sdk;

import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hshttplibrary.json.AddToGroupInfo;
import com.shgbit.hshttplibrary.json.BusyMeetingInfo;
import com.shgbit.hshttplibrary.json.CancelInviteInfo;
import com.shgbit.hshttplibrary.json.CreateGroupInfo;
import com.shgbit.hshttplibrary.json.CreateMeetingInfo;
import com.shgbit.hshttplibrary.json.DeleFrmGroupInfo;
import com.shgbit.hshttplibrary.json.DeleteGroupInfo;
import com.shgbit.hshttplibrary.json.DeleteInfo;
import com.shgbit.hshttplibrary.json.Favorite;
import com.shgbit.hshttplibrary.json.FrequentContactsPost;
import com.shgbit.hshttplibrary.json.InviteMeetingInfo;
import com.shgbit.hshttplibrary.json.JoinMeetingInfo;
import com.shgbit.hshttplibrary.json.LoginInfo;
import com.shgbit.hshttplibrary.json.LogoutInfo;
import com.shgbit.hshttplibrary.json.MeetingRecord;
import com.shgbit.hshttplibrary.json.QueryGroupInfo;
import com.shgbit.hshttplibrary.json.QuiteMeetingInfo;
import com.shgbit.hshttplibrary.json.ReserveInfo;
import com.shgbit.hshttplibrary.json.ReserveMeetingInfo;
import com.shgbit.hshttplibrary.json.SendInfo;
import com.shgbit.hshttplibrary.json.SyncPidInfo;
import com.shgbit.hshttplibrary.json.UpdateGroupInfo;

public class CommonCtrl {
    public void login (String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().login(loginInfo);
    }

    public void logout () {
        LogoutInfo logoutInfo = new LogoutInfo();
        ServerInteractManager.getInstance().logout();
    }

    public void checkPwd(String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().checkPwd(loginInfo);
    }

    public void modifyPwd(String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().motifyPwd(loginInfo);
    }

    public void createReserveMeeting (String createUser, String meetingName, String startTime, String endTime, String[] inviteUsers) {
        ReserveInfo reserveMeeting = new ReserveInfo();
        reserveMeeting.setCreatedUser(createUser);
        reserveMeeting.setMeetingName(meetingName);
        reserveMeeting.setStartTime(startTime);
        reserveMeeting.setEndTime(endTime);
        reserveMeeting.setInvitedUsers(inviteUsers);
        ServerInteractManager.getInstance().reserveMeeting(reserveMeeting);
    }

    public void createInstantMeeting(String createUser, String[] inviteUsers) {
        CreateMeetingInfo instantMeeting = new CreateMeetingInfo();
        instantMeeting.setCreatedUser(createUser);
        instantMeeting.setInvitedUsers(inviteUsers);
        ServerInteractManager.getInstance().createMeeting(instantMeeting);
    }

    public void joinMeeting(String userName, String meetingId, String meetingPwd){
        JoinMeetingInfo joinMeetingInfo = new JoinMeetingInfo();
        joinMeetingInfo.setMeetingId(meetingId);
        joinMeetingInfo.setPassword(meetingPwd);
        joinMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().joinMeeting(joinMeetingInfo);
    }

    public void quitMeeting(String userName, String meetingId){
        QuiteMeetingInfo quiteMeetingInfo = new QuiteMeetingInfo();
        quiteMeetingInfo.setMeetingId(meetingId);
        quiteMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().quiteMeeting(quiteMeetingInfo);
    }

    public void inviteUsers(String userName, String meetingId, String[] users){
        InviteMeetingInfo inviteMeetingInfo = new InviteMeetingInfo();
        inviteMeetingInfo.setMeetingId(meetingId);
        inviteMeetingInfo.setInvitedUsers(users);
        inviteMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().inviteMeeting(inviteMeetingInfo);
    }

    public void cancelInviting(String meetingId, String invitedName){
        CancelInviteInfo cancelInviteInfo = new CancelInviteInfo();
        cancelInviteInfo.setMeetingId(meetingId);
        cancelInviteInfo.setInvitedUser(invitedName);
        ServerInteractManager.getInstance().cancleMeeting(cancelInviteInfo);
    }

    public void refuseInviting(String userName, String meetingId, String inviter){
        BusyMeetingInfo busyMeetingInfo = new BusyMeetingInfo();
        busyMeetingInfo.setMeetingId(meetingId);
        busyMeetingInfo.setInviter(inviter);
        busyMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().busyMeeting(busyMeetingInfo);
    }

    public void modifyReserveMeeting (String meetingId, String meetingName, String startTime, String endTime, String[] inviteUsers) {
        ReserveMeetingInfo reserveMeetingInfo = new ReserveMeetingInfo();
        reserveMeetingInfo.setMeetingId(meetingId);
        reserveMeetingInfo.setMeetingName(meetingName);
        reserveMeetingInfo.setInvitedUsers(inviteUsers);
        reserveMeetingInfo.setStartTime(startTime);
        reserveMeetingInfo.setEndTime(endTime);
        ServerInteractManager.getInstance().updateMeeting(reserveMeetingInfo);
    }

    public void deleteReserveMeeting(String meetingId){
        DeleteInfo deleteReserveMeeting = new DeleteInfo();
        deleteReserveMeeting.setMeetingId(meetingId);
        ServerInteractManager.getInstance().deleteMeeting(deleteReserveMeeting);
    }

    public void sendMsg(String meetingId, String[] users) {
        SendInfo sendInfo = new SendInfo();
        sendInfo.setMeetingId(meetingId);
        sendInfo.setUsers(users);
        ServerInteractManager.getInstance().sendMessage(sendInfo);
    }

    public void syncPid (String meetingId) {
        SyncPidInfo syncPidInfo = new SyncPidInfo();
        syncPidInfo.setMeetingId(meetingId);
        ServerInteractManager.getInstance().syncPid(syncPidInfo);
    }

    public String getSyncMeetingById(String meetingId) {
        return ServerInteractManager.getInstance().getSyncGetMeeting(meetingId);
    }

    public String getSyncCurrentMeeting() {
        return ServerInteractManager.getInstance().getSyncCurrentMeeting();
    }

    public String getContactJsonString() {
        return ServerInteractManager.getInstance().getAddrString();
    }

    public void getFrequentContacts() {
        ServerInteractManager.getInstance().getContactUser();
    }

    public void addFrequentContacts(String[] userNames) {
        FrequentContactsPost frequentContactsPost = new FrequentContactsPost();
        if (userNames != null) {
            Favorite[] favorites = new Favorite[userNames.length];
            for (int i=0; i < userNames.length; i++) {
                Favorite favorite = new Favorite();
                favorite.setUserName(userNames[i]);
                favorites[i] = favorite;
            }
            frequentContactsPost.setFavorites(favorites);
        }
        ServerInteractManager.getInstance().FrequentContacts(frequentContactsPost);
    }

    public void createGroup(String groupName, String[] menbers) {
        CreateGroupInfo createGroupInfo = new CreateGroupInfo();
        createGroupInfo.setName(groupName);
        createGroupInfo.setMember(menbers);
        ServerInteractManager.getInstance().createGroup(createGroupInfo);
    }

    public void deleteGroup (String groupId) {
        DeleteGroupInfo deleteGroupInfo = new DeleteGroupInfo();
        deleteGroupInfo.setId(groupId);
        ServerInteractManager.getInstance().deleteGroup(deleteGroupInfo);
    }

    public void updateGroupName(String groupId, String groupName) {
        UpdateGroupInfo updateGroupInfo = new UpdateGroupInfo();
        updateGroupInfo.setId(groupId);
        updateGroupInfo.setName(groupName);
        ServerInteractManager.getInstance().updateGroup(updateGroupInfo);
    }

    public void queryGroupById(String groupId) {
        QueryGroupInfo queryGroupInfo = new QueryGroupInfo();
        queryGroupInfo.setId(groupId);
        ServerInteractManager.getInstance().queryGroup(queryGroupInfo);
    }

    public void addMembersToGroup(String groupId, String[] menbers) {
        AddToGroupInfo addToGroupInfo = new AddToGroupInfo();
        addToGroupInfo.setId(groupId);
        addToGroupInfo.setMembers(menbers);
        ServerInteractManager.getInstance().addToGroup(addToGroupInfo);
    }

    public void deleteMembersFromGroup(String groupId, String[] menbers) {
        DeleFrmGroupInfo deleFrmGroupInfo = new DeleFrmGroupInfo();
        deleFrmGroupInfo.setId(groupId);
        deleFrmGroupInfo.setMembers(menbers);
        ServerInteractManager.getInstance().deleFrmGroup(deleFrmGroupInfo);
    }

    public void startRecord(String meetingId) {
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingId(meetingId);
        ServerInteractManager.getInstance().startRecord(meetingRecord);
    }

    public void endRecord(String meetingId) {
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingId(meetingId);
        ServerInteractManager.getInstance().endRecord(meetingRecord);
    }
}
