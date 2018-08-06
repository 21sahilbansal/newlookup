package com.loconav.lookup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairAfterForm extends BaseFragment {
    @BindView(R.id.Vehicleimage) CustomImagePicker vehicleimage;
    @BindView(R.id.proceedRep) Button proceedRep;
    @BindView(R.id.pbHeaderProgress) ProgressBar pbHeaderProgress;
    RepairRequirements repairRequirements;
    PassingReason passingReason;
    String str;
    private ApiInterface apiService = StagingApiClient.getClient().create(ApiInterface.class);

    @Override
    public int setViewId() {
        return R.layout.repair_after_form;
    }

    @Override
    public void onFragmentCreated() {
        repairRequirements = (RepairRequirements) getArguments().getSerializable("req");
        passingReason = (PassingReason) getArguments().getSerializable("req2");
        proceedRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vehicleimage.GetimagesList().size() >= 1) {
                    proceedRep.setVisibility(View.GONE);
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                    HandlerThread handlerThread = new HandlerThread("background");
                    handlerThread.start();
                    new Handler(handlerThread.getLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> imagesList1 = new ArrayList<>();
                            imagesList1.addAll(passingReason.getImagesList());
                            for (ImageUri imageUri : (vehicleimage.GetimagesList())) {
                                imagesList1.add(imageUri.getUri().toString());
                            }
                            passingReason.setImagesPostRepair(vehicleimage.GetimagesList().size());
                            passingReason.imagesList.clear();
                            passingReason.setImagesList(imagesList1);
                            ArrayList<String> al = new ArrayList<>();
                            for (int i = 0; i < passingReason.getImagesPreRepair(); i++) {
                                String str2 = ((EnterDetails) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                al.add(str2);
                            }
                            repairRequirements.setPre_repair_images(al);
                            ArrayList<String> al1 = new ArrayList<>();
                            for (int i = passingReason.getImagesPreRepair(); i < passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i++) {
                                String str5 = ((EnterDetails) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                al1.add(str5);
                            }
                            for (int i = passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i < passingReason.getImagesList().size(); i++) {
                                String str3 = ((EnterDetails) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                al1.add(str3);
                            }
                            repairRequirements.setPost_repair_images(al1);
                            hitApi(repairRequirements);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Add Vehicle Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }

    public void hitApi(RepairRequirements repairRequirements) {

        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                proceedRep.setVisibility(View.VISIBLE);
                pbHeaderProgress.setVisibility(View.GONE);
                final AlertDialog.Builder builder= new AlertDialog.Builder(getContext(),R.style.DialogTheme);;
                builder.setMessage(response.body().getMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(getContext(),LookUpEntry.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void handleFailure(Call<RepairResponse> call, Throwable t) {
                proceedRep.setVisibility(View.VISIBLE);
                pbHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getImagePickingEvents(GalleryEvents event) {
//        if (event.getMessage().equals(GalleryEvents.IMAGE_COMPRESSED)) {
//            // Toast.makeText(getContext(), "Image Compressed", Toast.LENGTH_LONG).show();
//            Log.e("after toast", "" + (String) event.getObject().toString());
//            File image = new File((String) event.getObject(), "");
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
//            str = EncodingDecoding.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 0);
//
//            Log.e("print ", "getImagePickingEvents: " + str.length());
//
//        }
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

    public Bitmap bitmapTouri(Uri imageUri) {
        Bitmap bm = null;
        try {
            bm = (MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri));
            Log.e("bitmap is", "" + bm.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
//                    String pathIS=getRealPathFromURI(getImageUri(getContext(),bitmapTouri(uri)));
//                    Log.e("path is",""+pathIS);
//                    UtilCompress utilCompress=new UtilCompress(getContext());
//                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
//                        utilCompress.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pathIS);
//                    else
//                        utilCompress.execute(pathIS);
//String str=((EnterDetails) getActivity()).reduceBititmap( bitmapTouri(uri));

