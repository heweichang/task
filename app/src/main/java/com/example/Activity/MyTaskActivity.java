package com.example.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MyTaskActivity extends AppCompatActivity {

    private RelativeLayout rlt_mytasking, rlt_mytasked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytask);

        rlt_mytasking = (RelativeLayout) findViewById(R.id.rlt_mytasking);
        rlt_mytasked = (RelativeLayout) findViewById(R.id.rlt_mytasked);
        rlt_mytasking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MyTaskingActivity.class);
                startActivity(intent);
            }
        });
        rlt_mytasked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(),MyTaskedActivity.class);
                startActivity(intent1);
            }
        });
    }
}
