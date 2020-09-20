package com.example.photorecognition.uploadphoto.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.photorecognition.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<message> {
    private int resourceId;

    public MessageAdapter(Context context, int textViewResourceId, List<message> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        message mes=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView title_tv=(TextView)view.findViewById(R.id.message_title);
        TextView time_tv=(TextView)view.findViewById((R.id.message_time));

        title_tv.setText(mes.getTitle());
        time_tv.setText((mes.getTime()));
        return view;
    }

}
