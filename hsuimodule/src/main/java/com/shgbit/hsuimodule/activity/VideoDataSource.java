package com.shgbit.hsuimodule.activity;

public class VideoDataSource implements ServerDataSource {

    private static VideoDataSource INSTANCE;

    public static VideoDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VideoDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getVideos(LoadVideosCallback loadVideosCallback) {

    }

    @Override
    public void getInviteUsers(InviteUsersCallback inviteUsersCallback) {

    }

    @Override
    public void getInviteMeeting(InviteMeetingCallback inviteMeetingCallback) {

    }
}
