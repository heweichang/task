package com.example.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.Activity.R;
import com.example.Activity.TaskListDetailActivity;
import com.example.beans.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 何伟昌 on 2018/4/8.
 */
public class TaskAdapter extends BaseAdapter implements View.OnClickListener {

    private LinkedList<task> mData;
    private Context mContext;
    private TextView textView1,textView2,textView3;
    private Button task_accept;
    private List<Map<String, String>> list = new ArrayList<>();
    public String taskid,userid,username;

    public TaskAdapter(LinkedList<task> mData,Context mContext){
        this.mData = mData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.tasklist_item, parent, false);
        textView1 = (TextView)convertView.findViewById(R.id.task_name);
        textView2 = (TextView)convertView.findViewById(R.id.task_content);
        textView3 = (TextView)convertView.findViewById(R.id.task_id);
        task_accept = (Button)convertView.findViewById(R.id.task_accept);

        textView3.setText(mData.get(position).getTask_id());
        textView1.setText(mData.get(position).getTask_name());
        textView2.setText(mData.get(position).getTask_content());

        task_accept.setTag(R.id.task_accept,position);
        task_accept.setOnClickListener(this);
        return convertView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.task_accept:
                Intent intent = new Intent(mContext.getApplicationContext(), TaskListDetailActivity.class);
                int position = (int) v.getTag(R.id.task_accept);
                intent.putExtra("taskid", mData.get(position).getTask_id());
                intent.putExtra("tname", mData.get(position).getTask_name());
                intent.putExtra("tdate", mData.get(position).getTask_date());
                intent.putExtra("taddr", mData.get(position).getTask_addr());
                intent.putExtra("treq", mData.get(position).getTask_req());
                intent.putExtra("tcontent", mData.get(position).getTask_content());
                mContext.startActivity(intent);
        }
    }
}
