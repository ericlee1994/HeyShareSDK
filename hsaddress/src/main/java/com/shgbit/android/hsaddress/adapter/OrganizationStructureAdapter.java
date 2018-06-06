package com.shgbit.android.hsaddress.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.RootOrganization;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.sdk.Common;

import java.util.List;

public class OrganizationStructureAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mcontext;
	private List<Object> mDataList;
	private boolean isMeeting;
	private String type;
	private TextView cGroupId;
	private ImageView cGroupSelect;
	private ImageView cChildSelect;
	private ImageView cChildPng;
	private TextView cChildId;
	private TextView cChildStatus;
	private LinearLayout mAddressList_group;
	private LinearLayout mAddressList_child;
	private LinearLayout mCircular;
	private LinearLayout mSquare;
	private boolean isSelectOrg;
	private int AllUser = 0;
	private int SelectUser = 0;
	private String SystemName;
	
	public OrganizationStructureAdapter(Context c, List<Object> Data, boolean ismeeting, String ishorizontal ,String name ){
		isMeeting=ismeeting;
		type = ishorizontal;
		mcontext=c;
		mDataList=Data;
		SystemName = name;
		mInflater = LayoutInflater.from(mcontext);
	}

	
	public void update (List<Object> Data) {
		mDataList=Data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;
		
        if (convertView == null) {
			if(type.equals("horizontal")){
				v = mInflater.inflate(R.layout.item_organization_structure2, null);
			}else {
				v = mInflater.inflate(R.layout.item_organization_structure, null);
			}

        } else {
            v = convertView;
        }
        cGroupId=(TextView)v.findViewById(R.id.addresslist_groupid);
        mCircular=(LinearLayout)v.findViewById(R.id.organization_circular_layout);

		cGroupSelect = (ImageView)v.findViewById(R.id.addresslist_groupselect);
		cGroupSelect.setImageResource(R.drawable.select_no);
        cChildSelect=(ImageView)v.findViewById(R.id.addresslist_childselect);
        cChildPng=(ImageView)v.findViewById(R.id.addresslist_childpng);
        cChildId=(TextView)v.findViewById(R.id.addresslist_childnum);
        cChildStatus=(TextView)v.findViewById(R.id.addresslist_childstatus);
        mSquare = (LinearLayout)v.findViewById(R.id.organization_square_layout);

        mAddressList_group=(LinearLayout)v.findViewById(R.id.addresslist_group);
        mAddressList_child=(LinearLayout)v.findViewById(R.id.addresslist_child);


        
        if(isMeeting ==false){
        	mCircular.setVisibility(View.INVISIBLE);
			mSquare.setVisibility(View.INVISIBLE);
        }
        
        if (mDataList.get(position) instanceof UserOrganization) {
        	mAddressList_group.setVisibility(View.INVISIBLE);
        	mAddressList_child.setVisibility(View.VISIBLE);

        	cChildId.setText(((UserOrganization)mDataList.get(position)).getDisplayName());
        	
   			String status=((UserOrganization)mDataList.get(position)).getStatus();
			if(status.equalsIgnoreCase("busy") || (status.equalsIgnoreCase("online"))){
				if (status.equalsIgnoreCase("busy")) {
					cChildStatus.setText(mcontext.getString(R.string.person_busy));
					cChildId.setTextColor(mcontext.getResources().getColor(R.color.online_color));
   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.busy_color));
				} else {
					cChildStatus.setText(mcontext.getString(R.string.person_online));
					cChildId.setTextColor(mcontext.getResources().getColor(R.color.online_color));
   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.online_color));
				}
				
				if(((UserOrganization)mDataList.get(position)).isSelect()==true){
					cChildSelect.setImageResource(R.drawable.icon_status_select);
   					cChildPng.setImageResource(R.drawable.child_user_selecdt);
   					cChildId.setTextColor(mcontext.getResources().getColor(R.color.select_color));
   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				}else{
					cChildSelect.setImageResource(R.drawable.icon_status_normal);
   					cChildPng.setImageResource(R.drawable.child_user_normal);
				}
			}else{
				cChildSelect.setImageResource(R.drawable.icon_state_offline);
				cChildStatus.setText(mcontext.getString(R.string.person_gone));
				cChildId.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
				cChildPng.setImageResource(R.drawable.child_user_offline);
				cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
				if(((UserOrganization)mDataList.get(position)).isSelect()==true){
					cChildSelect.setImageResource(R.drawable.icon_status_select);
   					cChildPng.setImageResource(R.drawable.child_user_selecdt);
   					cChildId.setTextColor(mcontext.getResources().getColor(R.color.select_color));
   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				}
			}

//   			String status=((UserOrganization)mDataList.get(position)).getStatus();
//			if(status.equalsIgnoreCase("busy") || (status.equalsIgnoreCase("online"))){
//				if (status.equalsIgnoreCase("busy")) {
//					cChildStatus.setText(mcontext.getString(R.string.person_busy));
//					cChildId.setTextColor(mcontext.getResources().getColor(R.color.online_color));
//   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.busy_color));
//				} else {
//					cChildStatus.setText(mcontext.getString(R.string.person_online));
//					cChildId.setTextColor(mcontext.getResources().getColor(R.color.online_color));
//   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.online_color));
//				}
//
//				if(((UserOrganization)mDataList.get(position)).isSelect()==true){
//					cChildSelect.setImageResource(R.drawable.icon_status_select);
//   					cChildPng.setImageResource(R.drawable.child_user_selecdt);
//   					cChildId.setTextColor(mcontext.getResources().getColor(R.color.select_color));
//   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
//				}else{
//					cChildSelect.setImageResource(R.drawable.icon_status_normal);
//   					cChildPng.setImageResource(R.drawable.child_user_normal);
//				}
//			}else{
//				cChildSelect.setImageResource(R.drawable.icon_state_offline);
//				cChildStatus.setText(mcontext.getString(R.string.person_gone));
//				cChildId.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
//				cChildPng.setImageResource(R.drawable.child_user_offline);
//				cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
//				if(((UserOrganization)mDataList.get(position)).isSelect()==true){
//					cChildSelect.setImageResource(R.drawable.icon_status_select);
//   					cChildPng.setImageResource(R.drawable.child_user_selecdt);
//   					cChildId.setTextColor(mcontext.getResources().getColor(R.color.select_color));
//   					cChildStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
//				}
//			}

			cChildId.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
			cChildStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);

       } else {
			mAddressList_group.setVisibility(View.VISIBLE);
			mAddressList_child.setVisibility(View.INVISIBLE);
			AllUser = 0;
			SelectUser = 0;
			ChangeImage((RootOrganization) mDataList.get(position));
			if(SelectUser == AllUser){
				cGroupSelect.setImageResource(R.drawable.select_all);
			}else if(SelectUser == 0){
				cGroupSelect.setImageResource(R.drawable.select_no);
			}else if(SelectUser>0 && SelectUser <AllUser){
				cGroupSelect.setImageResource(R.drawable.select_half);
			}

			mSquare.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mOnItemSelectListener.onSelectClick(((RootOrganization) mDataList.get(position)));
				}
			});
			String id = ((RootOrganization) mDataList.get(position)).getOrganizationName();
			String num = "(" + getOnlineNum((RootOrganization) mDataList.get(position)) + "/" + getChildNum((RootOrganization) mDataList.get(position)) + ")";
			cGroupId.setText(id + num);

			cGroupId.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
			cGroupId.setTextColor(mcontext.getResources().getColor(R.color.online_color));
		}
        return v;
	}

	public interface onItemSelectListener {
		void onSelectClick(RootOrganization mRoot);
	}

	private onItemSelectListener mOnItemSelectListener;

	public void setOnItemSelectClickListener(onItemSelectListener mOnItemSelectListener) {
		this.mOnItemSelectListener = mOnItemSelectListener;
	}

	private int getChildNum (RootOrganization rootOrganization){
		int num = 0;
		if (rootOrganization == null) {
			return 0;
		}
		if (rootOrganization.getUserOrganizations() != null){
			num +=rootOrganization.getUserOrganizations().size();
		}
		if (rootOrganization.getRootOrganizations() != null && rootOrganization.getRootOrganizations().size() > 0) {
			for  (RootOrganization ro : rootOrganization.getRootOrganizations()) {
				num += getChildNum(ro);
			}
		}
		return num;
	}
	
	private int getOnlineNum(RootOrganization rootOrganization){
		int onlineName = 0;
		if (rootOrganization == null) {
			return 0;
		}
		if (rootOrganization.getUserOrganizations() != null){
			for(UserOrganization mOrganization:rootOrganization.getUserOrganizations()){
				if(mOrganization.getStatus().equalsIgnoreCase("busy") || mOrganization.getStatus().equalsIgnoreCase("online")){
					onlineName +=1;
				}
			}
		}
		if (rootOrganization.getRootOrganizations() != null && rootOrganization.getRootOrganizations().size() > 0) {
			for  (RootOrganization ro : rootOrganization.getRootOrganizations()) {
				onlineName += getOnlineNum(ro);
			}
		}
		Log.i("########","Onlinenum:"+onlineName);
		return onlineName;
	}


	private void ChangeImage(RootOrganization mRootOrganization){
		if(mRootOrganization == null){
			return;
		}

		if(mRootOrganization.getUserOrganizations() !=null){
			AllUser = AllUser + mRootOrganization.getUserOrganizations().size();
			for(UserOrganization mUo:mRootOrganization.getUserOrganizations()){
				if(mUo.isSelect()==true){
					SelectUser++;
				}
				if(mUo.getUserName().equals(SystemName)){
                    AllUser = AllUser -1;
                }
			}
		}

		if(mRootOrganization.getRootOrganizations() !=null){
			for(RootOrganization mUr:mRootOrganization.getRootOrganizations()){
				ChangeImage(mUr);
			}
		}
	}
}
