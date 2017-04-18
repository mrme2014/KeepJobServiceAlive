package com.example.mrs.keepjobservicealive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import service.GuardService;
import service.JobAwakenService;
import service.MessageService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        stopService(new Intent(this, MessageService.class));
        stopService(new Intent(this, GuardService.class));
        stopService(new Intent(this, JobAwakenService.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        startService(new Intent(this, MessageService.class));
        startService(new Intent(this, GuardService.class));
        startService(new Intent(this, JobAwakenService.class));
        super.onPause();
    }

}
