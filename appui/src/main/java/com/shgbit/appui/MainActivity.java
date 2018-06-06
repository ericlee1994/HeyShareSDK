package com.shgbit.appui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shgbit.hsuimodule.sdk.HSVideoSDK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button button;
    private EditText editText;
    private boolean isInit = false;
    private String meetingNumber;
    private TextView tvContent;
    private Button btn_reserve;
    private Button btn_instant;
    private String userName = "lizheng";
    private String[] inviteUsers = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        button = findViewById(R.id.btn_intent);

        btn_reserve = findViewById(R.id.btn_reserve);
        btn_instant = findViewById(R.id.btn_instant);
        tvContent = findViewById(R.id.tv_content);
        editText = findViewById(R.id.meetingid);
        tvContent = findViewById(R.id.tv_content);

        HSVideoSDK.getInstance().connect("test012", "123456");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                    meetingNumber = "910002785324";
                } else {
                    meetingNumber = editText.getText().toString();
                }
                HSVideoSDK.getInstance().startMeeting(meetingNumber, "603918", "abc", "00010");

            }
        });

    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 0);
        }
    }
}
