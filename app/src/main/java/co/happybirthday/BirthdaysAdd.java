package co.happybirthday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import co.happybirthday.notification.ScheduleClient;

/**
 * Created by Dharma on 1/22/2016.
 */
public class BirthdaysAdd extends AppCompatActivity {
    private ListView listview;
    //ArrayList<String> items = new ArrayList<String>();
    private int count;
    private boolean[] thumbnailsselection;
    //private String[] arrayMonths, arrayDays, arraysCategory;
    private Spinner spinnerMonth, spinnerDay, spinnerCategory;
    EditText editTextName, editTextEmail, editTextMobile;
    String strName, strEmail, strMobile;
    int intMonth, intDay;
    String spinStringCategroy;
    ProgressDialog pd;
    int progressStatus;
    Handler handler;
    Button btnBirthDayReminder;
    int birthdayId, status;
    Birthday birthdayEdit;
    ImageView imageViewFav;
    int intFav=0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ScheduleClient scheduleClient;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;
    private boolean mGameIsInProgress;
    private long mTimerMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthdayadd);

        // This is a handle so that we can call methods on our service
        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        //start ads

        //end ads
        getSupportActionBar().setTitle(getResources().getString(R.string.add_birthday_message));
        Intent intentBack = getIntent();
        birthdayId = intentBack.getIntExtra(Birthday.KEY_ID, 1);
        status = intentBack.getIntExtra(Birthday.Status, 0);


        fillViews();
        Birthday.getTimes();
        count = Birthday.arrayNotificationCheckList.size();
        thumbnailsselection = new boolean[count];
        for (int i = 0; i < count; i++) {
            thumbnailsselection[i] = false;
        }
        listview = (ListView) findViewById(R.id.listViewRemindMe);


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Birthday.arraysCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);


        // spinner month
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Birthday.arrayMonths);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // image.setImageResource(imgs.getResourceId(spinnerspinnerDay.getSelectedItemPosition(), -1));
//                Toast.makeText(getApplicationContext(),
//                        spinnerMonth.getItemAtPosition(position).toString(), Toast.LENGTH_LONG)
//                        .show();
                // spinnerMonth=spinnerMonth.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //day spinner

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Birthday.arrayDays);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        spinnerDay.getItemAtPosition(position).toString(), Toast.LENGTH_LONG)
//                        .show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        if (status == 1) {
            Log.e("Editing", "status=" + status);
            editData(birthdayId);
        }
        listview.setAdapter(new ImageAdapter(BirthdaysAdd.this));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void loadAd(){

    }
    public void editData(int birthdayId) {

        Log.e("BirthDay ID", "Edit birth day id=" + birthdayId + " studentEdit.name=");
        BirthdayRepo birthdayRepo = new BirthdayRepo(this);
        birthdayEdit = birthdayRepo.getStudentById(birthdayId);
        if (birthdayEdit.name.length() == 0) {
            editTextName.setText("");
        } else {
            editTextName.setText(birthdayEdit.name);
        }
        if (birthdayEdit.email.length() == 0) {
            editTextEmail.setText("");
        } else {
            editTextEmail.setText(birthdayEdit.email);
        }

        if (birthdayEdit.mobile.length() == 0) {
            editTextMobile.setText("");
        } else {
            editTextMobile.setText(birthdayEdit.mobile);
        }

        if(birthdayEdit.favourite==0){
            imageViewFav.setBackgroundResource(R.drawable.nofav_32);
        }else{
            imageViewFav.setBackgroundResource(R.drawable.fav_32);
        }
        int intCategoryId = Birthday.getCategoryId(birthdayEdit.category);
        spinnerCategory.setSelection(intCategoryId);

        String strbirthdate = birthdayEdit.date;
        Log.e("birthdate", "birthdate=" + strbirthdate);
        String[] splitDate = strbirthdate.split("-");
        int month = Integer.parseInt(splitDate[0].toString());
        int day = Integer.parseInt(splitDate[1].toString());

        spinnerMonth.setSelection(month-1);
        spinnerDay.setSelection(day-1);
        Log.e("NOTI", "noti=" + birthdayEdit.notification);
        String strNotification = birthdayEdit.notification.substring(1, birthdayEdit.notification.length() - 1).toString();

        if (strNotification.length() > 0) {
            String replace = strNotification.replace("[", "");
            String replace1 = replace.replace("]", "");
            List<String> arrayList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
            List<Integer> favList = new ArrayList<Integer>();
            for (String fav : arrayList) {
                favList.add(Integer.parseInt(fav.trim()));
            }

            // thumbnailsselection[id] = false;
            for (int i = 0; i < favList.size(); i++) {
                for (int j = 0; j < count; j++) {
                    if (favList.get(i) == j) {
                        thumbnailsselection[j] = true;
                        break;
                    }
                }
            }
        }

    }

    private void fillViews() {
        spinnerMonth = (Spinner) findViewById(R.id.spinMonth);
        spinnerDay = (Spinner) findViewById(R.id.spinDay);
        spinnerCategory = (Spinner) findViewById(R.id.spinCategory);

        editTextName = (EditText) findViewById(R.id.etName);
        editTextEmail = (EditText) findViewById(R.id.etEmail);
        editTextMobile = (EditText) findViewById(R.id.etMobile);
        btnBirthDayReminder = (Button) findViewById(R.id.btnBirthDayReminder);
        imageViewFav=(ImageView)findViewById(R.id.imageViewFav);
        imageViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intFav==0){
                    intFav=1;
                    imageViewFav.setBackgroundResource(R.drawable.fav_32);
                }else {
                    intFav=0;
                    imageViewFav.setBackgroundResource(R.drawable.nofav_32);
                }

            }
        });

    }

    public void enableSubmitIfReady() {

        boolean isReady = editTextName.getText().toString().length() > 3;
        if (!isReady)
            editTextName.setError("This field can not be blank");

        btnBirthDayReminder.setEnabled(isReady);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BirthdaysAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://co.happybirthday/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
       if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BirthdaysAdd Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://co.happybirthday/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.row_birthday_reminders, null);
                holder.textview = (TextView) convertView
                        .findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.textview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.textview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                }
            });
            holder.textview.setText(Birthday.arrayNotificationCheckList.get(position));
            //Log.e("NOtes", "Nottes=" + " position==" + position + " --->>>" + thumbnailsselection[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        TextView textview;
        CheckBox checkbox;
        int id;
    }

    public static void doBackUp() {

        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                File folder = new File(Environment.getExternalStorageDirectory()
                        + "/BD/DB_Backup/");
                boolean success = true;

                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                Time now = new Time();
                now.setToNow();
                if (success) {

                    String strnewDbPath = Environment.getExternalStorageDirectory()
                            + "/BD/DB_Backup/";
                    String newDbName = now.year + "-" + (now.month + 1) + "-"
                            + now.monthDay + "-" + now.hour + "-" + now.minute
                            + "-" + now.second + ".db";
                    boolean out = Birthday.importDB(strnewDbPath, newDbName);
                    Log.e("IMPORTin","importing="+out);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void click(View v) {
        if (v.getId() == R.id.btnBirthDayReminder) {
            boolean isValid=true;

            if (editTextName.getText().toString().trim().equalsIgnoreCase("")) {
                editTextName.setError("Name field can not be blank");
                isValid=false;
            } else {
                strName = editTextName.getText().toString().trim();

                if (editTextEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    strEmail = "";
                } else {
                    strEmail = editTextEmail.getText().toString().trim();
                   boolean boolFlag=Common.isValidEmail(strEmail);
                    if (!boolFlag){
                        editTextEmail.setError("Email is not valid");
                        isValid=false;
                    }
                }
                if (editTextMobile.getText().toString().trim().equalsIgnoreCase("")) {
                    strMobile = "";
                } else {
                    strMobile = editTextMobile.getText().toString().trim();
                }



                spinStringCategroy = spinnerCategory.getSelectedItem().toString();
                intMonth = spinnerMonth.getSelectedItemPosition()+1;
                intDay = spinnerDay.getSelectedItemPosition() + 1;


                final ArrayList<Integer> posSel = new ArrayList<Integer>();
                posSel.clear();
                boolean noSelect = false;
                for (int i = 0; i < thumbnailsselection.length; i++) {
                    if (thumbnailsselection[i] == true) {
                        noSelect = true;
                        //   Log.e("sel pos thu-->", "" + i);
                        posSel.add(i);
                        // break;
                    }
                }
                if (!noSelect) {
//                    Toast.makeText(BirthdaysAdd.this, "Please Select Item!",
//                            Toast.LENGTH_SHORT).show();
                } else {
//                Toast.makeText(BirthdaysAdd.this,
//                        "Selected Items:" + posSel.toString() +"\n Month="+intMonth+" Day=" +intDay,
//                        Toast.LENGTH_LONG).show();
                }

                // save to sql database
                BirthdayRepo repo = new BirthdayRepo(this);
                Birthday birthday = new Birthday();
                birthday.date = intMonth + "-" + intDay;
                Log.e("birthday", "birthday.date=" + birthday.date);

                birthday.email = strEmail;
                birthday.mobile = strMobile;
                birthday.name = strName;
                birthday.category = spinStringCategroy;
                birthday.favourite=intFav;


                Gson gson = new Gson();

                String notificationString = gson.toJson(posSel);
                Log.e("birthday", "notification=" + notificationString);
                birthday.notification = notificationString;

            if(isValid) {
                if (status == 1) {
                    repo.update(birthday, birthdayId);
                    Log.e("", "Last Updated ID=" + birthdayId);
                    loadProgress(birthdayId);
                    //loadad();
                } else {
                    repo.insert(birthday);
                    BirthdayRepo birthdayRepo = new BirthdayRepo(this);
                    int lastInsertedId= birthdayRepo.getLastBirthday();
                    Log.e("", "Last Inserted ID=" + lastInsertedId);
                    loadProgress(lastInsertedId);
                   // loadad();
                }
            }


            }
        }
    }

    public void loadad(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadAd();
        }

    }

    public void loadProgress(int id){
        final int bid=id;
        pd = new ProgressDialog(this, R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        // Finally, show the progress dialog
        // Set the progress status zero on each button click
        progressStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    doBackUp();
                    setNotification(bid);
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                pd.dismiss();
                Intent ibacktomain = new Intent(BirthdaysAdd.this, MainActivity.class);
                startActivity(ibacktomain);

                finish();
            }
        }).start();
    }

    public void setNotification(int id){

        int day = intDay;
        int month = intMonth;
        int year = Birthday.currentYear;

        Log.e("NOTIFICATION","DAY="+day);
        Log.e("NOTIFICATION","MONTH="+month);
        Log.e("NOTIFICATION","YEAR="+year);
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        scheduleClient.setAlarmForNotification(c,id);
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: " + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();

    }
}
