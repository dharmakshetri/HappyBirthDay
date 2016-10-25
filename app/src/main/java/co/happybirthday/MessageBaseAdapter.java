package co.happybirthday;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageBaseAdapter extends BaseAdapter {

    ArrayList<Message> messagesList = new ArrayList<Message>();

    LayoutInflater inflater;
    Context context;


    public MessageBaseAdapter(Context context, ArrayList<Message> nameList) {
        this.messagesList = nameList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);

      //  updateAdapter(myList);
    }
    public void updateAdapter(ArrayList<Message> arrylst) {
        this.messagesList= arrylst;
    }
    @Override
    public int getCount() {Log.e("messagesList","messagesList Size"+messagesList.size());
        return messagesList.size();
    }

    @Override
    public String getItem(int position) {
        return messagesList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        Log.e("messagesList","messagesList"+messagesList.size());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.messagerow, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
         String message=messagesList.get(position).messageName;
        if(message.length()<=100){
            mViewHolder.tvMessageTitle.setText(message);
        }else{

            String subMessage=message.substring(0,100);
            mViewHolder.tvMessageTitle.setText(subMessage+"...");
        }



        return convertView;
    }

    private class MyViewHolder {
        TextView tvMessageTitle;

        public MyViewHolder(View item) {
            tvMessageTitle = (TextView) item.findViewById(R.id.tvMessageName);

        }
    }


}
