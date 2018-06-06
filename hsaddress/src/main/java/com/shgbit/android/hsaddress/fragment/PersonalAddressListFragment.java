package com.shgbit.android.hsaddress.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.Favorite;
import com.shgbit.android.hsaddress.bean.User;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.callback.InternalCallBack;
import com.shgbit.android.hsaddress.sdk.Common;


public class PersonalAddressListFragment extends Fragment {
	
	private final String Tag = "PersonalAddressListFragment";
	ImageView btnImageView;
	private TextView mTitleTextView;
	private TextView mNameTextView;
	private TextView mCTextView;
	private TextView mConpanyTextView;
	private TextView mDTextView;
	private TextView mDepartmentTextView;
	private TextView mSTextView;
	private TextView mSdepartmentTextView;
	private TextView mPTextView;
	private TextView mPhoneTextView;
	private Button mButton;
	private TextView mBtnCollect;
	private TextView mStatusCollect;
	private UserOrganization mInformatinal;
	private String TphoneNum;
	private boolean isCollect;
	private Favorite mContacts;
	private InternalCallBack mInCallBack;
	private String LoginName;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.personal_addresslist, null);
		initView(v);
		return v;
	}

	public void setOrgData(UserOrganization pInformatinal,String name,InternalCallBack mInCallBack) {
		mInformatinal=pInformatinal;
		this.mInCallBack = mInCallBack;
		LoginName = name;
	}

	private void initView(View v) {
		btnImageView=(ImageView)v.findViewById(R.id.img_back_personal_addresslist);
		mTitleTextView=(TextView)v.findViewById(R.id.title_personal_addresslist);
		mNameTextView=(TextView)v.findViewById(R.id.personal_addresslist_name);
		mBtnCollect=(TextView)v.findViewById(R.id.btn_collect);
		mStatusCollect=(TextView)v.findViewById(R.id.collect_status);
		mCTextView=(TextView)v.findViewById(R.id.personal_conpany);
		mConpanyTextView=(TextView)v.findViewById(R.id.personal_addresslist_conpany);
		mDTextView=(TextView)v.findViewById(R.id.personal_department);
		mDepartmentTextView=(TextView)v.findViewById(R.id.personal_addresslist_department);
		mSTextView=(TextView)v.findViewById(R.id.personal_Sdepartment);
		mSdepartmentTextView=(TextView)v.findViewById(R.id.personal_addresslist_second_department);
		mPTextView=(TextView)v.findViewById(R.id.personal_phone);
		mPhoneTextView=(TextView)v.findViewById(R.id.personal_addresslist_phone);
		mButton=(Button)v.findViewById(R.id.btn_personal_addresslist);

		mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/30);
		mCTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mConpanyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mDTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mDepartmentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mSTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mSdepartmentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mPTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mPhoneTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/35);
		mStatusCollect.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/50);
		
		isCollect = mInformatinal.isCollect();
		
		if(isCollect == false){
			mBtnCollect.setBackgroundResource(R.drawable.collect_nor);
			mStatusCollect.setText(getActivity().getResources().getString(R.string.collect));
		}else {
			mBtnCollect.setBackgroundResource(R.drawable.collect_pre);
			mStatusCollect.setText(getActivity().getResources().getString(R.string.cancle_collext));
		}
		
		btnImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Syntony.getInstance().des2();
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		mPhoneTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (TphoneNum == null || TphoneNum.equals("")) {
					return;
				}else {
//					Intent dialIntent =  new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + TphoneNum));
					Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + TphoneNum));
					dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(dialIntent);
				}
			}
		});
		
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mInformatinal == null){
					getActivity().getSupportFragmentManager().popBackStack();
					return;
				}
				
//				String[] userName = new String[1];
//				userName[0] = mInformatinal.getUserName();
				
				User[] user = new User[1];
				user[0] = new User();
				user[0].setUserName(mInformatinal.getUserName());
				user[0].setDisplayName(mInformatinal.getDisplayName());
				user[0].setStatus(mInformatinal.getStatus()); 
				
				boolean isperson=true;
				
				if(LoginName.equals(mInformatinal.getUserName())){
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_26), Toast.LENGTH_SHORT).show();
					return;
				}else{
						mInCallBack.onPersonalMeeting(user,isperson);
				}
				//getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		if(mInformatinal != null){
			mNameTextView.setText(mInformatinal.getDisplayName());
			mPhoneTextView.setText(mInformatinal.getMobilePhone());
			TphoneNum=mPhoneTextView.getText().toString();
			mConpanyTextView.setText(getDepartment(mInformatinal.getDepartment(), 0));
			mDepartmentTextView.setText(getDepartment(mInformatinal.getDepartment(), 1));
			mSdepartmentTextView.setText(getDepartment(mInformatinal.getDepartment(), 2));
			
			mBtnCollect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(mInformatinal.getUserName().equals(LoginName)){
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_21), Toast.LENGTH_SHORT).show();
					}else{
						if(isCollect == false){
							mBtnCollect.setBackgroundResource(R.drawable.collect_pre);
							mStatusCollect.setText(getActivity().getResources().getString(R.string.cancle_collext));
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_37), Toast.LENGTH_SHORT).show();

							isCollect = true;
							mInformatinal.setCollect(true);
							mContacts = new Favorite();
							mContacts.setUserName(mInformatinal.getUserName());
							mInCallBack.onPostContactsUser(mContacts, null);
						} else {
							mBtnCollect.setBackgroundResource(R.drawable.collect_nor);
							mStatusCollect.setText(getActivity().getResources().getString(R.string.collect));
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.tips_38), Toast.LENGTH_SHORT).show();

							isCollect = false;
							mInformatinal.setCollect(false);
							mContacts = new Favorite();
							mContacts.setUserName(mInformatinal.getUserName());
							mInCallBack.onPostContactsUser(null, mContacts);
						}

				    }
				}
			});
		}
	}
	public static String getDepartment (String[] dep, int degree) {
		if (dep == null || dep.length <= 0) {
			return "";
		}

		String depString = "";
		for (String d : dep) {
			if (d == null) {
				continue;
			}
			if (d.contains(",") == true) {
				String[] ds = d.split(",");
				if (ds.length > degree) {
					depString += ds[ds.length - degree - 1] + ",";
				}
			}
			if(degree == 0){
				break;
			}
		}

		if (depString.length() > 0) {
			depString = depString.substring(0, depString.length()-1);
		}
		return depString;
	}
}
