package com.loconav.lookup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.loconav.lookup.EncodingDecoding.decodeBase64;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class encoding extends AppCompatActivity {
ImageView imageView1,imageView2;
EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoding);
        imageView1=findViewById(R.id.iv1);
        imageView2=findViewById(R.id.iv2);
        editText=findViewById(R.id.et1);
                displayImage();
    }

    private void displayImage() {
        Bitmap icon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.red_tick);
        imageView1.setImageBitmap(icon);
        String myBase64Image = encodeToBase64(icon,Bitmap.CompressFormat.PNG, 80);
        Log.e("ss",""+myBase64Image);
        editText.setText(myBase64Image);
        Bitmap myBitmapAgain = decodeBase64(myBase64Image);
        imageView2.setImageBitmap(myBitmapAgain);
    }

}
