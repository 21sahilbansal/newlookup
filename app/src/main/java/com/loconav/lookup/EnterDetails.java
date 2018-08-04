package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;
import com.loconav.lookup.sharedetailsfragmants.DeviceChange;
import com.loconav.lookup.sharedetailsfragmants.NewInstallation;
import com.loconav.lookup.sharedetailsfragmants.SimChange;
import com.loconav.lookup.sharedetailsfragmants.VehicleChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;

public class EnterDetails extends AppCompatActivity {
    CustomActionBar customActionBar ;
    private List<Bitmap> deviceImageBitmap = new ArrayList<>();
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
        customActionBar = new CustomActionBar();
        customActionBar.getActionBar(this, R.drawable.leftarrow, R.string.enter_details, true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Bundle bundle = getIntent().getExtras();
       // uri= Uri.parse(bundle.getString("Image"));
        passingReason=(PassingReason) bundle.getSerializable("str");
        deviceID = passingReason.getDeviceid();
        userChoice=passingReason.getUserChoice();
        client = passingReason.getClientId();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(userChoice.equals("newInstall")){
            NewInstallation f1 = new NewInstallation();
            fragmentTransaction.add(R.id.frameLayoutSecond, f1);
        }else {
            SimChange f1 = SimChange.newInstance(passingReason);
            fragmentTransaction.add(R.id.frameLayoutSecond, f1);
        }

        fragmentTransaction.commit();
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
