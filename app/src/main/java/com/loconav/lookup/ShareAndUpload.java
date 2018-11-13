package com.loconav.lookup;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.ActivityShareAndUploadBinding;

public class ShareAndUpload extends BaseTitleFragment {

    private SharedPrefHelper sharedPrefHelper ;
    private String url, message;
    private ActivityShareAndUploadBinding binding;


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
        binding.uploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(url);
            }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnWhatsApp(message,getContext());
            }
        });
    }

    @Override
    public void bindView(View view) {
        binding= DataBindingUtil.bind(view);}

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
            Toast.makeText(getContext(), "Whatsapp not found", Toast.LENGTH_SHORT)
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
