package com.shgbit.android.hsaddress.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.adapter.AddressListAdapter;
import com.shgbit.android.hsaddress.adapter.CharacterParser;
import com.shgbit.android.hsaddress.adapter.FrequentlyContactAdapter;
import com.shgbit.android.hsaddress.adapter.GroupAdapter;
import com.shgbit.android.hsaddress.adapter.HorizontalAdapter;
import com.shgbit.android.hsaddress.adapter.HorizontalListView;
import com.shgbit.android.hsaddress.adapter.OrganizationStructureAdapter;
import com.shgbit.android.hsaddress.adapter.SideBar;
import com.shgbit.android.hsaddress.bean.AddToGroupInfo;
import com.shgbit.android.hsaddress.bean.CreateGroupInfo;
import com.shgbit.android.hsaddress.bean.DeleteGroupInfo;
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.QueryGroupInfo;
import com.shgbit.android.hsaddress.bean.RootOrganization;
import com.shgbit.android.hsaddress.bean.UpdateGroupInfo;
import com.shgbit.android.hsaddress.bean.User;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.callback.ExternalCallBack;
import com.shgbit.android.hsaddress.callback.InternalCallBack;
import com.shgbit.android.hsaddress.sdk.Common;
import com.shgbit.android.hsaddress.sdk.StructureDataCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllAddressListFragment extends Fragment {
	
	private final String Tag = "AllAddressListFragment";
	private boolean normal;
    private LinearLayout mLlytTital;
	private ImageView mImgStatus;
	private TextView mmy_groups;
	private TextView maddress_list;
	private TextView morganization_structure;
	
    private LinearLayout mParentLayout;
	private LinearLayout mHorizontalLayout;

	
	private HorizontalListView mHorizontalListView;
	private ListView mListView;
	
//	private ListView mContactListView;
	private TextView mTitalName;
	private TextView mTitalDeparment;
	private TextView mTitalStatus;
	
//	private ListView mAddressListView;
	private SideBar mSideBar;
	
	private TextView mButtonCancel;
	private TextView mButtonConferencing;
	
	private TextView mMeetingSelectUser;
	private TextView mMeetingSelected_title;
	
	private TextView mHorizontalTitalTextView;
	private ImageView mHorizontalView;
	
	private EditText mEditText;
	
	private TextView mDialog;
	private String screen_status;
	private int index;
	private int page;
	private boolean Refresh = true;
    private boolean mIsReserve = false;
    
    private List<RootOrganization> mRData;
    private List<UserOrganization> mUData;
    private RootOrganization mRootOrganization;
	private List<Object> mObjects;
	private List<UserOrganization> mContactUserOrganization;
	private UserOrganization mContactUOrganization;
	private List<UserOrganization> mAddressUserOrganization;
	private List<UserOrganization> filterDateList;
    
    private HorizontalAdapter mHorizontalListViewAdapter;
    private AddressListAdapter mAddressListAdapter;
    private FrequentlyContactAdapter mFrequentlyContactAdapter;
    private FrequentlyContactAdapter mEditAdapter;
	private GroupAdapter mGroupAdapter;
    private OrganizationStructureAdapter mOrganizationStructureAdapter;
	private ArrayList<UserOrganization> list ;
	private ArrayList<User> mReserverUser;
	private int AllUser = 0;
	private int SelectUser = 0;
	private PopupWindow mPopupWindow;
    
    private final int MYGROUPS = 0x001;
    private final int ADDRESSLIST = 0x002;
    private final int ORGANIZATIONSTRUCTURE = 0x003;
    private final int SEARCHBAR = 0x004;

	private LinearLayout mCreateGroup;
	private TextView mCreateText;
	private Dialog dialog;
	private QueryGroupInfo queryGroupInfo;
	private ArrayList<Group> mGroup;
	private CreateGroupInfo createGroupInfo;
	private DeleteGroupInfo deleteGroupInfo;
	private UpdateGroupInfo updateGroupInfo;
	private Group mAddGroup;
	private AddToGroupInfo addToGroupInfo;
	private GroupFragment groupFragment;
	private InternalCallBack mInCallBack;
	private ExternalCallBack mExCallBack;
	private String SystemName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v = null;
		if(normal == false){
		    v = inflater.inflate(R.layout.fragment_addresslist, null);
		}else{
			if (screen_status == null) {
				screen_status = "vertical";
			}
			if(screen_status.equals("horizontal")){
				 v = inflater.inflate(R.layout.fragment_horizontal_meetingaddresslist, null);
			}else{
				 v = inflater.inflate(R.layout.fragment_meetingaddresslist, null);
			}
		}
		initView(v);
		setStatus();
		StructureDataCollector.getInstance().setDataUpdateListener(mDataUpdateListener);

//		queryGroupInfo = new QueryGroupInfo();
//		ServerInteractManager.getInstance().queryGroup(queryGroupInfo);
//		ServerInteractManager.getInstance().getContactUser();

		updateData();
		return v;
	}

   public StructureDataCollector.DataUpdateListener mDataUpdateListener = new StructureDataCollector.DataUpdateListener() {

		@Override
		public void onDataUpdate() {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(MESSAGE);
			}
		}
	};

	private final int MESSAGE = 0x005;
	private Handler mHandler = new Handler(){

		@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE:
			updateData();
			break;
		default:
			break;
		}
		super.handleMessage(msg);
	}};
	@Override
	public void onDetach() {
		StructureDataCollector.getInstance().resetData();
		if (mHandler != null) {
			mHandler.removeMessages(MESSAGE);
			mHandler = null;
		}
		super.onDetach();
	}

	public void setListener(){
		StructureDataCollector.getInstance().setDataUpdateListener(mDataUpdateListener);
		updateData();
	}
	public void setExCallBack(ExternalCallBack mExCallBack){
		this.mExCallBack = mExCallBack;
	}
	public void setInCallBack(InternalCallBack mInCallBack){
		this.mInCallBack = mInCallBack;
	}

	public void setLoginName(String name){
		SystemName = name;
	}

	private void updateData() {
		Log.i(Tag, "updateData start");
		if(index == MYGROUPS){
			mContactUserOrganization = StructureDataCollector.getInstance().getGeneralContacts();
			Log.i(Tag,"##########"+mContactUserOrganization.size());
			mGroup = StructureDataCollector.getInstance().getGpList();

			mHorizontalLayout.setVisibility(View.GONE);
			mSideBar.setVisibility(View.GONE);
			if(normal == false){
				mCreateGroup.setVisibility(View.VISIBLE);
			}else {
				mCreateGroup.setVisibility(View.GONE);
			}

			syncSelectedUser(mContactUserOrganization);

			updateContactData();

		}else if(index == ADDRESSLIST){
			mAddressUserOrganization = StructureDataCollector.getInstance().getOrderedContacts();

			mHorizontalLayout.setVisibility(View.GONE);
			mCreateGroup.setVisibility(View.GONE);
			
			if(screen_status.equals("vertical") && mAddressUserOrganization != null && mAddressUserOrganization.size() != 0){
				mSideBar.setVisibility(View.VISIBLE);
			}else {
				mSideBar.setVisibility(View.GONE);
				mDialog.setVisibility(View.GONE);
			}

			syncSelectedUser(mAddressUserOrganization);

			updateAddressData();

		}else if(index == ORGANIZATIONSTRUCTURE){
			mRootOrganization = StructureDataCollector.getInstance().getAddressData();
			mSideBar.setVisibility(View.GONE);
			mHorizontalLayout.setVisibility(View.VISIBLE);
			mCreateGroup.setVisibility(View.GONE);

			syncData();

			updateOrganizationData();

			updateUserData();

		}else if(index==SEARCHBAR){
			filterDateList = searchFirstWord(StructureDataCollector.getInstance().getOrderedContacts(),mEditText.getText().toString());

			mCreateGroup.setVisibility(View.GONE);
			mHorizontalLayout.setVisibility(View.GONE);
			mSideBar.setVisibility(View.GONE);

			filterData();

			syncSelectedUser(filterDateList);
		}
	}

	private void initView(View v) {
		if(normal == false){
			mmy_groups=(TextView)v.findViewById(R.id.my_groups);
			maddress_list=(TextView)v.findViewById(R.id.address_list);
			morganization_structure=(TextView)v.findViewById(R.id.organization_structure);

			mParentLayout=(LinearLayout)v.findViewById(R.id.parent_layout);
			mHorizontalLayout=(LinearLayout)v.findViewById(R.id.HorizontalListView_layout);

			mListView=(ListView)v.findViewById(R.id.organization_listview);
			mHorizontalListView=(HorizontalListView)v.findViewById(R.id.recycle);
			mCreateGroup = (LinearLayout)v.findViewById(R.id.btn_creategroup);
			mCreateText = (TextView)v.findViewById(R.id.text_create);

			mSideBar=(SideBar)v.findViewById(R.id.address_sidrbar);

			mEditText=(EditText)v.findViewById(R.id.filter_edit);
			mDialog=(TextView)v.findViewById(R.id.dialog);
			mLlytTital = (LinearLayout) v.findViewById(R.id.llyt_tital_frag_address);
			mImgStatus = (ImageView) v.findViewById(R.id.img_status_frag_address);
//			mImgStatus.setOnClickListener(mClickListener);

			mmy_groups.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
			mCreateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
  		    maddress_list.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
			morganization_structure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		}else{
			if(screen_status.equals("vertical")){
				mButtonCancel=(TextView)v.findViewById(R.id.button_cancel);
				mButtonConferencing=(TextView)v.findViewById(R.id.button_conferencing);
				mmy_groups=(TextView)v.findViewById(R.id.meeting_my_groups);
				morganization_structure=(TextView)v.findViewById(R.id.meeting_organization_structure);
				maddress_list=(TextView)v.findViewById(R.id.meeting_address_list);

				mParentLayout=(LinearLayout)v.findViewById(R.id.meeting_parent_layout);
				mHorizontalLayout=(LinearLayout)v.findViewById(R.id.Meeting_HorizontalListView_layout);
				mCreateGroup = (LinearLayout)v.findViewById(R.id.btn_meeting_creategroup);
				mCreateText = (TextView)v.findViewById(R.id.text_create_meeting);
				mSideBar=(SideBar)v.findViewById(R.id.meeting_sidrbar);

				mHorizontalListView=(HorizontalListView)v.findViewById(R.id.meeting_recycle);
				mListView=(ListView)v.findViewById(R.id.meeting_listview);

				mMeetingSelectUser=(TextView)v.findViewById(R.id.selected_person);
				mMeetingSelected_title=(TextView)v.findViewById(R.id.selected_title);

		        mMeetingSelected_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/35);

		        mEditText=(EditText)v.findViewById(R.id.meeting_filter_edit);

		        mDialog=(TextView)v.findViewById(R.id.meeting_dialog);

				mmy_groups.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
				mCreateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
				maddress_list.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
				morganization_structure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
			}else{
				mButtonCancel=(TextView)v.findViewById(R.id.horizontal_button_cancel);
				mButtonConferencing=(TextView)v.findViewById(R.id.horizontal_button_conferencing);
				mmy_groups=(TextView)v.findViewById(R.id.horizontal_meeting_my_groups);
				morganization_structure=(TextView)v.findViewById(R.id.horizontal_meeting_organization_structure);
				maddress_list=(TextView)v.findViewById(R.id.horizontal_meeting_address_list);

				mParentLayout=(LinearLayout)v.findViewById(R.id.horizontalmeeting_parent_layout);
				mHorizontalLayout=(LinearLayout)v.findViewById(R.id.HorizontalListMeeting_HorizontalListView_layout);

				mHorizontalView=(ImageView)v.findViewById(R.id.horizontal_selected_title);
				mCreateGroup = (LinearLayout)v.findViewById(R.id.btn_hormeeting_creategroup);
				mCreateText = (TextView)v.findViewById(R.id.text_create_hormeeting);

				mSideBar=(SideBar)v.findViewById(R.id.horizontalconferencing_sidrbar);

				mHorizontalListView=(HorizontalListView)v.findViewById(R.id.horizontalconferencing_HorizontalListView);
				mListView=(ListView)v.findViewById(R.id.horizontalconferencing_listview);

				mMeetingSelectUser=(TextView)v.findViewById(R.id.horizontal_selected_person);

				mEditText=(EditText)v.findViewById(R.id.horizontalmeeting_filter_edit);

				mDialog=(TextView)v.findViewById(R.id.horizontalmeeting_dialog);

				mmy_groups.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
				mCreateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
				maddress_list.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
				Log.i(Tag,"Common.SCREENWIDTH:"+Common.SCREENWIDTH);
				morganization_structure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
			}
			mButtonCancel.setOnClickListener(mClickListener);
			mButtonConferencing.setOnClickListener(mClickListener);

			mButtonCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/42);
			mButtonConferencing.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/42);
	        mMeetingSelectUser.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		}
		mmy_groups.setText(getActivity().getResources().getString(R.string.tital_contact));
		maddress_list.setText(getActivity().getResources().getString(R.string.tital_addresslist));
		morganization_structure.setText(getActivity().getResources().getString(R.string.tital_organization));
//		maddress_list.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.mScreenHeight/40);
//		morganization_structure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.mScreenHeight/40);

		morganization_structure.setBackgroundResource(R.drawable.zuzhi);
		morganization_structure.setTextColor(getActivity().getResources().getColor(R.color.blue_color));

		mHorizontalLayout.setVisibility(View.VISIBLE);
		mSideBar.setVisibility(View.GONE);
		index=ORGANIZATIONSTRUCTURE;
		page=ORGANIZATIONSTRUCTURE;

		mmy_groups.setOnClickListener(mClickListener);
		maddress_list.setOnClickListener(mClickListener);
		morganization_structure.setOnClickListener(mClickListener);
		mParentLayout.setOnClickListener(mClickListener);
		mCreateGroup.setOnClickListener(mClickListener);

		mListView.setOnItemClickListener(mItemClickListener);
		mListView.setOnItemLongClickListener(mItemLongClickListener);
		mHorizontalListView.setOnItemClickListener(mItemClickListener);

		mSideBar.setOnTouchingLetterChangedListener(onTouchingLetterChangedListener);
		mEditText.addTextChangedListener(tWatcher);

		mSideBar.setTextView(mDialog);
	}

	public void setStatus() {
		if (mImgStatus != null) {
	    	if (Common.mMyStatus == Common.STATUS_ONLINE) {
	    		mImgStatus.setImageResource(R.drawable.btn_online);
			} else if (Common.mMyStatus == Common.STATUS_BUSY) {
				mImgStatus.setImageResource(R.drawable.btn_busy);
			}
		}
	}

    private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
	        imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
			if(v.getId()== R.id.my_groups || v.getId()== R.id.meeting_my_groups || v.getId()== R.id.horizontal_meeting_my_groups){
				Log.i(Tag, "[user operation]: click mmy_groups");
				mmy_groups.setBackgroundResource(R.drawable.chang);
				mmy_groups.setTextColor(getActivity().getResources().getColor(R.color.blue_color));
				maddress_list.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				morganization_structure.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				maddress_list.setBackgroundResource(0);
				morganization_structure.setBackgroundResource(0);
				mCreateGroup.setVisibility(View.VISIBLE);
				mEditText.setText("");
				index=MYGROUPS;
				page=MYGROUPS;
				updateData();
				mGroupAdapter = new GroupAdapter(getActivity(),mGroup,mContactUserOrganization,normal,mUData);
				mListView.setAdapter(mGroupAdapter);
				mGroupAdapter.setOnItemSelectClickListener(new GroupAdapter.onItemSelectListener() {
					@Override
					public void onSelectClick(List<UserOrganization> mUserList) {
						SelectAllGroup(mUserList);
						mGroupAdapter.notifyDataSetChanged();
					}
				});
			}else if(v.getId()== R.id.address_list || v.getId()== R.id.meeting_address_list || v.getId()== R.id.horizontal_meeting_address_list){
				Log.i(Tag, "[user operation]: click maddress_list");
				maddress_list.setBackgroundResource(R.drawable.tongxun);
				maddress_list.setTextColor(getActivity().getResources().getColor(R.color.blue_color));
				morganization_structure.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				mmy_groups.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				morganization_structure.setBackgroundResource(0);
				mmy_groups.setBackgroundResource(0);
				mSideBar.setVisibility(View.VISIBLE);
				mEditText.setText("");
				index=ADDRESSLIST;
				page=ADDRESSLIST;
				updateData();
				mAddressListAdapter = new AddressListAdapter(getActivity(), mAddressUserOrganization, normal,screen_status);
				mListView.setAdapter(mAddressListAdapter);
			}else if(v.getId()== R.id.organization_structure || v.getId()== R.id.meeting_organization_structure || v.getId()== R.id.horizontal_meeting_organization_structure) {
				Log.i(Tag, "[user operation]: click morganization_structure");
				morganization_structure.setBackgroundResource(R.drawable.zuzhi);
				morganization_structure.setTextColor(getActivity().getResources().getColor(R.color.blue_color));
				maddress_list.setBackgroundResource(0);
				maddress_list.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				mmy_groups.setBackgroundResource(0);
				mmy_groups.setTextColor(getActivity().getResources().getColor(R.color.white_color));
				mEditText.setText("");
				index=ORGANIZATIONSTRUCTURE;
				page=ORGANIZATIONSTRUCTURE;
				updateData();
				mOrganizationStructureAdapter = new OrganizationStructureAdapter(getActivity(), mObjects,normal,screen_status,SystemName);
				mListView.setAdapter(mOrganizationStructureAdapter);
				mOrganizationStructureAdapter.setOnItemSelectClickListener(new OrganizationStructureAdapter.onItemSelectListener() {

					@Override
					public void onSelectClick(RootOrganization mRoot) {
						list = new ArrayList<>();
						SyncSelectUser(mRoot);
						mOrganizationStructureAdapter.notifyDataSetChanged();
					}
				});
			}else if(v.getId()== R.id.parent_layout || v.getId()== R.id.meeting_parent_layout || v.getId()== R.id.horizontalmeeting_parent_layout) {

			}else if(v.getId()== R.id.button_cancel || v.getId()== R.id.horizontal_button_cancel ) {
				Log.i(Tag, "[user operation]: click mButtonCancel");
				mExCallBack.onDesFragment();
				getActivity().getSupportFragmentManager().popBackStack();

//				if(mExCallBack instanceof MainCallBack){
//					Log.i(Tag, "#####cancel1");
//					Syntony.getInstance().des4();
//					Log.i(Tag, "#####cancel2");
//				}
			}else if(v.getId()== R.id.button_conferencing || v.getId()== R.id.horizontal_button_conferencing ) {
				Log.i(Tag, "[user operation]: click mButtonConferencing");
				if(mUData == null || mUData.size() <= 0){
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_add), Toast.LENGTH_SHORT).show();
					return;
				}

				User[] users = new User[mUData.size()];
				for(int i = 0; i < mUData.size(); i++){
					users[i] = new User();
					users[i].setUserName(mUData.get(i).getUserName());
					users[i].setDisplayName(mUData.get(i).getDisplayName());
					users[i].setStatus(mUData.get(i).getStatus());
				}

				String[] GroupUsers = new String[mUData.size()];
				for(int i = 0; i < mUData.size(); i++){
					GroupUsers[i] = new String();
					GroupUsers[i] = mUData.get(i).getUserName();
				}

				if (mIsReserve) {
					mExCallBack.onReserveUsers(users);
					getActivity().getSupportFragmentManager().popBackStack();
				} else {
					boolean isPersonal = false;
					if(mAddGroup == null){
						mExCallBack.onInvitedUsers(users,isPersonal);
						mExCallBack.invite(users);
						mExCallBack.onDesFragment();
					}else {
						addToGroupInfo = new AddToGroupInfo();
						addToGroupInfo.setId(mAddGroup.get_id());
						addToGroupInfo.setMembers(GroupUsers);
						mExCallBack.onAddToGroup(addToGroupInfo);
					}
				}
              getActivity().getSupportFragmentManager().popBackStack();
			}else if(v.getId()== R.id.img_status_frag_address){
				Log.i(Tag, "[user operation]: click status");
//				if (isVideoCallBack == false) {
//					((MianCallBack)getActivity()).onSwitchStatus(mLlytTital);
//				}
			}else if(v.getId()== R.id.btn_creategroup){
				showDialog("create",null);
			}else if(v.getId()== R.id.btn_meeting_creategroup || v.getId()== R.id.btn_hormeeting_creategroup){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_41), Toast.LENGTH_SHORT).show();
			}
		}
   };
   private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
	   @Override
	   public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                      long id) {
		   if (index==MYGROUPS) {
               if(position != 0){
				   showPopWindow(mGroup.get(position-1));
               }
		   }
		   return true;
	   }
   };

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
	    	InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			// 得到InputMethodManager的实例
			imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
			if(parent.getId()== R.id.recycle || parent.getId()== R.id.meeting_recycle || parent.getId()== R.id.horizontalconferencing_HorizontalListView){
				Log.i(Tag, "[user operation]: click HorizontalListView");
				for (int i = mRData.size() - 1; i > position; i--) {
					mRData.remove(i);
				}
				if (mHorizontalListViewAdapter == null) {
					mHorizontalListViewAdapter = new HorizontalAdapter(getActivity(), mRData);
					mHorizontalListView.setAdapter(mHorizontalListViewAdapter);
				} else {
					mHorizontalListViewAdapter.update(mRData);
				}

				mHorizontalListView.setSelection(mRData.size()-1);

				updateUserData();
			}else if(parent.getId() == R.id.organization_listview || parent.getId() == R.id.meeting_listview || parent.getId() == R.id.horizontalconferencing_listview){
				Log.i(Tag, "[user operation]: click listview");
				if(index==ORGANIZATIONSTRUCTURE){
					if(mObjects.get(position) instanceof RootOrganization){
						mRData.add((RootOrganization) mObjects.get(position));

						if (mHorizontalListViewAdapter == null) {
							mHorizontalListViewAdapter = new HorizontalAdapter(getActivity(), mRData);
							mHorizontalListView.setAdapter(mHorizontalListViewAdapter);
						} else {
							mHorizontalListViewAdapter.update(mRData);
						}

						mHorizontalListView.setSelection(mRData.size()-1);
						Refresh=false;
						updateUserData();
					}else{
						if(normal==false){
							Log.i(Tag,"##########222222222222");
							mInCallBack.onPersonalAddressFragment((UserOrganization)mObjects.get(position));
							Log.i(Tag,"##########333333333333");
						}else {
							if (((UserOrganization)mObjects.get(position)).getUserName().equals(SystemName)) {
								Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_21), Toast.LENGTH_SHORT).show();
								return;
							}

							if (mUData == null) {
								mUData = new ArrayList<UserOrganization>();
							}

							if(mUData == null){
								mUData = new ArrayList<UserOrganization>();
								((UserOrganization)mObjects.get(position)).setSelect(true);
								mUData.add((UserOrganization)mObjects.get(position));
							}else{
								if (((UserOrganization)mObjects.get(position)).isSelect()) {
									((UserOrganization)mObjects.get(position)).setSelect(false);
									for (int i = 0; i < mUData.size(); i++) {
										if (((UserOrganization)mObjects.get(position)).getUserName().equals(mUData.get(i).getUserName()) == true) {
											mUData.remove(i);
											break;
										}
									}
								} else {
									((UserOrganization)mObjects.get(position)).setSelect(true);
									mUData.add((UserOrganization)mObjects.get(position));
								}

								mOrganizationStructureAdapter.notifyDataSetChanged();

								ShowmUData();
							}
						}
					}
				}
				if(index==ADDRESSLIST){
					ClickListView(mAddressUserOrganization, position);
				}
				if (index==MYGROUPS) {
					if(mContactUserOrganization != null){
						syncSelectedUser(mContactUserOrganization);
					}
					if(position ==0){
						mInCallBack.onGroupFragment(null,mContactUserOrganization,normal,mUData,screen_status);
					}else{
						mInCallBack.onGroupFragment(mGroup.get(position-1),null,normal,mUData,screen_status);
					}

//		    		ClickListView(mContactUserOrganization, position);
				}
				if(index == SEARCHBAR){
					ClickListView(filterDateList,position);
				}
			}
	    }
    };

    private void ClickListView(List<UserOrganization> mOrganization, int position){
    	if(normal == false){
			mInCallBack.onPersonalAddressFragment(mOrganization.get(position));
		}else{
			if (mOrganization.get(position).getUserName().equals(SystemName)) {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_21), Toast.LENGTH_SHORT).show();
				return;
			}

			if (mUData == null) {
				mUData = new ArrayList<UserOrganization>();
			}

			if(mUData == null){
				mUData = new ArrayList<UserOrganization>();
				mOrganization.get(position).setSelect(true);
				mUData.add(mOrganization.get(position));
			}else{
				if (mOrganization.get(position).isSelect()) {
					(mOrganization.get(position)).setSelect(false);
					for (int i = 0; i < mUData.size(); i++) {
						if (mOrganization.get(position).getUserName().equals(mUData.get(i).getUserName()) == true) {
							mUData.remove(i);
							break;
						}
					}
				} else {
					(mOrganization.get(position)).setSelect(true);
					mUData.add(mOrganization.get(position));
				}

				if(index ==ADDRESSLIST ){
					mAddressListAdapter.notifyDataSetChanged();
				}else if (index == SEARCHBAR) {
					mEditAdapter.notifyDataSetChanged();
				}

				ShowmUData();
			}
		}
    }
    private SideBar.OnTouchingLetterChangedListener onTouchingLetterChangedListener = new SideBar.OnTouchingLetterChangedListener(){

		@Override
		public void onTouchingLetterChanged(String s) {
			Log.i(Tag, "[user operation]: click OnTouchingLetterChanged");
			int position = mAddressListAdapter.getPositionForSection(s.charAt(0));
			if(position != -1){
				mListView.setSelection(position);
			}
		}
    };

    private TextWatcher tWatcher = new TextWatcher() {

    	@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
    		Log.i(Tag, "[user operation]: click EditText");
    		if(s.toString() == null || s.toString().equals("")){
    			InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
    	        imm1.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    			if(page ==MYGROUPS){
    				index=MYGROUPS;
    				updateData();
					mGroupAdapter = new GroupAdapter(getActivity(),mGroup,mContactUserOrganization,normal,mUData);
					mListView.setAdapter(mGroupAdapter);
	 			}
    			if(page == ADDRESSLIST){
    				index=ADDRESSLIST;
    				updateData();
    				mAddressListAdapter = new AddressListAdapter(getActivity(), mAddressUserOrganization, normal,screen_status);
    				mListView.setAdapter(mAddressListAdapter);
				}
    			if(page ==ORGANIZATIONSTRUCTURE){
    				index=ORGANIZATIONSTRUCTURE;
    				updateData();
    				mOrganizationStructureAdapter = new OrganizationStructureAdapter(getActivity(), mObjects,normal,screen_status,SystemName);
    				mListView.setAdapter(mOrganizationStructureAdapter);
				}
    		}else {
    			index = SEARCHBAR;

//				if(page ==MYGROUPS){
//					filterDateList=searchFirstWord(StructureDataCollector.getInstance().getGeneralContacts(),mEditText.getText().toString());
//				}else {
//					filterDateList=searchFirstWord(StructureDataCollector.getInstance().getOrderedContacts(),mEditText.getText().toString());
//				}
				filterDateList=searchFirstWord(StructureDataCollector.getInstance().getOrderedContacts(),mEditText.getText().toString());
    			mHorizontalLayout.setVisibility(View.GONE);
    			mSideBar.setVisibility(View.GONE);
				mCreateGroup.setVisibility(View.GONE);
				syncSelectedUser(filterDateList);
//    			if(filterDateList.size()>1){
//    				Collections.sort(filterDateList, new PinyinComparator2());
//    	 			Collections.sort(filterDateList, new StatusComparator());
//    	 	    }

     			mEditAdapter = new FrequentlyContactAdapter(getActivity(), filterDateList, normal,screen_status);
     	 		mListView.setAdapter(mEditAdapter);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private void filterData(){
// 		if(filterDateList.size()>1){
//	 			Collections.sort(filterDateList, new StatusComparator());
//	 	}

 		if(mEditAdapter == null){
 			mEditAdapter = new FrequentlyContactAdapter(getActivity(), filterDateList, normal,screen_status);
 	 		mListView.setAdapter(mEditAdapter);
 		}else {
 			mEditAdapter.update(filterDateList);
		}
	}

	private void syncSelectedUser(List<UserOrganization> mUo) {
		if (mUo == null) {
			return;
		}

		if (mUData == null) {
			mUData = new ArrayList<UserOrganization>();
			return;
		}

		for (int i = 0; i < mUData.size(); i++) {
			for (int j = 0; j < mUo.size(); j++) {
				itCUserganization(mUData.get(i).getUserName(), mUo.get(j));
			}
		}
	}

	private void itCUserganization(String userName, UserOrganization mOrganization) {
		if (mOrganization == null) {
	   		 return;
	   	 }
		 if (userName.equals(mOrganization.getUserName()) == true) {
   			 mOrganization.setSelect(true);
   		 }
	}

	private void updateContactData() {
		if(mGroup == null){
			mGroup = new ArrayList<>();
		}

		if(mContactUserOrganization == null){
			mContactUOrganization = new UserOrganization();
		}
		if(mGroupAdapter == null){
			mGroupAdapter = new GroupAdapter(getActivity(),mGroup,mContactUserOrganization,normal,mUData);
			mListView.setAdapter(mGroupAdapter);
		}else {
			mGroupAdapter.update(mGroup,mContactUserOrganization,mUData);
		}
	}

	private void SelectAllGroup(List<UserOrganization> mUserList){
		AllUser=0;
		SelectUser = 0;
		int dex = 0;

		if(mUserList == null){
			return;
		}
		AllUser = mUserList.size();
		for(UserOrganization uo:mUserList){
			if(uo.isSelect()==true){
				SelectUser++;
			}
		}

		if(SelectUser ==0 && AllUser ==0){
			dex = 0;
		}
		if(SelectUser == AllUser){
			dex = 1;
		}else {
			dex = 2;
		}

		if (dex ==0){
			return;
		}

		for(UserOrganization mUo:mUserList) {
			if (dex == 1) {
				mUo.setSelect(false);
			} else if (dex == 2) {
				mUo.setSelect(true);
			}
		}


		if(dex==2) {
			if (mUserList != null) {
				for (int i = 0; i < mUserList.size(); i++) {
					mUData.add(mUserList.get(i));
				}
			}

			for (int i = 0; i < mUData.size(); i++) {
				for (int j = mUData.size() - 1; j > i; j--) {
					if (mUData.get(i).getUserName().equals(mUData.get(j).getUserName())) {
						mUData.remove(j);
					}
				}
				if (mUData.get(i).getUserName().equals(SystemName)) {
					mUData.remove(i);
				}
			}
		}else {
			for(int i = 0; i<mUserList.size() ;i++){
				for(int j= 0;j<mUData.size();j++){
					if(mUData.get(j).getUserName().equals(mUserList.get(i).getUserName())) {
						mUData.remove(mUData.get(j));
					}
				}
			}
		}

		ShowmUData();
	}

	private void updateAddressData() {
		if(mAddressUserOrganization == null){
			mAddressUserOrganization = new ArrayList<>();
		}else {
			for(int i = 0 ; i < mAddressUserOrganization.size() ; i++){
				String pinyin = CharacterParser.getInstance().getSelling(mAddressUserOrganization.get(i).getDisplayName());
				String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				mAddressUserOrganization.get(i).setfWordString(sortString.toUpperCase());
			}else{
				mAddressUserOrganization.get(i).setfWordString("#");
			}
    	}
		Collections.sort(mAddressUserOrganization, new PinyinComparator());

		}
		if (mAddressListAdapter == null) {
		   mAddressListAdapter = new AddressListAdapter(getActivity(), mAddressUserOrganization, normal,screen_status);
		   mListView.setAdapter(mAddressListAdapter);
    	}else {
    	   mAddressListAdapter.update(mAddressUserOrganization);
    	}
	}

	public void setLayout(boolean ismeeting, String s, boolean isReserve, ArrayList<User> mRuser, Group group){
		normal=ismeeting;
		screen_status=s;
		mIsReserve=isReserve;
		mReserverUser = mRuser;
		mAddGroup = group;
	}

	public void setGroupSelect(){
		ShowmUData();
		if(mGroupAdapter != null){
			mGroupAdapter.notifyDataSetChanged();
		}
	}


	private void updateUserData () {
		if (mObjects == null) {
			mObjects = new ArrayList<Object>();
		} else {
			mObjects.clear();
		}

		if (mRData != null && mRData.size() > 0) {
			if(mRData.get(mRData.size() - 1) != null) {
				if (mRData.get(mRData.size() - 1).getUserOrganizations() != null) {
					for (UserOrganization userOrganization : mRData.get(mRData.size() - 1).getUserOrganizations()) {
						mObjects.add(userOrganization);
					}
				}

				if (mRData.get(mRData.size() - 1).getRootOrganizations() != null) {
					for (RootOrganization rOrganization : mRData.get(mRData.size() - 1).getRootOrganizations()) {
						mObjects.add(rOrganization);
					}
				}
			}


//			Collections.sort(mObjects, new PinyinComparator2());
//			Collections.sort(mObjects, new StatusComparator());


		}

		if (mReserverUser != null) {
			SyncReserverUser();
			if(mUData != null){
				for (int i = 0; i < mUData.size(); i++) {
					for (int j = mUData.size() - 1; j > i; j--) {
						if (mUData.get(i).getUserName().equals(mUData.get(j).getUserName())) {
							mUData.remove(j);
						}
					}
				}
				ShowmUData();
			}
			mReserverUser = null;
		}
		if (Refresh == false) {
			mOrganizationStructureAdapter = new OrganizationStructureAdapter(getActivity(), mObjects, normal,screen_status,SystemName);
			mListView.setAdapter(mOrganizationStructureAdapter);
			mOrganizationStructureAdapter.setOnItemSelectClickListener(new OrganizationStructureAdapter.onItemSelectListener() {
				@Override
				public void onSelectClick(RootOrganization mRoot) {
					list = new ArrayList<>();
					SyncSelectUser(mRoot);
					mOrganizationStructureAdapter.notifyDataSetChanged();
				}
			});
			Refresh = true;
		} else {
			if (mOrganizationStructureAdapter == null) {
				mOrganizationStructureAdapter = new OrganizationStructureAdapter(getActivity(), mObjects, normal,screen_status,SystemName);
				mListView.setAdapter(mOrganizationStructureAdapter);
				mOrganizationStructureAdapter.setOnItemSelectClickListener(new OrganizationStructureAdapter.onItemSelectListener() {

					@Override
					public void onSelectClick(RootOrganization mRoot) {
						list = new ArrayList<>();
						SyncSelectUser(mRoot);
						mOrganizationStructureAdapter.notifyDataSetChanged();
					}
				});
			} else {
				mOrganizationStructureAdapter.update(mObjects);
			}
		}
	}

	 private void SyncReserverUser(){
		 if(mReserverUser.size()>1) {
			 for(int i=1;i<mReserverUser.size();i++){
				 SearchSelectOrg(mReserverUser.get(i).getUserName(),mRootOrganization);
			 }
		 }
	 }

	private void SyncSelectUser(RootOrganization mRoot){
		AllUser=0;
		SelectUser = 0;
		int dex = InitializationUserStatus(mRoot);
		if (dex ==0){
			return;
		}
		SyncOrganizatinSelectData(dex,mRoot,mRootOrganization);

		if(dex==2) {
			AddSelectUser(mRoot);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					mUData.add(list.get(i));
				}
			}

			for (int i = 0; i < mUData.size(); i++) {
				for (int j = mUData.size() - 1; j > i; j--) {
					if (mUData.get(i).getUserName().equals(mUData.get(j).getUserName())) {
						mUData.remove(j);
					}
				}
				if (mUData.get(i).getUserName().equals(SystemName)) {
					mUData.remove(i);
				}
			}
		}else {
			DeleteSelectUser(mUData,mRoot);
		}

		ShowmUData();
	}

	private void ShowmUData(){
		String userNames = "";
		for(int i = 0; i < mUData.size(); i++){
			if(screen_status.equals("vertical")){
				userNames = userNames + mUData.get(i).getDisplayName() + "、";
			}else {
				userNames = userNames + mUData.get(i).getDisplayName() + "\n";
			}
		}
		if (userNames.equals("") == false) {
			userNames = userNames.substring(0,userNames.length()-1);
		}

        mMeetingSelectUser.setText(userNames);
		mButtonConferencing.setText(getActivity().getResources().getString(R.string.sure)+"("+mUData.size()+")");
		mButtonConferencing.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/48);
	}

	private class StatusComparator implements Comparator {
		@Override
		public int compare(Object arg0, Object arg1) {
			String status1 = ((UserOrganization)arg0).getStatus();
			String status2 = ((UserOrganization)arg1).getStatus();
			if ((status1.equals("online") || status1.equals("busy"))
					&& (status2.equals("online") == false && status2.equals("busy") == false)) {
				return -1;
			}

			if ((status2.equals("online") || status2.equals("busy"))
					&& (status1.equals("online") == false && status1.equals("busy") == false)) {
				return 1;
			}
			return 0;
		}
     }

	public class PinyinComparator2 implements Comparator {
		public int compare(Object o1, Object o2) {
			if (((UserOrganization)o1).getUserName().equals("@")
					|| ((UserOrganization)o2).getUserName().equals("#")) {
				return -1;
			} else if (((UserOrganization)o1).getUserName().equals("#")
					|| ((UserOrganization)o2).getUserName().equals("@")) {
				return 1;
			} else {
				return ((UserOrganization)o1).getUserName().compareTo(((UserOrganization)o2).getUserName());
			}
		}
	}

	public class PinyinComparator implements Comparator<UserOrganization> {
		public int compare(UserOrganization o1, UserOrganization o2) {
			if (o1.getfWordString().equals("@")
					|| o2.getfWordString().equals("#")) {
				return -1;
			} else if (o1.getfWordString().equals("#")
					|| o2.getfWordString().equals("@")) {
				return 1;
			} else {
				return o1.getfWordString().compareTo(o2.getfWordString());
			}
		}
	}

	private void  syncData () {
		if (mRootOrganization == null) {
			return;
		}
		if (mUData == null) {
			mUData = new ArrayList<UserOrganization>();
			return;
		}

		for (int i = 0; i < mUData.size(); i++) {
			itUserganization(mUData.get(i).getUserName(), mRootOrganization);
		}
	 }

	private void itUserganization(String userName, RootOrganization mOrganization){
   	 if (mOrganization == null) {
   		 return;
   	 }

   	 if (mOrganization.getUserOrganizations() == null) {
   		 return;
   	 }

   	 for (int i = 0; i < mOrganization.getUserOrganizations().size(); i++) {
   		 if (userName.equals(mOrganization.getUserOrganizations().get(i).getUserName()) == true) {
   			 mOrganization.getUserOrganizations().get(i).setSelect(true);
   			 break;
   		 }
   	 }

   	 if (mOrganization.getRootOrganizations() != null) {
   		 for (int i = 0; i < mOrganization.getRootOrganizations().size(); i++) {
   			 itUserganization(userName, mOrganization.getRootOrganizations().get(i));
   		 }
   	 }
    }

	private void updateOrganizationData () {
		 if (mRData == null) {
			 mRData = new ArrayList<RootOrganization>();
			 if(mRootOrganization != null){
				 mRData.add(mRootOrganization);
			 }
		 } else {
		 	if(mRootOrganization != null) {
				for (int i = 0; i < mRData.size(); i++) {
					RootOrganization rootOrganization = itRtOrganization(mRData.get(i).getOrganizationId(), mRootOrganization);
					if (rootOrganization != null) {
						mRData.set(i, rootOrganization);
					} else {
						continue;
					}
				}
			}else {
				mRData = new ArrayList<RootOrganization>();
			}
		 }
		 
		 if (mHorizontalListViewAdapter == null) {
			 mHorizontalListViewAdapter = new HorizontalAdapter(getActivity(), mRData);
			 mHorizontalListView.setAdapter(mHorizontalListViewAdapter);
		 } else {
			 mHorizontalListViewAdapter.update(mRData);
		 }
	 }

	private RootOrganization itRtOrganization(String orgId, RootOrganization mOrganization){
   	 if (mOrganization == null) {
   		 return null;
   	 }

   	 if(orgId.equals(mOrganization.getOrganizationId()) == true){

   		 return mOrganization;
   	 }

   	 RootOrganization rootOrganization ;
   	 if(mOrganization.getRootOrganizations()  != null){
   		 for(RootOrganization organization : mOrganization.getRootOrganizations()){
   			 rootOrganization = itRtOrganization(orgId, organization);
   			 if (rootOrganization != null) {
   				 return rootOrganization;
   			 }
   		 }
   	 }
   	 return null;
    }

	public ArrayList<UserOrganization> searchFirstWord (List<UserOrganization> userOrganizations, String words) {
		ArrayList<UserOrganization> list = new ArrayList<UserOrganization>();
		if (words == null || words.equals("") == true) {
			return list;
		}

		if (userOrganizations == null || userOrganizations.size() <= 0) {
			return list;
		}

		for (int i = 0; i < userOrganizations.size(); i++) {
			if (userOrganizations.get(i).getFirstWord().contains(words) == true) {
				list.add(userOrganizations.get(i));
				continue;
			}
			if(userOrganizations.get(i).getUserName().contains(words)==true){
				list.add(userOrganizations.get(i));
				continue;
			}
			if(userOrganizations.get(i).getDisplayName().contains(words)==true){
				list.add(userOrganizations.get(i));
				continue;
			}
		}
		return list;
	}

	private void StatisticsUserStatus(RootOrganization mR){
		if(mR == null){
			return;
		}

		if(mR.getUserOrganizations() !=null){
			AllUser = AllUser + mR.getUserOrganizations().size();
			for(UserOrganization mUo:mR.getUserOrganizations()){
				if(mUo.isSelect()==true){
					SelectUser++;
				}
				if(mUo.getUserName().equals(SystemName)){
					AllUser = AllUser -1;
				}
			}
		}

		if(mR.getRootOrganizations() !=null){
			for(RootOrganization mUr:mR.getRootOrganizations()){
				StatisticsUserStatus(mUr);
			}
		}
	}


	private int InitializationUserStatus(RootOrganization mR){
		StatisticsUserStatus(mR);
		if(SelectUser ==0 && AllUser ==0){
			return 0;
		}
		if(SelectUser == AllUser){
			return 1;
		}else {
			return 2;
		}
	}

	private void SyncOrganizatinSelectData(int dex,RootOrganization mR,RootOrganization mRootOrganization) {
		if (mR == null) {
			return;
		}
		if(mRootOrganization != null){
			if(mR.getOrganizationId().equals(mRootOrganization.getOrganizationId())){
				ChangeUserStatus(dex,mRootOrganization);
			}
		}

		if(mRootOrganization.getRootOrganizations()!= null){
			for(RootOrganization mRt:mRootOrganization.getRootOrganizations()){
				if(mR.getOrganizationId().equals(mRt.getOrganizationId())){
					ChangeUserStatus(dex,mRt);
					break;
				}

				if (mRt.getRootOrganizations() != null) {
					for (RootOrganization mUr : mRt.getRootOrganizations()) {
						SyncOrganizatinSelectData(dex,mR,mUr);
					}
				}
			}
		}
	}

	private void  ChangeUserStatus(int dex,RootOrganization mRootOrg){
		if(mRootOrg == null){
			return;
		}

		if(mRootOrg.getUserOrganizations() !=null){
			for(UserOrganization mUo:mRootOrg.getUserOrganizations()) {
				if (dex == 1) {
					mUo.setSelect(false);
				} else if(dex == 2){
					mUo.setSelect(true);
				}

				if(mUo.getUserName().equals(SystemName)){
					mUo.setSelect(false);
				}
			}
		}

		if(mRootOrg.getRootOrganizations() !=null){
			for(RootOrganization mUr:mRootOrg.getRootOrganizations()){
				ChangeUserStatus(dex,mUr);
			}
		}
	}


	private void AddSelectUser(RootOrganization mR){
		if(mR == null){
			return ;
		}

		if(mR.getUserOrganizations() != null){
			for(int i = 0; i<mR.getUserOrganizations().size() ;i++){
				list.add(mR.getUserOrganizations().get(i));
			}
		}


		if(mR.getRootOrganizations() != null){
			for(RootOrganization Rt :mR.getRootOrganizations()){
				AddSelectUser(Rt);
			}
		}
	}

	private void DeleteSelectUser(List<UserOrganization> mu, RootOrganization mRootOrganization){
		if(mRootOrganization == null){
			return;
		}

		if(mRootOrganization.getUserOrganizations() != null){
			for(int i = 0; i<mRootOrganization.getUserOrganizations().size() ;i++){
				for(int j= 0;j<mu.size();j++){
					if(mu.get(j).getUserName().equals(mRootOrganization.getUserOrganizations().get(i).getUserName())) {
						mu.remove(mu.get(j));
					}
				}
			}
		}

		if(mRootOrganization.getRootOrganizations() != null){
			for(RootOrganization Rt :mRootOrganization.getRootOrganizations()){
				DeleteSelectUser(mu,Rt);
			}
		}
	}

	private void SearchSelectOrg(String name, RootOrganization mr){
		if(name ==null || mr == null ){
			return;
		}

		if(mr.getUserOrganizations() != null) {
			for (UserOrganization mUo : mr.getUserOrganizations()) {
				if (name.equals(mUo.getUserName())) {
					mUo.setSelect(true);
					mUData.add(mUo);
				}
			}
		}

		if(mr.getRootOrganizations() != null){
			for(RootOrganization mRo : mr.getRootOrganizations()){
				SearchSelectOrg(name,mRo);
			}
		}
	}

	private void showDialog(final String status, final Group group){
		if (dialog != null){
			dialog.dismiss();
			dialog = null;
		}

		dialog = new Dialog(getActivity(), R.style.Dialog);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_groupoperation, null);
		dialog.setContentView(v);

		Window win = dialog.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		win.setGravity(Gravity.CENTER);
		lp.width = Common.SCREENHEIGHT/4*3;
		lp.height = Common.SCREENWIDTH/4;
		win.setAttributes(lp);

		final TextView textTital = (TextView)v.findViewById(R.id.txt_tital_dialog_groupoperation);
		final EditText editText = (EditText)v.findViewById(R.id.edit_dialog_groupoperation);
		final TextView BtnExit = (TextView)v.findViewById(R.id.txt_exit_dialog_groupoperation);
		final TextView BtnSure = (TextView)v.findViewById(R.id.txt_goset_dialog_groupoperation);
		textTital.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
		BtnExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
		BtnSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);

		final int maxLen = 20;
		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
				int dindex = 0;
				int count = 0;

				while (count <= maxLen && dindex < dest.length()) {
					char c = dest.charAt(dindex++);
					if (c < 128) {
						count = count + 1;
					} else {
						count = count + 2;
					}
				}

				if (count > maxLen) {
					return dest.subSequence(0, dindex - 1);
				}

				int sindex = 0;
				while (count <= maxLen && sindex < src.length()) {
					char c = src.charAt(sindex++);
					if (c < 128) {
						count = count + 1;
					} else {
						count = count + 2;
					}
				}

				if (count > maxLen) {
					sindex--;
				}

				return src.subSequence(0, sindex);
			}
		};
		editText.setFilters(new InputFilter[]{filter});

		if(status.equals("create")){
			textTital.setText(getActivity().getResources().getString(R.string.creategruop));
		}else {
			textTital.setText(getActivity().getResources().getString(R.string.revisegruopname));
		}
		BtnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                if(dialog != null){
					setBackgroundAlpha(1.0f);
					dialog.dismiss();
				}
			}
		});

		BtnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (status.equals("create")) {
					createGroupInfo = new CreateGroupInfo();
					createGroupInfo.setName(editText.getText().toString());
					mExCallBack.onCreateGroup(createGroupInfo);
				}else {
					updateGroupInfo = new UpdateGroupInfo();
					updateGroupInfo.setName(editText.getText().toString());
					updateGroupInfo.setId(group.get_id());
					mExCallBack.onUpdateGroup(updateGroupInfo);
				}
                if (dialog != null) {
                    InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialog.dismiss();
                }
				setBackgroundAlpha(1.0f);
			}
		});

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				setBackgroundAlpha(1.0f);
			}
		});

		setBackgroundAlpha(0.5f);
		dialog.show();
	}

	private void showPopWindow(final Group group) {
		View popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_longclickgroup, null);
		mPopupWindow = new PopupWindow(popView, (int) (Common.SCREENHEIGHT * 0.86), (int) (Common.SCREENHEIGHT * 0.86 * 0.36));
//            popupWindow.setAnimationStyle(R.style.PopAnimStyle);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		TextView tvRename = (TextView) popView.findViewById(R.id.rename_dialog_clickgroup);
		TextView tvDelete = (TextView) popView.findViewById(R.id.delete_dialog_clickgroup);
		tvRename.setText(getActivity().getResources().getString(R.string.rename_group));
		tvDelete.setText(getActivity().getResources().getString(R.string.delete_group));
		tvRename.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 35);
		tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 35);
		tvRename.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				showDialog("rename",group);
			}
		});
		tvDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteGroupInfo = new DeleteGroupInfo();
				deleteGroupInfo.setId(group.get_id());
				mExCallBack.onDeleteGroup(deleteGroupInfo);
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.showAtLocation(popView, Gravity.CENTER, 0,0);
		setBackgroundAlpha(0.5f);

		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				setBackgroundAlpha(1.0f);
			}
		});
	}

	public void setBackgroundAlpha(float alpha) {
		Window window = getActivity().getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = alpha;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
}
