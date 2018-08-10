package com.loconav.lookup.sharedetailsfragmants;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.loconav.lookup.CustomInflater;
import com.loconav.lookup.EnterDetails;
import com.loconav.lookup.Input;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.RepairAfterForm;
import com.loconav.lookup.databinding.SimchangeBinding;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by prateek on 13/11/17.
 */

public class SimChangeFragment extends Fragment {
    private SimchangeBinding binding;
    RepairRequirements repairRequirements;
    int reasonid, sizelist;
    PassingReason passingReason;
    private String userChoice;
    ArrayList<ImageUri> images =new ArrayList<>();
    ArrayList<Input> addtional = new ArrayList<>();
    ArrayList<String> SpinnerList = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> keyJson = new ArrayList<>();
    ArrayList<String> filedtype = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.simchange, vg, false);
        passingReason = (PassingReason) getArguments().getSerializable("str");
        userChoice = passingReason.getUserChoice();
        if (!openFragment(userChoice).isEmpty() ) {
            for (int i = 0; i < addtional.size(); i++) {
                name.add((String) addtional.get(i).getName());
                keyJson.add((String) addtional.get(i).getKey());
                filedtype.add((String) addtional.get(i).getFeild_type());
            }
            for (int i = 0; i < filedtype.size(); i++) {
                if (filedtype.get(i).equals("text")) {
                    CustomInflater customInflater = new CustomInflater(getContext());
                    LinearLayout linearLayout = binding.ll;
                    Log.e("tab", "swj" + linearLayout.getChildCount());
                    editTexts.add(customInflater.addEditText(linearLayout, name.get(i), 0 + i));
                }
            }
            binding.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.SimimageAfter.GetimagesList().size() >= 1) {
                        editTexts.get(0).setTag("new sim");
                        editTexts.get(1).setTag("old sim");
                        if (CommonFunction.validate(new EditText[]{editTexts.get(0), editTexts.get(1), binding.remarks,
                                binding.imei})&& CommonFunction.validateLength(new EditText[]{editTexts.get(0), editTexts.get(1)})) {
                            if (binding.spinnerSim.getSelectedItem().toString().equals("Select option")) {
                                Toast.makeText(getContext(), "Select reasons", Toast.LENGTH_LONG).show();
                            } else {
                                willDetails();
                                FragmentManager fragmentManager = getFragmentManager();
                                RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
                                Bundle bundle = new Bundle();
                                ArrayList<String> imagesList1 = new ArrayList<>();
                                imagesList1.addAll(passingReason.getImagesList());
                                for (ImageUri imageUri : (binding.SimimageAfter.GetimagesList())) {
                                    imagesList1.add(imageUri.getUri().toString());
                                }
                                passingReason.setImagesInRepair(binding.SimimageAfter.GetimagesList().size());
                                passingReason.imagesList.clear();
                                passingReason.setImagesList(imagesList1);
                                bundle.putSerializable("req", repairRequirements);
                                bundle.putSerializable("req2", passingReason);
                                fragmentRepairAfterForm.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.frameLayoutSecond, fragmentRepairAfterForm, "Fragment One");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Add Image After Repair", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            binding.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.SimimageAfter.GetimagesList().size() >= 1) {
                        if (CommonFunction.validate(new EditText[]{binding.remarks,
                                binding.imei,})) {
                            if (binding.spinnerSim.getSelectedItem().toString().equals("Select option")) {
                                Toast.makeText(getContext(), "Select reasons", Toast.LENGTH_LONG).show();
                            } else {
                                willDetails1();
                                FragmentManager fragmentManager = getFragmentManager();
                                RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
                                Bundle bundle = new Bundle();
                                ArrayList<String> imagesList1 = new ArrayList<>();
                                imagesList1.addAll(passingReason.getImagesList());
                                for (ImageUri imageUri : (binding.SimimageAfter.GetimagesList())) {
                                    imagesList1.add(imageUri.getUri().toString());
                                }
                                passingReason.setImagesInRepair(binding.SimimageAfter.GetimagesList().size());
                                passingReason.imagesList.clear();
                                passingReason.setImagesList(imagesList1);
                                bundle.putSerializable("req", repairRequirements);
                                bundle.putSerializable("req2", passingReason);
                                fragmentRepairAfterForm.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.frameLayoutSecond, fragmentRepairAfterForm, "Fragment One");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Add Image After Repair", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        String deviceId = ((EnterDetails) getActivity()).getDeviceID();
        CommonFunction.setEditText(binding.imei, deviceId);
        return binding.getRoot();
    }

    public static SimChangeFragment newInstance(PassingReason passingReason1) {
        SimChangeFragment fragment = new SimChangeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("str", passingReason1);
        fragment.setArguments(bundle);
        return fragment;
    }

    ArrayList<Input> openFragment(String userChoice) {
        SpinnerList.add("Select option");
        switch (userChoice) {
            case "SIM Change": {
                addtional = passingReason.getReasons().get(1).getAdditional_fields();
                sizelist = passingReason.getReasons().get(1).getReasons().size();
                for (int i = 0; i < sizelist; i++) {
                    SpinnerList.add(passingReason.getReasons().get(1).getReasons().get(i).getName());
                }
                setSpinner(SpinnerList, binding.spinnerSim);
                break;
            }
            case "Device Change": {
                addtional = passingReason.getReasons().get(0).getAdditional_fields();
                sizelist = passingReason.getReasons().get(0).getReasons().size();
                for (int i = 0; i < sizelist; i++) {
                    SpinnerList.add(passingReason.getReasons().get(0).getReasons().get(i).getName());
                }
               setSpinner(SpinnerList, binding.spinnerSim);
                break;
            }
            case "Repairs": {
                addtional = passingReason.getReasons().get(2).getAdditional_fields();
                sizelist = passingReason.getReasons().get(2).getReasons().size();
                for (int i = 0; i < sizelist; i++) {
                    SpinnerList.add(passingReason.getReasons().get(2).getReasons().get(i).getName());
                }
                setSpinner(SpinnerList, binding.spinnerSim);
                break;
            }
        }
        return addtional;
    }
    public void setSpinner(ArrayList<String> categories, Spinner spinnerRep ) {
        spinnerRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRep.setAdapter(dataAdapter);
    }
    public void willDetails() {
        getSpinnerData();
        JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put(userChoice,"yes");
            jsonObj.put("Old Sim No",editTexts.get(0).getText().toString());
            jsonObj.put("New Sim No",editTexts.get(1).getText().toString());
            jsonObj.put("IMEI",binding.imei.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sej6", "" + jsonObj);
        ArrayList<String> al = new ArrayList<>();
        ArrayList<String> al1 = new ArrayList<>();
        repairRequirements = new RepairRequirements(passingReason.getDeviceid(), reasonid, binding.remarks.getText().toString(), jsonObj.toString(), al, al1);
    }

    public void willDetails1() {
        getSpinnerData();
        JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put(userChoice, "yes");
            jsonObj.put("IMEI", binding.imei.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> al = new ArrayList<>();
        ArrayList<String> al1 = new ArrayList<>();
        repairRequirements = new RepairRequirements(passingReason.getDeviceid(), reasonid, binding.remarks.getText().toString(), jsonObj.toString(), al, al1);

    }

    void getSpinnerData() {
        String text = binding.spinnerSim.getSelectedItem().toString();
            for (int i = 0; i < sizelist+1; i++) {
                if (SpinnerList.get(i).equals(text)) {
                    if (userChoice.equals("SIM Change")) {
                        reasonid = passingReason.getReasons().get(1).getReasons().get(i-1).getId();
                    } else if (userChoice.equals("Device Change")) {
                        reasonid = passingReason.getReasons().get(0).getReasons().get(i-1).getId();
                    } else if (userChoice.equals("Repairs")) {
                        reasonid = passingReason.getReasons().get(2).getReasons().get(i-1).getId();
                    }

                }
            }
    }

}
