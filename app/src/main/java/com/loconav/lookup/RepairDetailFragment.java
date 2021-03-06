package com.loconav.lookup;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.databinding.FragmentRepairDetailsBinding;
import com.loconav.lookup.dialog.FullImageDialog;
import com.loconav.lookup.model.Input;
import com.loconav.lookup.model.RepairDetail;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.ID;

public class RepairDetailFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private FragmentRepairDetailsBinding repairDetailsBinding;
    private int repairId=0;
    @Override
    public int setViewId() {
        return R.layout.fragment_repair_details;
    }

    @Override
    public void onFragmentCreated() {
        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            repairId = bundle.getInt(ID);
        }
        repairDetailsBinding.swipeRefresh.setOnRefreshListener(this);
        loadRepairDetail();
    }
    @Override
    public void bindView(View view) {
        repairDetailsBinding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        repairDetailsBinding.unbind();
    }

    @Override
    public void onRefresh() {
        loadRepairDetail();
        if(repairDetailsBinding.repairdata.getChildCount() > 0)
            repairDetailsBinding.repairdata.removeAllViews();
    }

    private void loadRepairDetail() {
        apiService.getRepairDetail(repairId).enqueue(new RetrofitCallback<RepairDetail>() {
            @Override
            public void handleSuccess(Call<RepairDetail> call, Response<RepairDetail> response) {
                repairDetailsBinding.swipeRefresh.setRefreshing(false);
                RepairDetail repairs=response.body();
                if(repairs!=null) {
                    if (getContext() != null) {
                        CustomInflater customInflater = new CustomInflater(getContext());
                        int index = 0;
                        repairDetailsBinding.setRepairs(repairs);
                        for (String r : repairs.getRepairData().keySet()) {
                            Input input = new Input(r, r, r, r);
                            TextView labels = customInflater.addtext(r, repairDetailsBinding.repairdata, input, index);
                            labels.setTextSize(11);
                            labels.setTextColor(getResources().getColor(R.color.black_38));
                            Input output = new Input(repairs.getRepairData().get(r), repairs.getRepairData().get(r), repairs.getRepairData().get(r), repairs.getRepairData().get(r));
                            TextView result = customInflater.addtext(repairs.getRepairData().get(r), repairDetailsBinding.repairdata, output, index);
                            result.setTextSize(14);
                            result.setTextColor(getResources().getColor(R.color.black));
                        }

                        LinearLayoutManager preRepairlayoutManager = new LinearLayoutManager(getContext());
                        preRepairlayoutManager.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayoutManager postRepairlayoutManager = new LinearLayoutManager(getContext());
                        postRepairlayoutManager.setOrientation(LinearLayout.HORIZONTAL);
                        ImageSetterAdapter postRepairAdapter, preRepairAdapter;
                        preRepairAdapter = new ImageSetterAdapter(repairs.getPreRepairImages(), new Callback() {
                            @Override
                            public void onEventDone(Object object) {
                                if(getActivity()!=null) {
                                    FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                    fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                                }
                            }
                        });
                        repairDetailsBinding.prerepairimages.setLayoutManager(preRepairlayoutManager);
                        repairDetailsBinding.prerepairimages.setAdapter(preRepairAdapter);
                        postRepairAdapter = new ImageSetterAdapter(repairs.getPostRepairImages(), new Callback() {
                            @Override
                            public void onEventDone(Object object) {
                                if(getActivity()!=null) {
                                    FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                    fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                                }
                            }
                        });
                        repairDetailsBinding.postrepairimages.setLayoutManager(postRepairlayoutManager);
                        repairDetailsBinding.postrepairimages.setAdapter(postRepairAdapter);
                    }
                }
                else
                {
                    Toaster.makeToast(getString(R.string.swipe_to_refresh));
                }
            }
            @Override
            public void handleFailure(Call<RepairDetail> call, Throwable t) {
                repairDetailsBinding.swipeRefresh.setRefreshing(false);
                Toaster.makeToast(t.getMessage());
            }
        });
    }
}
