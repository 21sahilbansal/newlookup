package com.loconav.lookup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.sharedetailsfragmants.NewInstallation;
import com.loconav.lookup.sharedetailsfragmants.SimChangeFragment;

import java.util.ArrayList;
import java.util.List;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;

public class EnterDetails extends BaseActivity {
    CustomActionBar customActionBar ;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String deviceID;
    private Client client;
    Uri uri;
    PassingReason passingReason;
    private String userChoice;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
//        customActionBar = new CustomActionBar();
//        customActionBar.getActionBar(this, R.drawable.leftarrow, R.string.enter_details, true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Bundle bundle = getIntent().getExtras();
        passingReason=(PassingReason) bundle.getSerializable("str");
        deviceID = passingReason.getDeviceid();
        userChoice=passingReason.getUserChoice();
        client = passingReason.getClientId();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(userChoice.equals("New Install")){
            NewInstallation f1 = new NewInstallation();
            fragmentTransaction.add(R.id.frameLayoutSecond, f1);
        }else {
            SimChangeFragment f1 = SimChangeFragment.newInstance(passingReason);
            fragmentTransaction.add(R.id.frameLayoutSecond, f1);
        }

        fragmentTransaction.commit();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    public String getDeviceID(){
        return deviceID;
    }

    public Client getClient() { return client; }


    public String reduceBititmap(Bitmap bitmap){
        Log.e("sizeof",""+encodeToBase64(bitmap, Bitmap.CompressFormat.PNG,0).length());
        Bitmap bm1 = getResizedBitmap(bitmap,500);
        String str= "data:image/png;base64,"+encodeToBase64(bm1, Bitmap.CompressFormat.PNG,0);
        Log.e("SIZE OF",""+str.length());
        return str;

    }
}
