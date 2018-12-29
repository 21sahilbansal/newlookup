package com.loconav.lookup.sharedetailsfragmants;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.InstallLogsFragment;
import com.loconav.lookup.databinding.FragmentNewInstallationBinding;
import com.loconav.lookup.customcamera.FileUtils;
import com.loconav.lookup.FragmentController;
import com.loconav.lookup.customcamera.ImageUtils;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.model.Notes;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import com.loconav.lookup.model.NewInstall;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallationFragment extends BaseTitleFragment {
    private FragmentNewInstallationBinding binding;
    private PassingReason passingReason;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
    HandlerThread handlerThread = new HandlerThread("background");
    List<Attachments> attachmentsList=new ArrayList<>();
    NewInstall newInstall = new NewInstall();
    Handler handler;
    String compressedImage;
    FragmentController fragmentController = new FragmentController();
    int accessoriesCheckCount=0;
    @Override
    public int setViewId() {
        return R.layout.fragment_new_installation;
    }
    @Override
    public void onFragmentCreated() {
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setCancelable(false);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        final String deviceId = passingReason.getDeviceid();
        final Client client = passingReason.getClientId();
        binding.cbImm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                accessoriesChecked(isChecked);
            }
        });
        binding.cbSos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                accessoriesChecked(isChecked);
            }
        });
        binding.cbTrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                accessoriesChecked(isChecked);
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.validate(new EditText[]{binding.dealerName,binding.ownerName,binding.clientId,binding.location, binding.registrationNo,binding.chassisNo, binding.manufacture,
                        binding.model, binding.typeOfGoods, binding.odometerReading, binding.simNo, binding.imei, binding.deviceModel})) {
                    // these are functions to check if the imagepicker has atleast one image
                    boolean isDevice= checkImages(binding.DeviceImage,"device");
                    boolean isTruck=checkImages(binding.TruckImages,"truck");
                    boolean isWiredConnection=checkImages(binding.WireConnection,"wire connection");
                    boolean isEarthWiredConnection=checkImages(binding.WireConnection,"earth wire connection");
                    boolean isFitted=checkImages(binding.DeviceFitting,"Fitting");
                    boolean isAccessories=true;
                    if(getFeatures(binding.cbSos).equals("YES") || getFeatures(binding.cbTrip).equals("YES") || getFeatures(binding.cbImm).equals("YES")){
                        isAccessories=checkImages(binding.Accessories,"Accessories");
                    }
                    if(isDevice && isTruck && isWiredConnection && isFitted && isEarthWiredConnection && isAccessories) {
                        progressDialog.setMessage("Image Compressing..");
                       progressDialog.show();
                       handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    attachmentsList.clear();
                                    compressImages(binding.TruckImages,"truck_image");
                                    compressImages(binding.DeviceImage,"device_image");
                                    compressImages(binding.WireConnection,"wire_connection");
                                    compressImages(binding.DeviceFitting,"device_fitting");
                                    compressImages(binding.Accessories,"accessories");
                                    compressImages(binding.EarthwireConnection,"earth_wire_connection");
                                    //It is a type of raw data we can send anything in it(used to keep the record that the thing we are sending in newIntsall is correct)
                                    Notes notes=new Notes();
                                    notes.setDealer_name(binding.dealerName.getText().toString());
                                    notes.setOwner_name(binding.ownerName.getText().toString());
                                    notes.setLocation(binding.location.getText().toString());
                                    notes.setChassis_number(binding.chassisNo.getText().toString());
                                    notes.setManufacturer(binding.manufacture.getText().toString());
                                    notes.setModel(binding.model.getText().toString());
                                    notes.setTypeOfGoods(binding.typeOfGoods.getText().toString());
                                    notes.setOdometer_reading(binding.odometerReading.getText().toString());
                                    notes.setSim_number(binding.simNo.getText().toString());
                                    notes.setDeviceModel(binding.deviceModel.getText().toString());
                                    notes.setSos(getFeatures(binding.cbSos));
                                    notes.setImmobilizer(getFeatures(binding.cbImm));
                                    notes.setTrip_button(getFeatures(binding.cbTrip));
                                    notes.setClientid(binding.clientId.getText().toString());
                                    notes.setInstalldate(String.valueOf((System.currentTimeMillis()/1000)));
                                    newInstall.setClient_id(binding.clientId.getText().toString());
                                    newInstall.setTruck_number(binding.registrationNo.getText().toString());
                                    newInstall.setImei_number(binding.imei.getText().toString());
                                    newInstall.setTransporter_id(Long.parseLong(binding.transporterId.getText().toString()));
                                    newInstall.setNotes(notes);
                                    newInstall.setImmobilizer(getFeatures(binding.cbImm));
                                    newInstall.setSOS(getFeatures(binding.cbSos));
                                    newInstall.setTripbutton(getFeatures(binding.cbSos));
                                    newInstall.setAttachments(attachmentsList);
                                    if(getActivity()!=null) {
                                        getActivity().runOnUiThread(new Runnable() { // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                                            @Override
                                            public void run() {
                                                progressDialog.setMessage("Uploading...");
                                            }
                                        });
                                    }
                                    upload(newInstall);
                            }
                        });
                    }
                }
            }
        });
        if(client!=null) {
            CommonFunction.setEditText(binding.imei, deviceId);
            CommonFunction.setEditText(binding.ownerName, client.getName());
            CommonFunction.setEditText(binding.clientId, client.getClientId());
            CommonFunction.setEditText(binding.transporterId, client.getTransporter_id());
        }
    }
    //check if all the custom image pickers has images
    public boolean checkImages(CustomImagePicker imagePicker,String title)
    {
        if(!imagePicker.getimagesList().isEmpty())
        {
            return true;
        }
        else
        {
            Toast.makeText(getContext(), "Upload "+title+" Images", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    /**
     * This function is only used if you want to compress images and make list Attachement class type from the Custom Image Picker
     * @param imagePicker It takes the instance of CustomImagePicker class to get the images from it
     * @param title It is used to set the tiltle for those images
     */
    public void compressImages(CustomImagePicker imagePicker,String title)
    {
        for (ImageUri imageUri : imagePicker.getimagesList()) {
            Attachments attachments=new Attachments();
            try {
                compressedImage = ImageUtils.reduceBititmap(FileUtils.bitmapTouri(getContext(), imageUri.getUri()), getActivity());
                attachments.setTitle(title);
                attachments.setImage(compressedImage);
                attachmentsList.add(attachments);
                Log.e("the attachment", "the image in base 64 is title"+title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void upload(NewInstall newInstall) {
        Log.e("odometerreading","theimage"+"thetitle"+newInstall.getAttachments().get(0).getImage());
           apiService.addNewInstall(newInstall).enqueue(new RetrofitCallback<ResponseBody>() {
               @Override
               public void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                   progressDialog.dismiss();
                   FileUtils.deleteFiles(getContext());
                   final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
                   builder.setMessage("New Installation created successfully")
                           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   fragmentController.deleteFragmentStack(getActivity().getSupportFragmentManager());
                                   Bundle bundle = new Bundle();
                                   bundle.putInt("layout", R.id.frameLayout);
                                   InstallLogsFragment installDetailFragment = new InstallLogsFragment();
                                   installDetailFragment.setArguments(bundle);
                                   fragmentController.loadFragment(installDetailFragment, getActivity().getSupportFragmentManager(), R.id.frameLayout, false);
                               }
                           })
                           .setCancelable(false)
                           .show();

               }
               @Override
               public void handleFailure(Call<ResponseBody> call, Throwable t) {
                   progressDialog.dismiss();
                   if(getContext()!=null)
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
    }

    private String getFeatures(CheckBox checkBox) {
        if(checkBox.isChecked())
            return "YES";
        else
            return "NO";
    }

    public void accessoriesChecked(boolean isChecked)
    {
        if(isChecked) {
            accessoriesCheckCount++;
            binding.Accessories.setVisibility(View.VISIBLE);
        }
        else {
            accessoriesCheckCount--;
            if(accessoriesCheckCount==0)
                binding.Accessories.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public String getTitle() {
        return "New Installations";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}
