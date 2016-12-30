package com.guru.login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SuccessActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Toast.makeText(SuccessActivity.this, "成功了!"
                , Toast.LENGTH_LONG).show();


    }
}
