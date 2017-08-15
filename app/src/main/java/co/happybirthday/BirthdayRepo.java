package co.happybirthday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class BirthdayRepo {
    private DBHelper dbHelper;

    public BirthdayRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Birthday birthday) {
        // TODO Auto-generated method stub

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Birthday.KEY_date, birthday.date);
        values.put(Birthday.KEY_email, birthday.email);
        values.put(Birthday.KEY_name, birthday.name);
        values.put(Birthday.KEY_mobile, birthday.mobile);
        values.put(Birthday.KEY_category, birthday.category);
        values.put(Birthday.KEY_favourite, birthday.favourite);
        values.put(Birthday.KEY_notifications, birthday.notification);

        // Inserting Row
        long birthday_Id = db.insert(Birthday.TABLE_BIRTHDAY, null, values);
        db.close(); // Closing database connection
        return (int) birthday_Id;
    }

    public void delete(int birthday_Id) {
        //int student_Id = getFirstStudent();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Birthday.TABLE_BIRTHDAY, Birthday.KEY_ID + "=" + birthday_Id, null);
        db.close(); // Closing database connection
    }

    public void update(Birthday birthday, int sid) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Birthday.KEY_date, birthday.date);
        values.put(Birthday.KEY_email, birthday.email);
        values.put(Birthday.KEY_name, birthday.name);
        values.put(Birthday.KEY_mobile, birthday.mobile);
        values.put(Birthday.KEY_category, birthday.category);
        values.put(Birthday.KEY_favourite, birthday.favourite);
        values.put(Birthday.KEY_notifications, birthday.notification);

        db.update(Birthday.TABLE_BIRTHDAY, values, Birthday.KEY_ID + "=" + sid, null);
        db.close(); // Closing database connection
    }

    public void updateFavourite(int fav, int sid) {

        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Birthday.KEY_favourite, fav);
        db.update(Birthday.TABLE_BIRTHDAY, values, Birthday.KEY_ID + "=" + sid, null);
        db.close(); // Closing database connection
    }

    // get all data
    public ArrayList getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_category + "," +
                Birthday.KEY_notifications + "," +
                Birthday.KEY_date +
                " FROM " + Birthday.TABLE_BIRTHDAY;

        ArrayList birthdayList = new ArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                birthdayList.add(cursor.getString(cursor.getColumnIndex(Birthday.KEY_ID)) + "_"
                                + cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                                + cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                                + cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthdayList;

    }

    // get all data and set in students

    public ArrayList<Birthday> getAllBirthday() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + "," +
                Birthday.KEY_date + "," +
                Birthday.KEY_category + "," +
                Birthday.KEY_favourite +" , "+
                Birthday.KEY_notifications +
                " FROM " + Birthday.TABLE_BIRTHDAY +" ORDER BY "+Birthday.KEY_name+ " ASC";

        //ArrayList studentList =new ArrayList();
        ArrayList<Birthday> birthdayList = new ArrayList<Birthday>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Birthday birthday = new Birthday();
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name));
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email));
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile));
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category));
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date));
                birthday.favourite=cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite));
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications));

                birthdayList.add(birthday);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthdayList;

    }

    // get data  accorading to category and set in students

    public ArrayList<Birthday> getCategoryBirthDay(String cat) {
        String category = cat;
        Log.e("category", "===" + cat);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + "," +
                Birthday.KEY_date + "," +
                Birthday.KEY_category + "," +
                Birthday.KEY_favourite + "," +
                Birthday.KEY_notifications +
                " FROM " + Birthday.TABLE_BIRTHDAY + " WHERE " + Birthday.KEY_category + "='" + category + "'";

        //ArrayList studentList =new ArrayList();
        ArrayList<Birthday> birthdayList = new ArrayList<Birthday>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Birthday birthday = new Birthday();
                Log.e("Category", " Frined= Name=" + cursor.getColumnIndex(Birthday.KEY_name));
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name));
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email));
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile));
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category));
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date));
                birthday.favourite=cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite));
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications));

                birthdayList.add(birthday);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthdayList;

    }

    // get all favourite

    public ArrayList<Birthday> getFavouriteBirthDay() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + "," +
                Birthday.KEY_date + "," +
                Birthday.KEY_category + "," +
                Birthday.KEY_favourite + "," +
                Birthday.KEY_notifications +
                " FROM " + Birthday.TABLE_BIRTHDAY + " WHERE " + Birthday.KEY_favourite + "=" + 1;

        //ArrayList studentList =new ArrayList();
        ArrayList<Birthday> birthdayList = new ArrayList<Birthday>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Birthday birthday = new Birthday();
                Log.e("Category", " Frined= Name=" + cursor.getColumnIndex(Birthday.KEY_name));
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name));
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email));
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile));
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category));
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date));
                birthday.favourite=cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite));
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications));

                birthdayList.add(birthday);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthdayList;

    }

    public ArrayList<HashMap<String, String>> getStudentList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_date +
                " FROM " + Birthday.TABLE_BIRTHDAY;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> student = new HashMap<String, String>();
                student.put("id", cursor.getString(cursor.getColumnIndex(Birthday.KEY_ID)));
                student.put("name", cursor.getString(cursor.getColumnIndex(Birthday.KEY_name)));
                studentList.add(student);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;

    }

    public Birthday getStudentById(int Id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + " , " +
                Birthday.KEY_category + "," +
                Birthday.KEY_notifications + "," +
                Birthday.KEY_favourite + ","+
                Birthday.KEY_date +
                " FROM " + Birthday.TABLE_BIRTHDAY
                + " WHERE " +
                Birthday.KEY_ID + "=" + Id;


        int iCount = 0;
        Birthday birthday = new Birthday();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name));
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email));
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date));
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile));
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category));
                birthday.favourite=cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite));
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthday;
    }

    private int getFirstStudent() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID +
                " FROM " + Birthday.TABLE_BIRTHDAY
                + " LIMIT 1;";


        int student_Id = 0;


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                student_Id = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return student_Id;
    }

    public int getLastBirthday() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Birthday.KEY_ID +
                " FROM " + Birthday.TABLE_BIRTHDAY
                + " ORDER BY " +
                Birthday.KEY_ID + " DESC ";
        //" DESC LIMIT 1;" ;


        int birthday_Id = 0;


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                birthday_Id = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID));
                break;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return birthday_Id;
    }


}