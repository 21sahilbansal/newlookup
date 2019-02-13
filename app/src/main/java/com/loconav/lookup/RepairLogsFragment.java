package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.loconav.lookup.adapter.RepairLogAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Repairs;
import com.loconav.lookup.databinding.FragmentRepairLogsBinding;
import com.loconav.lookup.model.RepairsDataandTotalRepairCount;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.ID;


@SuppressWarnings("deprecation")
public class RepairLogsFragment extends BaseFragment  {
    private FragmentRepairLogsBinding binding;
    private RepairLogAdapter repairLogAdapter;
    private final List<Repairs> fullRepairsList=new ArrayList<>();
    private List<Repairs> repairsList=new ArrayList<>();
    private final ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private final int itemsToLoad =20;
    private LinearLayoutManager layoutManager;
    private final FragmentController fragmentController=new FragmentController();
    @Override
    public int setViewId() {
        return R.layout.fragment_repair_logs;
    }
    @Override
    public void onFragmentCreated() {
        //It is to initially load the number of items
        int start = 0,end=8;
        getRepairLogs(start,end);
        //set repair log adapter
        setRepairLogAdapter();
    }

    public void setRepairLogAdapter()
    {
        repairLogAdapter=new RepairLogAdapter(fullRepairsList, object -> {
            if(AppUtils.isNetworkAvailable()) {
                Bundle bundle = new Bundle();
                Repairs repairs = (Repairs) object;
                if(repairs!=null) {
                    RepairDetailFragment repairDetailFragment=new RepairDetailFragment();
                    bundle.putInt(ID, (repairs.getId()));
                    repairDetailFragment.setArguments(bundle);
                    fragmentController.loadFragment(repairDetailFragment,getFragmentManager(),R.id.fragment_host,true);
                }
            }
            else
            {
                Toaster.makeToast(getString(R.string.internet_not_available));
            }
        });

        RecyclerView.OnScrollListener  scrollListener=new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)&& dy>0) {
                    int totalItemCount = layoutManager.getItemCount();
                    binding.progessbar.setVisibility(View.VISIBLE);
                    getRepairLogs(totalItemCount,totalItemCount+ itemsToLoad);
                }
            }
        };
        binding.repairs.setOnScrollListener(scrollListener);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        binding.repairs.setLayoutManager(layoutManager);
        binding.repairs.setAdapter(repairLogAdapter);
    }

    @Override
    public void bindView(View view) {
        binding =DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    private void getRepairLogs(int first, int last)
    {
        apiInterface.getRepairLogs(first, last).enqueue(new RetrofitCallback<RepairsDataandTotalRepairCount>() {
            @Override
            public void handleSuccess(Call<RepairsDataandTotalRepairCount> call, Response<RepairsDataandTotalRepairCount> response) {
                repairsList = response.body().getData();
                if(!repairsList.isEmpty()) {
                    fullRepairsList.addAll(repairsList);
                    repairLogAdapter.notifyDataSetChanged();
                }
                else {
                    binding.repairs.removeOnScrollListener(null);
                }
                binding.progessbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void handleFailure(Call<RepairsDataandTotalRepairCount> call, Throwable t) {
                Log.e("the data ", "the data not found");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}
