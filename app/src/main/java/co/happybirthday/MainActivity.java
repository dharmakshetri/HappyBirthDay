package co.happybirthday;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.happybirthday.fragments.AllFragment;
import co.happybirthday.fragments.ColleagueFragment;
import co.happybirthday.fragments.FamilyFragment;
import co.happybirthday.fragments.FavouriteFragment;
import co.happybirthday.fragments.FriendsFragment;
import co.happybirthday.fragments.OtherFragment;
import co.happybirthday.fragments.RelativeFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    long currentTimeMilli;
    private InterstitialAd mInterstitialAd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setNotification(bid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RateThisApp.init(new RateThisApp.Config(3, 5));

        Birthday.arraysCategory = getResources().getStringArray(R.array.Categories);
        Birthday.arrayMonths = getResources().getStringArray(R.array.months);
        Birthday.arrayDays = getResources().getStringArray(R.array.days);
        String[] arrayNotificationCheckList = getResources().getStringArray(R.array.NotificationCheckList);
        Birthday.arrayNotificationCheckList = Arrays.asList(arrayNotificationCheckList);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        // Set an AdListener.
        mInterstitialAd.setAdListener(new AdListener() {
            //     @Override
//            public void onAdLoaded() {
//                Toast.makeText(MainActivity.this,
//                        "The interstitial is loaded", Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                requestNewInterstitial();
                goToNextLevel();
            }
        });

        requestNewInterstitial();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // without ever showing it.
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    // Proceed to the next level.
                    goToNextLevel();
                }
                //goToNextLevel();
            }
        });


        //setNotification();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void goToNextLevel() {
        Intent iAddBirthDay = new Intent(MainActivity.this, BirthdaysAdd.class);
        startActivity(iAddBirthDay);
    }


    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public void setNotification() {
        BirthdayRepo birthdayRepo = new BirthdayRepo(this);
        ArrayList<Birthday> studentsList = new ArrayList<Birthday>();

        studentsList = birthdayRepo.getAllBirthday();

        Calendar rightNow = Calendar.getInstance();

// offset to add since we're not UTC
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
                (24 * 60 * 60 * 1000);

        System.out.println(sinceMidnight + " milliseconds since midnight");

        Date date = new Date();
      /*getTime():It returns the number of milliseconds since
       * January 1, 1970, 00:00:00 GMT
       * represented by this date.
       */
        currentTimeMilli = date.getTime();
        System.out.println("Time in milliseconds using Date class: " + currentTimeMilli);


        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long midNight = c.getTimeInMillis();
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000;
        long minPassed = secondsPassed / 60;
        long hourpassed = minPassed / 60;

        for (int i = 0; i < studentsList.size(); i++) {
            String strDate = studentsList.get(i).date.toString();
            String[] splitArray = strDate.split("-");
            String month = Birthday.getMonth(splitArray[0].toString());

            int birthMonth = Integer.parseInt(splitArray[0].toString());
            int birthDay = Integer.parseInt(splitArray[1].toString());

            if (Birthday.currentMonth == birthMonth && (Birthday.currentDay - 1) == birthDay) {
                Log.e("One Day", "One before +set notificaiton==" + (currentTimeMilli - Common.dayMiliseconds));

            }
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllFragment(), "All");
        adapter.addFrag(new FriendsFragment(), "Friends");
        adapter.addFrag(new FamilyFragment(), "Family");
        adapter.addFrag(new RelativeFragment(), "Relatives");
        adapter.addFrag(new ColleagueFragment(), "Colleagues");
        adapter.addFrag(new OtherFragment(), "Others");
        adapter.addFrag(new FavouriteFragment(), "Favourite");
        viewPager.setAdapter(adapter);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_rate_app) {
            // Toast.makeText(getApplicationContext(),"Rate App",Toast.LENGTH_SHORT).show();
            RateThisApp.showRateDialog(MainActivity.this);
            return true;
        }
        if (id == R.id.action_favourite) {
            // displayFavourite();
            //  Toast.makeText(getApplicationContext(),"Favourite Display",Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(6, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
