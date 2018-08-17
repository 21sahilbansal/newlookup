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
import com.loconav.lookup.databinding.SimchangeBinding;
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
    // ArrayList<EditText> editTexts = new ArrayList<>();
    JSONObject jsonObj = new JSONObject();

    @Override
    public int setViewId() {
        return R.layout.simchange;
    }

    @Override
    public void onFragmentCreated() {
        passingReason = ((LookupSubActivity) getActivity()).getPassingReason();
        addOtherFields();
        addSpinnerData();
        CustomInflater customInflater = new CustomInflater(getContext());
        LinearLayout linearLayout = binding.ll;
        userChoice = passingReason.getUserChoice();
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
                    if (addtional.get(i).getField_type().equals("text")) {
                        TextInputLayout textInputLayout = (TextInputLayout) view;
                        EditText editText = textInputLayout.getEditText();
                        makeJson(editText);
                    } else if (addtional.get(i).getField_type().equals("spinner")) {
                        Spinner spinner = (Spinner) view;
                        getSpinnerData(spinner);
                    } else if (addtional.get(i).getField_type().equals("ImagePicker")) {
                        CustomImagePicker customImagePicker = (CustomImagePicker) view;
                        customImagePicker.getimagesList();
                    }
                }
            }});
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    public void makeJson(EditText editText) {
        if (CommonFunction.validateEdit(editText)) {
            Input i = (Input) editText.getTag();
            Log.e("ss", "makeJson: " + i + i.getName());
            try {
                jsonObj.put(userChoice, "yes");
                jsonObj.put(i.getName(), editText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sej6", "" + jsonObj);
            ArrayList<String> al = new ArrayList<>();
            ArrayList<String> al1 = new ArrayList<>();
            repairRequirements = new RepairRequirements(passingReason.getDeviceid(), reasonid, "", jsonObj.toString(), al, al1);
        }
    }

    void getSpinnerData(Spinner spinner) {
        String text = spinner.getSelectedItem().toString();
        if (spinner.getSelectedItem().toString().equals("Select option")) {
            Toast.makeText(getContext(), "Select reasons", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < sizelist + 1; i++) {
                if (spinnerList.get(i).equals(text)) {
                    reasonid = passingReason.getReasonResponse().getReasons().get(i - 1).getId();
                }
            }
        }
            Log.e("id is", "getSpinnerData: " + reasonid);
        }

        @Override
        public String getTitle () {
            return "" + userChoice;
        }

    private void addOtherFields() {
        Input i1 = new Input("deviceId", "imei", "textView", "Device Id :");
        Input i2 = new Input("remarks", "remarks", "text", "");
        Input i3 = new Input("reasons", "reasons", "spinner", spinnerList);
        Input i4 = new Input("addImage", "addImage", "ImagePicker", "");
        addtional.add(i1);
        addtional.add(i2);
        addtional.add(i3);
        addtional.add(i4);
    }

    void addSpinnerData() {
        spinnerList.add("Select option");
        addtional.addAll(passingReason.getReasonResponse().getAdditional_fields());
        sizelist = passingReason.getReasonResponse().getReasons().size();
        for (int i = 0; i < sizelist; i++) {
            spinnerList.add(passingReason.getReasonResponse().getReasons().get(i).getName());
        }
    }
}

    //                if (binding.SimimageAfter.GetimagesList().size() >= 1) {
    // if (CommonFunction.validateEdit(editTexts)) {
//                       else {
    //  makeJson();
//    RepairAfterForm fragmentRepairAfterForm = new RepairAfterForm();
//    Bundle bundle = new Bundle();
//    ArrayList<String> imagesList1 = new ArrayList<>();
//                imagesList1.addAll(passingReason.getImagesList());
////                            for (ImageUri imageUri : (binding.SimimageAfter.GetimagesList())) {
////                                imagesList1.add(imageUri.getUri().toString());
////                            }
//                        //  passingReason.setImagesInRepair(binding.SimimageAfter.GetimagesList().size());
//                        passingReason.imagesList.clear();
//                        passingReason.setImagesList(imagesList1);
//                        ((LookupSubActivity) getActivity()).setPassingReason(passingReason);
////                            bundle.putSerializable("req", repairRequirements);
////                            fragmentRepairAfterForm.setArguments(bundle);
////                            loadFragment(fragmentRepairAfterForm,getFragmentManager(),R.id.frameLayout,true);
//                        // }
//                        // }
////                } else {
////                    Toast.makeText(getContext(), "Add Image After Repair", Toast.LENGTH_SHORT).show();
//                        // }
//
//                        }
//                        });
//                        String deviceId = passingReason.getDeviceid();
//CommonFunction.setEditText(binding.imei, deviceId);