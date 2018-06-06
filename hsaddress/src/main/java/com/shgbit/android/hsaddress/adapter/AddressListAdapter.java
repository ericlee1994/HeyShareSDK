package com.shgbit.android.hsaddress.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.sdk.Common;

import java.util.List;

public class AddressListAdapter extends BaseAdapter implements SectionIndexer {
	private LayoutInflater mInflater;
	private Context mcontext;
	private List<UserOrganization> mDataList;
	private boolean isMeeting;
	private String type;
	private TextView mCatalog;
	private LinearLayout mCircular;
	private ImageView mSelect;
	private ImageView mPng;
	private TextView mNum;
	private TextView mDeparment;
	private TextView mStatus;
	private LinearLayout mLayout;
	
	public AddressListAdapter(Context c, List<UserOrganization> Data, boolean ismeeting, String ishorizontal){
		isMeeting=ismeeting;
		type= ishorizontal;
		mcontext=c;
		mDataList=Data;
		mInflater = LayoutInflater.from(mcontext);
	}
	
	public void update (List<UserOrganization> data) {
		mDataList = data;
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
		return arg0;
	}
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView == null) {
			if(type.equals("horizontal")){
				v = mInflater.inflate(R.layout.item_address_list2, null);
			}else{
				v = mInflater.inflate(R.layout.item_address_list, null);
			}
        } else {
            v = convertView;
        }
        
        mLayout=(LinearLayout)v.findViewById(R.id.catalog_layout);
        mCatalog=(TextView)v.findViewById(R.id.addresslist_catalog);
        mCircular=(LinearLayout)v.findViewById(R.id.addresslist_circular_layout);
        mSelect=(ImageView)v.findViewById(R.id.addresslist_childselect);
        mPng=(ImageView)v.findViewById(R.id.addresslist_png);
        mNum=(TextView)v.findViewById(R.id.addresslist_name);
        mDeparment=(TextView)v.findViewById(R.id.addresslist_department);
        mStatus=(TextView)v.findViewById(R.id.addresslist_status);
        
        int section = getSectionForPosition(position);
        
        mCatalog.setText(mDataList.get(position).getfWordString());
        mNum.setText(mDataList.get(position).getDisplayName());
        String status=mDataList.get(position).getStatus();
        
        mDeparment.setText(getDepartment(mDataList.get(position).getDepartment(), 1));
        
        ViewGroup.LayoutParams lp =mLayout.getLayoutParams();
        lp.height= Common.SCREENHEIGHT/17;
        
        if(position == getPositionForSection(section)){
        	mLayout.setVisibility(View.VISIBLE);
        	mCatalog.setVisibility(View.VISIBLE);
        	mDeparment.setVisibility(View.VISIBLE);
        	mStatus.setVisibility(View.VISIBLE);
        	mPng.setVisibility(View.VISIBLE);
		}else{
			mLayout.setVisibility(View.GONE);
		}
        
        if(isMeeting == false){
        	mCircular.setVisibility(View.INVISIBLE);
        }

        if(status.equalsIgnoreCase("busy") || (status.equalsIgnoreCase("online"))){
			if (status.equalsIgnoreCase("busy")) {
				mStatus.setText(mcontext.getString(R.string.person_busy));
				mStatus.setTextColor(mcontext.getResources().getColor(R.color.busy_color));
			} else {
				mStatus.setText(mcontext.getString(R.string.person_online));
				mStatus.setTextColor(mcontext.getResources().getColor(R.color.online_color));
			}
			
			if(mDataList.get(position).isSelect()==true){
				    mSelect.setImageResource(R.drawable.icon_status_select);
				    mPng.setImageResource(R.drawable.child_user_selecdt);
				    mNum.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				    mDeparment.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				    mStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
			}else{
				    mSelect.setImageResource(R.drawable.icon_status_normal);
				    mPng.setImageResource(R.drawable.child_user_normal);
				    mNum.setTextColor(mcontext.getResources().getColor(R.color.online_color));
				    mDeparment.setTextColor(mcontext.getResources().getColor(R.color.online_color));
			}
		}else{
			mSelect.setImageResource(R.drawable.icon_state_offline);
			mStatus.setText(mcontext.getString(R.string.person_gone));
			mNum.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
			mPng.setImageResource(R.drawable.child_user_offline);
			mDeparment.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
			mStatus.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
			if(mDataList.get(position).isSelect()==true){
				    mSelect.setImageResource(R.drawable.icon_status_select);
				    mPng.setImageResource(R.drawable.child_user_selecdt);
				    mNum.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				    mDeparment.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				    mStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
			}
		}
        mCatalog.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
        mNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
    	mDeparment.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
    	mStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
        return v;
	}
	@Override
	public int getSectionForPosition(int position) {
		if(mDataList.get(position).getfWordString() == null){
		   return mDataList.size()-1;
		}
		return mDataList.get(position).getfWordString().charAt(0);
    } 
	@Override
	public int getPositionForSection(int section) {
		
		for (int i = 0; i < getCount(); i++) {
			if(mDataList.get(i).getfWordString() != null){
			    String sortStr = mDataList.get(i).getfWordString();
			    char firstChar = sortStr.toUpperCase().charAt(0);
			    if (firstChar == section) {
				    return i;
		     	}
			}
		}
		return -1;
	}
	
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
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
