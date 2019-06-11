package com.example.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapters.TaskAdapter;
import com.example.beans.task;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.CustomDatePicker;
import com.example.tools.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by 何伟昌 on 2018/3/25.
 */
public class TaskActivity extends Activity {

    private List<task> mData = null;
    private Context mContext;
    private TaskAdapter mAdapter = null;
    private ListView list_task;
    private List<Map<String, String>> list;
    private ImageView img,tasklist_return;
    private TextView tv_bt;
    private CustomDatePicker customDatePicker;
    private String now;
    Intent Mapintent;
    String task_location,latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initView();
    }

    private void initView() {
        tasklist_return = (ImageView)findViewById(R.id.tasklist_return);
        new DownAsyncTask().execute("");
        Mapintent = getIntent();
        task_location = Mapintent.getStringExtra("task_location");
        latitude = Mapintent.getStringExtra("tlatitude");
        longitude = Mapintent.getStringExtra("tlongitude");
        Log.d("taskActivity", "endPt.longitude: " + longitude);
        Log.d("taskActivity", "endPt.longitude: " + latitude);
        Log.d("taskActivity", "stu_name: " + task_location);
        if(!task_location.equals(" ")){
            showAddDialog();
        }
        list = new ArrayList<Map<String, String>>();
        list_task = (ListView)findViewById(R.id.list_task);
        list_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position 点击的Item位置，从0开始算

            }
        });
        tv_bt = (TextView) findViewById(R.id.tv_biaoti);
        tv_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(TaskActivity.this, GetTaskDetailActivity.class);
                startActivity(intent1);
            }
        });
        img = (ImageView) findViewById(R.id.menu);
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showPopupMenu(img);
            }
        });
        tasklist_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnMain = new Intent(TaskActivity.this,MainActivity.class);
                startActivity(returnMain);
            }
        });
    }
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String str = item.getTitle().toString();

                        if ("发布任务".equals(str)) {
                            String userpri = SharedPreferencesUtils.getParam(TaskActivity.this, "privilege", "").toString();
                            if (userpri.equals("1")) {
                                showAddDialog();
                            }else {
                                Toast.makeText(TaskActivity.this, "抱歉，管理员才能发布任务！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }
    public void showAddDialog() {
        View view = LayoutInflater.from(TaskActivity.this).inflate(
                R.layout.tasklist_add_item, null);
        final TextView get_location = (TextView) view.findViewById(R.id.get_location);
        final TextView get_date = (TextView) view.findViewById(R.id.get_date);

        final EditText task_name = (EditText) view.findViewById(R.id.et_taskname_add);

        final EditText task_addr = (EditText)view.findViewById(R.id.et_taskaddr_add);
        task_addr.setText(task_location);

        final EditText task_date = (EditText) view.findViewById(R.id.et_taskdate_add);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        //获取当前时间
        now = sdf.format(new Date());
        //tvElectricalTime.setText(now.split(" ")[0]);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                Log.d("yyyyy", time);
                task_date.setText(time);
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        final EditText task_req = (EditText) view.findViewById(R.id.et_taskreq_add);

        final EditText task_content = (EditText) view.findViewById(R.id.et_taskcontent_add);

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);

        builder.setTitle("发布任务");

        builder.setView(view);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, ShowMapActivity.class);
                startActivity(intent);
            }
        });
        get_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(task_date.getText().toString().trim()))
                    customDatePicker.show(now);
                else  // 日期格式为yyyy-MM-dd
                    customDatePicker.show(task_date.getText().toString());
            }
        });
        builder.setPositiveButton("发布", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String FileTime = timesdf.format(new Date()).toString();//获取系统时间
                String FileTime1 = FileTime.replace(" ", "");
                String FileTime2 = FileTime1.replace("-", "");
                String taskid = FileTime2.replace(":", "");

                String tname = task_name.getText().toString();
                String tdate = task_date.getText().toString();
                String taddr = task_addr.getText().toString();
                String treq = task_req.getText().toString();
                String tcontent = task_content.getText().toString();
                String tlatitude = latitude;
                String tlongitude = longitude;
                Map<String, String> map = new HashMap<String, String>();
                map.put("taskid", taskid);
                map.put("tname", tname);
                map.put("tdate", tdate);
                map.put("taddr", taddr);
                map.put("treq", treq);
                map.put("tcontent", tcontent);
                map.put("tlatitude", tlatitude);
                map.put("tlongitude", tlongitude);
                list.add(map);
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.i("demo", "client:" + json);
                new UpAsyncTask().execute(json);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        builder.create();
        builder.show();
    }

    class UpAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = Constant.BASE_URL+"/Taskadd.do";
            String json = params[0];

            Log.i("demo", "params[0]" + json);
            HttpUtils.doPost(url, json);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            Toast.makeText(TaskActivity.this, "发布完毕！", Toast.LENGTH_SHORT)
                    .show();
            new DownAsyncTask().execute("");
            super.onPostExecute(result);
        }
    }

    class DownAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/listTaskall.do";

            String json = HttpUtils.doPost(url, "");
            Log.d("taskActivity", "json_no: " + json);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = TaskActivity.this;

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
                    String treq = jsonObject.getString("treq");
                    String taddr = jsonObject.getString("taddr");
                    Log.d("taskActivity", "stu_no: " + taskid);
                    Log.d("taskActivity", "stu_no: " + tcontent);
                    Log.d("taskActivity","stu_name: " + tname);
                    mData.add(new task(taskid, tname, tcontent, tdate, taddr, treq));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAdapter = new TaskAdapter((LinkedList<task>)mData,mContext);
            list_task.setAdapter(mAdapter);
        }
    }
}
