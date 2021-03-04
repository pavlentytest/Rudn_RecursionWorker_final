package com.samsung.itschool.recursionexample;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;

import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //строка для хранения списка директорий
    private StringBuilder strTree = new StringBuilder();

    //поле для вывода реузультатов работы программы на экран
    private TextView logTview;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTview = findViewById(R.id.tvLog);
        checkPermissions();
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // Выход
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                initialize();
                break;
        }
    }


    public void initialize() {
        final OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance().enqueue(work);
        WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    Log.w("WWWW",workInfo.getOutputData().getString("RESULT"));
                    logTview.setText(workInfo.getOutputData().getString("RESULT"));
                }
            }
        });
    }


  }
