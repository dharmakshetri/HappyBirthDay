package co.happybirthday;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dharma on 1/22/2016.
 */
public class BirthdayDetails extends AppCompatActivity implements OnClickListener,OnItemClickListener {
    TextView tvName, tvBirthDay, tvRemainingTime;
    Button btnEmail, btnMessage, btnShare, btnEditNames;
    int birthDayId;
    Birthday birthday;
    ListView listViewMesssage;
    ArrayList<Message> arrayListMessae= new ArrayList<Message>();
    MessageBaseAdapter messageAdapter;
    Button btnEditMessage;
    TextView tvMessage;
    EditText editMessage;
    String messageType="";
    String textSubject="Happy Birthday to You!!!";
    String textMessage="";
    ImageView imgFav;
    int flagFav=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthdaydetails);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey(Birthday.KEY_ID))
            {
                // extract the extra-data in the Notification
                birthDayId = extras.getInt(Birthday.KEY_ID);
            }
        }

        BirthdayRepo birthdayRepo = new BirthdayRepo(this);
        birthday = birthdayRepo.getStudentById(birthDayId);
        getSupportActionBar().setTitle(Birthday.name+" Details");
        setUpViews();
        setUpValues();
    }

    public void setUpViews() {
        tvName = (TextView) findViewById(R.id.txtName);
        tvBirthDay = (TextView) findViewById(R.id.txtBirthDate);
        tvRemainingTime = (TextView) findViewById(R.id.txtRTime);

        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnMessage = (Button) findViewById(R.id.btnMessage);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnEditNames=(Button) findViewById(R.id.btnEdit);


        btnEditNames.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        listViewMesssage=(ListView)findViewById(R.id.listmessage);
        listViewMesssage.setVisibility(View.VISIBLE);
        tvMessage=(TextView)findViewById(R.id.tvMessages);
        editMessage=(EditText)findViewById(R.id.editMessage);
        btnEditMessage=(Button)findViewById(R.id.btnEditMessage);
        btnEditMessage.setOnClickListener(this);

        imgFav=(ImageView)findViewById(R.id.imgFav);
        imgFav.setOnClickListener(this);

    }

    public void setUpValues() {
        tvName.setText(birthday.name);
        String [] splitArray=birthday.date.split("-");
        String month=Birthday.getMonth(splitArray[0].toString());
        tvBirthDay.setText(month + " - " + splitArray[1].toString());
        tvRemainingTime.setText(birthday.notification);
        MessageRepo messageRepo= new MessageRepo(getApplicationContext());
        Message message= new Message(getApplicationContext());
        Log.e("LIXE SIXE","   1     arrayListMessae="+arrayListMessae.size());
        arrayListMessae.clear();
        Log.e("LIXE SIXE", "   2     arrayListMessae=" + arrayListMessae.size());
        arrayListMessae= messageRepo.getAllMessages(getApplicationContext());

        Log.e("LIXE SIXE","   3     arrayListMessae="+arrayListMessae.size());
        messageAdapter= new MessageBaseAdapter(getApplicationContext(), arrayListMessae);
        listViewMesssage.setAdapter(messageAdapter);
        listViewMesssage.setOnItemClickListener(this);
        flagFav=birthday.favourite;
        if(flagFav==0){
            imgFav.setBackgroundResource(R.drawable.nofav_32);

        }else{
            imgFav.setBackgroundResource(R.drawable.fav_32);
        }

    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        // TODO Auto-generated method stub
        int messageId = arrayListMessae.get(arg2).message_ID;
        showMessage(messageId);

    }

    protected void showMessage(int messageId) {
       // Toast.makeText(getApplicationContext(), ""+messageId, Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        MessageRepo messageRepo= new MessageRepo(getApplicationContext());
        Message message= new Message(getApplicationContext());
        message=messageRepo.getMessage(messageId,getApplicationContext());
        listViewMesssage.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message.messageName);
        btnEditMessage.setVisibility(View.VISIBLE);
        messageType="textview";
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                //Toast.makeText(getApplicationContext(), "id="+student.birthday_ID,Toast.LENGTH_SHORT).show();
                Intent ibackToEdit= new Intent(BirthdayDetails.this, BirthdaysAdd.class);
                ibackToEdit.putExtra(Birthday.KEY_ID,birthDayId);
                ibackToEdit.putExtra(Birthday.Status,1);
                startActivity(ibackToEdit);
                break;
            case R.id.btnEmail:

                if(messageType.equalsIgnoreCase("textview")){
                    textMessage=tvMessage.getText().toString();
                }
                if(messageType.equalsIgnoreCase("edittext")){
                    textMessage=editMessage.getText().toString();
                }
                if(messageType.length()>0){
                    BirthdayRepo birthdayRepo = new BirthdayRepo(getApplicationContext());
                    Birthday student= new Birthday();
                    student= birthdayRepo.getStudentById(birthDayId);
                    if(student.email.length()==0){
                        Toast.makeText(BirthdayDetails.this, "Email Address is not provided", Toast.LENGTH_SHORT).show();
                    }else {
                        sendEmail(student.email,textMessage);
                    }
                }else{
                    Toast.makeText(BirthdayDetails.this, "Please choose birthday message", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.btnMessage:

                if(messageType.equalsIgnoreCase("textview")){
                    textMessage=tvMessage.getText().toString();
                }
                if(messageType.equalsIgnoreCase("edittext")){
                    textMessage=editMessage.getText().toString();
                }
                if(messageType.length()>0){
                    BirthdayRepo birthdayRepo = new BirthdayRepo(getApplicationContext());
                    Birthday birthdayBoy= new Birthday();
                    birthdayBoy= birthdayRepo.getStudentById(birthDayId);
                    if(birthdayBoy.mobile.length()==0){
                        Toast.makeText(BirthdayDetails.this, "Mobile Number is not provided", Toast.LENGTH_SHORT).show();
                    }else {
                        sendSmsBySIntent(birthdayBoy.mobile,textMessage+" "+tvName.getText().toString()+" !!!");
                    }
                }else{
                    Toast.makeText(BirthdayDetails.this, "Please choose birthday message", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnShare:

                if(messageType.equalsIgnoreCase("textview")){
                    textMessage=tvMessage.getText().toString();
                }
                if(messageType.equalsIgnoreCase("edittext")){
                    textMessage=editMessage.getText().toString();
                }
                if(messageType.length()>0){
                    shareMessage(textMessage);
                }else{
                    Toast.makeText(BirthdayDetails.this, "Please choose birthday message", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnEditMessage:
                editMessage.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
                listViewMesssage.setVisibility(View.GONE);
                btnEditMessage.setVisibility(View.GONE);
                editMessage.setText(tvMessage.getText().toString());
                messageType="edittext";
                break;
            case R.id.imgFav:

                BirthdayRepo birthdayRepo = new BirthdayRepo(this);

                if (flagFav==0){
                    //Toast.makeText(getApplicationContext()," Faveourite "+student.birthday_ID,Toast.LENGTH_SHORT).show();
                    imgFav.setBackgroundResource(R.drawable.fav_32);
                    flagFav=1;

                }else{
                    //Toast.makeText(getApplicationContext(),"No Faveourite= "+student.birthday_ID,Toast.LENGTH_SHORT).show();
                    imgFav.setBackgroundResource(R.drawable.nofav_32);
                    flagFav=0;
                }
                birthdayRepo.updateFavourite(flagFav,birthday.birthday_ID);
                break;
        }
    }

    //share content in social media
    public void shareMessage(String messsage){

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, textSubject +birthday.name);
        share.putExtra(Intent.EXTRA_TEXT, messsage);

        startActivity(Intent.createChooser(share, "Share Messages!"));
    }

// send message

    public void sendSmsBySIntent(String phoneNo, String message) {
        // add the phone number in the data
        Uri uri = Uri.parse("smsto:" + phoneNo);

        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", message);
        try{
            startActivity(smsSIntent);
        } catch (Exception ex) {
            Toast.makeText(BirthdayDetails.this, "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
protected void sendSMSMessage(String phoneNo, String message) {
    try {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }

    catch (Exception e) {
        Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}
    //sendEmail

    public void sendEmail(String toEmail, String message){
        String[] TO = {toEmail};
       // String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
       // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textSubject+birthday.name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BirthdayDetails.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
