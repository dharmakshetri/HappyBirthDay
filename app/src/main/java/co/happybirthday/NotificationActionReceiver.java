package co.happybirthday;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Dharma on 2/3/2016.
 */
public class NotificationActionReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        Log.e("action","action="+action);
        int id = intent.getIntExtra("id", 0);
          switch (action) {
            case "Wish":
                int bid=intent.getIntExtra(Birthday.KEY_ID,0);
                Log.e("Wish", "Wish==" + bid);
// Start service to approve transaction and hold wake lock
                Intent approveIntent = new Intent("co.happybirthday.BirthdayDetails");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Birthday.KEY_ID, bid);
                startWakefulService(context, approveIntent);
                //context.startActivity(approveIntent);
                break;
            case "dismiss":
                // Clear existing notification
                Log.e("Dismissed","Dismissed");
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(id);
                break;
            default:
                return;
        }
    }
}