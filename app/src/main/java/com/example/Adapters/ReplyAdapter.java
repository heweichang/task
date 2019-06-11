package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Activity.R;
import com.example.beans.Reply;

import java.util.LinkedList;

/**
 * Created by 何伟昌 on 2019/4/10.
 */
public class ReplyAdapter extends BaseAdapter implements View.OnClickListener{

    private LinkedList<Reply> mData;
    private Context mContext;
    private TextView username,replycontent;

    public ReplyAdapter(LinkedList<Reply> mData,Context mContext){
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.reply_item, parent, false);
        username = (TextView)convertView.findViewById(R.id.user_name);
        replycontent = (TextView)convertView.findViewById(R.id.reply_content);

        username.setText(mData.get(position).getUsername());
        replycontent.setText(mData.get(position).getReplycontent());
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
