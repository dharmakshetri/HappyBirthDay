package co.happybirthday.fragments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.happybirthday.Birthday;


public class MyBaseAdapter extends BaseAdapter {

	//ArrayList<String> myList = new ArrayList();
    ArrayList<Birthday> birthdayList = new ArrayList<>();

    LayoutInflater inflater;
    Context context;


    public MyBaseAdapter(Context context, ArrayList<Birthday> nameList) {
        this.birthdayList = nameList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);

      //  updateAdapter(myList);
    }
    public void updateAdapter(ArrayList<Birthday> arrylst) {
        this.birthdayList= arrylst;
    }
    @Override
    public int getCount() {
        return birthdayList.size();
    }

    @Override
    public String getItem(int position) {
        return birthdayList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.birthdayrowitem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        //ListData currentListData = getItem(position);


        Birthday.getTimes();


        String date=birthdayList.get(position).date.toString();
        String [] splitArray=date.split("-");
        String month=Birthday.getMonth(splitArray[0].toString());

        int birthMonth=Integer.parseInt(splitArray[0].toString());
        int birthDay=Integer.parseInt(splitArray[1].toString());

        Log.e("%%%%%%%%","##############################");
        int differenceMonth=Birthday.currentMonth-birthMonth;
        int differenceDay=Birthday.currentDay-birthDay;
        //Student.currentMonth=6;
        //Student.currentDay=10;
        if(birthMonth==Birthday.currentMonth && birthDay==Birthday.currentDay){
            mViewHolder.tvRTime.setText("Today is BirthDay, wish to click");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#0000FF"));
        }else if(birthMonth>Birthday.currentMonth && birthDay>Birthday.currentDay){
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
            mViewHolder.tvRTime.setText(birthMonth-Birthday.currentMonth+" Month and "+(birthDay-Birthday.currentDay)+" Days to go");
        }else if(birthMonth>Birthday.currentMonth && birthDay<Birthday.currentDay){
            int lastMonthDays=Birthday.daysInMonth-Birthday.currentDay;
            int finalMonths=(birthMonth-(Birthday.currentMonth+1));
            Log.e("final","FInal months="+finalMonths);
            if(finalMonths==0){
                mViewHolder.tvRTime.setText((lastMonthDays+birthDay) +" Days  Gone");
                mViewHolder.tvRTime.setTextColor(Color.parseColor("#FF0000"));
            }else{
                mViewHolder.tvRTime.setText((birthMonth-(Birthday.currentMonth+1))+" Month and "+(lastMonthDays+birthDay) +" Days to go");
                mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }else if(birthMonth<Birthday.currentMonth && birthDay>Birthday.currentDay){
            int dMonth=12-Birthday.currentMonth;
            if(dMonth+birthMonth>0){
                mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
                mViewHolder.tvRTime.setText((dMonth+birthMonth)+" Month and "+(birthDay-Birthday.currentDay)+" Days to go");
            }else{
                mViewHolder.tvRTime.setText((birthDay-Birthday.currentDay)+" Days Gone");
                mViewHolder.tvRTime.setTextColor(Color.parseColor("#FF0000"));
            }


        }else if(birthMonth<Birthday.currentMonth && birthDay<Birthday.currentDay){
            int dMonth=12-(Birthday.currentMonth+1);
            int lastMonthDays=Birthday.daysInMonth-Birthday.currentDay;
            mViewHolder.tvRTime.setText((dMonth+birthMonth)+" Month and "+(lastMonthDays+birthDay) +" Days to go");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(birthMonth==Birthday.currentMonth && birthDay<Birthday.currentDay){

            mViewHolder.tvRTime.setText( Birthday.currentDay-birthDay+" Days gone");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FF0000"));
        }else if(birthMonth==Birthday.currentMonth && birthDay>Birthday.currentDay){
            mViewHolder.tvRTime.setText( birthDay-Birthday.currentDay+" Days to go");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(birthMonth>Birthday.currentMonth && birthDay==Birthday.currentDay){
            mViewHolder.tvRTime.setText( birthMonth-Birthday.currentMonth+" Month to go");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(birthMonth<Birthday.currentMonth && birthDay==Birthday.currentDay){
            int dMonth=12-(Birthday.currentMonth+1);
            mViewHolder.tvRTime.setText( (dMonth+birthMonth)+" Month to go");
            mViewHolder.tvRTime.setTextColor(Color.parseColor("#FFFFFF"));
        }


        //Log.e("Data"," ->"+studentList.get(position).notification);
        if (birthdayList.get(position).name.toString().length()==0|| birthdayList.get(position)==null){
            mViewHolder.tvTitle.setText("No Name");
        }else {
            mViewHolder.tvTitle.setText(birthdayList.get(position).name.toString());
        }
        mViewHolder.tvDate.setText(month + "-" + splitArray[1].toString());

        //mViewHolder.tvDesc.setText(currentListData.getDescription());
        //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

        return convertView;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDate, tvRTime;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvName);
            tvDate=(TextView) item.findViewById(R.id.tvBirthDate);
            tvRTime=(TextView) item.findViewById(R.id.tvRemainingTime);
           // tvDesc = (TextView) item.findViewById(R.id.tvDesc);
           // ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
        }
    }


}
