package com.example.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tools.SharedPreferencesUtils;

public class PatrolActivity extends AppCompatActivity {

    private RelativeLayout rlt_patrolrandom ,rlt_mytaskpatrol,rlt_patrolresult;
    private ImageView patrol_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);
        patrol_return = (ImageView)findViewById(R.id.patrol_return);
        rlt_mytaskpatrol = (RelativeLayout) findViewById(R.id.rlt_mytaskpatrol);
        //rlt_patrolrandom = (RelativeLayout) findViewById(R.id.rlt_patrolrandom);
        rlt_patrolresult = (RelativeLayout) findViewById(R.id.rlt_patrolresult);
        rlt_mytaskpatrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String userpri = SharedPreferencesUtils.getParam(PatrolActivity.this, "privilege", "").toString();
                if (userpri.equals("1")) {
                    Intent intent1 = new Intent(PatrolActivity.this,PatrolListActivity.class);
                    startActivity(intent1);
                }else {
                    Toast.makeText(PatrolActivity.this, "抱歉，管理员才能巡逻任务！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rlt_patrolresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent2 = new Intent(PatrolActivity.this,PatrolResultActivity.class);
                startActivity(intent2);
            }
        });
        patrol_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnMain = new Intent(PatrolActivity.this,MainActivity.class);
                startActivity(returnMain);
            }
        });
    }
}
