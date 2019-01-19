package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.RepairLogAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.Callback;
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


public class RepairLogsFragment extends BaseFragment  {
    private FragmentRepairLogsBinding fragmentRepairLogsBinding;
    private RepairLogAdapter repairLogAdapter;
    private List<Repairs> fullRepairsList=new ArrayList<>(),repairsList=new ArrayList<>();
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private int totalitem,oppo;
    private int placeholdersToLoad=20;
    private boolean loadmore=true,itemsloaded=true;
    FragmentController fragmentController=new FragmentController();
    @Override
    public int setViewId() {
        return R.layout.fragment_repair_logs;
    }
    @Override
    public void onFragmentCreated() {
        //It is to initially load the number of items
        int start = 0,end=8;
        apiInterface.getRepairLogs(start,end).enqueue(new RetrofitCallback<RepairsDataandTotalRepairCount>() {
            @Override
            public void handleSuccess(Call<RepairsDataandTotalRepairCount> call, Response<RepairsDataandTotalRepairCount> response) {
                fragmentRepairLogsBinding.startprogressbar.setVisibility(View.INVISIBLE);
                repairsList=response.body().getData();
                totalitem=response.body().getTotalcount();
                for(Repairs repairs:repairsList)
                    fullRepairsList.add(repairs);
                repairLogAdapter.notifyDataSetChanged();
                Log.e("the data","The data is "+fullRepairsList);
            }
            @Override
            public void handleFailure(Call<RepairsDataandTotalRepairCount> call, Throwable t) {
            }
        });

        repairLogAdapter=new RepairLogAdapter(fullRepairsList, new Callback() {
            @Override
            public void onEventDone(Object object) {
                if(AppUtils.isNetworkAvailable()) {
                    Bundle bundle = new Bundle();
                    Repairs repairs = (Repairs) object;
                    if(repairs!=null) {
                        RepairDetailFragment repairDetailFragment=new RepairDetailFragment();
                        bundle.putInt("id", (repairs.getId()));
                        repairDetailFragment.setArguments(bundle);
                        fragmentController.loadFragment(repairDetailFragment,getFragmentManager(),R.id.fragment_host,true);
                    }
                }
                else
                {
                    Toaster.makeToast(getString(R.string.internet_not_available));
                }
            }
        });

        RecyclerView.OnScrollListener  scrollListener=new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildCount();
                int totalItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).getItemCount();
                int pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                switch (newState)
                {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if(AppUtils.isNetworkAvailable()) {
                            fragmentRepairLogsBinding.retry.setVisibility(View.GONE);
                            if (pastVisibleItems + visibleItemCount >= totalItemCount && itemsloaded) {
                                itemsloaded=false;
                                if(totalItemCount+placeholdersToLoad<totalitem && loadmore)
                                {
                                for (int i = 0; i <placeholdersToLoad ; i++) {
                                    Repairs repairs = new Repairs();
                                    fullRepairsList.add(repairs);
                                }
                                recyclerView.getAdapter().notifyDataSetChanged();
                                Log.e("the","the first is"+totalItemCount+"the last is"+totalItemCount+placeholdersToLoad);
                                getRepairLogs(totalItemCount,totalItemCount+placeholdersToLoad,recyclerView);
                                }
                                else
                                {
                                    loadmore=false;
                                    for(int i=0;i<totalitem-(totalItemCount);i++){
                                            Repairs repairs = new Repairs();
                                            fullRepairsList.add(repairs);
                                        }
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                   getRepairLogs(totalItemCount,totalItemCount+(totalitem-(totalItemCount)),recyclerView);
                                }
                            }
                        }
                        else
                        {
                            if(loadmore)
                                fragmentRepairLogsBinding.retry.setVisibility(View.VISIBLE);
                            else
                                fragmentRepairLogsBinding.retry.setVisibility(View.GONE);
                        }
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            if(!recyclerView.canScrollVertically(1)&& dy>0)
            {
                if(loadmore)
                    fragmentRepairLogsBinding.progessbar.setVisibility(View.VISIBLE);
                onScrollStateChanged(recyclerView,RecyclerView.SCROLL_STATE_IDLE);
            }
            }
        };

        fragmentRepairLogsBinding.repairs.setOnScrollListener(scrollListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        fragmentRepairLogsBinding.repairs.setLayoutManager(layoutManager);
        fragmentRepairLogsBinding.repairs.setAdapter(repairLogAdapter);
    }
    @Override
    public void bindView(View view) {
        fragmentRepairLogsBinding=DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }



    public void getRepairLogs(int first,int last,RecyclerView recyclerView)
    {
        apiInterface.getRepairLogs(first, last).enqueue(new RetrofitCallback<RepairsDataandTotalRepairCount>() {
            @Override
            public void handleSuccess(Call<RepairsDataandTotalRepairCount> call, Response<RepairsDataandTotalRepairCount> response) {
                repairsList = response.body().getData();
                oppo=0;
                for (int i = first; i < last; i++) {
                    fullRepairsList.set(i, repairsList.get(oppo));
                    recyclerView.getAdapter().notifyDataSetChanged();
                    oppo++;
                }
                    fragmentRepairLogsBinding.progessbar.setVisibility(View.INVISIBLE);
                    itemsloaded = true;
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
        fragmentRepairLogsBinding.unbind();
    }
}
