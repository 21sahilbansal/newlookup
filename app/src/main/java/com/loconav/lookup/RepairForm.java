package com.loconav.lookup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;


/**
 * Created by sejal on 12-07-2018.
 */

public class RepairForm  extends BaseFragment {
    @BindView (R.id.DevimageAfter) CustomImagePicker DevimageAfter;
    @BindView (R.id.owner_nameRep) EditText owner_nameRep;
    @BindView (R.id.imeiRep) EditText imeiRep;
    @BindView (R.id.Truck_no) EditText Truck_no;
    @BindView (R.id.client_idRep) EditText client_idRep;
    @BindView (R.id.spinnerRep) Spinner spinnerRep;
    @BindView (R.id.shareRep) Button shareRep;
    private boolean imageGet;
    private static RepairRequirements repairRequirements;
    Uri uri;
    int reasonid;
    PassingReason passingReason;


    @Override
    public int setViewId() {
        return R.layout.repair_form;
    }

    @Override
    public void onFragmentCreated() {
        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
        Client client = ((EnterDetails)getActivity()).getClient();
        CommonFunction.setEditText(imeiRep , deviceId);
        CommonFunction.setEditText(owner_nameRep, client.getName());
        CommonFunction.setEditText(client_idRep, client.getClientId());

        passingReason= (PassingReason)getArguments().getSerializable("str");
        uri= Uri.parse(getArguments().getString("Image"));
        ArrayList<String> list=new ArrayList<>();
        int sizelist=passingReason.getReasons().get(2).getReasons().size();
        for(int i=0;i<sizelist;i++) {
            list.add(passingReason.getReasons().get(2).getReasons().get(i).getName());
        }
        Log.e("ss", ""+String.valueOf(passingReason.getReasons().get(2).getReasons().get(1)));

       // ((EnterDetails) getActivity()).setSpinner(list,spinnerRep);

        shareRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DevimageAfter.GetimagesList().size()>=1){
                    imageGet=true;
                }else{
                    Toast.makeText(getContext(),"Add Device Image",Toast.LENGTH_SHORT).show();
                }
                if (CommonFunction.validate(new EditText[]{owner_nameRep, Truck_no,
                        imeiRep,client_idRep})) {
                    if(imageGet){
                        willDetails();
                        FragmentManager fragmentManager = getFragmentManager();
                        RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
                        Bundle bundle = new Bundle();
                        bundle.putString("Image", String.valueOf(uri));
                        bundle.putString("Image2", String.valueOf(DevimageAfter.GetimagesList().get(0).getUri()));
                        bundle.putSerializable("req",repairRequirements);
                        fragmentRepairAfterForm.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(android.R.id.content, fragmentRepairAfterForm, "Fragment One");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                }
            }
        });
    }

    public void willDetails(){
        spinnerData();
        JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put("Repairs","yes");
            jsonObj.put("Owner's name:",owner_nameRep.getText().toString());
            jsonObj.put("IMEI:",imeiRep.getText().toString());
            jsonObj.put( "Truck no.:",Truck_no.getText().toString());
            jsonObj.put("Client ID:",client_idRep.getText().toString());
          //  jsonObj.put("USER ID:", SharedPrefHelper.getInstance(getContext()).getStringData(USER_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("sej6",""+jsonObj);
        String b = jsonObj.toString().substring(1, jsonObj.toString().length() - 1);
        Log.e("sej9",""+b);
        ArrayList<String > al=new ArrayList<>();

     //   repairRequirements = new RepairRequirements(passingReason.getDeviceid(),reasonid,"remarks",b,al,"");

    }
    void spinnerData(){
        int sizelist=passingReason.getReasons().get(2).getReasons().size();
        String text = spinnerRep.getSelectedItem().toString();
        for(int i=0;i<sizelist;i++){
            if(passingReason.getReasons().get(2).getReasons().get(i).getName().equals(text)){
                reasonid=passingReason.getReasons().get(2).getReasons().get(i).getId();
            }
        }
        Log.e("sej8",""+reasonid);
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }

    public static RepairForm newInstance(PassingReason passingReason1, Uri stringUri) {
        RepairForm fragment = new RepairForm();
        Bundle bundle = new Bundle();
        bundle.putSerializable("str",passingReason1);
        bundle.putString("Image", String.valueOf(stringUri));
        fragment.setArguments(bundle);
        return fragment;
    }



}
