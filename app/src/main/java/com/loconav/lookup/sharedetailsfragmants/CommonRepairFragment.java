package com.loconav.lookup.sharedetailsfragmants;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.CustomInflater;
import com.loconav.lookup.Input;
import com.loconav.lookup.LandingActivity;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.databinding.FragmentCommonRepairBinding;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.customcamera.FileUtils;
import com.loconav.lookup.customcamera.ImageUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by prateek on 13/11/17.
 */

public class CommonRepairFragment extends BaseTitleFragment {
    private FragmentCommonRepairBinding binding;
    private RepairRequirements repairRequirements;
    private int reasonid, sizelist;
    private PassingReason passingReason;
    private String userChoice;
    private Boolean validate=false;
    private ArrayList<Input> addtional = new ArrayList<>();
    private ArrayList<String> spinnerList = new ArrayList<>();
    private JSONObject jsonObj = new JSONObject();
    CustomInflater customInflater;
    Attachments attachments;
    Handler handler;
    HandlerThread handlerThread = new HandlerThread("background");
    private ProgressDialog progressDialog;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    ArrayList<Attachments> postRepairAttachmentsList =new ArrayList<>(),preRepairAttachmentList=new ArrayList<>();
    @Override
    public int setViewId() {
        return R.layout.fragment_common_repair;
    }

    @Override
    public void onFragmentCreated() {
        //progress dialog features
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setMessage("Image Compressing..");
        progressDialog.setCancelable(false);
        //start the handler thread to get looper
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        passingReason = ((LookupSubActivity) getActivity()).getPassingReason();
        customInflater = new CustomInflater(getContext());
        addSpinnerData();
        repairRequirements = new RepairRequirements();
        userChoice = passingReason.getUserChoice();
        addtional.addAll(passingReason.getReasonResponse().getAdditional_fields());
        LinearLayout linearLayout = binding.ll;

        //making the custom view
        for (int i = 0; i < addtional.size(); i++) {
            if (addtional.get(i).getField_type().equals("textView")) {
                customInflater.addtext(addtional.get(i).getHint() + passingReason.getDeviceid(), linearLayout, addtional.get(i), 0 + i);
            } else if (addtional.get(i).getField_type().equals("text")) {
                customInflater.addEditText(linearLayout, addtional.get(i), 0 + i);
            } else if (addtional.get(i).getField_type().equals("spinner")) {
                customInflater.addSpinner(linearLayout, spinnerList, 0 + i, addtional.get(i));
            } else if (addtional.get(i).getField_type().equals("TruckImages")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_truck_images), 1, getString(R.string.truck_images));
            } else if (addtional.get(i).getField_type().equals("DeviceImages")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_device_images), 1, getString(R.string.device_images));
            } else if (addtional.get(i).getField_type().equals("WireConnectionImages")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_wire_connection), 2, getString(R.string.wire_connection_images));
            } else if (addtional.get(i).getField_type().equals("EarthWireConnectionImages")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_earth_wire_connection), 1, getString(R.string.earthwire_connection_images));
            } else if (addtional.get(i).getField_type().equals("DeviceFitting")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_device_fitting), 3, getString(R.string.device_fitting_images));
            } else if (addtional.get(i).getField_type().equals("Accessories")) {
                customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_accessories), 2, getString(R.string.accessories_images));
            }
        }

        // performing the click on upload button
        binding.share.setOnClickListener(v -> {
            binding.share.setEnabled(false);
            for (int i = 0; i < binding.ll.getChildCount() - 1; i++) {
                View view = binding.ll.getChildAt(i);
                validate = validator(view);
                if(!validate){
                    binding.share.setEnabled(true);
                    break;
                }
            }
            if(validate) {
                //Both the attachements are cleared because if one time the request to server fails then there should not be redundant
                //images next time so we have to clear the images list every time.
                preRepairAttachmentList.clear();
                postRepairAttachmentsList.clear();
                progressDialog.show();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Compresssiong Post Repair Images and making list of Attachment type to send to sever
                        for (int i = 0; i < binding.ll.getChildCount() - 1; i++) {
                            View view = binding.ll.getChildAt(i);
                            if (view instanceof CustomImagePicker) {
                                CustomImagePicker customImagePicker = (CustomImagePicker) view;
                                if(customImagePicker.textID.equals(getString(R.string.truck_images)))
                                    compressImages(customImagePicker,"truck_image");
                                else if(customImagePicker.textID.equals(getString(R.string.device_images)))
                                    compressImages(customImagePicker,"device_image");
                                else  if(customImagePicker.textID.equals(getString(R.string.wire_connection_images)))
                                    compressImages(customImagePicker,"wire_connection");
                                else  if(customImagePicker.textID.equals(getString(R.string.earthwire_connection_images)))
                                    compressImages(customImagePicker,"earth_wire_connection");
                                else  if(customImagePicker.textID.equals(getString(R.string.accessories_images)))
                                    compressImages(customImagePicker,"accessories");
                                else if(customImagePicker.textID.equals(getString(R.string.device_fitting_images)))
                                    compressImages(customImagePicker,"device_fitting");
                            }
                        }
                        repairRequirements.setPost_repair_images(postRepairAttachmentsList);

                        //Compression Pre Repair Images and making list of Attachement type to send to server
                        for (int i = 0; i < passingReason.getImagesPreRepair(); i++) {
                            String image = null;
                            attachments=new Attachments();
                            try {
                                image = ImageUtils.reduceBititmap(FileUtils.bitmapTouri(getContext(),Uri.parse(passingReason.getImagesList().get(i))),getContext());
                                attachments.setTitle("pre_repair");
                                attachments.setImage(image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            preRepairAttachmentList.add(attachments);
                        }
                        repairRequirements.setPre_repair_images(preRepairAttachmentList);

                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() { // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                                @Override
                                public void run() {
                                    progressDialog.setMessage("Uploading...");
                                }
                            });
                        }
                        //upload repair requirement
                        hitApi(repairRequirements);
                    }
                });
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
            Spinner spinner = (Spinner)object;
            if (spinner.getSelectedItem().toString().equals("Select option")) {
                Toast.makeText(getContext(), "Select reasons", Toast.LENGTH_LONG).show();
                return false;
            }else{
                getSpinnerData(spinner);
            }
        } else if (object instanceof CustomImagePicker) {
            CustomImagePicker customImagePicker = (CustomImagePicker) object;
            if (customImagePicker.getimagesList().size() < 1 && !customImagePicker.textID.equals(getString(R.string.accessories_images))) {
                Toast.makeText(getContext(), "Add "+customImagePicker.textID, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {}

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
        repairRequirements.setRepairData(jsonObj.toString());
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

    /**
     * This function is only used if you want to compress images and make list Attachement class type from the Custom Image Picker
     * @param imagePicker It takes the instance of CustomImagePicker class to get the images from it
     * @param title It is used to set the tiltle for those images
     */
    public void compressImages(CustomImagePicker imagePicker,String title) {
        String compressedImage;
        for (ImageUri imageUri : imagePicker.getimagesList()) {
            attachments=new Attachments();
            try {
                compressedImage = ImageUtils.reduceBititmap(FileUtils.bitmapTouri(getContext(), imageUri.getUri()), getActivity());
                attachments.setTitle(title);
                attachments.setImage(compressedImage);
                postRepairAttachmentsList.add(attachments);
                Log.e("the attachment", "the image in base 64 is title"+title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hitApi(RepairRequirements repairRequirements) {
        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                progressDialog.dismiss();
                FileUtils.deleteFiles(getContext());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
                builder.setMessage(response.body().getMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), LandingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void handleFailure(Call<RepairResponse> call, Throwable t) {
                progressDialog.dismiss();
                binding.share.setEnabled(true);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}