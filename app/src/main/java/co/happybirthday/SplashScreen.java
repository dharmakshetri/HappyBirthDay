package co.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Dharma on 1/18/2016.
 */
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splachscreen);
        String []messages=getResources().getStringArray(R.array.messages);
        Message.listMesssages= Arrays.asList(messages);


        Thread timeThread= new Thread(){
        public  void run(){
            try {
                Log.e("SplashScreen", "SplashScreen+ " + Message.listMesssages.size());
//                if(Message.listMesssages.size()>0){
//                    Message.listMesssages.clear();
//                }
                Message.insertMesssages(getApplicationContext());
                //Log.e("SplashScreen", "SplashScreen++++ ");
            sleep(700);
            }catch(InterruptedException ie){
                    ie.printStackTrace();
             }finally {

                Intent iSplash= new Intent(SplashScreen.this, MainActivity.class);
                startActivity(iSplash);
            }
        }
        };
        timeThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
