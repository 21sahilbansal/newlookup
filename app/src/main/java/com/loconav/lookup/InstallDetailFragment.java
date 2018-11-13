package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.AttachmentsDetails;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.model.RepairDetail;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.databinding.*;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InstallDetailFragment extends BaseFragment {
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    InstallDetailsBinding installDetailsBinding;
    ImageSetterAdapter imageSetterAdapter1,imageSetterAdapter2,imageSetterAdapter3;
    List<AttachmentsDetails> attachmentsList;
    List<String> deviceimages=new ArrayList<>(),truckimages=new ArrayList<>(),connectionimages=new ArrayList<>();
    @Override
    public int setViewId() {
        return R.layout.install_details;
    }

    @Override
    public void onFragmentCreated() {
        final int index=0;
        Bundle bundle = this.getArguments();
        int id = bundle.getInt("id");
        final CustomInflater customInflater=new CustomInflater(getContext());
        apiService.getInstallDetail(id).enqueue(new RetrofitCallback<InstallationDetails>() {
            @Override
            public void handleSuccess(Call<InstallationDetails> call, Response<InstallationDetails> response) {
                installDetailsBinding.setInstalls(response.body());
                attachmentsList=response.body().getAttachments();
                installDetailsBinding.notesdata.setText(Html.fromHtml(response.body().getNotes()));

                    for (AttachmentsDetails attachmentsDetails : attachmentsList) {
                        if(attachmentsDetails.getTag()!=null) {
                            if (attachmentsDetails.getTag().equals("truck_image"))
                                truckimages.add(attachmentsDetails.getUrls().getOriginal());
                            else if (attachmentsDetails.getTag().equals("device_image"))
                                deviceimages.add(attachmentsDetails.getUrls().getOriginal());
                            else if (attachmentsDetails.getTag().equals("wire_connection"))
                                connectionimages.add(attachmentsDetails.getUrls().getOriginal());
                        }
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayout.VERTICAL);
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                    layoutManager2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
                    layoutManager3.setOrientation(LinearLayout.VERTICAL);
                    imageSetterAdapter1 = new ImageSetterAdapter(getContext(), truckimages);
                    installDetailsBinding.truckimages.setLayoutManager(layoutManager);
                    installDetailsBinding.truckimages.setAdapter(imageSetterAdapter1);
                    imageSetterAdapter2 = new ImageSetterAdapter(getContext(), deviceimages);
                    installDetailsBinding.deviceimages.setLayoutManager(layoutManager2);
                    installDetailsBinding.deviceimages.setAdapter(imageSetterAdapter2);
                    imageSetterAdapter3 = new ImageSetterAdapter(getContext(), connectionimages);
                    installDetailsBinding.wireconnection.setLayoutManager(layoutManager3);
                    installDetailsBinding.wireconnection.setAdapter(imageSetterAdapter3);

            }
            @Override
            public void handleFailure(Call<InstallationDetails> call, Throwable t) {

            }
        });
    }

    @Override
    public void bindView(View view) {
        installDetailsBinding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
}
