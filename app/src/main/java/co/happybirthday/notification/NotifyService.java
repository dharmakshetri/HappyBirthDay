package co.happybirthday.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import co.happybirthday.Birthday;
import co.happybirthday.BirthdayDetails;
import co.happybirthday.NotificationActionReceiver;
import co.happybirthday.R;
import co.happybirthday.Student;
import co.happybirthday.BirthdayRepo;


/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyService extends Service {
	Context mcontext;
	Notification notification;
	/**
	 * Class for clients to access
	 */
	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}

	// Unique id to identify the notification.
	private static final int NOTIFICATION = 0;
	// Name of an intent extra we can use to identify if this service was started to create a notification	
	public static final String INTENT_NOTIFY = "co.happybirthday.notification.INTENT_NOTIFY";
	// The system notification manager
	private NotificationManager mNM;

	@Override
	public void onCreate() {
		Log.i("NotifyService", "onCreate()");
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mcontext=getApplicationContext();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		Log.e("", " intent.getIntExtra id=" + intent.getIntExtra("DATA",00));
		// If this service was started by out AlarmTask intent then we want to show our notification
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
			showNotification(intent.getIntExtra("DATA",999));
		
		// We don't care if this service is stopped as we have already delivered our notification
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	/**
	 * Creates a notification and shows it in the OS drag-down status bar
	 */


	@SuppressWarnings("deprecation")
	private void showNotification(int bid) {
		// This is the 'title' of the notification
		BirthdayRepo birthdayRepo = new BirthdayRepo(getApplicationContext());
		Birthday birthday= birthdayRepo.getStudentById(bid);
		CharSequence title =birthday.name+"'s Birthday today" ;
		// This is the icon to use on the notification
		//int icon = R.drawable.fre
		// This is the scrolling text of the notification
		CharSequence text = "Wish Happy Birthday";
		// What time to show on the notification
		long time = System.currentTimeMillis();
		
		//Notification notification = new Notification(icon, text, time);

		Intent iNoti= new Intent(this, BirthdayDetails.class);
		iNoti.putExtra(Birthday.KEY_ID,bid);
		iNoti.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, iNoti, PendingIntent.FLAG_CANCEL_CURRENT);

		// Set the info for the views that show in the notification panel.
		//notification.setLatestEventInfo(this, title, text, contentIntent);

		int notificationId = new Random().nextInt(); // just use a counter in some util class...
		//Intent iDismiss= new Intent();
		//PendingIntent dismissIntent = PendingIntent.getActivity(mcontext, 0, iDismiss, 0);

		Intent intent = new Intent(this, NotificationActionReceiver.class);
		intent.putExtra("action", "dismiss");
		PendingIntent dismissIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

//		Intent intent1 = new Intent(this, NotificationActionReceiver.class);
//		intent1.putExtra("action", "Wish");
//		intent1.putExtra(Student.KEY_ID,bid);
//		PendingIntent wishIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

		//Intent clearIntent = new Intent("clear_all_notifications");
		//PendingIntent clearNotesFromDb = PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Resources res = this.getResources();
//		Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
//				mcontext.getResources(),
//				R.drawable.free);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification();
			notification.icon = R.drawable.free_gift_16;
			try {
				Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				deprecatedMethod.invoke(notification, this, title, null, contentIntent);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Log.e("TAG", "Method not found", e);
			}
		} else {
			// Use new API
			Notification.Builder builder = new Notification.Builder(this)
					.setContentIntent(contentIntent)
					.setSmallIcon(R.drawable.free_gift_16)
					.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.free_gift_32))
					.addAction(android.R.drawable.ic_delete, "No,Thank You", dismissIntent)
					.addAction(R.drawable.free_gift_24, "Wish", contentIntent)
					.setContentTitle(title)
					.setContentText(text);
			notification = builder.build();
		}

		notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happybirthday);
		notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Send the notification to the system.
		mNM.notify(NOTIFICATION, notification);

		// Stop the service when we are finished
		stopSelf();


	}


}