package com.loconav.lookup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Customlook extends ArrayAdapter<Lookwrap> {
    Context context;
    Button share;
	public Customlook(Context context, ArrayList<Lookwrap> users, Button share) {
        super(context, 0, users);
        this.context = context;
        this.share = share;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// Get the data item for this position
        Lookwrap user = getItem(position);
       
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.looklayout, parent, false);
        }

        int deviceok = 0;
        int passedok = 0;
       // Lookup view for data population
//       TextView txtTitle1 = (TextView) convertView.findViewById(R.id.txt1);
//       TextView txtTitle2 = (TextView) convertView.findViewById(R.id.txt2);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.bimg);
        ImageView imageView1 = (ImageView) convertView.findViewById(R.id.iimg);
        ImageView imageView2 = (ImageView) convertView.findViewById(R.id.iimg1);
        ImageView passed = (ImageView) convertView.findViewById(R.id.passed);
        TextView txtTitle1 = (TextView) convertView.findViewById(R.id.locatedatans);
        TextView txtTitle2 = (TextView) convertView.findViewById(R.id.locationans);
        TextView txtTitle3 = (TextView) convertView.findViewById(R.id.distanceans);
        TextView txtTitle4 = (TextView) convertView.findViewById(R.id.orientationans);
        TextView txtTitle5 = (TextView) convertView.findViewById(R.id.iostateans);
        TextView tView = (TextView) convertView.findViewById(R.id.device_id);
       
        if(user.ioState.substring(0, 1).equals("0") ) {
            passedok += 1;
            deviceok += 1;
            imageView.setImageResource(R.drawable.greentick);
        } else {
    	    imageView.setImageResource(R.drawable.transparent);
        }
       
		
       if(user.ioState.substring(1, 2).equals("1") ) {
           passedok += 1;
           imageView1.setImageResource(R.drawable.greentick);
       } else {
           imageView1.setImageResource(R.drawable.transparent);
       }

        String data = user.locatedAt.split("T",2)[0];
        String time = user.locatedAt.split("T",2)[1];
		txtTitle1.setText(data
										+ ", " + time.substring(0, 8));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        try {
            String parsestring = data+" "+time;
            Date date = simpleDateFormat.parse(parsestring);
            long difference = (System.currentTimeMillis() - date.getTime())/1000;
            if(difference < 300){
                passedok += 1;
                deviceok += 1;
                imageView2.setBackgroundResource(R.drawable.greentick);
            }else{
                imageView2.setBackgroundResource(R.drawable.transparent);
            }
            Log.e("DiffeTime",difference+"");
        }catch (Exception ex){
            Log.e("Exception",ex.toString());
        }
        if(passedok == 3){
            passed.setBackgroundResource(R.drawable.passed);
            passed.setVisibility(View.VISIBLE);
            share.setBackgroundResource(R.color.pressed);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShareDetails.class);
                    context.startActivity(intent);
                }
            });
        }else if(deviceok == 2){
            passed.setVisibility(View.GONE);
            share.setBackgroundResource(R.color.pressed);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShareDetails.class);
                    context.startActivity(intent);
                }
            });
        }else{
            passed.setVisibility(View.GONE);
            share.setBackgroundResource(R.color.gray);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Some Problem", Toast.LENGTH_LONG).show();
                }
            });
        }
		txtTitle2.setText(user.lati + ", " + user.longi);
		
		txtTitle3.setText(user.distance);
		
		txtTitle4.setText(user.orientation);

		txtTitle5.setText(user.ioState.substring(2));
		
		tView.setText(user.device_id);
		
		return convertView;
   }
    
    
    
}