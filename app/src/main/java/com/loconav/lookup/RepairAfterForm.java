package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.RepairAfterFormBinding;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairAfterForm extends BaseTitleFragment {
    RepairRequirements repairRequirements;
    private ProgressDialog progressDialog,progressDialog2;
    PassingReason passingReason;
    Boolean submitted = false;
    private RepairAfterFormBinding binding;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    int fail=0;
    @Override
    public int setViewId() {
        return R.layout.repair_after_form;
    }
    Date currentTime2,currentTime1;
    Long tsLong1,tsLong2 ;

    @Override
    public void onFragmentCreated() {
        repairRequirements = (RepairRequirements) getArguments().getSerializable("req");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Image Compressing..");
                progressDialog.setCancelable(false);
            }
        });
        binding.proceedRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.Vehicleimage.getimagesList().size() >= 1) {
                    if (Utility.isNetworkAvailable(getActivity())) {
                        binding.proceedRep.setVisibility(View.GONE);
                        progressDialog.show();
                        if (!submitted) {
                            if(fail==0)
                            {
                                submitted = true;
                                HandlerThread handlerThread = new HandlerThread("background");
                                handlerThread.start();
                                tsLong1 = System.currentTimeMillis() / 1000;
                                new Handler(handlerThread.getLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<String> imagesList1 = new ArrayList<>();
                                        imagesList1.addAll(passingReason.getImagesList());
                                        for (ImageUri imageUri : (binding.Vehicleimage.getimagesList())) {
                                            imagesList1.add(imageUri.getUri().toString());
                                        }
                                        passingReason.setImagesPostRepair(binding.Vehicleimage.getimagesList().size());
                                        passingReason.imagesList.clear();
                                        passingReason.setImagesList(imagesList1);
                                        ((LookupSubActivity) getActivity()).setPassingReason(passingReason);
                                        Log.e("size", "run: " + passingReason.getImagesList().size());
                                        ArrayList<String> al = new ArrayList<>();
                                        currentTime2 = Calendar.getInstance().getTime();
                                        for (int i = 0; i < passingReason.getImagesPreRepair(); i++) {
                                            String str2 = null;
                                            try {
                                                str2 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            al.add(str2);
                                        }
                                        repairRequirements.setPre_repair_images(al);
                                        ArrayList<String> al1 = new ArrayList<>();
                                        for (int i = passingReason.getImagesPreRepair(); i < passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i++) {
                                            String str5 = null;
                                            try {
                                                str5 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            al1.add(str5);
                                        }
                                        for (int i = passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i < passingReason.getImagesList().size(); i++) {
                                            String str3 = null;
                                            try {
                                                str3 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            al1.add(str3);
                                        }
                                        tsLong2 = System.currentTimeMillis() / 1000;
                                        Log.e("Sourav", String.valueOf((tsLong2 - tsLong1)));
                                        repairRequirements.setPost_repair_images(al1);
                                        getActivity().runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                progressDialog.setMessage("Uploading..");
                                            }
                                        });
                                        hitApi(repairRequirements);
                                    }
                                });
                            }
                            else
                            {
                                HandlerThread handlerThread = new HandlerThread("background");
                                handlerThread.start();
                                new Handler(handlerThread.getLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                progressDialog.setMessage("Uploading..");
                                            }
                                        });
                                        hitApi(repairRequirements);
                                    }
                                });
                            }
                        } else if (submitted) {
                            hitApi(repairRequirements);

                        }
                    } else
                        Toast.makeText(getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Add Vehicle Image", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    public void bindView(View view) {
        binding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
    private void initProgressDialog() {

    }


    public void hitApi(RepairRequirements repairRequirements) {
        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                progressDialog.dismiss();
                File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
                ;
                builder.setMessage(response.body().getMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), LookUpEntry.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void handleFailure(Call<RepairResponse> call, Throwable t) {
                binding.proceedRep.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                fail=1;
                Log.e("error ", t.getMessage());
            }
        });
    }

    /**
     *
     * @param imageUri :
     * @return :
     */
    public Bitmap bitmapTouri(Uri imageUri) {
        Bitmap bm = null;
        try {
            bm = (MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    @Override
    public String getTitle() {
        return "Upload Image";
    }
}

