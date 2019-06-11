package com.example.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Activity.PostDetailActivity;
import com.example.Activity.R;
import com.example.beans.Post;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 何伟昌 on 2018/5/22.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPostList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View postView;
        ImageView userImage,postImage;
        TextView userName,postTitle,posttime;

        public ViewHolder(View view) {
            super(view);
            postView = view;
            postImage = (ImageView) view.findViewById(R.id.post_image);
            userImage = (ImageView) view.findViewById(R.id.user_image);
            userName = (TextView) view.findViewById(R.id.user_name);
            postTitle = (TextView) view.findViewById(R.id.post_title);
            posttime = (TextView) view.findViewById(R.id.post_time);
        }
    }
    public PostAdapter(List<Post> postList) {
        mPostList = postList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Post post = mPostList.get(position);
                Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("posttitle", post.getPosttitle());
                intent.putExtra("postcontent", post.getPostcontent());
                intent.putExtra("posttime", post.getPosttime());
                intent.putExtra("userid", post.getUserid());
                intent.putExtra("username", post.getUsername());
                intent.putExtra("userimage", post.getUserimage());
                intent.putExtra("postimage", post.getPostimage());
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), "you clicked view " + post.getPosttitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Post post = mPostList.get(position);
               //Toast.makeText(v.getContext(), "you clicked image " + post.getPostimage(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPostList.get(position);

        DownLoadTask task = new DownLoadTask(holder.userImage);
        task.execute(post.getUserimage());
        DownLoadTask task1 = new DownLoadTask(holder.postImage);
        task1.execute(post.getPostimage());

        holder.userName.setText(post.getUsername());
        holder.postTitle.setText(post.getPosttitle());
        holder.posttime.setText(post.getPosttime());
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

    @Override
    public int getItemCount() {
        return mPostList.size();
    }


}
