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
    ArrayList<Input> addtional = new ArrayList<>();
    ArrayList<String> SpinnerList = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.simchange, vg, false);
        passingReason = (PassingReason) getArguments().getSerializable("str");
        userChoice = passingReason.getUserChoice();
        binding.imei.setTag("imei");
        binding.remarks.setTag("remarks");
        editTexts.add(binding.imei);
        editTexts.add(binding.remarks);
        if (!openFragment().isEmpty() ) {
            for (int i = 0; i < addtional.size(); i++) {
                if (addtional.get(i).getField_type().equals("text")) {
                    CustomInflater customInflater = new CustomInflater(getContext());
                    LinearLayout linearLayout = binding.ll;
                    editTexts.add(customInflater.addEditText(linearLayout, addtional.get(i), 0 + i));
                }
            }
            binding.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.SimimageAfter.GetimagesList().size() >= 1) {
                        if (CommonFunction.validateEdit(editTexts)) {
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

    ArrayList<Input> openFragment() {
        SpinnerList.add("Select option");
        addtional = passingReason.getReasonResponse().getAdditional_fields();
        sizelist = passingReason.getReasonResponse().getReasons().size();
        for (int i = 0; i < sizelist; i++) {
            SpinnerList.add(passingReason.getReasonResponse().getReasons().get(i).getName());
        }
        setSpinner(SpinnerList, binding.spinnerSim);
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
            for(int i=0;i<editTexts.size();i++) {
                jsonObj.put(editTexts.get(i).getTag().toString(), editTexts.get(i).getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sej6", "" + jsonObj);
        ArrayList<String> al = new ArrayList<>();
        ArrayList<String> al1 = new ArrayList<>();
        repairRequirements = new RepairRequirements(passingReason.getDeviceid(), reasonid, binding.remarks.getText().toString(), jsonObj.toString(), al, al1);
    }
    void getSpinnerData() {
        String text = binding.spinnerSim.getSelectedItem().toString();
            for (int i = 0; i < sizelist+1; i++) {
                if (SpinnerList.get(i).equals(text)) {
                    reasonid = passingReason.getReasonResponse().getReasons().get(i-1).getId();
                }
            }
        Log.e("id is", "getSpinnerData: "+reasonid );
    }

}
