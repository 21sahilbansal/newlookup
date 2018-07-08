package com.loconav.lookup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.loconav.lookup.R;
import com.loconav.lookup.widget.WrapHeightGridView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sejal on 05-07-2018.
 */

public class AdapterGridView extends android.widget.BaseAdapter {
    int groupid;
    Context context;
    ArrayList<Uri> imagesUriArrayList;
    Bitmap bitmap;
    ImageView imageSelcted;
    ImageView crossImage;
    private static final int PADDING = 8;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;

    public AdapterGridView(Context context, int groupid, ArrayList imagesUriArrayList){
        this.context=context;
        this.groupid=groupid;
        this.imagesUriArrayList=imagesUriArrayList;

    }
    public int getCount(){
        return imagesUriArrayList.size();
    }
    public long getItemId(int position) { return position; }
    public Uri getItem(int position) { return imagesUriArrayList.get(position);}

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        imageSelcted= (ImageView) itemView.findViewById(R.id.imageSelected);
        crossImage = (ImageView) itemView.findViewById(R.id.remove);
        Uri selectedImage = imagesUriArrayList.get(position);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),selectedImage);
            notifyDataSetChanged();
            //bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2 , bitmap.getHeight()/2, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageSelcted.setLayoutParams(new GridView.LayoutParams(WIDTH,HEIGHT));
        imageSelcted.setImageBitmap(bitmap);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesUriArrayList.remove(imagesUriArrayList.get(position));
                notifyDataSetChanged();
            }
        });
        return itemView;

    }



}

