package com.samsung.itschool.recursionexample;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;

public class MyWorker extends Worker {

    private final StringBuilder stringBuilder;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.stringBuilder = new StringBuilder();
    }

    @NonNull
    @Override
    public Result doWork() {
        StringBuilder result = searchFiles(new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures"));
        Data outputData = new Data.Builder().putString("RESULT",result.toString()).build();
        return Result.success(outputData);
    }

    private StringBuilder searchFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    stringBuilder.append("\n").append(" -- File: ").append(file.getName());
                }
                else if (file.isDirectory()) {
                    Log.w("RRRR", file.getName() + "D");
                    stringBuilder.append("\n").append("Dir: ").append(file.getName());
                    searchFiles(file.getAbsoluteFile());
                }
            }
        }
        return stringBuilder;
    }

}
