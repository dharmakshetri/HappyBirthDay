/*
 * Copyright 2013-2015 Keisuke Kobayashi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.happybirthday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;


public class RateThisApp {

    private static final String TAG = RateThisApp.class.getSimpleName();

    private static final String PREF_NAME = "RateThisApp";
    private static final String KEY_INSTALL_DATE = "rta_install_date";
    private static final String KEY_LAUNCH_TIMES = "rta_launch_times";
    private static final String KEY_OPT_OUT = "rta_opt_out";

    private static Date mInstallDate = new Date();
    private static int mLaunchTimes = 0;
    private static boolean mOptOut = false;

    private static Config sConfig = new Config();

    public static final boolean DEBUG = false;

    public static void init(Config config) {
        sConfig = config;
    }

    public static void onStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        // If it is the first launch, save the date in shared preference.
        if (pref.getLong(KEY_INSTALL_DATE, 0) == 0L) {
            Date now = new Date();
            editor.putLong(KEY_INSTALL_DATE, now.getTime());
            log("First install: " + now.toString());
        }
        // Increment launch times
        int launchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0);
        launchTimes++;
        editor.putInt(KEY_LAUNCH_TIMES, launchTimes);
        log("Launch times; " + launchTimes);

        editor.commit();

        mInstallDate = new Date(pref.getLong(KEY_INSTALL_DATE, 0));
        mLaunchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0);
        mOptOut = pref.getBoolean(KEY_OPT_OUT, false);

        printStatus(context);
    }


    public static boolean showRateDialogIfNeeded(final Context context) {
        if (shouldShowRateDialog()) {
            showRateDialog(context);
            return true;
        } else {
            return false;
        }
    }


    private static boolean shouldShowRateDialog() {
        if (mOptOut) {
            return false;
        } else {
            if (mLaunchTimes >= sConfig.mCriteriaLaunchTimes) {
                return true;
            }
            long threshold = sConfig.mCriteriaInstallDays * 24 * 60 * 60 * 1000L;	// msec
            return new Date().getTime() - mInstallDate.getTime() >= threshold;
        }
    }

    public static void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int titleId = sConfig.mTitleId != 0 ? sConfig.mTitleId : R.string.rta_dialog_title;
        int messageId = sConfig.mMessageId != 0 ? sConfig.mMessageId : R.string.rta_dialog_message;
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.rta_dialog_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String appPackage = context.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
                context.startActivity(intent);
                setOptOut(context, true);
            }
        });
        builder.setNeutralButton(R.string.rta_dialog_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSharedPreferences(context);
            }
        });
        builder.setNegativeButton(R.string.rta_dialog_no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setOptOut(context, true);
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearSharedPreferences(context);
            }
        });
        builder.create().show();
    }

    private static void clearSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.remove(KEY_INSTALL_DATE);
        editor.remove(KEY_LAUNCH_TIMES);
        editor.commit();
    }


    private static void setOptOut(final Context context, boolean optOut) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_OPT_OUT, optOut);
        editor.commit();
    }


    private static void printStatus(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        /*log("*** RateThisApp Status ***");
        log("Install Date: " + new Date(pref.getLong(KEY_INSTALL_DATE, 0)));
        log("Launch Times: " + pref.getInt(KEY_LAUNCH_TIMES, 0));
        log("Opt out: " + pref.getBoolean(KEY_OPT_OUT, false));*/
    }


    private static void log(String message) {
        if (DEBUG) {
            Log.v(TAG, message);
        }
    }


    public static class Config {
        private int mCriteriaInstallDays;
        private int mCriteriaLaunchTimes;
        private int mTitleId = 0;
        private int mMessageId = 0;

        public Config() {
            this(7, 10);
        }


        public Config(int criteriaInstallDays, int criteriaLaunchTimes) {
            this.mCriteriaInstallDays = criteriaInstallDays;
            this.mCriteriaLaunchTimes = criteriaLaunchTimes;
        }


        public void setTitle(int stringId) {
            this.mTitleId = stringId;
        }

        public void setMessage(int stringId) {
            this.mMessageId = stringId;
        }
    }
}
