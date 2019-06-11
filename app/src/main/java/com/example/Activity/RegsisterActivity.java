package com.example.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegsisterActivity extends Activity{
    private Button zhuce;
    private EditText username;
    private EditText password;
    private List<Map<String, String>> list;
    private VideoView mVideoView;
    private ImageView mIvVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsister);

        username = (EditText) findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        zhuce = (Button)findViewById(R.id.zhuce);
        list = new ArrayList<Map<String, String>>();

        mVideoView = (VideoView) findViewById(R.id.login_video_view);
        mIvVideo = (ImageView) findViewById(R.id.login_iv_video);

        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = username.getText().toString();
                String upassword =  password.getText().toString();

                Map<String, String> map = new HashMap<String, String>();
                map.put("username", uname);
                map.put("password", upassword);


                list.add(map);
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.i("demo", "client:" + json);
                new UpAsyncTask().execute(json);



            }
        });
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/video_login"));
        //循环播放
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });

        //防止照片隐藏黑屏
        mVideoView.start();
        mVideoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIvVideo.setVisibility(View.GONE);
            }
        }, 1000);
    }

    class UpAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = Constant.BASE_URL+"/useradd.do";
            String json = params[0];

            Log.i("demo", "params[0]" + json);
            HttpUtils.doPost(url, json);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            Toast.makeText(RegsisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegsisterActivity.this, LoginActivity.class);
            startActivity(intent);
            super.onPostExecute(result);
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
