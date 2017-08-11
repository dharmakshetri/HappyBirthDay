package co.happybirthday;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dharma on 1/29/2016.
 */
public class MessageRepo {
    private DBHelper dbHelper;

    public MessageRepo(Context context) {  dbHelper = new DBHelper(context);   }

    public int insert(Message message) {
        // TODO Auto-Me method stub

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Message.KEY_name, message.messageName);

        // Inserting Row
        long mId = db.insert(Message.TABLE_MESSAGE, null, values);

        db.close(); // Closing database connection
        return (int) mId;
    }

    public ArrayList<Message> getAllMessages(Context mcontext) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Message.KEY_ID + "," +
                Message.KEY_name +
                " FROM " + Message.TABLE_MESSAGE;

        ArrayList<Message> messagesList=  new ArrayList<Message>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.e("Cusor"," ="+cursor.getCount());
        // looping through all rows and adding to list
        messagesList.clear();
        Log.e("Cusor", " size of cursor=" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Message message= new Message(mcontext);
                message.message_ID=cursor.getInt(cursor.getColumnIndex(Message.KEY_ID));
                message.messageName=cursor.getString(cursor.getColumnIndex(Message.KEY_name));
                messagesList.add(message);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return messagesList;

    }

    public Message getMessage(int Id, Context context) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Message.KEY_ID + "," +
                Message.KEY_name +
                " FROM " + Message.TABLE_MESSAGE
                + " WHERE " +
                Message.KEY_ID + "=" + Id;

        Log.e("selectQuery", "selectQuery=" + selectQuery);
        int iCount = 0;
        Message message = new Message(context);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                message.message_ID = cursor.getInt(cursor.getColumnIndex(Message.KEY_ID));
                message.messageName = cursor.getString(cursor.getColumnIndex(Message.KEY_name));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return message;
    }
}
