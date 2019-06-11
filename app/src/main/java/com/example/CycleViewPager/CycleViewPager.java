package com.example.CycleViewPager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.Activity.NewsActivity;
import com.example.Activity.R;
import com.example.beans.ADInfo;
import com.example.https.HttpUtils;
import com.example.tools.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 实现可循环，可轮播的viewpager
 */
@SuppressLint("NewApi")
public class CycleViewPager extends Fragment implements OnPageChangeListener {
	
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private ImageView[] indicators;
	private FrameLayout viewPagerFragmentLayout;
	private LinearLayout indicatorLayout; // 指示器
	private BaseViewPager viewPager;
	private BaseViewPager parentViewPager;
	private ViewPagerAdapter adapter;
	private CycleViewPagerHandler handler;
	private int time = 5000; // 默认轮播时间
	private int currentPosition = 0; // 轮播当前位置
	private boolean isScrolling = false; // 滚动框是否滚动着
	private boolean isCycle = false; // 是否循环
	private boolean isWheel = false; // 是否轮播
	private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换
	private int WHEEL = 100; // 转动
	private int WHEEL_WAIT = 101; // 等待
	private ImageCycleViewListener mImageCycleViewListener;
	private List<ADInfo> infos;
	private String[] imageUrls = new String[5];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_cycle_viewpager_contet, null);
		new DownNewsAsyncTask().execute("");
		viewPager = (BaseViewPager) view.findViewById(R.id.viewPager);
		indicatorLayout = (LinearLayout) view
				.findViewById(R.id.layout_viewpager_indicator);

		viewPagerFragmentLayout = (FrameLayout) view
				.findViewById(R.id.layout_viewager_content);

		handler = new CycleViewPagerHandler(getActivity()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == WHEEL && imageViews.size() != 0) {
					if (!isScrolling) {
						int max = imageViews.size() + 1;
						int position = (currentPosition + 1) % imageViews.size();
						viewPager.setCurrentItem(position, true);
						if (position == max) { // 最后一页时回到第一页
							viewPager.setCurrentItem(1, false);
						}
					}

					releaseTime = System.currentTimeMillis();
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, time);
					return;
				}
				if (msg.what == WHEEL_WAIT && imageViews.size() != 0) {
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, time);
				}
			}
		};

		return view;
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
					imageUrls[i] = news_url;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener) {
		setData(views, list, listener, 0);
	}

	/**
	 * 初始化viewpager
	 * 
	 * @param views
	 *            要显示的views
	 * @param showPosition
	 *            默认显示位置
	 */
	public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener, int showPosition) {
		mImageCycleViewListener = listener;
		infos = list;
		this.imageViews.clear();

		if (views.size() == 0) {
			viewPagerFragmentLayout.setVisibility(View.GONE);
			return;
		}

		for (ImageView item : views) {
			this.imageViews.add(item);
		}

		int ivSize = views.size();

		// 设置指示器
		indicators = new ImageView[ivSize];
		if (isCycle)
			indicators = new ImageView[ivSize - 2];
		indicatorLayout.removeAllViews();
		for (int i = 0; i < indicators.length; i++) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.view_cycle_viewpager_indicator, null);
			indicators[i] = (ImageView) view.findViewById(R.id.image_indicator);
			indicatorLayout.addView(view);
		}

		adapter = new ViewPagerAdapter();

		// 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
		setIndicator(0);

		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(adapter);
		if (showPosition < 0 || showPosition >= views.size())
			showPosition = 0;
		if (isCycle) {
			showPosition = showPosition + 1;
		}
		viewPager.setCurrentItem(showPosition);

	}

	/**
	 * 设置指示器居中，默认指示器在右方
	 */
	public void setIndicatorCenter() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		indicatorLayout.setLayoutParams(params);
	}

	/**
	 * 是否循环，默认不开启，开启前，请将views的最前面与最后面各加入一个视图，用于循环
	 * 
	 * @param isCycle
	 *            是否循环
	 */
	public void setCycle(boolean isCycle) {
		this.isCycle = isCycle;
	}

	/**
	 * 是否处于循环状态
	 * 
	 * @return
	 */
	public boolean isCycle() {
		return isCycle;
	}

	/**
	 * 设置是否轮播，默认不轮播,轮播一定是循环的
	 * 
	 * @param isWheel
	 */
	public void setWheel(boolean isWheel) {
		this.isWheel = isWheel;
		isCycle = true;
		if (isWheel) {
			handler.postDelayed(runnable, time);
		}
	}

	/**
	 * 是否处于轮播状态
	 * 
	 * @return
	 */
	public boolean isWheel() {
		return isWheel;
	}

	final Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (getActivity() != null && !getActivity().isFinishing()
					&& isWheel) {
				long now = System.currentTimeMillis();
				// 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
				if (now - releaseTime > time - 500) {
					handler.sendEmptyMessage(WHEEL);
				} else {
					handler.sendEmptyMessage(WHEEL_WAIT);
				}
			}
		}
	};

	/**
	 * 释放指示器高度，可能由于之前指示器被限制了高度，此处释放
	 */
	public void releaseHeight() {
		getView().getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
		refreshData();
	}

	/**
	 * 设置轮播暂停时间，即没多少秒切换到下一张视图.默认5000ms
	 * 
	 * @param time
	 *            毫秒为单位
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * 刷新数据，当外部视图更新后，通知刷新数据
	 */
	public void refreshData() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	/**
	 * 隐藏CycleViewPager
	 */
	public void hide() {
		viewPagerFragmentLayout.setVisibility(View.GONE);
	}

	/**
	 * 返回内置的viewpager
	 * 
	 * @return viewPager
	 */
	public BaseViewPager getViewPager() {
		return viewPager;
	}

	/**
	 * 页面适配器 返回对应的view
	 * 
	 * @author Yuedong Li
	 * 
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			ImageView v = imageViews.get(position);
			if (mImageCycleViewListener != null) {
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mImageCycleViewListener.onImageClick(infos.get(currentPosition - 1), currentPosition, v);
						Intent intent = new Intent(v.getContext(), NewsActivity.class);
						intent.putExtra("newsUrl",imageUrls[position-1]);
						startActivity(intent);
//						if(position == 5) {
//							Intent intent = new Intent(v.getContext(), NewsActivity.class);
//							intent.putExtra("newsUrl",imageUrls[4]);
//							startActivity(intent);
//						}else if(position == 1)
//						{
//							Intent intent = new Intent(v.getContext(), NewsActivity.class);
//							intent.putExtra("newsUrl",imageUrls[0]);
//							startActivity(intent);
//						}else if(position == 2){
//							Intent intent = new Intent(v.getContext(), NewsActivity.class);
//							intent.putExtra("newsUrl",imageUrls[1]);
//							startActivity(intent);
//						}else if(position == 3){
//							Intent intent = new Intent(v.getContext(), NewsActivity.class);
//							intent.putExtra("newsUrl",imageUrls[2]);
//							startActivity(intent);
//						}else if(position == 4){
//							Intent intent = new Intent(v.getContext(), NewsActivity.class);
//							intent.putExtra("newsUrl",imageUrls[3]);
//							startActivity(intent);
//						}
					}
				});
			}
			container.addView(v);
			return v;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (arg0 == 1) { // viewPager在滚动
			isScrolling = true;
			return;
		} else if (arg0 == 0) { // viewPager滚动结束
			if (parentViewPager != null)
				parentViewPager.setScrollable(true);

			releaseTime = System.currentTimeMillis();

			viewPager.setCurrentItem(currentPosition, false);
			
		}
		isScrolling = false;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		int max = imageViews.size() - 1;
		int position = arg0;
		currentPosition = arg0;
		if (isCycle) {
			if (arg0 == 0) {
				currentPosition = max - 1;
			} else if (arg0 == max) {
				currentPosition = 1;
			}
			position = currentPosition - 1;
		}
		setIndicator(position);
	}

	/**
	 * 设置viewpager是否可以滚动
	 * 
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		viewPager.setScrollable(enable);
	}

	/**
	 * 返回当前位置,循环时需要注意返回的position包含之前在views最前方与最后方加入的视图，即当前页面试图在views集合的位置
	 * 
	 * @return
	 */
	public int getCurrentPostion() {
		return currentPosition;
	}

	/**
	 * 设置指示器
	 * 
	 * @param selectedPosition
	 *            默认指示器位置
	 */
	private void setIndicator(int selectedPosition) {
		for (int i = 0; i < indicators.length; i++) {
			indicators[i].setBackgroundResource(R.drawable.icon_point);
		}
		if (indicators.length > selectedPosition)
			indicators[selectedPosition].setBackgroundResource(R.drawable.icon_point_pre);
	}

	/**
	 * 如果当前页面嵌套在另一个viewPager中，为了在进行滚动时阻断父ViewPager滚动，可以 阻止父ViewPager滑动事件
	 * 父ViewPager需要实现ParentViewPager中的setScrollable方法
	 */
	public void disableParentViewPagerTouchEvent(BaseViewPager parentViewPager) {
		if (parentViewPager != null)
			parentViewPager.setScrollable(false);
	}

	
	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 单击图片事件
		 *
		 * @param imageView
		 */
		public void onImageClick(ADInfo info, int postion, View imageView);

	}
}