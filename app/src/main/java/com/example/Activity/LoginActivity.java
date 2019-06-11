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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button londa;
    static EditText nameText;
    static EditText passwordText;
    private Boolean check=false;
    private TextView zhuce;
    private VideoView mVideoView;
    private ImageView mIvVideo;
    String iduser,privilege,username,passwd,userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        londa = (Button) findViewById(R.id.login_bt_login);
        nameText = (EditText)findViewById(R.id.login_et_username);
        passwordText = (EditText)findViewById(R.id.login_et_password);
        zhuce = (TextView)findViewById(R.id.zhuce);

        mVideoView = (VideoView) findViewById(R.id.login_video_view);
        mIvVideo = (ImageView) findViewById(R.id.login_iv_video);

        londa.setOnClickListener(this);
        zhuce.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_bt_login:
                new CheckAsyncTask().execute("");
                break;
            case R.id.zhuce:
                Intent intent1 = new Intent();
                intent1 = new Intent(LoginActivity.this, RegsisterActivity.class);
                startActivity(intent1);
                break;

        }
    }

    class CheckAsyncTask extends AsyncTask<String, Void, String> {

        Intent intent = new Intent();
        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/listUserall.do            ";

            String json = HttpUtils.doPost(url, "");

            return json;
        }
        @Override
        protected void onPostExecute(String result) {

            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    iduser = jsonObject.getString("iduser");
                    privilege = jsonObject.getString("privilege");
                    username = jsonObject.getString("username");
                    passwd = jsonObject.getString("userpasswd");
                    userimage = jsonObject.getString("userimage");
                    Log.i("message:",username + passwd);
                    if (username.equals(nameText.getText().toString())&&passwd.equals(passwordText.getText().toString())){
                        check = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (check){
                SharedPreferencesUtils.setParam(LoginActivity.this, "iduser", iduser);
                SharedPreferencesUtils.setParam(LoginActivity.this, "username", username);
                SharedPreferencesUtils.setParam(LoginActivity.this, "privilege", privilege);
                SharedPreferencesUtils.setParam(LoginActivity.this, "userimage", userimage);
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
