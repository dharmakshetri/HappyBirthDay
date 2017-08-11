package co.happybirthday;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dharma on 1/29/2016.
 */
public class Message {

    public static final String TABLE_MESSAGE = "Message";
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";

    public int message_ID;
    public String messageName;
    public static List<String> listMesssages = new ArrayList<String>();
    private DBHelper dbHelper;

    public Message(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Message message) {
        // TODO Auto-generated method stub

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Message.KEY_name, message.messageName);

        // Inserting Row
        long messsage_Id = db.insert(Message.TABLE_MESSAGE, null, values);
        db.close(); // Closing database connection
        return (int) messsage_Id;
    }

    public static void  insertMesssages(Context mcontext){

        MessageRepo messageRepo= new MessageRepo(mcontext);

        for (int i=0; i<Message.listMesssages.size();i++){
            Message message= new Message(mcontext);
            Log.e("ME", "message+ " + i + " " + Message.listMesssages.get(i).toString());
            message.messageName=Message.listMesssages.get(i).toString();
            //Log.e("MEDD","message+ "+ i+ " "+message.messageName);
            messageRepo.insert(message);
        }

    }
}
