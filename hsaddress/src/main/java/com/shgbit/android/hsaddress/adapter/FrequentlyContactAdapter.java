package com.shgbit.android.hsaddress.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.sdk.Common;

import java.util.List;

public class FrequentlyContactAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mcontext;
	private List<UserOrganization> mDataList;
	private boolean isMeeting;
	private String type;
	private LinearLayout mCircular;
	private ImageView mSelect;
	private ImageView mPng;
	private TextView mNum;
	private TextView mDeparment;
	private TextView mStatus;
	
	public FrequentlyContactAdapter(Context c, List<UserOrganization> Data, boolean ismeeting, String ishorizontal){
		isMeeting=ismeeting;
		type = ishorizontal;
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
		if(mDataList == null){
			return 0;
		}else{
			return mDataList.size();
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        
        if (convertView == null) {
			if(type.equals("horizontal")){
				v = mInflater.inflate(R.layout.item_frequently_contact2, null);
			}else {
				v = mInflater.inflate(R.layout.item_frequently_contact, null);
			}
        } else {
            v = convertView;
        }
        
        mCircular=(LinearLayout)v.findViewById(R.id.contact_circular_layout);
        mSelect=(ImageView)v.findViewById(R.id.contact_childselect);
        mPng=(ImageView)v.findViewById(R.id.contact_png);
        mNum=(TextView)v.findViewById(R.id.contact_num);
        mDeparment=(TextView)v.findViewById(R.id.contact_deparment);
        mStatus=(TextView)v.findViewById(R.id.contact_status);
        
        if(isMeeting == false){
        	mCircular.setVisibility(View.INVISIBLE);
        }
		if(position<mDataList.size()) {
			mNum.setText(mDataList.get(position).getDisplayName());

			String status = mDataList.get(position).getStatus();

			mDeparment.setText(getDepartment(mDataList.get(position).getDepartment(), 1));
			if (status.equalsIgnoreCase("busy") || (status.equalsIgnoreCase("online"))) {
				if (status.equalsIgnoreCase("busy")) {
					mStatus.setText(mcontext.getString(R.string.person_busy));
					mStatus.setTextColor(mcontext.getResources().getColor(R.color.busy_color));
				} else {
					mStatus.setText(mcontext.getString(R.string.person_online));
					mStatus.setTextColor(mcontext.getResources().getColor(R.color.online_color));
				}

				if (mDataList.get(position).isSelect() == true) {
					mSelect.setImageResource(R.drawable.icon_status_select);
					mPng.setImageResource(R.drawable.child_user_selecdt);
					mNum.setTextColor(mcontext.getResources().getColor(R.color.select_color));
					mDeparment.setTextColor(mcontext.getResources().getColor(R.color.select_color));
					mStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				} else {
					mSelect.setImageResource(R.drawable.icon_status_normal);
					mPng.setImageResource(R.drawable.child_user_normal);
					mNum.setTextColor(mcontext.getResources().getColor(R.color.online_color));
					mDeparment.setTextColor(mcontext.getResources().getColor(R.color.online_color));
				}
			} else {
				mSelect.setImageResource(R.drawable.icon_state_offline);
				mStatus.setText(mcontext.getString(R.string.person_gone));
				mNum.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
				mPng.setImageResource(R.drawable.child_user_offline);
				mDeparment.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
				mStatus.setTextColor(mcontext.getResources().getColor(R.color.offline_color));
				if (mDataList.get(position).isSelect() == true) {
					mSelect.setImageResource(R.drawable.icon_status_select);
					mPng.setImageResource(R.drawable.child_user_selecdt);
					mNum.setTextColor(mcontext.getResources().getColor(R.color.select_color));
					mDeparment.setTextColor(mcontext.getResources().getColor(R.color.select_color));
					mStatus.setTextColor(mcontext.getResources().getColor(R.color.select_color));
				}
			}
			mNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
			mDeparment.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 45);
			mStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 45);
		}

		return v;
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
