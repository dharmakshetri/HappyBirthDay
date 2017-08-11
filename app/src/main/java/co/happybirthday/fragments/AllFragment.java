package co.happybirthday.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import co.happybirthday.Birthday;
import co.happybirthday.BirthdayDetails;
import co.happybirthday.BirthdayRepo;
import co.happybirthday.Common;
import co.happybirthday.R;
import co.happybirthday.notification.ScheduleClient;


public class AllFragment extends Fragment implements OnItemClickListener {
    ArrayList<String> nameList;
    ListView listViewAll;
    MyBaseAdapter birthDayAdapter;
     ArrayList<Birthday> birthdayList;
    private ScheduleClient scheduleClient;
    Calendar calender;
    Button btnToday;
    Handler backgroundHandler;


    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }
    @Override
    public void onStop() {
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all,
                container, false);
        Common.loadAd(view);
        RelativeLayout relativeLayoutMain= (RelativeLayout) view.findViewById(R.id.mainView);


        BirthdayRepo birthdayRepo = new BirthdayRepo(getActivity());
        birthdayList= new ArrayList<Birthday>();
        birthdayList= birthdayRepo.getAllBirthday();
//
      //  Common.loadAd(view);
        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();
        calender=Calendar.getInstance();

       listViewAll=(ListView) view.findViewById(R.id.listViewAll);
        //relativeLayoutMain.addView(btnToday, lp);
        //relativeLayoutMain.addView(listViewAll);


        birthDayAdapter= new MyBaseAdapter(getActivity(), birthdayList);
        listViewAll.setAdapter(birthDayAdapter);
        listViewAll.setOnItemClickListener(this);
        // Inflate the layout for this fragment


        return view;
    }
    public  void setNotification( Activity activity,ScheduleClient scheduleClient,Calendar c, int id, int intDay,int intMonth){

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
        scheduleClient.setAlarmForNotification(c, id);
        // Notify the user what they just did
        Toast.makeText(activity, "Notification set for: " + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        // TODO Auto-generated method stub
        int birthday_id = birthdayList.get(arg2).birthday_ID;
        Intent iDetails= new Intent(getActivity(), BirthdayDetails.class);
        iDetails.putExtra(Birthday.KEY_ID,birthday_id);
        startActivity(iDetails);
        //showToast(product);
    }
    protected void showToast(String product) {
        Toast.makeText(getActivity(), product, Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub

    }
}
