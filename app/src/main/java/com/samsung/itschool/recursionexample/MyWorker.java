package com.samsung.itschool.recursionexample;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;
import java.util.ArrayList;

public class MyWorker extends Worker {

    private final StringBuilder stringBuilder;
    private Context context;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.stringBuilder = new StringBuilder();
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        StringBuilder result = searchFiles(new File(context.getFilesDir().getParent()));
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
                    Log.w("RRR", file.getName() + "D");
                    stringBuilder.append("\n").append("Dir: ").append(file.getName());
                    searchFiles(file.getAbsoluteFile());
                }
            }
        }
        return stringBuilder;
    }

}
