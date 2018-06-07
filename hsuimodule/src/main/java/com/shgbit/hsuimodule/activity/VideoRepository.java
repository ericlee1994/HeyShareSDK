package com.shgbit.hsuimodule.activity;

import static com.google.common.base.Preconditions.checkNotNull;

public class VideoRepository implements VideosDataSource {

    private static VideoRepository INSTANCE = null;
    private final VideosDataSource mVideosDataSource;

    private VideoRepository(VideosDataSource videosDataSource) {
        mVideosDataSource = checkNotNull(videosDataSource);
    }

    public static VideoRepository getInstance(VideosDataSource videosDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VideoRepository(videosDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
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
