package com.shgbit.hssdk.sdk;

import com.shgbit.hshttplibrary.AddressCeche;
import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hshttplibrary.callback.AddressUpdateCallback;
import com.shgbit.hshttplibrary.callback.ServerAddressCallback;
import com.shgbit.hshttplibrary.json.AddToGroupInfo;
import com.shgbit.hshttplibrary.json.CreateGroupInfo;
import com.shgbit.hshttplibrary.json.DeleFrmGroupInfo;
import com.shgbit.hshttplibrary.json.DeleteGroupInfo;
import com.shgbit.hshttplibrary.json.Favorite;
import com.shgbit.hshttplibrary.json.FrequentContactsPost;
import com.shgbit.hshttplibrary.json.UpdateGroupInfo;
import com.shgbit.hssdk.callback.HeyshareAdrsCallback;
import com.shgbit.hssdk.json.Group;
import com.shgbit.hssdk.json.RootOrganization;
import com.shgbit.hssdk.json.UserOrganization;
import com.shgbit.hssdk.tool.JsonUtil;

public class AddressCtrl {
    private static AddressCtrl instance;
    private HeyshareAdrsCallback callback;
    private AddressCtrl () {
        ServerInteractManager.getInstance().setServerAddressCallback(serverAddressCallback);
        AddressCeche.getInstance().setDataUpdateListener(addressUpdateCallback);
    }

    public static void registerInstance () {
        if (instance == null) {
            instance = new AddressCtrl();
        }
        HeyShareSDK.setAddressCtrl(instance);
    }

    public void setHeyshareAdrsCallback (HeyshareAdrsCallback callback) {
        this.callback = callback;
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

    public String getContactJsonString(){
        return ServerInteractManager.getInstance().getAddrString();
    }

    public Group[] getGroupList() {
        return JsonUtil.modelAconvertoB(AddressCeche.getInstance().getGpList(),Group[].class);
    }

    public RootOrganization getAddressData() {
        return JsonUtil.modelAconvertoB(AddressCeche.getInstance().getAddressData(), RootOrganization.class);
    }

    public UserOrganization[] getFrequentContacts() {
        return JsonUtil.modelAconvertoB(AddressCeche.getInstance().getGeneralContacts(), UserOrganization[].class);
    }

    public UserOrganization[] getOrderedContacts() {
        return JsonUtil.modelAconvertoB(AddressCeche.getInstance().getOrderedContacts(), UserOrganization[].class);
    }

    public UserOrganization[] getGroupMenbers(String groupName) {
        return JsonUtil.modelAconvertoB(AddressCeche.getInstance().getGroupType(groupName), UserOrganization[].class);
    }

    private ServerAddressCallback serverAddressCallback = new ServerAddressCallback() {
        @Override
        public void onPostContactUser(boolean success, String error) {
            if (callback != null) {
                callback.onPostContactUser(success, error);
            }
        }

        @Override
        public void onCreateGroup(boolean success, String error) {
            if (callback != null) {
                callback.onCreateGroup(success, error);
            }
        }

        @Override
        public void onDeleteGroup(boolean success, String error) {
            if (callback != null) {
                callback.onDeleteGroup(success, error);
            }
        }

        @Override
        public void onUpdateGroup(boolean success, String error) {
            if (callback != null) {
                callback.onUpdateGroup(success, error);
            }
        }

        @Override
        public void onAddToGroup(boolean success, String error) {
            if (callback != null) {
                callback.onAddToGroup(success, error);
            }
        }

        @Override
        public void onDeleFrmGroup(boolean success, String error) {
            if (callback != null) {
                callback.onDeleFrmGroup(success, error);
            }
        }
    };

    private AddressUpdateCallback addressUpdateCallback = new AddressUpdateCallback() {
        @Override
        public void onDataUpdate() {
            if (callback != null) {
                callback.onDataUpdate();
            }
        }
    };
}
