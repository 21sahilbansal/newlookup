package com.loconav.lookup;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.CustomImagePicker;
import com.loconav.lookup.customcamera.ImagePickerEvent;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.customcamera.ImageUtils;
import com.loconav.lookup.databinding.FragmentFastTagPhotosBinding;
import com.loconav.lookup.model.AttachmentList;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class FastTagPhotosFragment extends BaseFragment {
    private FragmentFastTagPhotosBinding binding;
    private String truckNo, serialNo;
    private Integer installationId;
    private final HandlerThread handlerThread = new HandlerThread("background");
    private Handler handler;
    private boolean validate;
    private ProgressDialog progressDialog;
    private String compressedImage;
    private Uri uri;
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
        AttachmentList attachmentList = new AttachmentList();
        attachmentList.setAttachmentsArrayList(attachmentsList);
        apiService.addFastTagPhotos(installationId,attachmentList).enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            protected void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void handleFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toaster.makeToast(t.getMessage());
            }
        });
    }

    private void compressImages(CustomImagePicker imagePicker, String title) {
            Attachments attachments = new Attachments();
            try {
                compressedImage = ImageUtils.getbase64Image(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imagePicker.getimagesList().get(0).getUri()),imagePicker.getimagesList().get(0));
                attachments.setTitle(title);
                attachments.setImage(compressedImage);
                attachmentsList.add(attachments);
                Log.e("the attachment", "the image in base 64 is title" + title);
            } catch (Exception e) {
                e.printStackTrace();
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
        truckNo = receivedBundle.getString("Truck_No");
        serialNo = receivedBundle.getString("FastTag_Serial_No");
        installationId = receivedBundle.getInt("Installation_Id");
        if(receivedBundle.containsKey("Image_URI")){
        uri = Uri.parse(receivedBundle.getString("Image_URI"));
        String stringId = "Before Pasting Fastag";
        ImageUri imageUri = new ImageUri();
         imageUri.setUri(uri);
            List<ImageUri> imageUriList = new ArrayList<>();
         imageUriList.add(imageUri);
         EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId,imageUriList));


    }
    }

    private void setContent() {
        binding.serialNumberEt.setText(serialNo);
        binding.truckNumberEt.setText(truckNo);

    }

    @Override
    protected void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    protected void getComponentFactory() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();

    }
}
