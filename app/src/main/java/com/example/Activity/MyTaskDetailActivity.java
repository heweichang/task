package com.example.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.post.GlideImageLoader;
import com.example.post.HttpUtil;
import com.example.post.ImagePickerAdapter;
import com.example.post.MyStringCallBack;
import com.example.post.SelectDialog;
import com.example.tools.Constant;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MyTaskDetailActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private Button btn_slect;
    private ImageView tasking_return;
    private HttpUtil httpUtil;
    private TextView tv_task_id,tv_task_name,tv_task_addr,tv_task_req,tv_task_content,tv_task_date,tv_user_name,get_location;
    String task_id;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytaskdetail);
        initView();
        httpUtil = new HttpUtil();
        btn_slect = (Button)findViewById(R.id.btn);
        btn_slect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(selImageList);
            }
        });

        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();

    }

    private void initView() {
        tasking_return = (ImageView)findViewById(R.id.tasking_return);
        get_location = (TextView)findViewById(R.id.get_location);
        tv_task_id = (TextView)findViewById(R.id.task_id);
        tv_task_name = (TextView)findViewById(R.id.task_name);
        tv_task_date = (TextView)findViewById(R.id.task_date);
        tv_task_addr = (TextView)findViewById(R.id.task_addr);
        tv_task_req = (TextView)findViewById(R.id.task_req);
        tv_task_content = (TextView)findViewById(R.id.task_content);
        tv_user_name = (TextView)findViewById(R.id.user_name);
        intent = getIntent();
        task_id = intent.getStringExtra("taskid");
        tv_task_id.setText(intent.getStringExtra("taskid"));
        tv_task_name.setText(intent.getStringExtra("tname"));
        tv_task_date.setText(intent.getStringExtra("tdate"));
        tv_task_addr.setText(intent.getStringExtra("taddr"));
        tv_task_req.setText(intent.getStringExtra("treq"));
        tv_task_content.setText(intent.getStringExtra("tcontent"));
        tv_user_name.setText(intent.getStringExtra("uname"));
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyTaskDetailActivity.this, TaskMapActivity.class);
                intent1.putExtra("tlatitude", intent.getStringExtra("tlatitude"));
                intent1.putExtra("tlongitude", intent.getStringExtra("tlongitude"));
                Log.d("taskActivity", "endPt.longitude: " + intent.getStringExtra("tlatitude"));
                Log.d("taskActivity", "endPt.longitude: " + intent.getStringExtra("tlongitude"));
                startActivity(intent1);
            }
        });
        tasking_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnTasking = new Intent(MyTaskDetailActivity.this,MyTaskingActivity.class);
                startActivity(returnTasking);
                MyTaskDetailActivity.this.finish();
            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(MyTaskDetailActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(MyTaskDetailActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    private String url= Constant.BASE_URL+"/uploadimage.do";

    private void uploadImage(ArrayList<ImageItem> pathList) {
        httpUtil.postFileRequest(url, task_id, null,pathList, new MyStringCallBack() {

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                //返回图片的地址
                Intent intent = new Intent(MyTaskDetailActivity.this,MyTaskingActivity.class);
                startActivity(intent);
                Toast.makeText(MyTaskDetailActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                MyTaskDetailActivity.this.finish();
            }
        });
    }
}