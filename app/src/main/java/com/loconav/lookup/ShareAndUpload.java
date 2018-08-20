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

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShareAndUpload extends BaseTitleFragment {

    SharedPrefHelper sharedPrefHelper ;
    String url, message;
    @BindView(R.id.upload_document) Button upload_document;
    @BindView(R.id.share) Button share;

    @Override
    public String getTitle() {
        return "Share Details";
    }

    @Override
    public int setViewId() {
        return R.layout.activity_share_and_upload;
    }

    @Override
    public void onFragmentCreated() {
        initSharedPf();
        url= sharedPrefHelper.getStringData("upload_url");
        message= sharedPrefHelper.getStringData("message");
        upload_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(url);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnWhatsApp(message,getContext());
            }
        });
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }
    private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance(getContext());
    }

    public void shareOnWhatsApp( String text, Context context) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/html");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT,  text);
        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Whatapp not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void sendAppMsg(String message) {
        Log.e("message ", message);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = message;
        intent.setPackage("com.whatsapp");

        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            startActivity(Intent.createChooser(intent, text));
        } else {
            Toast.makeText(getContext(), "Whatapp not found", Toast.LENGTH_SHORT)
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
