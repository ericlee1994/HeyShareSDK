package com.shgbit.android.hsaddress.sdk;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.shgbit.android.hsaddress.bean.ContentOnlyStateSessionType;
import com.shgbit.android.hsaddress.bean.Favorite;
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.MobileStateSessionType;
import com.shgbit.android.hsaddress.bean.OnlineUser;
import com.shgbit.android.hsaddress.bean.Organization;
import com.shgbit.android.hsaddress.bean.OrganizationObject;
import com.shgbit.android.hsaddress.bean.PCStateSessionType;
import com.shgbit.android.hsaddress.bean.RootOrganization;
import com.shgbit.android.hsaddress.bean.UserInfo;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.fragment.WAFile;

import java.util.ArrayList;


public class StructureDataCollector {
	private final String TAG = "StructureDataCollector";
	private static StructureDataCollector mCollector;

	private String mUserName = "";
	private Context context;

	private Organization[] organizations = null;
	private UserInfo[] userInfos = null;
	private OnlineUser[] onlineUsers = null;
	
	private ArrayList<RootOrganization> mDataList;
	private ArrayList<UserOrganization> mGeneralList = new ArrayList<UserOrganization>();
	private ArrayList<UserOrganization> mOrderedList = new ArrayList<UserOrganization>();
	private ArrayList<UserOrganization> mGroupList = new ArrayList<UserOrganization>();
	private ArrayList<Group> mGpList = new ArrayList<Group>();
	
	private ArrayList<String> mUserOrgList = new ArrayList<String>();
	
	private DataUpdateListener mListener;
	private ContactsUpdateListener mContactsListener;
	private boolean isFUser= false;
	private Favorite[] mFcu;
	
	public void setDataUpdateListener (DataUpdateListener listener) {
		mListener = listener;
	}

	public interface DataUpdateListener {
		void onDataUpdate();
	}

	public void setContactsUpdateListener(ContactsUpdateListener contactsListener){
		mContactsListener = contactsListener;
	}

	public interface ContactsUpdateListener{
		void onContactsUpdate();
		void onGroupUpdate();
	}

	public StructureDataCollector(){

	}
	
	public static StructureDataCollector getInstance () {
		if (mCollector == null) {
			mCollector = new StructureDataCollector();
		}
		return mCollector;
	}

	public void init (String userName, Context context) {
		mUserName = userName;
		this.context = context;
		loadGeneralRoot();
	}

	public void Finalize () {
		mGeneralList = null;
	}

	public Favorite[] getContact(){
		return mFcu;
	}

	public void setGroups(Group[] groups){
		if (mGpList == null) {
			mGpList = new ArrayList<Group>();
		} else {
			mGpList.clear();
		}

		if (groups != null) {
			for(int i=0;i<groups.length;i++){
				mGpList.add(groups[i]);
			}
		}

		if (mListener != null) {
			mListener.onDataUpdate();
		}
	}

	public ArrayList<Group> getGpList(){
		return mGpList;
	}
	
	public void setContactUsers(Favorite[] favs){
		if (mGeneralList == null) {
			mGeneralList = new ArrayList<UserOrganization>();
		} else {
			mGeneralList.clear();
		}

		if (mDataList != null && mDataList.size() > 0) {
			if (favs == null || favs.length == 0) {
				ChangeContactStatus(mDataList.get(0));
			} else {
				for(int i = 0; i < favs.length; i++){
					UserOrganization uo = getFrequentUserOrganization(favs[i].getUserName(), mDataList.get(0));
					if (uo != null) {
						mGeneralList.add(uo);
					}
				}
			}
		}

        if (mListener != null) {
            mListener.onDataUpdate();
        }
	}
	
	public void setOnlineUsers (OnlineUser[] onlineUsers) {
		if (onlineUsers == null){
			onlineUsers = new OnlineUser[0];
		}
		this.onlineUsers = onlineUsers;

		if (mDataList != null && mDataList.size() > 0) {
			updateUserOranizationState(mDataList.get(0));

			syncGeneralContacts();

			syncOrderedContacts();
		}
		
		if (mListener != null) {
			mListener.onDataUpdate();
		}
	}
	
	public void setaddressData(Organization[] organizations, UserInfo[] userInfos) {
		filtUserInfos(userInfos);
		
		this.organizations = organizations.clone();
		this.userInfos = userInfos.clone();
		
		saveGeneralRoot(organizations, userInfos);

		if (mUserOrgList == null) {
			mUserOrgList = new ArrayList<String>();
		} else {
			mUserOrgList.clear();
		}
		
		GetUserOrg(mUserName);

		ParseOrganization();
		
		syncOrderedContacts();
	}
	
	public RootOrganization getAddressData () {
		if (mDataList == null || mDataList.size() <= 0) {
			return null;
		}
		
		return mDataList.get(0);
	}
	
	public ArrayList<UserOrganization> getGeneralContacts () {
		return mGeneralList;
	}

	public String transformName(String username){
		String displayName = "";
		for(UserInfo userInfo : userInfos){
			if(userInfo.getUserName().equalsIgnoreCase(username)){
				displayName = userInfo.getDisplayName();
			}
		}
		return displayName;
	}
	
	public ArrayList<UserOrganization> getOrderedContacts () {
		return mOrderedList;
	}

	public void resetData () {
		if (mDataList == null || mDataList.size() <= 0) {
			return;
		}
		
		resetSelectedStatus(mDataList.get(0));
	}
	
	private void filtUserInfos(UserInfo[] userInfos){
		if (userInfos == null || userInfos.length <= 0) {
			return;
		}
		
		for (int i = 0; i < userInfos.length; i++) {
			if (userInfos[i].getOrganizations().length <=1) {
				continue;
			}
			
			ArrayList<String> ogIds = new ArrayList<String>();
			k:for (String id : userInfos[i].getOrganizations()) {
				for (String id2:ogIds) {
					if (id2.equals(id) == true) {
						break k;
					}
				}
				ogIds.add(id);
			}
			
			userInfos[i].setOrganizations(ogIds.toArray(new String[0]));
		}
	}
	
	private void updateUserOranizationState (RootOrganization rootOrganization) {
		if (rootOrganization == null) {
			return;
		}
		
		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				rootOrganization.getUserOrganizations().get(i).setStatus("offline");
				for (OnlineUser ou : onlineUsers) {
					if (ou.getUserName().equals(rootOrganization.getUserOrganizations().get(i).getUserName()) == true) {
						rootOrganization.getUserOrganizations().get(i).setStatus(ou.getStatus());
					}
				}
			}
		}
		
		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				updateUserOranizationState(rootOrganization.getRootOrganizations().get(i));
			}
		}
	}
	
	private void loadGeneralRoot(){
		String OrganizationOjson = "";
		String UserOjson = "";
		try{
			String dir = Environment.getExternalStorageDirectory().getPath() + "/HSAddresSDK/" + context.getPackageName() + "/Contact";
			OrganizationOjson = WAFile.readString(dir+"/Organization.json");
			UserOjson = WAFile.readString(dir +"/User.json");
		}catch(Throwable e){
			Log.e(TAG, "read json Throwable: " + e.toString());
		}

		Organization[] o = null;
		UserInfo[] u = null;
		try{
			o = new Gson().fromJson(OrganizationOjson, Organization[].class);
			u = new Gson().fromJson(UserOjson, UserInfo[].class);
		}catch(Exception e){
			Log.e(TAG, "parse json Throwable: " + e.toString());
		}

		if (o == null) {
			o = new Organization[0];
		}

		if (u == null) {
			u = new UserInfo[0];
		}
		setaddressData(o,u);
	}

	private void saveGeneralRoot(Organization[] organizations, UserInfo[] userInfos){
		try{
			String dir = Environment.getExternalStorageDirectory().getPath() + "/HSSDK/" + context.getPackageName() + "/Contact";

			String OrganizationString = new Gson().toJson(organizations);
			WAFile.write(dir + "/Organization.json", false, OrganizationString);
			
			String UserString = new Gson().toJson(userInfos);
			WAFile.write(dir +  "/User.json", false, UserString);
		}catch(Throwable e){
			Log.e(TAG, "saveGeneralRoot Throwable: " + e.toString());
		}
	}
	
	private void GetUserOrg(String username) {
		String orgFirst = "";
		for (int i = 0; i < userInfos.length; i++) {
			if (userInfos[i].getUserName().equals(username) == true) {
				if (userInfos[i].getOrganizations() != null 
						&& userInfos[i].getOrganizations().length > 0) {
					orgFirst = userInfos[i].getOrganizations()[0];
//					mUserOrgList.add(orgFirst);
					GetOrganization(organizations, orgFirst);
				}
				break;
			}
		}
	}
	
	private void GetOrganization(Organization[] list, String orgId) {
		for (Organization org : list) {
			if (org.getOrganizationId().equals(orgId) == true) {
				mUserOrgList.add(0, org.getOrganizationId());
				GetOrganization(organizations, org.getParent());
			}
		}
	}
	
	private void syncGeneralContacts () {

		if (mDataList == null || mDataList.size() <= 0) {
			return;
		}

		if(mGeneralList != null){
			resetFUserStatus(mGeneralList , mDataList.get(0));
		}
	}
	
	private void syncOrderedContacts () {
		if (mDataList == null || mDataList.size() <= 0) {
			return;
		}
		
		if (mOrderedList == null) {
			mOrderedList = new ArrayList<UserOrganization>();
		} else {
			mOrderedList.clear();
		}
		
		getUserOrganization(mOrderedList, mDataList.get(0));
	}
	
	private  UserOrganization getUserOrganization (String userName, RootOrganization rootOrganization) {
		if (userName == null || userName.equals("")) {
			return null;
		}
		if (rootOrganization == null) {
			return null;
		}
		
		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				if (userName.equals(rootOrganization.getUserOrganizations().get(i).getUserName()) == true) {
					return rootOrganization.getUserOrganizations().get(i);
				}
			}
		}
		
		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				UserOrganization uo = getUserOrganization(userName, rootOrganization.getRootOrganizations().get(i));
				if (uo != null) {
					return uo;
				}
			}
		}
		
		return null;
	}
	
	private UserOrganization getFrequentUserOrganization (String userName, RootOrganization rootOrganization) {
		if (userName == null || userName.equals("")) {
			return null;
		}
		if (rootOrganization == null) {
			return null;
		}
		
		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				if (userName.equals(rootOrganization.getUserOrganizations().get(i).getUserName()) == true) {
					rootOrganization.getUserOrganizations().get(i).setCollect(true);
					return rootOrganization.getUserOrganizations().get(i);
				}
			}
		}
		
		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				UserOrganization uo = getFrequentUserOrganization(userName, rootOrganization.getRootOrganizations().get(i));
				if (uo != null) {
					return uo;
				}
			}
		}
		
		return null;
	}
	
	private void getUserOrganization (ArrayList<UserOrganization> list, RootOrganization rootOrganization) {
		if (rootOrganization == null) {
			return;
		}
		
		if (rootOrganization.getUserOrganizations() != null) {
			k:for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getUserName().equals(rootOrganization.getUserOrganizations().get(i).getUserName()) == true) {
						continue k;
					}
				}
				list.add(rootOrganization.getUserOrganizations().get(i));
			}
		}
		
		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				getUserOrganization(list, rootOrganization.getRootOrganizations().get(i));
			}
		}
	}

	private void resetFUserStatus (ArrayList<UserOrganization> list, RootOrganization rootOrganization) {
		if (rootOrganization == null) {
			return;
		}

		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getUserName().equals(rootOrganization.getUserOrganizations().get(i).getUserName()) == true) {
						list.get(j).setStatus(rootOrganization.getUserOrganizations().get(i).getStatus());
					}
				}
			}
		}

		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				resetFUserStatus(list, rootOrganization.getRootOrganizations().get(i));
			}
		}
	}
	
	private void resetSelectedStatus (RootOrganization rootOrganization) {
		if (rootOrganization == null) {
			return;
		}
		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				rootOrganization.getUserOrganizations().get(i).setSelect(false);
			}
		}
		
		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				resetSelectedStatus(rootOrganization.getRootOrganizations().get(i));
			}
		}
	}
	
	private void ParseOrganization(){
		 if(mDataList == null){
			 mDataList = new ArrayList<RootOrganization>();
		 }else {
			 mDataList.clear();
		 }
		 
		 for(Organization organization : organizations) {
			 if(organization != null && (organization.getParent() == null || organization.getParent().equals("null"))){
				 RootOrganization ROrganization = new RootOrganization();
				 ROrganization.setOrganizationId(organization.getOrganizationId());
				 ROrganization.setOrganizationName(organization.getOrganizationName());
				 
				 itOrganizations(organizations, ROrganization);

				 if (ROrganization.getRootOrganizations().size() == 1) {
					 mDataList.add(ROrganization.getRootOrganizations().get(0));
				 } else {
					 mDataList.add(ROrganization);
				 }
			 }
		 }
		 
		 for(UserInfo userInfo:userInfos){
			  if(userInfo==null || userInfo.getOrganizations() == null || userInfo.getOrganizations().length <= 0){
			    continue;
			  }
			  
			  for (String orgId:userInfo.getOrganizations() ) {
				  for(RootOrganization rootOrganization : mDataList){
					  itUsers(orgId, userInfo, rootOrganization);
				  }
			  }
	      }
	 }
	
	private  void itOrganizations(Organization[] organizations, RootOrganization rootOrganization){
		 for(Organization organizations1 : organizations){
			 if(rootOrganization.getOrganizationId().equals(organizations1.getParent())){
				 RootOrganization ro = new RootOrganization();
				 ro.setOrganizationId(organizations1.getOrganizationId());
				 ro.setOrganizationName(organizations1.getOrganizationName());
				 if (mUserOrgList.size() >= 2 && organizations1.getOrganizationId().equals(mUserOrgList.get(1)) == true) {
					 rootOrganization.getRootOrganizations().add(0, ro);
				 } else {
					 rootOrganization.getRootOrganizations().add(ro);
				 }
				itOrganizations(organizations, ro);
			 }
		 }
	}
	
	private void itUsers(String orgId, UserInfo userInfo, RootOrganization rootOrganization){

		if(rootOrganization.getOrganizationId().equals(orgId)){
			UserOrganization userOrganization = new UserOrganization();
			userOrganization.setUserName(userInfo.getUserName());
			userOrganization.setDisplayName(userInfo.getDisplayName());
			userOrganization.setFirstWord(userInfo.getFirstWord());
			userOrganization.setMobilePhone(userInfo.getMobilePhone());
			userOrganization.setCollect(false);
			userOrganization.setSelect(false);
			userOrganization.setStatus("offline");
			PCStateSessionType pcStateSessionType = new PCStateSessionType();
			pcStateSessionType.setStatus("offline");
			userOrganization.setPCStateSessionType(pcStateSessionType);
			MobileStateSessionType mobileStateSessionType = new MobileStateSessionType();
			mobileStateSessionType.setStatus("offline");
			userOrganization.setMobileStateSessionType(mobileStateSessionType);
			ContentOnlyStateSessionType contentOnlyStateSessionType = new ContentOnlyStateSessionType();
			contentOnlyStateSessionType.setStatus("offline");
			userOrganization.setContentOnlyStateSessionType(contentOnlyStateSessionType);
			userOrganization.setOrganizationObjects(userInfo.getOrganizationObjects());
			
			ArrayList<String> departments = new ArrayList<String>();
			if (userInfo.getOrganizations() != null) {
				for (String id : userInfo.getOrganizations()) {
					String dep = getUserOranizations(id);
					if (dep.equals("") == false) {
						departments.add(dep);
					}
				}
			}

			userOrganization.setDepartment(departments.toArray(new String[0]));
			sortUsers(orgId, rootOrganization.getUserOrganizations(), userOrganization);
//			rootOrganization.getUserOrganizations().add(userOrganization);
		}else {
			if(rootOrganization.getRootOrganizations() == null || rootOrganization.getRootOrganizations().size() <= 0){
				return;
			}
			for(RootOrganization rootOrganization1:rootOrganization.getRootOrganizations()){
				itUsers(orgId, userInfo, rootOrganization1);
			}
		}
	}

	public String getUserStatus(String username){
		if(username == null){
			return null;
		}
		if (mDataList == null || mDataList.size() <= 0) {
			return null;
		}
		
		UserOrganization  uo = new UserOrganization();
		uo = getUserOrganization(username,mDataList.get(0));
		
		if (uo == null) {
			return null;
		}

		try {
			if(uo.getPCStateSessionType().getStatus().equalsIgnoreCase("offline")&&uo.getMobileStateSessionType().getStatus().equalsIgnoreCase("offline")&&
				uo.getContentOnlyStateSessionType().getStatus().equalsIgnoreCase("offline")){
			return "offline";
		}else {
			if(uo.getPCStateSessionType().getStatus().equalsIgnoreCase("busy") || uo.getMobileStateSessionType().getStatus().equalsIgnoreCase("busy") || uo.getContentOnlyStateSessionType().getStatus().equalsIgnoreCase("busy")){
				return "busy";
			}else {
				return "online";
			}
		}
		} catch (Throwable e) {
			return null;
		}

	}

	private void sortUsers (String orgId, ArrayList<UserOrganization> list, UserOrganization uo) {
		int sortNum = getSortNUm(orgId, uo);
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			int order = getSortNUm(orgId, list.get(i));
			if (sortNum < order) {
				index = i;
				break;
			} else {
				index = -1;
				continue;
			}
		}

		if (index == -1) {
			list.add(uo);
		} else {
			list.add(index,uo);
		}
	}

	private int getSortNUm (String orgId, UserOrganization uo) {
		int num = 0;
		if (uo.getOrganizationObjects() != null) {
			for (OrganizationObject oo : uo.getOrganizationObjects()) {
				if (oo == null) {
					continue;
				}
				if (oo.getOrgId().equals(orgId) == true) {
					num = oo.getSortNumber();
					break;
				}
			}
		}
		return num;
	}
	
	private String getUserOranizations (String childOrgId) {
		if (childOrgId == null || childOrgId.equals("")) {
			return "";
		}
		
		String dep = "";
		try {
			for (Organization org : organizations) {
				if (childOrgId.equals(org.getOrganizationId()) == true) {
					dep += org.getOrganizationName();
					if (org.getParent() != null && org.getParent().equals("") == false) {
						dep += "," + getUserOranizations(org.getParent());
					}
					break;
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, "getUserOranizations Throwable: " + e.toString());
		}
		return dep;
	}

	public ArrayList<UserOrganization> getGroupType(Group group){
		if(group == null){
			return null;
		}

		if (mGroupList == null) {
			mGroupList = new ArrayList<UserOrganization>();
		} else {
			mGroupList.clear();
		}

		for(int i=0;i<group.getMembers().length;i++){
			if(getUserOrganization((group.getMembers())[i],mDataList.get(0))!=null){
				mGroupList.add(getUserOrganization((group.getMembers())[i],mDataList.get(0)));
			}
		}
		return mGroupList;
	}

	public Group syncGroup(Group group){
		if(group == null){
			return null;
		}

		for(int i=0;i<mGpList.size();i++){
			if(mGpList.get(i).getName().equals(group.getName())){
				group = mGpList.get(i);
			}
		}

		return group;
	}

	private  void  ChangeContactStatus(RootOrganization rootOrganization){
		if (rootOrganization == null) {
			return;
		}
		if (rootOrganization.getUserOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getUserOrganizations().size(); i++) {
				rootOrganization.getUserOrganizations().get(i).setCollect(false);
			}
		}

		if (rootOrganization.getRootOrganizations() != null) {
			for (int i = 0; i < rootOrganization.getRootOrganizations().size(); i++) {
				ChangeContactStatus( rootOrganization.getRootOrganizations().get(i));
			}
		}
	}

}
