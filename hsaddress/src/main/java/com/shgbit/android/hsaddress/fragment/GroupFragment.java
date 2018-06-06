package com.shgbit.android.hsaddress.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.adapter.FrequentlyContactAdapter;
import com.shgbit.android.hsaddress.bean.DeleFrmGroupInfo;
import com.shgbit.android.hsaddress.bean.Favorite;
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.QueryGroupInfo;
import com.shgbit.android.hsaddress.bean.UpdateGroupInfo;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.callback.ExternalCallBack;
import com.shgbit.android.hsaddress.callback.InternalCallBack;
import com.shgbit.android.hsaddress.sdk.Common;
import com.shgbit.android.hsaddress.sdk.StructureDataCollector;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class GroupFragment extends Fragment {

    private final String TAG = "AllAddressListFragment";
    private ImageView btnImageView;
    private TextView mTitleTextView;
    private TextView mNameTextView;
    private EditText mEditText;
    private LinearLayout mPartitionLayout;
    private LinearLayout mAddUserLayout;
    private ListView mListView;
    private LinearLayout mOperationLayout;
    private TextView mExit;
    private TextView mSure;
    private String TitalName;
    private Group mGroup;
    private List<UserOrganization> mContactList;
    private List<UserOrganization> mGroupList;
    private List<UserOrganization> mDeleteList;
    private List<UserOrganization> mUData;
    private FrequentlyContactAdapter mGroupFragAdapter;
    private boolean isLongClick;
    private PopupWindow mPopupWindow;
    private LinearLayout NoMemberLayout;
    private DeleFrmGroupInfo deleFrmGroupInfo;
    private QueryGroupInfo queryGroupInfo;
    private UpdateGroupInfo updateGroupInfo;
    private FrequentlyContactAdapter mEditAdapter;
    private List<UserOrganization> filterDateList;
    private boolean normal;
    private boolean isSearch;
    private String screen_status;
    private Favorite mContacts;
    private String LoginName;
    private InternalCallBack mInCallBack;
    private ExternalCallBack mExCallBack;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        if(screen_status.equals("horizontal")){
            v = inflater.inflate(R.layout.fragment_horizontal_group, null);
        }else {
            v = inflater.inflate(R.layout.fragment_group, null);
        }
        initView(v);
        SyncData();
        StructureDataCollector.getInstance().setDataUpdateListener(mDataUpdateListener);
        return v;
    }

    private StructureDataCollector.DataUpdateListener mDataUpdateListener = new StructureDataCollector.DataUpdateListener() {

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
                    SyncData();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }};

    public void onDetach() {
        StructureDataCollector.getInstance().resetData();
        if (mHandler != null) {
            mHandler.removeMessages(MESSAGE);
            mHandler = null;
        }
        if(normal == true){
            if(isLongClick == false){
                mInCallBack.onUpdataGroup();
                mInCallBack.onGroupUsers();
            }
        }else {
            mInCallBack.onUpdataGroup();
        }
        super.onDetach();
    }

    public void setExCallBack(ExternalCallBack mExCallBack){
        this.mExCallBack = mExCallBack;
    }
    public void setInCallBack(InternalCallBack mInCallBack){
        this.mInCallBack = mInCallBack;
    }


    public void setGroupFrag(String name, Group group, List<UserOrganization> userOrganizations, boolean isMeeting, List<UserOrganization> selectUsers, String type,String username,InternalCallBack mInCallBack){
        TitalName = name;
        mGroup = group;
        mContactList = userOrganizations;
        normal = isMeeting;
        mUData = selectUsers;
        screen_status = type;
        LoginName = username;
        this.mInCallBack = mInCallBack;
    }

    private void initView(View v){
        if(screen_status.equals("horizontal")){
            btnImageView = (ImageView)v.findViewById(R.id.img_back_frag_hor_group);
            mTitleTextView = (TextView)v.findViewById(R.id.back_frag_hor_group);
            mNameTextView = (TextView)v.findViewById(R.id.tital_frag_hor_group);
            mEditText = (EditText)v.findViewById(R.id.filter_edit_frag_hor_group);
            mPartitionLayout = (LinearLayout)v.findViewById(R.id.partition_frag_hor_group);
            mAddUserLayout = (LinearLayout)v.findViewById(R.id.adduser_frag_hor_group);
            mListView = (ListView)v.findViewById(R.id.list_frag_hor_group);
            mOperationLayout = (LinearLayout)v.findViewById(R.id.operation_frag_hor_group);
            mExit  = (TextView)v.findViewById(R.id.exit_frag_hor_group);
            mSure  = (TextView)v.findViewById(R.id.delete_frag_hor_group);
            NoMemberLayout = (LinearLayout)v.findViewById(R.id.nomember_hor_layout);

            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
        }else {
            btnImageView = (ImageView)v.findViewById(R.id.img_back_frag_group);
            mTitleTextView = (TextView)v.findViewById(R.id.back_frag_group);
            mNameTextView = (TextView)v.findViewById(R.id.tital_frag_group);
            mEditText = (EditText)v.findViewById(R.id.filter_edit_frag_group);
            mPartitionLayout = (LinearLayout)v.findViewById(R.id.partition_frag_group);
            mAddUserLayout = (LinearLayout)v.findViewById(R.id.adduser_frag_group);
            mListView = (ListView)v.findViewById(R.id.list_frag_group);
            mOperationLayout = (LinearLayout)v.findViewById(R.id.operation_frag_group);
            mExit  = (TextView)v.findViewById(R.id.exit_frag_group);
            mSure  = (TextView)v.findViewById(R.id.delete_frag_group);
            NoMemberLayout = (LinearLayout)v.findViewById(R.id.nomember_layout);

            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
            mSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
        }

        mOperationLayout.setVisibility(View.GONE);
        isLongClick =false;

        if(normal ==true){
            mAddUserLayout.setVisibility(View.GONE);
            mPartitionLayout.setVisibility(View.GONE);
        }else {
            mAddUserLayout.setVisibility(View.VISIBLE);
            mPartitionLayout.setVisibility(View.VISIBLE);
        }

        if(TitalName !=null) {
            mNameTextView.setText(getActivity().getResources().getString(R.string.group_contact));
        }else {
            mNameTextView.setText(mGroup.getName());
        }

        mExit.setOnClickListener(mClickListener);
        mSure.setOnClickListener(mClickListener);
        btnImageView.setOnClickListener(mClickListener);
        mAddUserLayout.setOnClickListener(mClickListener);
        mListView.setOnItemClickListener(mItemClickListener);
        mListView.setOnItemLongClickListener(mItemLongClickListener);
        mEditText.addTextChangedListener(tWatcher);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if(view.getId() == R.id.img_back_frag_group || view.getId() == R.id.img_back_frag_hor_group){
                getActivity().getSupportFragmentManager().popBackStack();
            }else if(view.getId() == R.id.adduser_frag_group || view.getId() == R.id.adduser_frag_hor_group){
                if(TitalName ==null){
                    mInCallBack.onGroupAddMember(mGroup);
                }
            }else if(view.getId() == R.id.delete_frag_group || view.getId() == R.id.delete_frag_hor_group){
                DeleteGroupMember(mDeleteList);
                mOperationLayout.setVisibility(View.GONE);
                mSure.setText(getActivity().getResources().getString(R.string.group_delete));
                mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mGroupList, false,screen_status);
                mListView.setAdapter(mGroupFragAdapter);
            }else if(view.getId() == R.id.exit_frag_group || view.getId() == R.id.exit_frag_hor_group){
                mOperationLayout.setVisibility(View.GONE);
                mDeleteList = null;
                mSure.setText(getActivity().getResources().getString(R.string.group_delete));
                CancelChoice();
                mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mGroupList, false,screen_status);
                mListView.setAdapter(mGroupFragAdapter);
            }
        }
    };

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            if (isLongClick == false && normal == false && isSearch ==false) {
                if (TitalName == null) {
                    mInCallBack.onPersonalAddressFragment(mGroupList.get(position));
                } else {
                    mInCallBack.onPersonalAddressFragment(mContactList.get(position));
                }
            } else if (isLongClick == true && normal == false && isSearch ==false) {
                if (mGroupList.get(position).getUserName().equals(LoginName)) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_21), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mDeleteList == null) {
                    mDeleteList = new ArrayList<UserOrganization>();
                }

                if (mDeleteList == null) {
                    mDeleteList = new ArrayList<UserOrganization>();
                    mGroupList.get(position).setSelect(true);
                    mDeleteList.add(mGroupList.get(position));
                } else {
                    if (mGroupList.get(position).isSelect()) {
                        mGroupList.get(position).setSelect(false);
                        for (int i = 0; i < mDeleteList.size(); i++) {
                            if (mGroupList.get(position).getUserName().equals(mDeleteList.get(i).getUserName()) == true) {
                                mDeleteList.remove(i);
                                break;
                            }
                        }
                    } else {
                        (mGroupList.get(position)).setSelect(true);
                        mDeleteList.add(mGroupList.get(position));
                    }
                    mGroupFragAdapter.notifyDataSetChanged();

                    mSure.setText(getActivity().getResources().getString(R.string.group_delete) + "(" + mDeleteList.size() + ")");
                }
            } else if (isLongClick == false && normal == true && isSearch ==false) {
                if(TitalName == null) {
                    if (mUData == null) {
                        mUData = new ArrayList<UserOrganization>();
                        mGroupList.get(position).setSelect(true);
                        mUData.add(mGroupList.get(position));
                    } else {
                        if (mGroupList.get(position).isSelect()) {
                            mGroupList.get(position).setSelect(false);
                            for (int i = 0; i < mUData.size(); i++) {
                                if (mGroupList.get(position).getUserName().equals(mUData.get(i).getUserName()) == true) {
                                    mUData.remove(i);
                                    break;
                                }
                            }
                        } else {
                            (mGroupList.get(position)).setSelect(true);
                            mUData.add(mGroupList.get(position));
                        }

                        mGroupFragAdapter.notifyDataSetChanged();
                    }
                }else {
                    if (mUData == null) {
                        mUData = new ArrayList<UserOrganization>();
                        mContactList.get(position).setSelect(true);
                        mUData.add(mContactList.get(position));
                    } else {

                        if (mContactList.get(position).isSelect()) {
                            mContactList.get(position).setSelect(false);
                            for (int i = 0; i < mUData.size(); i++) {
                                if (mContactList.get(position).getUserName().equals(mUData.get(i).getUserName()) == true) {
                                    mUData.remove(i);
                                    break;
                                }
                            }
                        } else {
                            (mContactList.get(position)).setSelect(true);
                            mUData.add(mContactList.get(position));
                        }

                        mGroupFragAdapter.notifyDataSetChanged();
                    }
                }
            }else if(isSearch == true){
                if(normal == false){
                    mInCallBack.onPersonalAddressFragment(filterDateList.get(position));
                }else {
                    if (mUData != null) {
                        ChangeUserStatus(filterDateList);
                    }
                    if (mUData == null) {
                        mUData = new ArrayList<UserOrganization>();
                        filterDateList.get(position).setSelect(true);
                        mUData.add(filterDateList.get(position));
                    } else {
                        if (filterDateList.get(position).isSelect()) {
                            filterDateList.get(position).setSelect(false);
                            for (int i = 0; i < mUData.size(); i++) {
                                if (filterDateList.get(position).getUserName().equals(mUData.get(i).getUserName()) == true) {
                                    mUData.remove(i);
                                    break;
                                }
                            }
                        } else {
                            (filterDateList.get(position)).setSelect(true);
                            mUData.add(filterDateList.get(position));
                        }
                        mEditAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                       long id) {
            InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            if(normal == false){
                if( isSearch==false){
                    if(TitalName ==null){
                        showPopWindow(mGroupList.get(position));
                    }else {
                        showPopWindow(mContactList.get(position));
                    }
                }
            }
            return true;
        }
    };

    private void showPopWindow(final UserOrganization userOrganization) {
        if(TitalName == null){
            View popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_longclickmumber, null);
            mPopupWindow = new PopupWindow(popView, (int) (Common.SCREENHEIGHT * 0.86), (int) (Common.SCREENHEIGHT * 0.86 * 0.36));
//            popupWindow.setAnimationStyle(R.style.PopAnimStyle);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            TextView tvDelete = (TextView) popView.findViewById(R.id.delete_dialog_clickmember);
            TextView tvChoice = (TextView) popView.findViewById(R.id.choice_dialog_clickmember);
            tvDelete.setText(getActivity().getResources().getString(R.string.delete_member));
            tvChoice.setText(getActivity().getResources().getString(R.string.select_operation));
            tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 35);
            tvChoice.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 35);
            tvDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] members = {userOrganization.getUserName()};
                    if(deleFrmGroupInfo == null){
                        deleFrmGroupInfo = new DeleFrmGroupInfo();
                    }
                    deleFrmGroupInfo.setId(mGroup.get_id());
                    deleFrmGroupInfo.setMembers(members);
                    mExCallBack.onDeleFrmGroup(deleFrmGroupInfo);
                    mPopupWindow.dismiss();
                }
            });
            tvChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mGroupList,true,screen_status);
                    mListView.setAdapter(mGroupFragAdapter);
                    mOperationLayout.setVisibility(View.VISIBLE);
                    isLongClick = true;
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
            setBackgroundAlpha(0.5f);
        }else {
            View popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);
            mPopupWindow = new PopupWindow(popView, (int) (Common.SCREENHEIGHT * 0.86), (int) (Common.SCREENHEIGHT * 0.86 * 0.18));
//            popupWindow.setAnimationStyle(R.style.PopAnimStyle);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            TextView tvDelete=(TextView) popView.findViewById(R.id.tv_delete);
            tvDelete.setText(getActivity().getResources().getString(R.string.delete_member));
            tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 35);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userOrganization.setCollect(false);
                    mContacts = new Favorite();
                    mContacts.setUserName(userOrganization.getUserName());
                    mInCallBack.onPostContactsUser(null, mContacts);
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
            setBackgroundAlpha(0.5f);
        }
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

    public void DeleteGroupMember(List<UserOrganization> userOrganizations){
        if(userOrganizations == null){
            return;
        }
        List<String> nameList = new ArrayList<>();
        for(int i=0;i<userOrganizations.size();i++){
            nameList.add(userOrganizations.get(i).getUserName());
        }

        String[] members = nameList.toArray(new String[0]);

        if(deleFrmGroupInfo == null){
            deleFrmGroupInfo = new DeleFrmGroupInfo();
        }
        deleFrmGroupInfo.setId(mGroup.get_id());
        deleFrmGroupInfo.setMembers(members);
        mExCallBack.onDeleFrmGroup(deleFrmGroupInfo);
    }

    public void SyncData(){
        if(isSearch == false) {
            mGroupList = StructureDataCollector.getInstance().getGroupType(StructureDataCollector.getInstance().syncGroup(mGroup));
            if (mGroup == null) {
                NoMemberLayout.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            } else {
                if (mGroupList.size() > 0) {
                    NoMemberLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                } else {
                    NoMemberLayout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
            }
            if (TitalName != null) {
                if (mContactList == null) {
                    NoMemberLayout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }else {
                    NoMemberLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
            }

            if(normal == true){
                if (mUData != null) {
                    ChangeUserStatus(filterDateList);
                    ChangeUserStatus(mGroupList);
                }
            }

            if (TitalName != null) {
                mAddUserLayout.setVisibility(View.GONE);
                mPartitionLayout.setVisibility(View.GONE);
                if (mGroupFragAdapter == null) {
                    mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mContactList, normal,screen_status);
                    mListView.setAdapter(mGroupFragAdapter);
                } else {
                    mGroupFragAdapter.update(mContactList);
                }
            } else {
                if (mGroupFragAdapter == null) {
                    mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mGroupList, normal,screen_status);
                    mListView.setAdapter(mGroupFragAdapter);
                } else {
                    mGroupFragAdapter.update(mGroupList);
                }
            }
        }
    }

    private void CancelChoice(){
        if(mGroupList == null){
            return;
        }

        for(int i=0;i<mGroupList.size();i++){
            if(mGroupList.get(i).isSelect()==true){
                mGroupList.get(i).setSelect(false);
            }
        }
    }

    private TextWatcher tWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "[user operation]: click EditText");
            if(s.toString() == null || s.toString().equals("")){
                InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                if(normal ==true){
                    mAddUserLayout.setVisibility(View.GONE);
                    mPartitionLayout.setVisibility(View.GONE);
                }else {
                    mAddUserLayout.setVisibility(View.VISIBLE);
                    mPartitionLayout.setVisibility(View.VISIBLE);
                }
                if(TitalName !=null){
                    mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mContactList, normal,screen_status);
                    mListView.setAdapter(mGroupFragAdapter);
                }else {
                    mGroupFragAdapter = new FrequentlyContactAdapter(getActivity(), mGroupList, normal,screen_status);
                    mListView.setAdapter(mGroupFragAdapter);
                }
                isSearch = false;
            }else {
                if(TitalName !=null){
                    filterDateList=searchFirstWord(mContactList,mEditText.getText().toString());
                }else {
                    filterDateList=searchFirstWord(mGroupList,mEditText.getText().toString());
                }

//    			if(filterDateList.size()>1){
//    				Collections.sort(filterDateList, new PinyinComparator2());
//    	 			Collections.sort(filterDateList, new StatusComparator());
//    	 	    }
                mAddUserLayout.setVisibility(View.GONE);
                mPartitionLayout.setVisibility(View.GONE);

                mEditAdapter = new FrequentlyContactAdapter(getActivity(), filterDateList, normal,screen_status);
                mListView.setAdapter(mEditAdapter);

                isSearch =true;
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

    private void ChangeUserStatus(List<UserOrganization> userOrganizations){
        if(userOrganizations == null){
            return;
        }

        for(int i=0;i<mUData.size();i++){
            for(int j=0;j<userOrganizations.size();j++){
                if(mUData.get(i).getUserName().equals(userOrganizations.get(j).getUserName())){
                    userOrganizations.get(j).setSelect(true);
                }
            }
        }
    }

    public void setListener(){
        StructureDataCollector.getInstance().setDataUpdateListener(mDataUpdateListener);
    }
}

