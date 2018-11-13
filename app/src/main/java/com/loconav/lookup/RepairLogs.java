package com.loconav.lookup;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.RepairLogAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Repairs;
import com.loconav.lookup.databinding.FragmentRepairLogsBinding;
import com.loconav.lookup.model.Repairsdataandcount;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class RepairLogs extends BaseFragment  {
    FragmentRepairLogsBinding fragmentRepairLogsBinding;
    RepairLogAdapter repairLogAdapter;
    List<Repairs> fullRepairsList=new ArrayList<>(),repairsList=new ArrayList<>();
    ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    int start = 0,end=8,totalitem,oppo;
    int placeholdersToLoad=10;
    private ProgressDialog progressDialog;
    HandlerThread handlerThread = new HandlerThread("background");
    Handler handler;
    boolean loadmore=true,itemsloaded=true;
    @Override
    public int setViewId() {
        return R.layout.fragment_repair_logs;
    }
    @Override
    public void onFragmentCreated() {
        Bundle bundle = this.getArguments();
        int layout = bundle.getInt("layout");
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setCancelable(false);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        apiInterface.getRepairLogs(0,8).enqueue(new RetrofitCallback<Repairsdataandcount>() {
            @Override
            public void handleSuccess(Call<Repairsdataandcount> call, Response<Repairsdataandcount> response) {
                fullRepairsList=response.body().getData();
                totalitem=response.body().getTotalcount();
                repairLogAdapter.repairsdata=fullRepairsList;
                repairLogAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                Log.e("the data","The data is "+fullRepairsList);
            }
            @Override
            public void handleFailure(Call<Repairsdataandcount> call, Throwable t) {
            }
        });

        repairLogAdapter=new RepairLogAdapter(fullRepairsList, new Callback() {
            @Override
            public void onEventDone(Object object) {
                if(Utility.isNetworkAvailable(getActivity())) {
                    Bundle bundle = new Bundle();
                    Repairs repairs = (Repairs) object;
                    if(repairs!=null) {
                        bundle.putInt("id", (repairs.getId()));
                        RepairDetailFragment repairDetailFragment = new RepairDetailFragment();
                        repairDetailFragment.setArguments(bundle);
                        FragmentController.loadFragment(repairDetailFragment, getFragmentManager(),layout,true);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView.OnScrollListener  scrollListener=new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int items = recyclerView.getLayoutManager().getChildCount();
                int last = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition()+items-1;
                int pos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                switch (newState)
                {

                    case RecyclerView.SCROLL_STATE_IDLE:
                        if(last>end ) {
                        }

                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if(Utility.isNetworkAvailable(getActivity())) {
                            fragmentRepairLogsBinding.retry.setVisibility(View.GONE);
                            if (pos >= last && itemsloaded) {
                                itemsloaded=false;

                                if(last+placeholdersToLoad<totalitem && loadmore)
                                {
                                for (int i = 0; i <placeholdersToLoad ; i++) {
                                    Repairs repairs = new Repairs();
                                    repairs = null;
                                    fullRepairsList.add(repairs);
                                }
                                repairLogAdapter.repairsdata=fullRepairsList;
                                recyclerView.getAdapter().notifyDataSetChanged();
                                getRepairLogs(last,pos,recyclerView,placeholdersToLoad);
                                }
                                else
                                {
                                    loadmore=false;
                                    for(int i=0;i<(totalitem-(last+1));i++){
                                            Repairs repairs = new Repairs();
                                            repairs = null;
                                            fullRepairsList.add(repairs);
                                        }
                                    repairLogAdapter.repairsdata=fullRepairsList;
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    getRepairLogs(last,pos,recyclerView,(totalitem-(last+1)));
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
             onScrollStateChanged(recyclerView,RecyclerView.SCROLL_STATE_DRAGGING);
            }
            }
        };
        fragmentRepairLogsBinding.repairs.setOnScrollListener(scrollListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
    public void getRepairLogs(int last,int pos,RecyclerView recyclerView,int itemsToLoad)
    {
        Log.e("the last","the pos"+pos+"the las"+last);
        apiInterface.getRepairLogs(last+1, last+itemsToLoad+1).enqueue(new RetrofitCallback<Repairsdataandcount>() {
            @Override
            public void handleSuccess(Call<Repairsdataandcount> call, Response<Repairsdataandcount> response) {
                repairsList = response.body().getData();
                int size=last+repairsList.size();
                oppo=size;
                Log.e("the last","the first is "+last+"the last is"+last+placeholdersToLoad+"the oppo is "+oppo);
                for (int i = last+1; i < last+itemsToLoad+1; i++) {
                    Log.e("th", "the location" + (size - oppo));
                    fullRepairsList.set(i, repairsList.get(size - oppo));
                    repairLogAdapter.repairsdata = fullRepairsList;
                    recyclerView.getAdapter().notifyDataSetChanged();
                    oppo--;
                    if (pos == last) {
                        fragmentRepairLogsBinding.progessbar.setVisibility(View.INVISIBLE);
                        itemsloaded = true;
                    }

                }
            }

            @Override
            public void handleFailure(Call<Repairsdataandcount> call, Throwable t) {
                Log.e("the data ", "the data not found");
            }
        });
    }

}
