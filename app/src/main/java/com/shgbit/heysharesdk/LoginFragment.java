package com.shgbit.heysharesdk;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.shgbit.hshttplibrary.json.Meeting;
import com.shgbit.hssdk.callback.HSSDKInstantListener;
import com.shgbit.hssdk.sdk.HeyShareSDK;

public class LoginFragment extends Fragment{

    private String meetingId = "910048601916";
    private String meetingPwd = "603918";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        final Button btn_enterMeeting = view.findViewById(R.id.btn_enterMeeting);
        final Button btn_instant = view.findViewById(R.id.btn_instant);
        final EditText et_meetingId = view.findViewById(R.id.ed_meetingId);
        final EditText et_meetingPwd = view.findViewById(R.id.ed_meetingPwd);
        et_meetingId.setText(meetingId);
        et_meetingPwd.setText(meetingPwd);
        btn_enterMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isLogin) {
                    HeyShareSDK.getInstance().joinMeeting(et_meetingId.getText().toString(), et_meetingPwd.getText().toString());
                }
            }
        });

        btn_instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] a = new String[1];
                a[0] = "xudingxing";
                    if (Common.isLogin){
                        HeyShareSDK.getInstance().createInstantMeeting(a, true, new HSSDKInstantListener() {
                            @Override
                            public void onCreateMeeting(boolean b, String s, Meeting meeting) {

                            }
                        });
                    }
            }
        });
    }
}
