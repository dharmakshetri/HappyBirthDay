package co.happybirthday;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Birthday {
    // Labels table name
    public static final String TABLE_BIRTHDAY = "Birthday";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_email = "email";
    public static final String KEY_mobile = "mobile";
    public static final String KEY_date = "date";
    public static final String KEY_category = "category";
    public static final String KEY_notifications = "notification";
    public static final String KEY_favourite="favourite";




    public static final String TABLE_CATEGORY = "Category";
    public static final String KEY_catId = "id";
    public static final String KEY_canName = "catname";

    public static final String TABLE_NOTIFICATION = "Botification";
    public static final String KEY_notId = "id";
    public static final String KEY_notName = "name";
    public static final String KStudentEY_time = "time";



    public int birthday_ID;
    public String name;
    public String email;
    public String mobile;
    public String date;
    public String category;
    public String notification;
    public  int favourite;

    public int category_ID;
    public String catName;

    public int notification_ID;
    public String notificationName;
    public String notificationTime;

    public static int currentDay;
    public static int currentMonth;
    public static int currentYear;
    public static int daysInMonth;


    public static final String all = "All";
    public static final String friends = "Friend";
    public static final String relative = "Relative";
    public static final String colleague = "Colleague";
    public static final String other = "Other";
    public static final String family = "Family";

    public static final String Status = "status";
    public static  String Edit = "edit";

    public static String[] arrayMonths = null;
    public static String[] arrayDays = null;
    public static String[] arraysCategory = null;
    public static String[] arraysMessages = null;
    public static List<String> arrayNotificationCheckList = new ArrayList<String>();


   public String packageName;

//    public Student(Context mcontext) {
//        super();
//        //packageName = mcontext.getPackageName();
//    }

    public static boolean importDB(String newDbPath, String newDbName) {
        boolean tmp = false;
        try{
            // TODO Auto-generated method stub
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Log.e("SD","sd.canWrite()="+sd.canWrite());
            if (sd.canWrite()){
                // First Create a new database

                String currentDBPath = "//data/data/" + "co.happybirthday" + "//databases//happybirth.db";
                String backupDBPath = newDbPath + newDbName;

                File currentDB = new File(currentDBPath);
                File backupDB = new File(backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                tmp = true;

            }
        }catch (Exception e){
            //Common.CreateLogFile("ImportExport", e);
            tmp = false;
        }
        return tmp;
    }

    public static String getMonth(String strmonth) {

        int intmonth = Integer.parseInt(strmonth);
        String month = "";
        switch (intmonth) {
            case 1:
                month = Birthday.arrayMonths[0].toString();
                break;
            case 2:
                month = Birthday.arrayMonths[1].toString();
                break;
            case 3:
                month = Birthday.arrayMonths[2].toString();
                break;
            case 4:
                month = Birthday.arrayMonths[3].toString();
                break;
            case 5:
                month = Birthday.arrayMonths[4].toString();
                break;
            case 6:
                month = Birthday.arrayMonths[5].toString();
                break;
            case 7:
                month = Birthday.arrayMonths[6].toString();
                break;
            case 8:
                month = Birthday.arrayMonths[7].toString();
                break;
            case 9:
                month = Birthday.arrayMonths[8].toString();
                break;
            case 10:
                month = Birthday.arrayMonths[9].toString();
                break;
            case 11:
                month = Birthday.arrayMonths[10].toString();
                break;
            case 12:
                month = Birthday.arrayMonths[11].toString();
                break;
        }

        return month;

    }

    public static int getCategoryId(String cat) {
        int catid =0;

        if (cat.equalsIgnoreCase(Birthday.friends)) {
            catid = 0;
        }
       else if (cat.equalsIgnoreCase(Birthday.family)) {
            catid = 1;
        }
        else if (cat.equalsIgnoreCase(Birthday.relative)) {
            catid = 2;
        }
        else if (cat.equalsIgnoreCase(Birthday.colleague)) {
            catid = 3;
        }
        else if (cat.equalsIgnoreCase(Birthday.other)) {
            catid = 4;
        }

        return catid;
    }

    public static void getTimes() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        Date currentTime = localCalendar.getTime();
        currentDay = localCalendar.get(Calendar.DATE);
        currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        currentYear = localCalendar.get(Calendar.YEAR);
        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        int currentDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
        int CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);
        daysInMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        System.out.println("Current Date and time details in local timezone");
        System.out.println("Current Date: " + currentTime);
        System.out.println("Current Day: " + currentDay);
        System.out.println("Current Month: " + currentMonth);
        System.out.println("Current Year: " + currentYear);
        System.out.println("Current Day of Week: " + currentDayOfWeek);
        System.out.println("Current Day of Month: " + currentDayOfMonth);
        System.out.println("Current Day of Year: " + CurrentDayOfYear);


        //getting time, date, day of week and other details in GMT timezone
        Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));


    }
}