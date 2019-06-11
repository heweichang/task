package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Activity.R;
import com.example.beans.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 何伟昌 on 2019/2/26.
 */
public class TaskingAdapter extends BaseAdapter implements View.OnClickListener{
    private LinkedList<task> mData;
    private Context mContext;
    private TextView textView1,textView2,textView3;
    private List<Map<String, String>> list = new ArrayList<>();

    public TaskingAdapter(LinkedList<task> mData,Context mContext){
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.taskedlist_item, parent, false);
        textView1 = (TextView)convertView.findViewById(R.id.task_name);
        textView2 = (TextView)convertView.findViewById(R.id.task_content);
        textView3 = (TextView)convertView.findViewById(R.id.task_id);

        textView3.setText(mData.get(position).getTask_id());
        textView1.setText(mData.get(position).getTask_name());
        textView2.setText(mData.get(position).getTask_content());
        return convertView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }
    }
}
