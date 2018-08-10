package com.loconav.lookup.sharedetailsfragmants;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.EnterDetails;
import com.loconav.lookup.R;
import com.loconav.lookup.RepairAfterForm;
import com.loconav.lookup.RepairForm;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.DevicechangeBinding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.loconav.lookup.Constants.USER_ID;


/**
 * Created by prateek on 13/11/17.
 */

public class DeviceChange extends Fragment {

//    private DevicechangeBinding binding;
//    Uri uri;
//    Boolean imageGet;
//    RepairRequirements repairRequirements;
//    int reasonid,sizelist;
//    PassingReason passingReason;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
//                             Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(
//                inflater, R.layout.devicechange, vg, false);
//
//        passingReason= (PassingReason)getArguments().getSerializable("str");
//        uri= Uri.parse(getArguments().getString("Image"));        ArrayList<String> list=new ArrayList<>();
//        sizelist=passingReason.getReasons().get(0).getReasons().size();
//        for(int i=0;i<sizelist;i++) {
//            list.add(passingReason.getReasons().get(0).getReasons().get(i).getName());
//        }
//        Log.e("ss", ""+String.valueOf(passingReason.getReasons().get(0).getReasons().get(1)));
//
//        binding.share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(binding.DevimageAfter.GetimagesList().size()>=1){
//                    imageGet=true;
//                }else{
//                    Toast.makeText(getContext(),"Add Device Image",Toast.LENGTH_SHORT).show();
//                }
//                if(CommonFunction.validate(new EditText[]{binding.ownerName, binding.vehicleNo,
//                        binding.oldImei, binding.newImei, binding.oldSimNo, binding.newSimNo, binding.deviceModel, binding.clientId})) {
//                    if(imageGet){
//                        willDetails();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("Image", String.valueOf(uri));
//                        bundle.putString("Image2", String.valueOf(binding.DevimageAfter.GetimagesList().get(0).getUri()));
//                        bundle.putSerializable("req",repairRequirements);
//                        fragmentRepairAfterForm.setArguments(bundle);
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(android.R.id.content, fragmentRepairAfterForm, "Fragment One");
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                    }
//                }
//            }
//        });
//        String deviceId = ((EnterDetails)getActivity()).getDeviceID();
//        Client client = ((EnterDetails)getActivity()).getClient();
//        CommonFunction.setEditText(binding.newImei , deviceId);
//        CommonFunction.setEditText(binding.ownerName, client.getName());
//        CommonFunction.setEditText(binding.clientId, client.getClientId());
//
//     //   ((EnterDetails) getActivity()).setSpinner(list,binding.spinnerDev);
//        return binding.getRoot();
//    }
//
//    void willDetails(){
//        spinnerData();
//        JSONObject jsonObj=new JSONObject();
//        try {
//            jsonObj.put("Device Change","yes");
//            jsonObj.put("Owner's name:",binding.ownerName.getText().toString());
//            jsonObj.put( "Vehicle  no.:",binding.vehicleNo.getText().toString());
//            jsonObj.put( "Old Imei No: ", binding.oldImei.getText().toString());
//            jsonObj.put( "New Imei No: ", binding.newImei.getText().toString());
//            jsonObj.put("Old Sim No",binding.oldSimNo.getText().toString());
//            jsonObj.put("New Sim No:",binding.newSimNo.getText().toString());
//            jsonObj.put("Device Model:", binding.deviceModel.getText().toString());
//            jsonObj.put("Client ID:",binding.clientId.getText().toString());
//            jsonObj.put("USER ID:", SharedPrefHelper.getInstance(getContext()).getStringData(USER_ID));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String b = jsonObj.toString().substring(1, jsonObj.toString().length() - 1);
//       // repairRequirements = new RepairRequirements(passingReason.getDeviceid(),reasonid,"remarks",b,"","");
//    }
//    public static DeviceChange newInstance(PassingReason passingReason1, Uri stringUri) {
//        DeviceChange fragment = new DeviceChange();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("str",passingReason1);
//        bundle.putString("Image", String.valueOf(stringUri));
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    void spinnerData(){
//        String text = binding.spinnerDev.getSelectedItem().toString();
//        for(int i=0;i<sizelist;i++){
//            if(passingReason.getReasons().get(0).getReasons().get(i).getName().equals(text)){
//                reasonid=passingReason.getReasons().get(0).getReasons().get(i).getId();
//            }
//        }
//    }
}
