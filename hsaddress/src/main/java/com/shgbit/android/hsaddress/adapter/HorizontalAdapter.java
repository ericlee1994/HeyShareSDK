package com.shgbit.android.hsaddress.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.RootOrganization;
import com.shgbit.android.hsaddress.sdk.Common;

import java.util.List;

public class HorizontalAdapter extends BaseAdapter {
	private Context mContext;
	private List<RootOrganization> mDataList;
	private LayoutInflater mInflater;

	public HorizontalAdapter(Context c, List<RootOrganization> Data){
		mContext=c;
		mDataList=Data;
		mInflater= LayoutInflater.from(mContext);
	}
	
	public void update (List<RootOrganization> data) {
		mDataList = data;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        View v = null;
		
        if (convertView == null) {
        	v = mInflater.inflate(R.layout.item_recycleview, null);
        } else {
            v = convertView;
        }
       
        TextView organizationId=(TextView)v.findViewById(R.id.txt);
        ImageView img=(ImageView) v.findViewById(R.id.img);
        
        if(position == mDataList.size()-1){
        	img.setVisibility(View.INVISIBLE);
        	organizationId.setTextColor(mContext.getResources().getColor(R.color.gray_color));
        }else{
            organizationId.setTextColor(mContext.getResources().getColor(R.color.white_color));
        }
        
        organizationId.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);
		if (mDataList.get(position) != null) {
			organizationId.setText(mDataList.get(position).getOrganizationName());
		}

        return v;
	}

}
