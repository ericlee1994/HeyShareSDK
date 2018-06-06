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
import com.shgbit.android.hsaddress.bean.Group;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.sdk.Common;
import com.shgbit.android.hsaddress.sdk.StructureDataCollector;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class GroupAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mcontext;
    private List<Group> mGDataList;
    private List<UserOrganization> mUDataList;
    private List<UserOrganization> mSelectList;
    private TextView TextName;
    private ImageView imageView;
    private boolean  isMeeting;
    private LinearLayout mGroupSquare;
    private ImageView mGroupSelect;
    private int AllUser = 0;
    private int SelectUser = 0;

    final int  TYPE_CONTACT = 1;
    final int  TYPE_GROUP = 2;

    public GroupAdapter(Context c, List<Group> GData, List<UserOrganization> UData, boolean ismeeting, List<UserOrganization> mselectList){
        mcontext=c;
        mGDataList=GData;
        mUDataList=UData;
        mSelectList = mselectList;
        isMeeting = ismeeting;
        mInflater = LayoutInflater.from(mcontext);
    }

    public void update (List<Group> GData, List<UserOrganization> UData, List<UserOrganization> mselectList) {
        mGDataList=GData;
        mUDataList=UData;
        mSelectList = mselectList;
        notifyDataSetChanged();
    }

    private int getItemType(int position){
        if(position ==0){
            return TYPE_CONTACT;
        }else {
            return TYPE_GROUP;
        }
    }

    @Override
    public int getCount() {
        if(mGDataList == null){
            return getItemType(0);
        }else{
            return mGDataList.size()+1;
        }
    }

    @Override
    public Object getItem(int arg0) {
        if(arg0 ==0){
            return mUDataList;
        }else {
            return mGDataList.get(arg0);
        }
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;

        if (convertView == null) {
            v = mInflater.inflate(R.layout.item_groupname, null);
        } else {
            v = convertView;
        }

        mGroupSquare = (LinearLayout)v.findViewById(R.id.group_square_layout);
        mGroupSelect = (ImageView)v.findViewById(R.id.image_groupselect);
        TextName = (TextView)v.findViewById(R.id.addresslist_groupid);
        imageView = (ImageView)v.findViewById(R.id.group_png);
        TextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/40);

        if(isMeeting == false){
            mGroupSquare.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams lp =imageView.getLayoutParams();
        lp.height= Common.SCREENHEIGHT/6;

        String id = "";
        String num = "";
        int type = getItemType(position);
        switch (type){
            case TYPE_CONTACT:
                imageView.setImageResource(R.drawable.group_contact);
                id = mcontext.getString(R.string.group_contact);
                if(mUDataList !=null){
                    num = "(" + getContactOnlineNum(mUDataList) + "/" + mUDataList.size() + ")";
                }
                TextName.setText(id+num);
                break;
            case TYPE_GROUP:
                imageView.setImageResource(R.drawable.group_organization);
                id = mGDataList.get(position-1).getName();
                num = "(" + getContactOnlineNum(StructureDataCollector.getInstance().getGroupType(mGDataList.get(position-1)))+ "/" + mGDataList.get(position-1).getMembers().length + ")";
                TextName.setText(id+num);
                break;
            default:
                break;
        }

        AllUser = 0;
        SelectUser = 0;
        if(position == 0){
            ChangeImage(mUDataList);
        }else {
            ChangeImage(StructureDataCollector.getInstance().getGroupType(mGDataList.get(position-1)));
        }

        if(SelectUser == AllUser){
            if(SelectUser == 0 && AllUser == 0){
                mGroupSelect.setImageResource(R.drawable.select_no);
            }else {
                mGroupSelect.setImageResource(R.drawable.select_all);
            }
        }else if(SelectUser == 0){
            mGroupSelect.setImageResource(R.drawable.select_no);
        }else if(SelectUser>0 && SelectUser <AllUser){
            mGroupSelect.setImageResource(R.drawable.select_half);
        }

        mGroupSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0){
                    if(mUDataList != null){
                        mOnItemSelectListener.onSelectClick(mUDataList);
                    }
                }else {
                    if(StructureDataCollector.getInstance().getGroupType(mGDataList.get(position-1))!=null){
                        mOnItemSelectListener.onSelectClick(StructureDataCollector.getInstance().getGroupType(mGDataList.get(position-1)));
                    }
                }
            }
        });

        return v;
    }

    public interface onItemSelectListener {
        void onSelectClick(List<UserOrganization> mUserList);
    }

    private onItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectClickListener(onItemSelectListener mOnItemSelectListener) {
        this.mOnItemSelectListener = mOnItemSelectListener;
    }

    private int getContactOnlineNum (List<UserOrganization> userOrganization){
        int num = 0;
        if (userOrganization == null) {
            return 0;
        }
        for(int i= 0;i<userOrganization.size();i++){
            if(userOrganization.get(i).getStatus().equalsIgnoreCase("busy") || userOrganization.get(i).getStatus().equalsIgnoreCase("online")){
                num +=1;
            }
        }
        return num;
    }

    private void ChangeImage(List<UserOrganization> mUList) {
        if (mUList == null) {
            return;
        }

        AllUser = mUList.size();
        for (int i = 0; i < mSelectList.size(); i++) {
            for (int j = 0; j < mUList.size(); j++) {
                if (mSelectList.get(i).getUserName().equals(mUList.get(j).getUserName())) {
                    SelectUser = SelectUser + 1;
                }
            }
        }
    }
}
