package com.loconav.lookup.sharedetailsfragmants;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.CustomInflater;
import com.loconav.lookup.LandingActivity;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.customcamera.FileUtils;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.customcamera.ImageUtils;
import com.loconav.lookup.databinding.FragmentCommonRepairBinding;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.Input;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.SLUG_ID_FOR_DEVICE_REMOVED;


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
    private final ArrayList<Input> addtional = new ArrayList<>();
    private final ArrayList<String> spinnerList = new ArrayList<>();
    private final JSONObject jsonObj = new JSONObject();
    private CustomInflater customInflater;
    private Attachments attachments;
    private Handler handler;
    private final HandlerThread handlerThread = new HandlerThread("background");
    private ProgressDialog progressDialog;
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private final ArrayList<Attachments> postRepairAttachmentsList =new ArrayList<>();
    private final ArrayList<Attachments> preRepairAttachmentList=new ArrayList<>();
    @Override
    public int setViewId() {
        return R.layout.fragment_common_repair;
    }

    @Override
    public void onFragmentCreated() {
        //progress dialog features
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setMessage(getString(R.string.image_compressing));
        progressDialog.setCancelable(false);
        //start the handler thread to get looper
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        passingReason = ((LookupSubActivity) Objects.requireNonNull(getActivity())).getPassingReason();
        customInflater = new CustomInflater(getContext());
        addSpinnerData();
        repairRequirements = new RepairRequirements();
        userChoice = passingReason.getUserChoice();
        addtional.addAll(passingReason.getReasonResponse().getAdditional_fields());
        LinearLayout linearLayout = binding.linearLayout;

        //making the custom view (if the the reson is "Remove device" is selected then we only have to add device image and truck images)
        for (int i = 0; i < addtional.size(); i++) {
            switch (addtional.get(i).getField_type()) {
                case "textView":
                    customInflater.addtext(addtional.get(i).getHint() + passingReason.getDeviceid(), linearLayout, addtional.get(i), 0 + i);
                    break;
                case "text":
                    customInflater.addEditText(linearLayout, addtional.get(i), 0 + i);
                    break;
                case "spinner":
                    customInflater.addSpinner(linearLayout, spinnerList, 0 + i, addtional.get(i));
                    break;
                case "TruckImages":
                    customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_truck_images), 1, getString(R.string.truck_images));
                    break;
                case "DeviceImages":
                    customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_device_images), 1, getString(R.string.device_images));
                    break;
                case "WireConnectionImages":
                    if(!passingReason.getReasonResponse().getSlug().equals(SLUG_ID_FOR_DEVICE_REMOVED))
                        customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_wire_connection), 2, getString(R.string.wire_connection_images));
                    break;
                case "EarthWireConnectionImages":
                    if(!passingReason.getReasonResponse().getSlug().equals(SLUG_ID_FOR_DEVICE_REMOVED))
                        customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_earth_wire_connection), 1, getString(R.string.earthwire_connection_images));
                    break;
                case "DeviceFitting":
                    if(!passingReason.getReasonResponse().getSlug().equals(SLUG_ID_FOR_DEVICE_REMOVED))
                        customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_device_fitting), 3, getString(R.string.device_fitting_images));
                    break;
                case "Accessories":
                    if(!passingReason.getReasonResponse().getSlug().equals(SLUG_ID_FOR_DEVICE_REMOVED))
                        customInflater.addImagePicker(linearLayout, 0 + i, addtional.get(i), getString(R.string.upload_accessories), 2, getString(R.string.accessories_images));
                    break;
            }
        }

        // performing the click on upload button
        binding.share.setOnClickListener(v -> {

            //So that button is not clicked twice
            binding.share.setEnabled(false);

            //so validate that all feilds are filled correctly
            for (int i = 0; i < binding.linearLayout.getChildCount() - 1; i++) {
                View view = binding.linearLayout.getChildAt(i);
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
                handler.post(() -> {
                    //Compresssiong Post Repair Images and making list of Attachment type to send to sever
                    for (int i = 0; i < binding.linearLayout.getChildCount() - 1; i++) {
                        View view = binding.linearLayout.getChildAt(i);
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
                            image=ImageUtils.getbase64Image(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(passingReason.getImagesList().get(i))));
                            attachments.setTitle("pre_repair");
                            attachments.setImage(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        preRepairAttachmentList.add(attachments);
                    }
                    repairRequirements.setPre_repair_images(preRepairAttachmentList);

                    if(getActivity()!=null) {
                        // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                        getActivity().runOnUiThread(() -> progressDialog.setMessage(getString(R.string.uploading)));
                    }
                    //upload repair requirement
                    hitApi(repairRequirements);
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
                Toaster.makeToast(getString(R.string.select_reasons));
                return false;
            }else{
                getSpinnerData(spinner);
            }
        } else if (object instanceof CustomImagePicker) {
            CustomImagePicker customImagePicker = (CustomImagePicker) object;
            if (customImagePicker.getimagesList().size() < 1 && !customImagePicker.textID.equals(getString(R.string.accessories_images))) {
                Toaster.makeToast("Add "+customImagePicker.textID);
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

    private void makeJson(EditText editText) {
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

    private void getSpinnerData(Spinner spinner) {
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

    private void addSpinnerData() {
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
    private void compressImages(CustomImagePicker imagePicker, String title) {
        String compressedImage;
        for (ImageUri imageUri : imagePicker.getimagesList()) {
            attachments=new Attachments();
            try {
                compressedImage = ImageUtils.getbase64Image(MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), imageUri.getUri()));
                attachments.setTitle(title);
                attachments.setImage(compressedImage);
                postRepairAttachmentsList.add(attachments);
                Log.e("the attachment", "the image in base 64 is title"+title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hitApi(RepairRequirements repairRequirements) {
        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                progressDialog.dismiss();
                FileUtils.deleteFiles(Objects.requireNonNull(getContext()));
                final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogTheme);
                builder.setMessage(Objects.requireNonNull(response.body()).getMessage())
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            Intent intent = new Intent(getContext(), LandingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void handleFailure(Call<RepairResponse> call, Throwable t) {
                progressDialog.dismiss();
                binding.share.setEnabled(true);
                Toaster.makeToast(t.getMessage());
                Log.e("error ", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handlerThread.quit();
        binding.unbind();
    }
}