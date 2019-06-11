package com.example.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonDataActivity extends AppCompatActivity {

    private TextView user_name,user_pri,user_phone,user_addr,modify_data;
    private ImageView user_image;
    private List<Map<String, String>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondata);

        initView();
    }

    private void initView() {
        user_name = (TextView)findViewById(R.id.user_name);
        user_pri = (TextView)findViewById(R.id.user_pri);
        user_phone = (TextView)findViewById(R.id.user_phone);
        user_addr = (TextView)findViewById(R.id.user_addr);
        user_image = (ImageView)findViewById(R.id.user_image);
        modify_data = (TextView)findViewById(R.id.modify_data);

        String username = SharedPreferencesUtils.getParam(PersonDataActivity.this, "username", "").toString();
        String userpri = SharedPreferencesUtils.getParam(PersonDataActivity.this, "privilege", "").toString();
        String userimage = SharedPreferencesUtils.getParam(PersonDataActivity.this, "userimage", "").toString();
        Log.i("message:", userimage);
        if(userimage.equals("null")|| TextUtils.isEmpty(userimage)) {
            userimage = "http://119.23.232.90:8080/image/xiaomf.jpg";
        }
        DownLoadTask task = new DownLoadTask(user_image);
        task.execute(userimage);
        user_name.setText(username);
        if(userpri.equals("1")){
            user_pri.setText("管理员");
        }else{
            user_pri.setText("普通用户");
        }
        modify_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonDataActivity.this, ModifyDataActivity.class);
                startActivity(intent);
       }});
        String userid = SharedPreferencesUtils.getParam(PersonDataActivity.this, "iduser", "").toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid",userid);
        list = new ArrayList<Map<String, String>>();
        list.add(map);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        new UserdateAsyncTask().execute(json);
    }
    class UserdateAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/UserDate.do";
            String json = params[0];
            String json1 = HttpUtils.doPost(url, json);
            return json1;
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
                    String username = jsonObject.getString("username");
                    String userphone = jsonObject.getString("userphone");
                    String useraddr = jsonObject.getString("useraddr");
                    user_phone.setText(userphone);
                    user_addr.setText(useraddr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * 异步加载图片
     */
    class DownLoadTask extends AsyncTask<String ,Void,Bitmap> {
        private ImageView mImageView;
        String url;
        public DownLoadTask(ImageView imageView){
            mImageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = downLoadBitmap(url);
            //BitmapDrawable drawable = new BitmapDrawable(bitmap);
            return  bitmap;
        }

        private Bitmap downLoadBitmap(String url) {
            Bitmap bitmap = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if ( mImageView != null && bitmap != null){
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
