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
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.CustomImagePicker;
import com.loconav.lookup.InstallLogsFragment;
import com.loconav.lookup.databinding.FragmentNewInstallationBinding;
import com.loconav.lookup.utils.FileUtils;
import com.loconav.lookup.FragmentController;
import com.loconav.lookup.utils.ImageUtils;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.Notes;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import static com.loconav.lookup.UserPrefs.code;
import com.loconav.lookup.model.NewInstall;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by prateek on 13/11/17.
 */

public class NewInstallationFragment extends BaseTitleFragment {
    private FragmentNewInstallationBinding binding;
    private SharedPrefHelper sharedPrefHelper;
    private PassingReason passingReason;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
    HandlerThread handlerThread = new HandlerThread("background");
    List<Attachments> attachmentsList=new ArrayList<>();
    NewInstall newInstall = new NewInstall();
    Handler handler;
    String compressedImage;
    Attachments attachments;
    FragmentController fragmentController = new FragmentController();
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
        sharedPrefHelper=SharedPrefHelper.getInstance();
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        final String deviceId = passingReason.getDeviceid();
        final Client client = passingReason.getClientId();

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonFunction.validate(new EditText[]{binding.dealerName,binding.ownerName,binding.clientId,binding.location, binding.registrationNo,binding.chassisNo, binding.manufacture,
                        binding.model, binding.typeOfGoods, binding.odometerReading, binding.simNo, binding.imei, binding.deviceModel})) {
                    String message = "";
                    message += "Dealer's name: "+ binding.dealerName.getText().toString() + "\n";
                    message += "Owner Name: "+ binding.ownerName.getText().toString() + "\n";
                    message += "Client Id: "+ binding.clientId.getText().toString() + "\n";
                    message += "Location: "+ binding.location.getText().toString() + "\n";
                    message += "Registration no.: "+ binding.registrationNo.getText().toString()+ "\n";
                    message += "Chassis no: "+ binding.chassisNo.getText().toString() + "\n";
                    message += "Manufacture: "+ binding.manufacture.getText().toString() + "\n";
                    message += "Model: "+ binding.model.getText().toString() + "\n";
                    message += "Type of goods: "+ binding.typeOfGoods.getText().toString() + "\n";
                    message += "Odometer Reading: "+ binding.odometerReading.getText().toString() + "\n";
                    message += "Sim No: "+ binding.simNo.getText().toString() + "\n";
                    message += "IMEI: "+ binding.imei.getText().toString() + "\n";
                    message += "Device Model: "+ binding.deviceModel.getText().toString()+"\n";
                    message += "SOS: " + getFeatures(binding.cbSos) +  "\n";
                    message += "Trip Button: " + getFeatures(binding.cbTrip) +  "\n";
                    message += "Immobilizer: " + getFeatures(binding.cbImm) +  "\n";
                    message += "USER ID: " + SharedPrefHelper.getInstance().getStringData(code) +  "\n";
                    message += "Sent By Device Checker:"+ " " + System.currentTimeMillis() ;
                    String url = "http://www.loconav.com/?type=new_vehicle&model="+
                            binding.model.getText().toString()+"&manufacturer="+
                            binding.manufacture.getText().toString()+"&deviceid="+ deviceId;
                    sharedPrefHelper.setStringData("message", message);
                    sharedPrefHelper.setStringData("upload_url", url);
                    // these are functions to check if the imagepicker has atleast one image
                    boolean isDevice= checkImages(binding.DeviceImage,"device");
                    boolean isTruck=checkImages(binding.TruckImages,"truck");
                    boolean isWiredConnection=checkImages(binding.WireConnection,"connection");
                    if(isDevice && isTruck && isWiredConnection) {
                        progressDialog.setMessage("Image Compressing..");
                       progressDialog.show();
                       handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    attachmentsList.clear();
                                    compressImages(binding.TruckImages,"truck_image");
                                    compressImages(binding.DeviceImage,"device_image");
                                    compressImages(binding.WireConnection,"wire_connection");
                                    Notes notes=new Notes();
                                    notes.setDealer_name(binding.dealerName.getText().toString());
                                    notes.setOwner_name(binding.ownerName.getText().toString());
                                    newInstall.setClient_id(binding.clientId.getText().toString());
                                    notes.setLocation(binding.location.getText().toString());
                                    newInstall.setTruck_number(binding.registrationNo.getText().toString());
                                    notes.setChassis_number(binding.chassisNo.getText().toString());
                                    notes.setManufacturer(binding.manufacture.getText().toString());
                                    notes.setModel(binding.model.getText().toString());
                                    notes.setTypeOfGoods(binding.typeOfGoods.getText().toString());
                                    notes.setOdometer_reading(binding.odometerReading.getText().toString());
                                    notes.setSim_number(binding.simNo.getText().toString());
                                    newInstall.setImei_number(binding.imei.getText().toString());
                                    notes.setDeviceModel(binding.deviceModel.getText().toString());
                                    notes.setSos(getFeatures(binding.cbSos));
                                    newInstall.setTransporter_id(Long.parseLong(binding.transporterId.getText().toString()));
                                    notes.setImmobilizer(getFeatures(binding.cbImm));
                                    notes.setTrip_button(getFeatures(binding.cbTrip));
                                    notes.setClientid(binding.clientId.getText().toString());
                                    notes.setInstalldate(String.valueOf((System.currentTimeMillis()/1000)));
                                    newInstall.setNotes(notes);
                                    newInstall.setImmobilizer(getFeatures(binding.cbImm));
                                    newInstall.setSOS(getFeatures(binding.cbSos));
                                    newInstall.setTruck_number(getFeatures(binding.cbTrip));
                                    newInstall.setAttachments(attachmentsList);
                                    getActivity().runOnUiThread(new Runnable() { // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                                        @Override
                                        public void run() {
                                            progressDialog.setMessage("Uploading...");
                                        }
                                    });
                                    upload(newInstall);
                            }
                        });
                    }
                }
            }
        });
        CommonFunction.setEditText(binding.imei, deviceId);
        CommonFunction.setEditText(binding.ownerName, client.getName());
        CommonFunction.setEditText(binding.clientId, client.getClientId());
        CommonFunction.setEditText(binding.transporterId, client.getTransporter_id());
    }
    public boolean checkImages(CustomImagePicker imagePicker,String title)
    {
        if(!imagePicker.getimagesList().isEmpty()) {
            return true;
        }
        else
        {
            Toast.makeText(getContext(), "Upload "+title+" Images", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void compressImages(CustomImagePicker imagePicker,String title)
    {

        for (ImageUri imageUri : imagePicker.getimagesList()) {
            attachments=new Attachments();
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
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });

    }
    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }


    private String getFeatures(CheckBox checkBox) {
        if(checkBox.isChecked())
            return "YES";
        else
            return "NO";
    }

    @Override
    public String getTitle() {
        return "New Installations";
    }
}
