package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.*;
import com.loconav.lookup.databinding.FragmentRepairDetailsBinding;
import com.loconav.lookup.model.RepairDetail;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Response;

public class RepairDetailFragment extends BaseFragment {
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    FragmentRepairDetailsBinding repairDetailsBinding;
    RepairDetail repairs;
    ImageSetterAdapter imageSetterAdapter;
    @Override
    public int setViewId() {
        return R.layout.fragment_repair_details;
    }

    @Override
    public void onFragmentCreated() {
        final int index=0;
        Bundle bundle = this.getArguments();
        final CustomInflater customInflater=new CustomInflater(getContext());
        int id = bundle.getInt("id");
        apiService.getRepairDetail(id).enqueue(new RetrofitCallback<RepairDetail>() {
            @Override
            public void handleSuccess(Call<RepairDetail> call, Response<RepairDetail> response) {
                repairs=response.body();
                repairDetailsBinding.setRepairs(repairs);
                 for (String  r : repairs.getRepairData().keySet())
                    {
                        Input input =new Input(r,r,r,r);
                        TextView labels = customInflater.addtext(r,repairDetailsBinding.repairdata,input,index);
                        labels.setTextSize(11);
                        labels.setTextColor(getResources().getColor(R.color.black_38));
                        Input output =new Input(repairs.getRepairData().get(r),repairs.getRepairData().get(r),repairs.getRepairData().get(r),repairs.getRepairData().get(r));
                        TextView  result= customInflater.addtext(repairs.getRepairData().get(r),repairDetailsBinding.repairdata,output,index);
                        result.setTextSize(14);
                        result.setTextColor(getResources().getColor(R.color.black));
                    }
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayout.VERTICAL);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                layoutManager2.setOrientation(LinearLayout.VERTICAL);
                imageSetterAdapter = new ImageSetterAdapter(repairs.getPreRepairImages(), new Callback() {
                    @Override
                    public void onEventDone(Object object) {

                    }
                });
                repairDetailsBinding.prerepairimages.setLayoutManager(layoutManager);
                repairDetailsBinding.prerepairimages.setAdapter(imageSetterAdapter);
                imageSetterAdapter = new ImageSetterAdapter(repairs.getPostRepairImages(), new Callback() {
                    @Override
                    public void onEventDone(Object object) {

                    }
                });
                repairDetailsBinding.postrepairimages.setLayoutManager(layoutManager2);
                repairDetailsBinding.postrepairimages.setAdapter(imageSetterAdapter);
            }
            @Override
            public void handleFailure(Call<RepairDetail> call, Throwable t) {

            }
        });
    }
    @Override
    public void bindView(View view)
    {
        repairDetailsBinding= DataBindingUtil.bind(view); }
    @Override
    public void getComponentFactory() {
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        repairDetailsBinding.unbind();
    }

}
