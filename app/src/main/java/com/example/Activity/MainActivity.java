package com.example.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tools.CP;
import com.example.tools.IBtnCallListener;
import com.example.tools.RemindService;
import com.example.tools.SharedPreferencesUtils;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout btn_home, btn_discuss, btn_my;

    private HomeActivity homeActivity;
    private DiscussActivity discussActivity;
    private  MyActivity myActivity;
    long exitTime=0;// 退出时间
    HandlerThread handlerThread;
    Handler UIHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String username = SharedPreferencesUtils.getParam(MainActivity.this, "username", "").toString();
        if(username.equals("")){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        initView();
        bindRemindService();
    }
    private void bindRemindService() {
        RemindService.open(MainActivity.this);
    }
    private void initView() {
        addHandle();
        btn_home = (RelativeLayout) findViewById(R.id.btn_home);
        //btn_liaot = (RelativeLayout) findViewById(R.id.btn_liaot);
        btn_discuss = (RelativeLayout) findViewById(R.id.btn_discuss);
        btn_my = (RelativeLayout) findViewById(R.id.btn_my);
        btn_home.setOnClickListener(this);
        //btn_liaot.setOnClickListener(this);
        btn_discuss.setOnClickListener(this);
        btn_my.setOnClickListener(this);

        if(homeActivity==null){

            homeActivity = new HomeActivity();
            addFragment(homeActivity);
            showFragment(homeActivity);
        }else {
            showFragment(homeActivity);
        }
    }
    private void addHandle() {
        handlerThread = new HandlerThread("MainActivity");
        handlerThread.start();
        UIHandler = new Handler();
        CP.NotiHanler = new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                addNotification();
                            }
                        });
                        break;
                }
            }
        };
    }
    private void addNotification() {
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取系统服务
        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            noti = new Notification.Builder(MainActivity.this)
                    .setTicker("新消息")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("论坛有新公告！")
                    .setContentText("请及时查看")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                    .build();
        }
        notificationManager.notify(1, noti);
    }
    /** 捕捉按下返回键操作 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btn_home:
                if(homeActivity==null){

                    homeActivity = new HomeActivity();
                    addFragment(homeActivity);
                    showFragment(homeActivity);
                }else {
                    if (homeActivity.isHidden()) {
                        showFragment(homeActivity);
                    }
                }
                BackgroundColors(R.color.aqua, R.color.slategrey, R.color.slategrey, R.color.slategrey);
                break;
            case R.id.btn_discuss:
                if(discussActivity==null){

                    discussActivity = new DiscussActivity();
                    addFragment(discussActivity);
                    showFragment(discussActivity);
                }else {
                    if (discussActivity.isHidden()) {
                        showFragment(discussActivity);
                    }
                }
                BackgroundColors(R.color.slategrey,R.color.aqua, R.color.slategrey, R.color.slategrey);
                break;
            case R.id.btn_my:
                if(myActivity==null){
                    myActivity = new MyActivity();
                    addFragment(myActivity);
                    showFragment(myActivity);
                }else {
                    if (myActivity.isHidden()) {
                        showFragment(myActivity);
                    }
                }
                BackgroundColors(R.color.slategrey, R.color.slategrey, R.color.slategrey,R.color.aqua);
                break;
        }
    }
        private void showFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

        if (homeActivity != null) {
            ft.hide(homeActivity);
        }
        if (discussActivity != null) {
            ft.hide(discussActivity);
        }
        if (myActivity != null) {
            ft.hide(myActivity);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    private void addFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment);
        ft.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void BackgroundColors(int homeColor, int discussColor, int liaotColor, int myColor) {
        btn_home.setBackgroundColor(ContextCompat.getColor(this,homeColor));
        btn_discuss.setBackgroundColor(ContextCompat.getColor(this,discussColor));
        //btn_liaot.setBackgroundColor(ContextCompat.getColor(this, liaotColor));
        btn_my.setBackgroundColor(ContextCompat.getColor(this, myColor));
    }

    /** Fragment的回调函数 */
    @SuppressWarnings("unused")
    private IBtnCallListener btnCallListener;

    @Override
    public void onAttachFragment(Fragment fragment) {
        try {
            btnCallListener = (IBtnCallListener) fragment;
        } catch (Exception e) {
        }
        super.onAttachFragment(fragment);
    }
}
