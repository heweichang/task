package com.example.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapters.ImageAdapter;
import com.example.Adapters.ReplyAdapter;
import com.example.beans.Image;
import com.example.beans.Reply;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 何伟昌 on 2018/6/26.
 */
public class PostDetailActivity extends Activity {

    private ImageView userimage;
    private TextView username,posttitle,postcontent,posttime;
    private LinkedList<Image> mData1 = null;
    private LinkedList<Reply> mData2 = null;
    private List<Map<String, String>> list,list1;
    private Context mContext;
    private ImageAdapter mAdapter1 = null;
    private ReplyAdapter mAdapter2 = null;
    private ListView list_image1 , list_reply;
    private EditText postcomment;
    private Button btcomment;
    String postid;
    String json;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        initView();
    }
    private void initView(){
        btcomment = (Button)findViewById(R.id.bt_comment);
        postcomment = (EditText)findViewById(R.id.post_comment);
        username = (TextView)findViewById(R.id.user_name);
        userimage = (ImageView)findViewById(R.id.user_image);
        posttitle = (TextView)findViewById(R.id.post_title);
        postcontent = (TextView)findViewById(R.id.post_content);
        posttime = (TextView)findViewById(R.id.post_time);
        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("postid", postid);
        list = new ArrayList<Map<String, String>>();
        list.add(map);
        Gson gson = new Gson();
        json = gson.toJson(list);
        new DownUImageAsyncTask().execute(json);
        username.setText(intent.getStringExtra("username"));
        posttitle.setText(intent.getStringExtra("posttitle"));
        postcontent.setText(intent.getStringExtra("postcontent"));
        posttime.setText(intent.getStringExtra("posttime"));
        DownUserImageTask utask = new DownUserImageTask(userimage);
        utask.execute(intent.getStringExtra("userimage"));
        new DownReplyAsyncTask().execute(json);
        btcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SharedPreferencesUtils.getParam(PostDetailActivity.this, "iduser", "").toString();
                String username = SharedPreferencesUtils.getParam(PostDetailActivity.this, "username", "").toString();
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("postid",postid);
                map1.put("comment",postcomment.getText().toString());
                map1.put("userid", userid);
                map1.put("username", username);
                list1 = new ArrayList<Map<String, String>>();
                list1.add(map1);
                Gson gson1 = new Gson();
                String json1 = gson1.toJson(list1);
                String url = Constant.BASE_URL+"/Replyadd.do";
                new upPostCommentTask().execute(json1,url);
                new DownUImageAsyncTask().execute(json);
                new DownReplyAsyncTask().execute(json);
                postcomment.setText("");
            }
        });
    }
    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    class DownReplyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/TopicReply.do";
            String json = params[0];
            Log.i("demo", "params[0]" + json);
            String json1 = HttpUtils.doPost(url, json);
            return json1;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = PostDetailActivity.this;
            mData2 = new LinkedList<Reply>();
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    String replycontent = jsonObject.getString("replycontent");

                    Log.d("taskActivity", "stu_no: " + username);
                    Log.d("taskActivity", "stu_no: " + replycontent);

                    mData2.add(new Reply(username,replycontent));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_reply = (ListView)findViewById(R.id.list_reply);
            mAdapter2 = new ReplyAdapter((LinkedList<Reply>)mData2,mContext);
            list_reply.setAdapter(mAdapter2);
            setListViewHeightBasedOnChildren(list_reply);
        }
    }
    class upPostCommentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String json = params[0];
            String url = params[1];

            Log.i("demo", "params[0]" + json);
            HttpUtils.doPost(url, json);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
        }
    }
    class DownUImageAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/DownTopicImage.do";
            String json = params[0];
            String json1 = HttpUtils.doPost(url, json);
            return json1;
        }
        @Override
        protected void onPostExecute(String result) {
            mContext = PostDetailActivity.this;
            mData1 = new LinkedList<Image>();
            //mData2 = new LinkedList<Image>();
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imagepath = jsonObject.getString("imagepath");
                    Log.d("taskingActivity", "imagepath: " + imagepath);

                    mData1.add(new Image(imagepath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_image1 = (ListView)findViewById(R.id.list_image);
            //list_image2 = (ListView)findViewById(R.id.list_image2);
            mAdapter1 = new ImageAdapter((LinkedList<Image>)mData1,mContext);
            //mAdapter2 = new ImageAdapter((LinkedList<Image>)mData2,mContext);
            list_image1.setAdapter(mAdapter1);
            //list_image2.setAdapter(mAdapter2);
        }
    }

    /**
     * 异步加载图片
     */
    class DownUserImageTask extends AsyncTask<String ,Void,Bitmap> {
        private ImageView mImageView;
        String url;
        public DownUserImageTask(ImageView imageView){
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
