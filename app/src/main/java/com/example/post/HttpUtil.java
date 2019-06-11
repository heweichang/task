package com.example.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.google.gson.Gson;
import com.lzy.imagepicker.bean.ImageItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private PostFormBuilder mPost;
    private GetBuilder mGet;
    private List<Map<String, String>> list;
    private Context mContext;

    public HttpUtil() {
        OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .build();

        mPost = OkHttpUtils.post();
        mGet = OkHttpUtils.get();
    }

    //封装请求
    public void postRequest(String url, Map<String, String> params, MyStringCallBack callback) {
        mPost.url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    //发帖
    public void postPostRequest(String url, String title, String comment,String userid ,String topicid,ArrayList<ImageItem> pathList,Context mContext1, MyStringCallBack callback) {

        mContext  = mContext1;
        Map<String, String> map = new HashMap<String, String>();
        map.put("title",title);
        map.put("comment", comment);
        map.put("userid", userid);
        map.put("topicid", topicid);
        list = new ArrayList<Map<String, String>>();
        list.add(map);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        String photourl = Constant.BASE_URL+"/uploadPostimage.do";
        for (int i = 0; i < pathList.size(); i++) {
            new NetPhotoTask().execute(pathList.get(i).path,photourl,topicid);
        }
        new upPostTask().execute(json,url);
        callback.onResponse("ok",200);
    }
    class upPostTask extends AsyncTask<String, Void, Void> {

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
            Toast.makeText(mContext, "发帖成功！", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }
    class NetPhotoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPhotoPost(params[0],params[1],params[2]);
        }

        @Override
        protected void onPostExecute(String result) {

            if(!"error".equals(result)) {
                Log.i("TAG", "图片地址 " + "http://119.23.232.90:8080/image" + result);
            }
        }
    }

    private String doPhotoPost(String imagePath,String url,String topicid) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(url)
                .post(requestBody)
                .addHeader("topicid", topicid)
                .build();

        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d("TAG", "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d("TAG", "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //修改资料与上传头像
    public void UserPhotoFileRequest(String url, String userid, String username,String userpasswd,String userphone,String useraddr,ArrayList<ImageItem> pathList, MyStringCallBack callback) {

        for (int i = 0; i < pathList.size(); i++) {
            new NetModifyUserTask().execute(pathList.get(i).path,userid,url,username,userpasswd,userphone,useraddr);
        }
    }
    class NetModifyUserTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doUserData(params[0], params[1], params[2], params[3], params[4],params[5],params[6]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!"error".equals(result)) {
                Log.i("TAG", "图片地址 " + "http://119.23.232.90:8080/image" + result);
            }
        }
    }

    private String doUserData(String imagePath,String userid,String url,String username,String userpasswd,String userphone,String useraddr) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(url)
                .post(requestBody)
                .addHeader("userid", userid)
                .addHeader("username", getValueEncoded(username))
                .addHeader("userpassword",getValueEncoded(userpasswd))
                .addHeader("userphone", getValueEncoded(userphone))
                .addHeader("useraddr",getValueEncoded(useraddr))
                .build();

        Log.d("TAG", "请求地址 " + "http://119.23.232.90:8080/xmtest/uploadimage.do");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d("TAG", "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d("TAG", "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //提交任务和巡逻上传
    public void postFileRequest(String url, String taskid, String tcomment,ArrayList<ImageItem> pathList, MyStringCallBack callback) {

        for (int i = 0; i < pathList.size(); i++) {
            new NetworkTask().execute(pathList.get(i).path,taskid,url,tcomment);
        }
        callback.onResponse("ok",200);
    }
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0],params[1],params[2],params[3]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!"error".equals(result)) {
                Log.i("TAG", "图片地址 " + "http://119.23.232.90:8080/image" + result);
            }
        }
    }

    private String doPost(String imagePath,String taskid,String url,String tcomment) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(url)
                .post(requestBody)
                .addHeader("taskid", taskid)
                .addHeader("tcomment", getValueEncoded(tcomment))
                .build();

        Log.d("TAG", "请求地址 " + "http://119.23.232.90:8080/xmtest/uploadimage.do");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d("TAG", "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d("TAG", "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private static String getValueEncoded(String value) {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return newValue;
    }
}
