package com.example.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Adapters.PostAdapter;
import com.example.beans.Post;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何伟昌 on 2018/5/22.
 */
public class DiscussActivity extends Fragment {

    private RecyclerView postRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_discuss, null);

        initView(view);
        new DownTopicAsyncTask().execute("");
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.gank_swipe_refresh_layout);
        postRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PostActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownTopicAsyncTask().execute("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    class DownTopicAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/listTopicall.do";

            String json = HttpUtils.doPost(url, "");

            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            List<Post> postList = new ArrayList<Post>();
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = jsonArray.length()-1; i>= 0 ; i--) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String postid = jsonObject.getString("topicid");
                    String posttitle = jsonObject.getString("topictitle");
                    String postcontent = jsonObject.getString("topiccontent");
                    String postctime = jsonObject.getString("topicctime");
                    String userid = jsonObject.getString("userid");
                    String username = jsonObject.getString("username");
                    String userimage = jsonObject.getString("userimage");
                    String postimage = jsonObject.getString("imagepath");
                    Log.d("taskActivity", "stu_no: " + postimage);
                    Log.d("taskActivity", "stu_no: " + posttitle);
                    Log.d("taskActivity", "stu_name: " + postcontent);
                    postList.add(new Post(postid,posttitle,postcontent,postctime,userid,username,userimage,postimage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PostAdapter adapter = new PostAdapter(postList);
            postRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
