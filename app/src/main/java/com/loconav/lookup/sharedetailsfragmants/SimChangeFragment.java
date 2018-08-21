package com.loconav.lookup.sharedetailsfragmants;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CustomImagePicker;
import com.loconav.lookup.CustomInflater;
import com.loconav.lookup.Input;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.RepairAfterForm;
import com.loconav.lookup.Utility;
import com.loconav.lookup.databinding.SimchangeBinding;
import com.loconav.lookup.formData.Validation;
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
    private Boolean validate=false;
    ArrayList<Input> addtional = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>();
    JSONObject jsonObj = new JSONObject();

    @Override
    public int setViewId() {
        return R.layout.simchange;
    }

    @Override
    public void onFragmentCreated() {
        passingReason = ((LookupSubActivity) getActivity()).getPassingReason();
        addSpinnerData();
        CustomInflater customInflater = new CustomInflater(getContext());
        LinearLayout linearLayout = binding.ll;
        repairRequirements=new RepairRequirements();
        userChoice = passingReason.getUserChoice();
        addtional.addAll(passingReason.getReasonResponse().getAdditional_fields());
        for (int i = 0; i < addtional.size(); i++) {
            if (addtional.get(i).getField_type().equals("textView")) {
                customInflater.addtext(addtional.get(i).getHint() + passingReason.getDeviceid(), linearLayout, addtional.get(i), 0 + i);
            } else if (addtional.get(i).getField_type().equals("text")) {
                customInflater.addEditText(linearLayout, addtional.get(i), 0 + i);
            } else if (addtional.get(i).getField_type().equals("spinner")) {
                customInflater.addSpinner(linearLayout, spinnerList, 0 + i, addtional.get(i));
            } else if (addtional.get(i).getField_type().equals("ImagePicker")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), "Upload Image After Repair", 2, "view4");
            }
        }


        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < binding.ll.getChildCount() - 1; i++) {
                    View view = binding.ll.getChildAt(i);
                    validate = validator(view);
                    if(!validate){
                        break;
                    }
                }
                if(validate) {
                    RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("req", repairRequirements);
                    fragmentRepairAfterForm.setArguments(bundle);
                    loadFragment(fragmentRepairAfterForm, getFragmentManager(), R.id.frameLayout, true);
                }
            }
        });
    }

    private Boolean validator(View object) {
        if (object instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) object;
            EditText editText = textInputLayout.getEditText();
            if(CommonFunction.validateEdit(editText)){
                makeJson(editText);
            }else{
                return false;
            }
        } else if (object instanceof Spinner) {
            Spinner spinner=(Spinner)object;
            if (spinner.getSelectedItem().toString().equals("Select option")) {
                Toast.makeText(getContext(), "Select reasons", Toast.LENGTH_LONG).show();
                return false;
            }else{
                getSpinnerData(spinner);
            }
        } else if (object instanceof CustomImagePicker) {
            CustomImagePicker customImagePicker = (CustomImagePicker) object;
            if (customImagePicker.getimagesList().size() < 1) {
                Toast.makeText(getContext(), "Add Image After Repair", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                passImages(customImagePicker.getimagesList());
            }
        }
        return true;
    }

    private void passImages(ArrayList<ImageUri> imageUris) {
            ArrayList<String> imagesList1 = new ArrayList<>();
            imagesList1.addAll(passingReason.getImagesList());
            for (ImageUri imageUri : (imageUris)) {
                imagesList1.add(imageUri.getUri().toString());
            }
            passingReason.setImagesInRepair(imageUris.size());
            passingReason.imagesList.clear();
            passingReason.setImagesList(imagesList1);
            ((LookupSubActivity) getActivity()).setPassingReason(passingReason);
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    public void makeJson(EditText editText) {
        Input i = (Input) editText.getTag();
        try {
            jsonObj.put(userChoice, "yes");
            jsonObj.put(i.getName(), editText.getText().toString());
            if(i.getName().equals("remarks")){
                repairRequirements.setRemarks(editText.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sej6", "" + jsonObj);
        repairRequirements.setRepair_data(jsonObj.toString());
    }

    void getSpinnerData(Spinner spinner) {
        String text = spinner.getSelectedItem().toString();
        for (int i = 0; i < sizelist + 1; i++) {
            if (spinnerList.get(i).equals(text)) {
                reasonid = passingReason.getReasonResponse().getReasons().get(i - 1).getId();
            }
        }
        Log.e("id is", "getSpinnerData: " + reasonid);
        repairRequirements.setDevice_id(passingReason.getDeviceid());
        repairRequirements.setReason_id(reasonid);
    }

        @Override
        public String getTitle () {
            return "" + userChoice;
        }


    void addSpinnerData() {
        spinnerList.add("Select option");
        sizelist = passingReason.getReasonResponse().getReasons().size();
        for (int i = 0; i < sizelist; i++) {
            spinnerList.add(passingReason.getReasonResponse().getReasons().get(i).getName());
        }

    }
}