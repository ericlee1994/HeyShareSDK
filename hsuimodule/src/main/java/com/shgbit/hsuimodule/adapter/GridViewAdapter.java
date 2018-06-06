package com.shgbit.hsuimodule.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.SESSIONTYPE;
import com.shgbit.hssdk.bean.STATUS;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hsuimodule.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mcontext;
    private List<MemberInfo> mDataList;
    private boolean isJoin;
    private TextView mTextName;
    private ImageView mImage;
    private List<MemberInfo> mScreenList;
    private List<MemberInfo> mOtherList;
    private View mLine;
    private LinearLayout mSpaceLayout;
    private boolean isUpdate;
    private ModeDto modeDto;

    public GridViewAdapter(Context c, List<MemberInfo> Data, List<MemberInfo> mShowList, List<MemberInfo> mHideList){
        mcontext=c;
        mScreenList = mShowList;
        mOtherList = mHideList ;
        mDataList=Data;
        mInflater = LayoutInflater.from(mcontext);
        isUpdate =false;
        modeDto =new ModeDto();
    }

    public void update (List<MemberInfo> data, List<MemberInfo> mShowList, List<MemberInfo> mHideList) {
        mDataList = data;
        mScreenList = mShowList;
        mOtherList = mHideList ;
        notifyDataSetChanged();
    }

    class ModeDto {
        private boolean hasFirstLoaded = false;

        public boolean isHasFirstLoaded() {
            return hasFirstLoaded;
        }

        public void setHasFirstLoaded(boolean hasFirstLoaded) {
            this.hasFirstLoaded = hasFirstLoaded;
        }

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
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.item_viewpopup, null);
        } else {
            v = convertView;
        }

        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.item_linear);
        LinearLayout.LayoutParams lpLayoutParams = new LinearLayout.LayoutParams(Common.SCREENHEIGHT/6, (Common.SCREENHEIGHT/6));
        relativeLayout.setLayoutParams(lpLayoutParams);

        mTextName = (TextView)v.findViewById(R.id.view_name);
        mImage = (ImageView)v.findViewById(R.id.img_status);
        mLine = (View)v.findViewById(R.id.item_line);

        if(position == mDataList.size()-1 || mDataList.size() ==1 || (position+1)%5 ==0){
            mLine.setVisibility(View.INVISIBLE);
        }else {
            mLine.setVisibility(View.VISIBLE);
        }

        mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH/45);
        mTextName.setTextColor(mcontext.getResources().getColor(R.color.white_color));
        mTextName.setText(mDataList.get(position).getDisplayName());

        if(mScreenList ==null && mOtherList == null) {
            if (mDataList.get(position).getStatus().equals(STATUS.INVITING) == false) {
                mImage.setImageResource(R.drawable.connect_again_selector);
            } else {
                mImage.setImageResource(R.drawable.connecting_selector);
            }
        }else {
            for(int i =0;i<mScreenList.size();i++){
                if(mDataList.get(position).getUserName().equals(mScreenList.get(i).getUserName()) && mDataList.get(position).getSessionType() == mScreenList.get(i).getSessionType()){
                    if (mDataList.get(position).getSessionType() == SESSIONTYPE.MOBILE){
                        if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal){
                            //手机图标
                            mImage.setImageResource(R.drawable.open_phone);
                        } else if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL ||  mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode){
                            //手机感叹号
                            mImage.setImageResource(R.drawable.open_nophone);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.PC){
                        if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal){
                            //PC图标
                            mImage.setImageResource(R.drawable.open_windows);
                        } else if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL ||  mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode ){
                            //PC感叹号
                            mImage.setImageResource(R.drawable.open_nowindows);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.CONTENTONLY || mDataList.get(position).getSessionType() == SESSIONTYPE.PC_CONTENT){
                        if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal){
                            //电脑图标
                            mImage.setImageResource(R.drawable.open_pc);
//                            mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT/90);
                        } else if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL ||  mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.ContentOnlyUnsend || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode){
                            //电脑感叹号
                            mImage.setImageResource(R.drawable.open_nopc);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.UNKNOW){
                        if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal){
                            //?图标
                            mImage.setImageResource(R.drawable.open_unknow);
                        } else if(mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode){
                            //?感叹号
                            mImage.setImageResource(R.drawable.open_nounknow);
                        }
                    }
                }
            }
            for(int j=0;j<mOtherList.size();j++) {
                if (mDataList.get(position).getUserName().equals(mOtherList.get(j).getUserName()) && mDataList.get(position).getSessionType() == mOtherList.get(j).getSessionType()) {
                    //"隐藏"图标"\"
                    if (mDataList.get(position).getSessionType() == SESSIONTYPE.MOBILE) {
                        if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal) {
                            //手机图标
                            mImage.setImageResource(R.drawable.close_phone);
                        } else if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode) {
                            //手机感叹号
                            mImage.setImageResource(R.drawable.close_nophone);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.PC) {
                        if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal) {
                            //PC图标
                            mImage.setImageResource(R.drawable.close_windows);
                        } else if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode) {
                            //PC感叹号
                            mImage.setImageResource(R.drawable.close_nowindows);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.CONTENTONLY || mDataList.get(position).getSessionType() == SESSIONTYPE.PC_CONTENT) {
                        if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal) {
                            //电脑图标
                            mImage.setImageResource(R.drawable.close_pc);
//                            mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT/90);
                        } else if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.ContentOnlyUnsend || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode) {
                            //电脑感叹号
                            mImage.setImageResource(R.drawable.close_nopc);
                        }
                    } else if (mDataList.get(position).getSessionType() == SESSIONTYPE.UNKNOW) {
                        if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Normal) {
                            //?图标
                            mImage.setImageResource(R.drawable.close_unknow);
                        } else if (mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Loading || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.Lost || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.NULL || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VideoMute || mDataList.get(position).getNet_status() == MemberInfo.NET_STATUS.VoiceMode) {
                            //?感叹号
                            mImage.setImageResource(R.drawable.close_nounknow);
                        }
                    }
                }
            }
//            if(mDataList.get(position).isContent()){
//                for(int i =0;i<mScreenList.size();i++){
//                    if(mDataList.get(position).getUserName().equals(mScreenList.get(i).getUserName()) && mDataList.get(position).getSessionType() == mScreenList.get(i).getSessionType()){
//                        mImage.setImageResource(R.drawable.close_pc_selector);
//                        mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT/90);
//                        break;
//                    }
//                }
//                for(int j=0;j<mOtherList.size();j++) {
//                    if (mDataList.get(position).getUserName().equals(mOtherList.get(j).getUserName()) && mDataList.get(position).getSessionType() == mOtherList.get(j).getSessionType()) {
//                        mImage.setImageResource(R.drawable.open_pc_selector);
//                        mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT/90);
//                        break;
//                    }
//                }
//            }else{
//
//
////            else {
////                for(int i =0;i<mScreenList.size();i++){
////                    if(mDataList.get(position).getUserName().equals(mScreenList.get(i).getUserName())){
////                        if(mDataList.get(position).isContent()){
////                            mImage.setImageResource(R.drawable.close_pc_selector);
////                        }else{
////                            mImage.setImageResource(R.drawable.close_video_selector);
////                        }
////                    }
////                }
////                for(int j=0;j<mOtherList.size();j++) {
////                    if (mDataList.get(position).getUserName().equals(mOtherList.get(j).getUserName())) {
////                        if (mDataList.get(position).isContent()) {
////                            mImage.setImageResource(R.drawable.open_pc_selector);
////                        } else {
////                            if (mDataList.get(position).getDataSourceID() == null || mDataList.get(position).getDataSourceID().equals("")) {
////                                mImage.setImageResource(R.drawable.open_novideo_selector);
////                            } else {
////                                mImage.setImageResource(R.drawable.open_video_selector);
////                            }
////
////                        }
////                    }
//               }
        }

        if (position == 0 && modeDto.isHasFirstLoaded()) {
            return v;
        }
        if (position == 0) {
            modeDto.setHasFirstLoaded(true);
        }

        return v;
        }
}
