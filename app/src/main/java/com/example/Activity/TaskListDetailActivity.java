package com.example.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 何伟昌 on 2019/4/11.
 */
public class TaskListDetailActivity extends Activity {

    private List<Map<String, String>> list = new ArrayList<>();
    private TextView tv_task_id,tv_task_name,tv_task_addr,tv_task_req,tv_task_content,tv_task_date,task_get;
    public String taskid,userid,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklistdetail);
        initView();
    }

    private void initView() {
        tv_task_id = (TextView)findViewById(R.id.task_id);
        tv_task_name = (TextView)findViewById(R.id.task_name);
        tv_task_date = (TextView)findViewById(R.id.task_date);
        tv_task_addr = (TextView)findViewById(R.id.task_addr);
        tv_task_req = (TextView)findViewById(R.id.task_req);
        tv_task_content = (TextView)findViewById(R.id.task_content);
        task_get = (TextView)findViewById(R.id.task_get);
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        tv_task_id.setText(intent.getStringExtra("taskid"));
        tv_task_name.setText(intent.getStringExtra("tname"));
        tv_task_date.setText(intent.getStringExtra("tdate"));
        tv_task_addr.setText(intent.getStringExtra("taddr"));
        tv_task_req.setText(intent.getStringExtra("treq"));
        tv_task_content.setText(intent.getStringExtra("tcontent"));

        task_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<String, String>();
                userid = SharedPreferencesUtils.getParam(TaskListDetailActivity.this, "iduser", "").toString();
                username = SharedPreferencesUtils.getParam(TaskListDetailActivity.this, "username","").toString();
                Log.i("huangyouchou:",userid);
                map.put("userid", userid);
                map.put("username", username);
                map.put("taskid",taskid );
                Log.i("huangyouchou1:", taskid);
                Log.i("huangyouchou3:", username);
                Log.i("huangyouchou2:", map.toString());
                list.add(map);
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.i("demo", "client:" + json);
                new UpAsyncTask().execute(json);
            }
        });

    }
    class UpAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = Constant.BASE_URL+"/Taskaccept.do";
            String json = params[0];

            Log.i("demo", "params[0]" + json);
            HttpUtils.doPost(url, json);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            Toast.makeText(TaskListDetailActivity.this, "领取成功!至我的页面查看任务！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TaskListDetailActivity.this, TaskActivity.class);
            intent.putExtra("task_location", " ");
            startActivity(intent);
            super.onPostExecute(result);
        }
    }
}
