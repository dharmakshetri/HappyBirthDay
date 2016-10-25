package co.happybirthday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "happybirth.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Birthday.TABLE_BIRTHDAY  + "("
                + Birthday.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Birthday.KEY_name + " TEXT, "
                + Birthday.KEY_email + " TEXT, "
                + Birthday.KEY_mobile + " TEXT, "
                + Birthday.KEY_date + " TEXT, "
                + Birthday.KEY_category + " TEXT, "
                + Birthday.KEY_favourite+" INTEGER,"
                + Birthday.KEY_notifications + " TEXT )";

        db.execSQL(CREATE_TABLE_STUDENT);

        String CREATE_TABLE_MESSAGE = "CREATE TABLE " + Message.TABLE_MESSAGE  + "("
                + Message.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Message.KEY_name + " TEXT )";

        db.execSQL(CREATE_TABLE_MESSAGE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + Birthday.TABLE_BIRTHDAY);
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_MESSAGE);
        // Create tables again
        onCreate(db);

    }

}
