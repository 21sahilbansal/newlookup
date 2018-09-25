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

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairAfterForm extends BaseTitleFragment {
    RepairRequirements repairRequirements;
    private ProgressDialog progressDialog;
    PassingReason passingReason;
    Boolean submitted = false;
    private RepairAfterFormBinding binding;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    boolean isImageUploaded;
    @Override
    public int setViewId() {
        return R.layout.repair_after_form;
    }

    HandlerThread handlerThread;
    @Override
    public void onFragmentCreated() {
        repairRequirements = (RepairRequirements) getArguments().getSerializable("req");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
                progressDialog = new ProgressDialog(getActivity());//we are on ui thread
                progressDialog.setMessage("Image Compressing..");
                progressDialog.setCancelable(false);
        binding.proceedRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.Vehicleimage.getimagesList().size() >= 1) {
                    if (Utility.isNetworkAvailable(getActivity())) {
                        binding.proceedRep.setVisibility(View.GONE);
                        progressDialog.show();
                        if (!submitted) {
                            if(!isImageUploaded)
                            {
                                submitted = true;
                                handlerThread = new HandlerThread("background");
                                handlerThread.start();
                                new Handler(handlerThread.getLooper()).post(new Runnable() {//we create a new thread for compression and uploading images
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
                                        for (int i = 0; i < passingReason.getImagesPreRepair(); i++) {
                                            String str2 = null;
                                            try {
                                                str2 = Utility.reduceBititmap(FileUtility.bitmapTouri(getContext(),Uri.parse(passingReason.getImagesList().get(i))),getActivity());
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
                                                str5 = Utility.reduceBititmap(FileUtility.bitmapTouri(getContext(),Uri.parse(passingReason.getImagesList().get(i))),getActivity());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            al1.add(str5);
                                        }
                                        for (int i = passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i < passingReason.getImagesList().size(); i++) {
                                            String str3 = null;
                                            try {
                                                str3 = Utility.reduceBititmap(FileUtility.bitmapTouri(getContext(),Uri.parse(passingReason.getImagesList().get(i))),getActivity());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            al1.add(str3);
                                        }
                                        repairRequirements.setPost_repair_images(al1);
                                        getActivity().runOnUiThread(new Runnable() { // now we are not on ui thread so we have to show progress on ui thread so we call method runOnUiThread()

                                            @Override
                                            public void run() {
                                                progressDialog.setMessage("Uploading...");
                                            }
                                        });
                                        hitApi(repairRequirements);
                                    }
                                });
                            }
                            else
                            {
                                progressDialog.setMessage("Uploading...");//we are on ui thread
                                        hitApi(repairRequirements);// now we donot need to make changes on ui thread so we donot need to create a new thread;
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
                    for (File f:dir.listFiles())
                    {
                            f.delete();
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
                isImageUploaded=true;
                Log.e("error ", t.getMessage());
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
        binding.unbind();
    }

    @Override
    public String getTitle() {
        return "Upload Image";
    }
}

