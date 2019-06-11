package com.example.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.Adapters.IconAdapter;
import com.example.CycleViewPager.CycleViewPager;
import com.example.CycleViewPager.CycleViewPager.ImageCycleViewListener;
import com.example.CycleViewPager.ViewFactory;
import com.example.beans.ADInfo;
import com.example.beans.Icon;
import com.example.https.HttpUtils;
import com.example.tools.Constant;
import com.example.tools.SharedPreferencesUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何伟昌 on 2018/3/25.
 */
public class HomeActivity extends Fragment{

    private RelativeLayout btn_task,btn_daka;

    private CycleViewPager cycleViewPager;

    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;
    private Context context;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private String[] imageUrls = new String[5];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_home, null);
        context = view.getContext();
        configImageLoader();
        initView(view);
        new DownNewsAsyncTask().execute("");
        //initialize();
        return view;
    }

    private void initView(View view) {

        grid_photo = (GridView) view.findViewById(R.id.grid_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.task, "任务"));
        mData.add(new Icon(R.mipmap.daka, "签到"));
        mData.add(new Icon(R.mipmap.patrol, "巡逻"));
        mData.add(new Icon(R.mipmap.pingfen, "评分"));

        mAdapter = new IconAdapter<Icon>(mData, R.layout.icon_item) {
            @Override
            public void bindView(ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };

        grid_photo.setAdapter(mAdapter);

        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(view.getContext(), TaskActivity.class);
                        intent.putExtra("task_location", " ");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(view.getContext(), MapSignActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(view.getContext(), PatrolActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        String userpri = SharedPreferencesUtils.getParam(view.getContext(), "privilege", "").toString();
                        if (userpri.equals("1")) {
                            Intent intent3 = new Intent(view.getContext(), TaskScoreActivity.class);
                            startActivity(intent3);
                        }else {
                            Toast.makeText(view.getContext(), "抱歉，管理员才能进行评分！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

    }
    class DownNewsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = Constant.BASE_URL+"/listNewsall.do";

            String json = HttpUtils.doPost(url, "");
            Log.d("taskActivity", "json_no: " + json);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            cycleViewPager = (CycleViewPager) ((Activity) context).getFragmentManager()
                    .findFragmentById(R.id.fragment_cycle_viewpager_content);
            try {
                //将json字符串jsonData装入JSON数组，即JSONArray
                //jsonData可以是从文件中读取，也可以从服务器端获得
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++) {
                    //循环遍历，依次取出JSONObject对象
                    //用getInt和getString方法取出对应键值
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String news_url = jsonObject.getString("news_url");
                    String news_image = jsonObject.getString("news_image");
                    imageUrls[i] = news_image;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < imageUrls.length; i++) {
                ADInfo info = new ADInfo();
                info.setUrl(imageUrls[i]);
                info.setContent("图片-->" + i);
                infos.add(info);
            }
            // 将最后一个ImageView添加进来
            views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getUrl()));
            for (int i = 0; i < infos.size(); i++) {
                views.add(ViewFactory.getImageView(context, infos.get(i).getUrl()));
            }
            // 将第一个ImageView添加进来
            views.add(ViewFactory.getImageView(context, infos.get(0).getUrl()));

            // 设置循环，在调用setData方法前调用
            cycleViewPager.setCycle(true);

            // 在加载数据前设置是否循环
            cycleViewPager.setData(views, infos, mAdCycleViewListener);
            // 设置轮播
            cycleViewPager.setWheel(true);

            // 设置轮播时间，默认5000ms
            cycleViewPager.setTime(2000);
            // 设置圆点指示图标组居中显示，默认靠右
            cycleViewPager.setIndicatorCenter();
        }
    }
    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) ((Activity) context).getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

        for (int i = 0; i < imageUrls.length; i++) {
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i);
            infos.add(info);
        }
        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(context, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        // 设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        // 设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
            }
        }
    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                .defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onDestroy() {
        cycleViewPager.onDestroy();
        super.onDestroy();
    }
}
