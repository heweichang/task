package com.example.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.Adapters.TaskingAdapter;
import com.example.beans.task;
import com.example.https.HttpUtils;
import com.example.tools.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TaskScoreActivity extends AppCompatActivity {

    private List<task> mData = null;
    private Context mContext;
    private TaskingAdapter mAdapter = null;
    private ListView list_taskpatroled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskscore);

        initView();
    }

    private void initView() {
        new DownPatroledAsyncTask().execute("");
        list_taskpatroled = (ListView)findViewById(R.id.list_taskpatroled);
        list_taskpatroled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position 点击的Item位置，从0开始算
                Intent intent = new Intent(TaskScoreActivity.this, TaskScoreDetailActivity.class);
                intent.putExtra("taskid", mData.get(position).getTask_id());
                intent.putExtra("tname", mData.get(position).getTask_name());
                intent.putExtra("tdate", mData.get(position).getTask_date());
                intent.putExtra("taddr", mData.get(position).getTask_addr());
                intent.putExtra("treq", mData.get(position).getTask_req());
                intent.putExtra("tcontent", mData.get(position).getTask_content());
                intent.putExtra("uname", mData.get(position).getUser_name());
                intent.putExtra("tcomment", mData.get(position).getTask_comment());
                startActivity(intent);
            }
        });
    }
    class DownPatroledAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/listPatroledall.do";

            String json = HttpUtils.doPost(url, "");

            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = TaskScoreActivity.this;

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
                    String tcomment = jsonObject.getString("tcomment");
                    Log.d("taskActivity", "stu_no: " + taskid);
                    Log.d("taskActivity", "stu_no: " + tcontent);
                    Log.d("taskActivity","stu_name: " + tname);
                    mData.add(new task(taskid, tname, tdate, taddr,treq,tcontent,tover,uname,tcomment,"",""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAdapter = new TaskingAdapter((LinkedList<task>)mData,mContext);
            list_taskpatroled.setAdapter(mAdapter);
        }
    }
}
