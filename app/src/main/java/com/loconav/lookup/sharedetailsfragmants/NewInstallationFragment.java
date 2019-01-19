package com.loconav.lookup.sharedetailsfragmants;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.BaseNavigationActivity;
import com.loconav.lookup.BaseTitleFragment;
import com.loconav.lookup.CommonFunction;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.databinding.FragmentNewInstallationBinding;
import com.loconav.lookup.customcamera.FileUtils;
import com.loconav.lookup.customcamera.ImageUtils;
import com.loconav.lookup.LookupSubActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.model.InstallationDetails;
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

import static com.loconav.lookup.Constants.FRAGMENT_NAME;

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
    int accessoriesCheckCount=0;
    boolean validate;
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

        //These are 3 checkboxes of feature (Immobilizer,SOS,and TripButton) and what to do if they are checked or unchecked
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

                for (int i = 0; i < binding.linear.getChildCount() - 1; i++) {
                    View view = binding.linear.getChildAt(i);
                    validate = validator(view);
                    if(!validate){
                        binding.share.setEnabled(true);
                        break;
                    }
                }

                if(validate)
                {
                    progressDialog.setMessage(getString(R.string.image_compressing));
                    progressDialog.show();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            attachmentsList.clear();
                            compressImages(binding.TruckImages,getString(R.string.truck_image_tag));
                            compressImages(binding.DeviceImage,getString(R.string.device_image_tag));
                            compressImages(binding.WireConnection,getString(R.string.wire_connection_tag));
                            compressImages(binding.DeviceFitting,getString(R.string.device_fitting_tag));
                            compressImages(binding.Accessories,getString(R.string.accessories_tag));
                            compressImages(binding.EarthwireConnection,getString(R.string.earth_wire_connection_tag));
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
                            notes.setInstalldate(String.valueOf((System.currentTimeMillis())));
                            newInstall.setClient_id(binding.clientId.getText().toString());
                            newInstall.setTruck_number(binding.registrationNo.getText().toString());
                            newInstall.setImei_number(binding.imei.getText().toString());
                            newInstall.setTransporter_id(Long.parseLong(binding.transporterId.getText().toString()));
                            newInstall.setNotes(notes);
                            newInstall.setImmobilizer(getFeatures(binding.cbImm));
                            newInstall.setSOS(getFeatures(binding.cbSos));
                            newInstall.setTripbutton(getFeatures(binding.cbSos));
                            newInstall.setAttachments(attachmentsList);
                            //Null check on activity that if activity is null or not
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() { // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage(getString(R.string.uploading));
                                    }
                                });
                            }
                            upload(newInstall);
                        }
                    });
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

    public boolean validator(View object)
    {
        if (object instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) object;
            EditText editText = textInputLayout.getEditText();
            if(!CommonFunction.validateEdit(editText))
                return false;
        }else if (object instanceof CustomImagePicker) {
            CustomImagePicker customImagePicker = (CustomImagePicker) object;
            //This is to check if custom image picker is accessories and any of the features is checked then we have to check for the images and not if nothing is checked
            if(customImagePicker.textID.equals("accessories") && (getFeatures(binding.cbSos).equals("YES") || getFeatures(binding.cbTrip).equals("YES") || getFeatures(binding.cbImm).equals("YES")))
            {
                if (customImagePicker.getimagesList().size() < 1 ) {
                    Toaster.makeToast("Add "+customImagePicker.textID);
                    return false;
                }
            }
            else if (customImagePicker.getimagesList().size() < 1 && !customImagePicker.textID.equals("accessories") ) {
                Toaster.makeToast("Add "+customImagePicker.textID);
                return false;
            }
        }
        return true;
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
                compressedImage = ImageUtils.getbase64Image(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri.getUri()));
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
                builder.setMessage(R.string.installation_creation_successfull)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.dismiss();
                                //These are the details we have to pass to share the to share details fragment we cannot pass the whole newInstall object as it contains photos
                                InstallationDetails details=new InstallationDetails();
                                details.setTruck_number(newInstall.getTruck_number());
                                details.setInstallable_serial_number(newInstall.getImei_number());
                                details.setChassis(newInstall.getNotes().getChassis_number());
                                details.setInstallation_date(System.currentTimeMillis());
                                details.setDevice_phone_number(newInstall.getNotes().getSim_number());
                                Intent intent =new Intent(getActivity(), BaseNavigationActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putParcelable(getString(R.string.installation_details),details);
                                bundle.putString(FRAGMENT_NAME,getString(R.string.screenshot_fragment));
                                intent.putExtras(bundle);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
            @Override
            public void handleFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if(getContext()!=null)
                    Toaster.makeToast(t.getMessage());
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
