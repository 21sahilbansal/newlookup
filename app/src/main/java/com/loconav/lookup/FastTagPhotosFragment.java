package com.loconav.lookup;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.customcamera.ImageUtils;
import com.loconav.lookup.databinding.FragmentFastTagPhotosBinding;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class FastTagPhotosFragment extends BaseFragment {
    private FragmentFastTagPhotosBinding binding;
    private String Truck_No, Serial_No;
    private final HandlerThread handlerThread = new HandlerThread("background");
    private Handler handler;
    private boolean validate;
    private ProgressDialog progressDialog;
    private String compressedImage;
    private final List<Attachments> attachmentsList = new ArrayList<>();
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected int setViewId() {
        return R.layout.fragment_fast_tag_photos;
    }

    @Override
    protected void onFragmentCreated() {
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setCancelable(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upload FastTag Photos");
        getIntentData();
        setContent();
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        binding.ftPhotoUpload.setOnClickListener(view -> {
            validate = validator();

            if (validate) {
                progressDialog.setMessage(getString(R.string.image_compressing));
                progressDialog.show();
                handler.post(() -> {
                    attachmentsList.clear();
                    compressImages(binding.ftBeforeInstall, getString(R.string.before_pasting_fastag));
                    compressImages(binding.ftInstalled, getString(R.string.after_pasting_fastag));
                    compressImages(binding.sideView1, getString(R.string.side_view));
                    compressImages(binding.frontView1, getString(R.string.front_view));
                    if (getActivity() != null) {
                        // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()
                        getActivity().runOnUiThread(() -> progressDialog.setMessage(getString(R.string.uploading)));
                    }
                    upload();

                });
            }
        });
    }

    private void upload() {
        apiService.addFastTagPhotos(Truck_No,attachmentsList.get(0)).enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            protected void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

            }

            @Override
            protected void handleFailure(Call<ResponseBody> call, Throwable t) {
                Toaster.makeToast(t.getMessage());
            }
        });
        progressDialog.dismiss();
    }

    private void compressImages(CustomImagePicker imagePicker, String title) {
        for (ImageUri imageUri : imagePicker.getimagesList()) {
            Attachments attachments = new Attachments();
            try {
                compressedImage = ImageUtils.getbase64Image(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri.getUri()));
                attachments.setTitle(title);
                attachments.setImage(compressedImage);
                attachmentsList.add(attachments);
                Log.e("the attachment", "the image in base 64 is title" + title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validator() {
        if (binding.ftBeforeInstall.getimagesList().size() <= 1 & binding.ftInstalled.getimagesList().size() == 1 & binding.frontView1.getimagesList().size() == 1 & binding.sideView1.getimagesList().size() == 1) {
            return true;
        } else {
            Toast.makeText(getContext(), "Add Mandatory Photos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void getIntentData() {
        Bundle receivedBundle;
        receivedBundle = getArguments();
        Truck_No = receivedBundle.getString("Truck_No");
        Serial_No = receivedBundle.getString("FastTag_Serial_no");
    }

    private void setContent() {
        binding.serialNumberEt.setText(Serial_No);
        binding.truckNumberEt.setText(Truck_No);
    }

    @Override
    protected void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    protected void getComponentFactory() {
    }
}
