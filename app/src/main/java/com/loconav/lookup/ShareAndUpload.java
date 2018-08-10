package com.loconav.lookup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseActivity;


public class ShareAndUpload extends BaseActivity {

    SharedPrefHelper sharedPrefHelper ;
    String url, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_and_upload);
        initSharedPf();
     //   url = sharedPreferences.getString("upload_url", "");
        url= sharedPrefHelper.getStringData("upload_url");
//        message = sharedPreferences.getString("message", "");
        message= sharedPrefHelper.getStringData("message");

        Button upload_document = (Button) findViewById(R.id.upload_document);
        upload_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(url);
            }
        });

        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAppMsg(message);
            }
        });

    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance(getBaseContext());
    }


    public void sendAppMsg(String message) {
        Log.e("message ", message);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = message;
        // change with required  application package
        intent.setPackage("com.whatsapp");

        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            startActivity(Intent.createChooser(intent, text));
        } else {
            Toast.makeText(this, "Whatapp not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void send(String message) {
        if(!message.equals("")) {
            Log.e("message to open ", message);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(message));
            startActivity(browserIntent);
        }
    }
}
