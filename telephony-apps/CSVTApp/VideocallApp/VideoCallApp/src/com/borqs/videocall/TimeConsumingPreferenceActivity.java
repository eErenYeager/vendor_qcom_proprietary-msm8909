/*
* Copyright (C) 2010 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.borqs.videocall;

import com.android.internal.telephony.CommandException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;

interface  TimeConsumingPreferenceListener {
    public void onStarted(Preference preference, boolean reading);
    public void onFinished(Preference preference, boolean reading);
    public void onError(Preference preference, int error);
    public void onException(Preference preference, CommandException exception);
}

public class TimeConsumingPreferenceActivity extends PreferenceActivity
                        implements TimeConsumingPreferenceListener,
                        DialogInterface.OnCancelListener {
    private static final String LOG_TAG = "TimeConsumingPreferenceActivity";
    private final boolean DBG = true;

    private class DismissOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }
    private class DismissAndFinishOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            finish();
        }
    }
    private final DialogInterface.OnClickListener mDismiss = new DismissOnClickListener();
    private final DialogInterface.OnClickListener mDismissAndFinish
            = new DismissAndFinishOnClickListener();

    private static final int BUSY_READING_DIALOG = 100;
    private static final int BUSY_SAVING_DIALOG = 200;

    static final int EXCEPTION_ERROR = 300;
    static final int RESPONSE_ERROR = 400;
    static final int RADIO_OFF_ERROR = 500;
    static final int FDN_CHECK_FAILURE = 600;
    static final int STK_CC_SS_TO_DIAL_ERROR = 700;
    static final int STK_CC_SS_TO_USSD_ERROR = 800;
    static final int STK_CC_SS_TO_SS_ERROR = 900;

    private final ArrayList<String> mBusyList = new ArrayList<String>();

    protected boolean mIsForeground = false;

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == BUSY_READING_DIALOG || id == BUSY_SAVING_DIALOG) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getText(R.string.updating_title));
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);

            switch(id) {
                case BUSY_READING_DIALOG:
                    dialog.setCancelable(true);
                    dialog.setOnCancelListener(this);
                    dialog.setMessage(getText(R.string.reading_settings));
                    return dialog;
                case BUSY_SAVING_DIALOG:
                    dialog.setCancelable(false);
                    dialog.setMessage(getText(R.string.updating_settings));
                    return dialog;
            }
            return null;
        }

        if (id == RESPONSE_ERROR || id == RADIO_OFF_ERROR || id == EXCEPTION_ERROR
                || id == FDN_CHECK_FAILURE || id == STK_CC_SS_TO_DIAL_ERROR
                || id == STK_CC_SS_TO_USSD_ERROR || id == STK_CC_SS_TO_SS_ERROR) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            int msgId;
            int titleId = R.string.error_updating_title;

            switch (id) {
                case RESPONSE_ERROR:
                    msgId = R.string.response_error;
                    builder.setPositiveButton(R.string.close_dialog, mDismiss);
                    break;
                case RADIO_OFF_ERROR:
                    msgId = R.string.radio_off_error;
                    // The error is not recoverable on dialog exit.
                    builder.setPositiveButton(R.string.close_dialog, mDismissAndFinish);
                    break;
                case FDN_CHECK_FAILURE:
                    msgId = R.string.fdn_check_failure;
                    builder.setPositiveButton(R.string.close_dialog, mDismiss);
                    break;
                case STK_CC_SS_TO_DIAL_ERROR:
                    msgId = R.string.stk_cc_ss_to_dial_error;
                    // Set Button 2
                    builder.setPositiveButton(R.string.close_dialog, mDismiss);
                    break;
                case STK_CC_SS_TO_USSD_ERROR:
                    msgId = R.string.stk_cc_ss_to_ussd_error;
                    // Set Button 2
                    builder.setPositiveButton(R.string.close_dialog, mDismiss);
                    break;
                case STK_CC_SS_TO_SS_ERROR:
                    msgId = R.string.stk_cc_ss_to_ss_error;
                    // Set Button 2
                    builder.setPositiveButton(R.string.close_dialog, mDismiss);
                    break;
                case EXCEPTION_ERROR:
                default:
                    msgId = R.string.exception_error;
                    // The error is not recoverable on dialog exit.
                    builder.setPositiveButton(R.string.close_dialog, mDismissAndFinish);
                    break;
            }

            builder.setTitle(getText(titleId));
            builder.setMessage(getText(msgId));
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();

            // make the dialog more obvious by blurring the background.
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            return dialog;
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mIsForeground = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    @Override
    public void onStarted(Preference preference, boolean reading) {
        if (DBG) dumpState();
        if (DBG) Log.d(LOG_TAG, "onStarted, preference=" + preference.getKey()
                + ", reading=" + reading);
        mBusyList.add(preference.getKey());

        if (mIsForeground) {
              if (reading) {
                  showDialog(BUSY_READING_DIALOG);
              } else {
                  showDialog(BUSY_SAVING_DIALOG);
              }
        }

    }

    @Override
    public void onFinished(Preference preference, boolean reading) {
        if (DBG) dumpState();
        if (DBG) Log.d(LOG_TAG, "onFinished, preference=" + preference.getKey()
                + ", reading=" + reading);
        mBusyList.remove(preference.getKey());

        if (mBusyList.isEmpty()) {
            if (reading) {
                dismissDialogSafely(BUSY_READING_DIALOG);
            } else {
                dismissDialogSafely(BUSY_SAVING_DIALOG);
            }
        }
        preference.setEnabled(true);
    }

    @Override
    public void onError(Preference preference, int error) {
        if (DBG) dumpState();
        if (DBG) Log.d(LOG_TAG, "onError, preference=" + preference.getKey() + ", error=" + error);

        if (mIsForeground) {
            showDialog(error);
        }
        preference.setEnabled(false);
    }

    @Override
    public void onException(Preference preference, CommandException exception) {
	if (DBG) Log.d(LOG_TAG, "onException, preference=" + preference.getKey() + ", exception=" + exception);

        if (exception.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
            onError(preference, FDN_CHECK_FAILURE);
        } else {
            preference.setEnabled(false);
            onError(preference, EXCEPTION_ERROR);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (DBG) dumpState();
        finish();
    }

    private void dismissDialogSafely(int id) {
        try {
            dismissDialog(id);
        } catch (IllegalArgumentException e) {
            // This is expected in the case where we were in the background
            // at the time we would normally have shown the dialog, so we didn't
            // show it.
        }
    }

    /* package */ void dumpState() {
        Log.d(LOG_TAG, "dumpState begin");
        for (String key : mBusyList) {
            Log.d(LOG_TAG, "mBusyList: key=" + key);
        }
        Log.d(LOG_TAG, "dumpState end");
    }
}
