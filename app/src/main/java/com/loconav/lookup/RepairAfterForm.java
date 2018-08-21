package com.loconav.lookup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairAfterForm extends BaseTitleFragment {
    @BindView(R.id.Vehicleimage) CustomImagePicker vehicleimage;
    @BindView(R.id.proceedRep) Button proceedRep;
    RepairRequirements repairRequirements;
    private ProgressDialog progressDialog;
    PassingReason passingReason;
    Boolean submitted=false;
    String str;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public int setViewId() {
        return R.layout.repair_after_form;
    }

    @Override
    public void onFragmentCreated() {
        repairRequirements = (RepairRequirements) getArguments().getSerializable("req");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        initProgressDialog();
        proceedRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vehicleimage.GetimagesList().size() >= 1) {
                        proceedRep.setVisibility(View.GONE);
                        progressDialog.show();
                    if(!submitted) {
                        submitted=true;
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
                                ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                                Log.e("size", "run: " + passingReason.getImagesList().size());
                                ArrayList<String> al = new ArrayList<>();
                                for (int i = 0; i < passingReason.getImagesPreRepair(); i++) {
                                    String str2 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                    al.add(str2);
                                }
                                repairRequirements.setPre_repair_images(al);
                                ArrayList<String> al1 = new ArrayList<>();
                                for (int i = passingReason.getImagesPreRepair(); i < passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i++) {
                                    String str5 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                    al1.add(str5);
                                }
                                for (int i = passingReason.getImagesPreRepair() + passingReason.getImagesInRepair(); i < passingReason.getImagesList().size(); i++) {
                                    String str3 = ((LookupSubActivity) getActivity()).reduceBititmap(bitmapTouri(Uri.parse(passingReason.getImagesList().get(i))));
                                    al1.add(str3);
                                }
                                repairRequirements.setPost_repair_images(al1);
                                hitApi(repairRequirements);
                            }
                        });
                    }else if(submitted){
                            hitApi(repairRequirements);
                        }
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
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    public void hitApi(RepairRequirements repairRequirements) {

        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                progressDialog.dismiss();
                final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity(),R.style.DialogTheme);;
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
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

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

    @Override
    public String getTitle() {
        return "Upload Image";
    }
}

