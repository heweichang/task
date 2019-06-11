package com.example.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapters.ImageAdapter;
import com.example.beans.Image;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PatrolDetailActivity extends AppCompatActivity {

    private TextView tv_task_id,tv_task_name,tv_task_addr,tv_task_req,tv_task_content,tv_task_date,tv_user_name;
    private TextView tv_patroldiscuss;
    String task_id;
    private List<Map<String, String>> list;
    private LinkedList<Image> mData1 = null , mData2 = null;
    private Context mContext;
    private ImageAdapter mAdapter1 = null,mAdapter2 = null;
    private ListView list_image1 , list_image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patroldetail);
        initView();
    }

    private void initView() {
        tv_patroldiscuss = (TextView)findViewById(R.id.tv_patroldiscuss);
        tv_task_id = (TextView)findViewById(R.id.task_id);
        tv_task_name = (TextView)findViewById(R.id.task_name);
        tv_task_date = (TextView)findViewById(R.id.task_date);
        tv_task_addr = (TextView)findViewById(R.id.task_addr);
        tv_task_req = (TextView)findViewById(R.id.task_req);
        tv_task_content = (TextView)findViewById(R.id.task_content);
        tv_user_name = (TextView)findViewById(R.id.user_name);
        Intent intent = getIntent();
        task_id = intent.getStringExtra("taskid");
        tv_task_id.setText(intent.getStringExtra("taskid"));
        tv_task_name.setText(intent.getStringExtra("tname"));
        tv_task_date.setText(intent.getStringExtra("tdate"));
        tv_task_addr.setText(intent.getStringExtra("taddr"));
        tv_task_req.setText(intent.getStringExtra("treq"));
        tv_task_content.setText(intent.getStringExtra("tcontent"));
        tv_user_name.setText(intent.getStringExtra("uname"));
        tv_patroldiscuss.setText(intent.getStringExtra("tcomment"));
        Map<String, String> map = new HashMap<String, String>();
        map.put("taskid",task_id);
        list = new ArrayList<Map<String, String>>();
        list.add(map);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        new DownPImageAsyncTask().execute(json);
    }
    class DownPImageAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/DownPImage.do";
            String json = params[0];
            String json1 = HttpUtils.doPost(url, json);
            return json1;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = PatrolDetailActivity.this;
            mData1 = new LinkedList<Image>();
            mData2 = new LinkedList<Image>();
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imagepath = jsonObject.getString("imagepath");
                    String imagecheck = jsonObject.getString("imagecheck");
                    Log.d("taskingActivity", "imagepath: " + imagepath);
                    Log.d("taskingActivity", "imagecheck: " + imagecheck);
                    if(imagecheck.equals("ok")) {
                        mData2.add(new Image(imagepath));
                    } else {
                        mData1.add(new Image(imagepath));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_image1 = (ListView)findViewById(R.id.list_image1);
            list_image2 = (ListView)findViewById(R.id.list_image2);
            mAdapter1 = new ImageAdapter((LinkedList<Image>)mData1,mContext);
            mAdapter2 = new ImageAdapter((LinkedList<Image>)mData2,mContext);
            list_image1.setAdapter(mAdapter1);
            list_image2.setAdapter(mAdapter2);
        }
    }
}
