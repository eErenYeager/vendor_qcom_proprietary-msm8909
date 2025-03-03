/*
 * Copyright (c) 2011-2013, Qualcomm Technologies, Inc. All Rights Reserved.
 * Qualcomm Technologies Proprietary and Confidential.
 */
package com.qualcomm.factory.SDCard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.qualcomm.factory.R;
import com.qualcomm.factory.Utilities;

public class SDCard extends Activity {

    TextView mTextView;
    String TAG = "SDCard";

    @Override
    public void finish() {

        super.finish();
    }

    @Override
    protected void onPause() {

        unregisterReceiver(mReceiver);
        super.onPause();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdcard);

        mTextView = (TextView) findViewById(R.id.sdcard_hint);
        mTextView.setText(getString(R.string.sdcard_wait));

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            exec("mount");
        } else
            showConfirmDialog(this);

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            boolean res = (Boolean) msg.obj;
            if (res)
                pass();
            
            else
                fail(null);
            
        };
    };

    void exec(final String para) {

        new Thread() {
            
            public void run() {
                try {
                    logd(para);
                    
                    Process mProcess;
                    String paras[] = para.split(",");
                    for (int i = 0; i < paras.length; i++)
                        logd(i + ":" + paras[i]);
                    mProcess = Runtime.getRuntime().exec(paras);
                    mProcess.waitFor();
                    
                    InputStream inStream = mProcess.getInputStream();
                    InputStreamReader inReader = new InputStreamReader(inStream);
                    BufferedReader inBuffer = new BufferedReader(inReader);
                    String s;
                    String data = "";
                    while ((s = inBuffer.readLine()) != null) {
                        data += s + "\n";
                    }
                    logd(data);
                    int result = mProcess.exitValue();
                    logd("ExitValue=" + result);
                    Message message = new Message();
                    if (data.contains("/storage/sdcard1"))
                        message.obj = true;
                    else
                        message.obj = false;
                    message.setTarget(mHandler);
                    message.sendToTarget();
                    
                } catch (Exception e) {
                    logd(e);
                }
                
            }
        }.start();
        
    }

    @Override
    protected void onResume() {

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction("android.intent.action.MEDIA_MOUNTED");
        mfilter.addDataScheme("file");
        registerReceiver(mReceiver, mfilter);
        super.onResume();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            logd(intent.getAction());
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED"))
                exec("mount");

        }

    };

    void showConfirmDialog(final Context context) {

        new AlertDialog.Builder(context).setTitle(getString(R.string.sdcard_confirm))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        fail(null);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        toast(getString(R.string.sdcard_to_insert));
                    }
                }).setCancelable(false).show();
    }

    void fail(Object msg) {

        loge(msg);
        toast(msg);
        setResult(RESULT_CANCELED);
        Utilities.writeCurMessage(this, TAG, "Failed");
        finish();
    }

    void pass() {

        setResult(RESULT_OK);
        Utilities.writeCurMessage(this, TAG, "Pass");
        finish();
    }

    public void toast(Object s) {

        if (s == null)
            return;
        Toast.makeText(this, s + "", Toast.LENGTH_SHORT).show();
    }

    private void loge(Object e) {

        if (e == null)
            return;
        Thread mThread = Thread.currentThread();
        StackTraceElement[] mStackTrace = mThread.getStackTrace();
        String mMethodName = mStackTrace[3].getMethodName();
        e = "[" + mMethodName + "] " + e;
        Log.e(TAG, e + "");
    }

    @SuppressWarnings("unused")
    private void logd(Object s) {

        Thread mThread = Thread.currentThread();
        StackTraceElement[] mStackTrace = mThread.getStackTrace();
        String mMethodName = mStackTrace[3].getMethodName();

        s = "[" + mMethodName + "] " + s;
        Log.d(TAG, s + "");
    }
}
