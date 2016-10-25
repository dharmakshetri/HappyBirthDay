package co.happybirthday;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import co.happybirthday.notification.ScheduleClient;

/**
 * Created by Dharma on 1/29/2016.
 */

public class Common {
    public static long dayMiliseconds=86400000;
    public  static long hourMiliseconds=3600000 ;
    public  static long fiveMinutesMiliseconds=300000;

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static void  loadAd(View v){
        AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    public final static void setNotification( Activity activity,ScheduleClient scheduleClient,Calendar c, int id, int intDay,int intMonth){

        int bid=id;
        Calendar calendar=Calendar.getInstance();
        calendar=c;
        scheduleClient= new ScheduleClient(activity);
        scheduleClient.doBindService();

        int day = intDay;
        int month = intMonth;
        int year = Birthday.currentYear;
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        //Calendar c = Calendar.getInstance();
        c.set(year, month-1, day);
        c.set(Calendar.HOUR_OF_DAY, 16);
        c.set(Calendar.MINUTE, 41);
        c.set(Calendar.SECOND, 0);
        scheduleClient.setAlarmForNotification(calendar,bid);
        // Notify the user what they just did
        Toast.makeText(activity, "Notification set for: " + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();

    }
}
