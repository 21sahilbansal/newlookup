package com.loconav.lookup.sharedetailsfragmants;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CustomInflater;
import com.loconav.lookup.Input;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.RepairAfterForm;
import com.loconav.lookup.databinding.SimchangeBinding;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.loconav.lookup.FragmentController.loadFragment;

/**
 * Created by prateek on 13/11/17.
 */

public class SimChangeFragment extends BaseTitleFragment {
    private SimchangeBinding binding;
    RepairRequirements repairRequirements;
    int reasonid, sizelist;
    PassingReason passingReason;
    private String userChoice;
    ArrayList<Input> addtional = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();

    @Override
    public int setViewId() {
        return R.layout.simchange;
    }

    @Override
    public void onFragmentCreated() {
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        userChoice = passingReason.getUserChoice();
        binding.imei.setTag("imei");
        binding.remarks.setTag("remarks");
        editTexts.add(binding.imei);
        editTexts.add(binding.remarks);
        openFragment();
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
                            makeJson();
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
                            ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                            bundle.putSerializable("req", repairRequirements);
                            fragmentRepairAfterForm.setArguments(bundle);
                            loadFragment(fragmentRepairAfterForm,getFragmentManager(),R.id.frameLayout,true);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Add Image After Repair", Toast.LENGTH_SHORT).show();
                }

            }
        });
        String deviceId =passingReason.getDeviceid();
        CommonFunction.setEditText(binding.imei, deviceId);
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    ArrayList<Input> openFragment() {
        spinnerList.add("Select option");
        addtional = passingReason.getReasonResponse().getAdditional_fields();
        sizelist = passingReason.getReasonResponse().getReasons().size();
        for (int i = 0; i < sizelist; i++) {
            spinnerList.add(passingReason.getReasonResponse().getReasons().get(i).getName());
        }
        setSpinner(spinnerList, binding.spinnerSim);
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
    public void makeJson() {
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
                if (spinnerList.get(i).equals(text)) {
                    reasonid = passingReason.getReasonResponse().getReasons().get(i-1).getId();
                }
            }
        Log.e("id is", "getSpinnerData: "+reasonid );
    }

    @Override
    public String getTitle() {
        return ""+userChoice;
    }
}
