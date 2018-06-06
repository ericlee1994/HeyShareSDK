package com.shgbit.android.hsaddress.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.gson.Gson;
import com.shgbit.android.hsaddress.bean.Contacts;
import com.shgbit.android.hsaddress.bean.CreateMeetingInfo;
import com.shgbit.android.hsaddress.bean.Favorite;
import com.shgbit.android.hsaddress.bean.FrequentContactsPost;
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.Organization;
import com.shgbit.android.hsaddress.bean.User;
import com.shgbit.android.hsaddress.bean.UserInfo;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.callback.ExternalCallBack;
import com.shgbit.android.hsaddress.callback.InternalCallBack;
import com.shgbit.android.hsaddress.fragment.AllAddressListFragment;
import com.shgbit.android.hsaddress.fragment.GroupFragment;
import com.shgbit.android.hsaddress.fragment.PersonalAddressListFragment;
import com.shgbit.android.hsaddress.fragment.PersonalMeetingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class AddressSDK {
    private final String TAG = "AddSDK";
    private static AddressSDK mCollector;

    private Context mContext;
    private int mBackgroundId;
    private int mBackgroundId2;
    private AllAddressListFragment mAllAddressListFrag;
    private AllAddressListFragment mMeetingAllAddressListFrag;
    private PersonalAddressListFragment mPersonalAddressListFrag;
    private PersonalMeetingFragment mPersonalMeetingFrag;
    private GroupFragment mGroupFrag;
    private boolean ismeeting;
    private String type;
    private boolean isReserve;
    private ArrayList<User> mRuser;
    private Group group;
    private String LoginName;
    private UserOrganization mPerson;
    private ArrayList<Favorite> mLocalFreq;
    private Favorite[] mFUsers;
    private String name;
    private Group mGroup;
    private String ScreenType;
    private List<UserOrganization> mSelectUsers;
    private boolean normal;
    private List<UserOrganization> mContactList;
    private String[] mUsers;
    private boolean ispersonal;
    private ExternalCallBack mExternalCallBack;

    public AddressSDK(){

    }

    public static AddressSDK getInstance () {
        if (mCollector == null) {
            mCollector = new AddressSDK();
        }
        return mCollector;
    }


    public StructureDataCollector.ContactsUpdateListener mCUpdateListener = new StructureDataCollector.ContactsUpdateListener() {

        @Override
        public void onContactsUpdate() {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MESSAGE_1);
            }
        }

        @Override
        public void onGroupUpdate() {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MESSAGE_2);
            }
        }
    };

    private final int MESSAGE_1 = 0x005;
    private final int MESSAGE_2 = 0x006;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_1:
                    mFUsers = StructureDataCollector.getInstance().getContact();
                    break;
                case MESSAGE_2:
                    if(mGroupFrag != null){
                        mGroupFrag.setListener();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }};


    public void init(Context c, int id, int id2, String name){
        mContext = c;
        mBackgroundId = id;
        mBackgroundId2 = id2;
        LoginName = name;
        StructureDataCollector.getInstance().setContactsUpdateListener(mCUpdateListener);
    }

    public void setData(Organization[] organizations, UserInfo[] userInfos) {
        if (organizations == null && userInfos == null) {
            return;
        }
        StructureDataCollector.getInstance().setaddressData(organizations, userInfos);
    }

    public void setData(String Organization,String UserInfo){
        if(Organization == null && UserInfo == null){
            return;
        }
        Organization[] o = null;
        UserInfo[] u = null;
        try{
            o = new Gson().fromJson(Organization, Organization[].class);
            u = new Gson().fromJson(UserInfo, UserInfo[].class);
        }catch(Exception e){
            Log.e(TAG, "parse json Throwable: " + e.toString());
        }

        if (o == null) {
            o = new Organization[0];
        }

        if (u == null) {
            u = new UserInfo[0];
        }
        StructureDataCollector.getInstance().setaddressData(o,u);
    }

    public void setData(String json){
        if(json == null){
            return;
        }
        Contacts contacts = null;
        try {
            contacts = new Gson().fromJson(json, Contacts.class);
        } catch (Throwable e) {
            Log.e(TAG, "parse Contacts Throwable: " + e.toString());
        }

        if (contacts != null && contacts.getResult().equalsIgnoreCase("success") == true) {
            StructureDataCollector.getInstance().setaddressData(contacts.getOrganization(), contacts.getUsers());
        }
    }

    public void setMeetingData(String result){
        if(result != null){
            if(mPersonalMeetingFrag != null){
                mPersonalMeetingFrag.setResult(result);
            }
        }
    }

    public void setCommon(int screenheight,int screenwidth){
        if(screenheight != 0 && screenwidth != 0){
            Common.SCREENHEIGHT = screenheight;
            Common.SCREENWIDTH = screenwidth;
        }
    }


    public void des () {
        Log.e(TAG, "############des");
        ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().remove(mAllAddressListFrag).commit();
    }

    private void ChangeFragment(int index,Object object){
        FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        if(index == 0){
            mAllAddressListFrag = new AllAddressListFragment();
            mAllAddressListFrag.setLayout(ismeeting,type,isReserve,mRuser,group);
            mAllAddressListFrag.setLoginName(LoginName);
            transaction.replace(mBackgroundId, mAllAddressListFrag).commit();
            mAllAddressListFrag.setInCallBack(mInCallBack);
        }else if(index == 1){
            mPersonalAddressListFrag = new PersonalAddressListFragment();
            mPersonalAddressListFrag.setOrgData((UserOrganization) object,LoginName,mInCallBack);
            transaction.add(mBackgroundId2, mPersonalAddressListFrag).addToBackStack(null).commit();
        }else if(index == 2){
            mPersonalMeetingFrag = new PersonalMeetingFragment();
            mPersonalMeetingFrag.setMeetingData((User[]) object, mPerson,LoginName,mInCallBack);
            transaction.add(mBackgroundId2, mPersonalMeetingFrag).addToBackStack(null).commit();
        }else if(index == 3){
            mGroupFrag = new GroupFragment();
            mGroupFrag.setGroupFrag(name,mGroup,mContactList,normal,mSelectUsers,ScreenType,LoginName,mInCallBack);
            transaction.add(mBackgroundId, mGroupFrag).addToBackStack(null).commit();
        }else if(index == 4){
            mMeetingAllAddressListFrag = new AllAddressListFragment();
            mMeetingAllAddressListFrag.setLayout(true, "vertical", false,null,(Group) object);
            transaction.add(mBackgroundId2, mMeetingAllAddressListFrag).addToBackStack(null).commit();
            mMeetingAllAddressListFrag.setInCallBack(mInCallBack);
        }
    }

    public void startAddressList(boolean ismeeting, String type, boolean isReserve, ArrayList<User> mRuser, Group group){
        this.ismeeting =ismeeting;
        this.isReserve = isReserve;
        this.type = type;
        this.mRuser = mRuser;
        this.group = group;
        ChangeFragment(0,null);
    }

    public void setExCallBack(ExternalCallBack mExterCallBack){
        if(mExterCallBack == null){
            return;
        }
        this.mExternalCallBack = mExterCallBack;
        if(mAllAddressListFrag != null){
            mAllAddressListFrag.setExCallBack(mExternalCallBack);
        }
        if(mGroupFrag != null){
            mGroupFrag.setExCallBack(mExternalCallBack);
        }
        if(mMeetingAllAddressListFrag != null){
            mMeetingAllAddressListFrag.setExCallBack(mExternalCallBack);
        }
    }

    private InternalCallBack mInCallBack = new InternalCallBack() {
        @Override
        public void onPersonalAddressFragment(UserOrganization pInformatinal) {
            if(pInformatinal == null){
                return;
            }
            mPerson = pInformatinal;
            ChangeFragment(1, pInformatinal);
        }


        @Override
        public void onPostContactsUser(Favorite mContacts, Favorite deleteFP) {
            FrequentContactsPost fp = new FrequentContactsPost();

            if (mLocalFreq == null) {
                mLocalFreq = new ArrayList<>();
            }
            mLocalFreq.clear();
            for (int i = 0; mFUsers!=null && i < mFUsers.length; i++) {
                mLocalFreq.add(mFUsers[i]);
            }

            if (mContacts != null) {
                mLocalFreq.add(mContacts);
                mFUsers = mLocalFreq.toArray(new Favorite[0]);
            }

            if (deleteFP != null) {
                for (int i = 0; i < mLocalFreq.size(); i++) {
                    if (mLocalFreq.get(i).getUserName().equals(deleteFP.getUserName())) {
                        mLocalFreq.remove(i);
                        i--;
                    }
                }
                mFUsers = mLocalFreq.toArray(new Favorite[0]);
            }
            Favorite[] favorite = mLocalFreq.toArray(new Favorite[0]);
            StructureDataCollector.getInstance().setContactUsers(favorite);

            fp.setFavorites(favorite);
            mExternalCallBack.onFrequentContacts(fp);
        }


        @Override
        public void onPersonalMeeting(User[] users, boolean isPerson) {

            if(users == null){
                return;
            }
            ChangeFragment(2,users);
        }

        @Override
        public void onGroupAddMember(Group group1) {
            ChangeFragment(4,group1);
        }

        @Override
        public void onGroupUsers() {
            if(mAllAddressListFrag != null){
                mAllAddressListFrag.setGroupSelect();
            }
        }

        @Override
        public void onUpdataGroup() {
            if (mAllAddressListFrag != null) {
                mAllAddressListFrag.setListener();
            }
        }

        @Override
        public void onDesFragment() {
            ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().remove(mAllAddressListFrag).commit();
        }

        @Override
        public void onInvitedUsers(User[] users, boolean isPersonal) {
            ispersonal = isPersonal;
            ArrayList arrayList = new ArrayList();
            String[] userName = new String[users.length];
            for(int i = 0; i < users.length; i++){
                userName[i] = users[i].getUserName();
                if (users[i].getStatus().equals("online")){
                    arrayList.add(users[i].getUserName());
                    mUsers = new String[arrayList.size()];
                    arrayList.toArray(mUsers);
                }
            }
            CreateMeetingInfo createMeetingInfo = new CreateMeetingInfo();
            createMeetingInfo.setCreatedUser(LoginName);
            createMeetingInfo.setInvitedUsers(userName);
            mExternalCallBack.onCreateMeeting(createMeetingInfo);
        }

        @Override
        public void onGroupFragment(Group group, List<UserOrganization> userOrganizations, boolean isMeeting, List<UserOrganization> selectUsers, String type) {
            if(group == null){
                name="contact";
            }else {
                name = null;
            }
            mGroup = group;
            mSelectUsers = selectUsers;
            normal = isMeeting;
            mContactList=userOrganizations;
            ScreenType = type;
            ChangeFragment(3,null);
        }
    };

    public void setGroupListener(){
        if(mGroupFrag != null){
            mGroupFrag.setListener();
        }
    }

    public void destroy(){
        if (mHandler != null) {
            mHandler.removeMessages(MESSAGE_1);
            mHandler.removeMessages(MESSAGE_2);
            mHandler = null;
        }
        try {
//            ServerInteractManager.getInstance().removeServerInteractCallback(mInteractCallback);
            FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            transaction.remove(mAllAddressListFrag);
            transaction.remove(mPersonalAddressListFrag);
            transaction.remove(mPersonalMeetingFrag);
            transaction.remove(mMeetingAllAddressListFrag);
            transaction.remove(mGroupFrag);

            mPersonalMeetingFrag = null;
            mGroupFrag = null;
            mAllAddressListFrag = null;
            mMeetingAllAddressListFrag = null;
            mPersonalAddressListFrag = null;
        }catch (Exception e) {
        }

    }
}
