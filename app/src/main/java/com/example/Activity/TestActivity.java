package com.example.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tools.SharedPreferencesUtils;

public class TestActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv = (TextView)findViewById(R.id.text);
        tv.setText(SharedPreferencesUtils.getParam(TestActivity.this, "iduser", "").toString());
    }
}
