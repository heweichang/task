package com.example.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.Adapters.TaskingAdapter;
import com.example.beans.task;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyTaskingActivity extends Activity {

    private List<task> mData = null;
    private Context mContext;
    private TaskingAdapter mAdapter = null;
    private ListView list_tasking;
    private List<Map<String, String>> list;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytasking);
        initView();
    }

    private void initView() {
        userid = SharedPreferencesUtils.getParam(MyTaskingActivity.this, "iduser", "").toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid",userid);
        list = new ArrayList<Map<String, String>>();
        list.add(map);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        new TaskingAsyncTask().execute(json);
        list_tasking = (ListView)findViewById(R.id.list_tasking);
        list_tasking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position 点击的Item位置，从0开始算
                Intent intent= new Intent(MyTaskingActivity.this,MyTaskDetailActivity.class);
                intent.putExtra("taskid", mData.get(position).getTask_id());
                intent.putExtra("tname", mData.get(position).getTask_name());
                intent.putExtra("tdate", mData.get(position).getTask_date());
                intent.putExtra("taddr", mData.get(position).getTask_addr());
                intent.putExtra("treq", mData.get(position).getTask_req());
                intent.putExtra("tcontent", mData.get(position).getTask_content());
                intent.putExtra("uname", mData.get(position).getUser_name());
                intent.putExtra("tlatitude", mData.get(position).getTask_latitude());
                intent.putExtra("tlongitude", mData.get(position).getTask_longitude());
                startActivity(intent);
                MyTaskingActivity.this.finish();
            }
        });
    }
    class TaskingAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/MyTask.do";
            String json = params[0];
            String json1 = HttpUtils.doPost(url,json);
            return json1;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = MyTaskingActivity.this;

            mData = new LinkedList<task>();
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String taskid = jsonObject.getString("taskid");
                    String tname = jsonObject.getString("tname");
                    String tcontent = jsonObject.getString("tcontent");
                    String tdate = jsonObject.getString("tdate");
                    String taddr = jsonObject.getString("taddr");
                    String treq = jsonObject.getString("treq");
                    String tover = jsonObject.getString("tover");
                    String uname = jsonObject.getString("uname");
                    String tlatitude = jsonObject.getString("tlatitude");
                    String tlongitude = jsonObject.getString("tlongitude");
                    Log.d("taskingActivity", "stu_no: " + taskid);
                    Log.d("taskingActivity", "stu_no: " + tcontent);
                    Log.d("taskingActivity","stu_name: " + tname);
                    mData.add(new task(taskid, tname, tdate, taddr,treq,tcontent,tover,uname,"",tlatitude,tlongitude));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAdapter = new TaskingAdapter((LinkedList<task>)mData,mContext);
            list_tasking.setAdapter(mAdapter);
        }
    }
}
