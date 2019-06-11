package com.example.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tools.SharedPreferencesUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyActivity extends Fragment {

    private RelativeLayout rlt_persondata,rlt_tasking,rlt_tasked,rlt_feedback;
    private ImageView imageView,userimage;
    private TextView tv_username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_my, null);
        initView(view);
        return view;
    }

    private void initView(View v) {
        rlt_persondata = (RelativeLayout) v.findViewById(R.id.rlt_persondata);
        rlt_tasking = (RelativeLayout) v.findViewById(R.id.rlt_tasking);
        rlt_tasked = (RelativeLayout) v.findViewById(R.id.rlt_tasked);
        rlt_feedback = (RelativeLayout) v.findViewById(R.id.rlt_feedback);
        tv_username = (TextView)v.findViewById(R.id.user_name);
        String username = SharedPreferencesUtils.getParam(v.getContext(), "username", "").toString();
        String user_image = SharedPreferencesUtils.getParam(v.getContext(), "userimage", "").toString();
        tv_username.setText(username);
        userimage = (ImageView)v.findViewById(R.id.user_image);
        if(user_image.equals("null")|| TextUtils.isEmpty(user_image)) {
            user_image = "http://119.23.232.90:8080/image/xiaomf.jpg";
        }
        DownLoadTask task = new DownLoadTask(userimage);
        task.execute(user_image);
        imageView = (ImageView)v.findViewById(R.id.shezhi);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        rlt_tasking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), MyTaskingActivity.class);
                startActivity(intent1);
            }
        });
        rlt_tasked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(view.getContext(), MyTaskedActivity.class);
                startActivity(intent2);
            }
        });
        rlt_persondata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(view.getContext(), PersonDataActivity.class);
                startActivity(intent3);
            }
        });
        rlt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent4 = new Intent(view.getContext(), TestActivity.class);
//                startActivity(intent4);
            }
        });
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
