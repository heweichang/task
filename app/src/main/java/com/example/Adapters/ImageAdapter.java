package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.Activity.R;
import com.example.beans.Image;

import java.util.LinkedList;

/**
 * Created by 何伟昌 on 2019/3/7.
 */
public class ImageAdapter extends BaseAdapter implements View.OnClickListener{

    private LinkedList<Image> mData;
    private Context mContext;
    private ImageView imageView;
    String url;

    public ImageAdapter(LinkedList<Image> mData,Context mContext){
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.imagelist_item, parent, false);
        imageView = (ImageView)convertView.findViewById(R.id.image);
        url = mData.get(position).getImagepath();
        Glide.with(mContext)
                .load(url)
                .into(imageView);
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

}
