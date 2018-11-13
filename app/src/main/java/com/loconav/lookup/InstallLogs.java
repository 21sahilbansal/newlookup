package com.loconav.lookup;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.InstallLogAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentInstallLogsBinding;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.model.Installs;
import com.loconav.lookup.model.NewInstall;
import com.loconav.lookup.model.NewInstallDataandCount;
import com.loconav.lookup.model.Repairs;
import com.loconav.lookup.model.Repairsdataandcount;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InstallLogs  extends BaseFragment {
    FragmentInstallLogsBinding fragmentInstallLogsBinding;
    InstallLogAdapter installLogAdapter;
    ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
    List<Installs> fullInstallList=new ArrayList<>(),installList=new ArrayList<>();
    int totalitem;
    int start = 0,end=8,oppo;
    int placeholdersToLoad=10;
    private ProgressDialog progressDialog;
    HandlerThread handlerThread = new HandlerThread("background");
    Handler handler;
    boolean loadmore=true,itemsloaded=true;
    @Override
    public int setViewId() {
        return R.layout.fragment_install_logs;
    }
    @Override
    public void onFragmentCreated() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Install Logs");
        Bundle bundle = this.getArguments();
        int layout = bundle.getInt("layout");
        progressDialog = new ProgressDialog(getActivity());//we are on ui thread
        progressDialog.setCancelable(false);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        apiInterface.getInstallLogs(0,8).enqueue(new RetrofitCallback<NewInstallDataandCount>() {
            @Override
            public void handleSuccess(Call<NewInstallDataandCount> call, Response<NewInstallDataandCount> response) {
                fullInstallList=response.body().getData();
                totalitem=response.body().getTotalcount();
                installLogAdapter.installsList=fullInstallList;
                installLogAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFailure(Call<NewInstallDataandCount> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
        installLogAdapter=new InstallLogAdapter(fullInstallList, new Callback() {
            @Override
            public void onEventDone(Object object) {

                Bundle bundle = new Bundle();
                Installs installs = (Installs) object;
                if(installs!=null) {
                    bundle.putInt("id", Integer.parseInt((installs.getId())));
                    InstallDetailFragment installDetailFragment = new InstallDetailFragment();
                    installDetailFragment.setArguments(bundle);
                    FragmentController.loadFragment(installDetailFragment, getFragmentManager(), layout, true);
                }
            }
        });

        RecyclerView.OnScrollListener  scrollListener=new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int items = recyclerView.getLayoutManager().getChildCount();
                int top = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition()+items-1;
                int pos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                Log.e("the last is ","The las is "+last);
                oppo=last;
                switch (newState)
                {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if(Utility.isNetworkAvailable(getActivity())) {
                            fragmentInstallLogsBinding.retry.setVisibility(View.GONE);
                            if(loadmore) {
                                if (pos >= last) {
                                    fragmentInstallLogsBinding.progessbar.setVisibility(View.VISIBLE);
                                    itemsloaded = false;
                                    if (last + placeholdersToLoad < totalitem && loadmore) {
                                        for (int i = 0; i < placeholdersToLoad; i++) {
                                            Installs installs = new Installs();
                                            installs = null;
                                            fullInstallList.add(installs);
                                        }
                                        installLogAdapter.installsList = fullInstallList;
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        getInstallLogs(last, pos, recyclerView, placeholdersToLoad);
                                    } else {
                                        Log.e("the last", "you have reached the last");
                                        loadmore = false;
                                        for (int i = 0; i < (totalitem - (last + 1)); i++) {
                                            Installs installs = new Installs();
                                            installs = null;
                                            fullInstallList.add(installs);
                                        }
                                        installLogAdapter.installsList = fullInstallList;
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        getInstallLogs(last, pos, recyclerView, (totalitem - (last + 1)));
                                    }
                                }
                            }
                        }
                        else
                        {
                            if(loadmore)
                                fragmentInstallLogsBinding.retry.setVisibility(View.VISIBLE);
                            else
                                fragmentInstallLogsBinding.retry.setVisibility(View.GONE);
                        }
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };

        fragmentInstallLogsBinding.installs.addOnScrollListener(scrollListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        fragmentInstallLogsBinding.installs.setLayoutManager(layoutManager);
        fragmentInstallLogsBinding.installs.setAdapter(installLogAdapter);
    }

    @Override
    public void bindView(View view) {
        fragmentInstallLogsBinding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }
    public void getInstallLogs(int last,int pos,RecyclerView recyclerView,int itemsToLoad)
    {
        apiInterface.getInstallLogs(last+1, last+itemsToLoad+1).enqueue(new RetrofitCallback<NewInstallDataandCount>() {
            @Override
            public void handleSuccess(Call<NewInstallDataandCount> call, Response<NewInstallDataandCount> response) {
                installList = response.body().getData();
                int size=last+installList.size();
                oppo=size;
                Log.e("the last","the first is "+last+"the last is"+last+placeholdersToLoad+"the oppo is "+oppo);
                for (int i = last+1; i < last+itemsToLoad+1; i++) {
                    Log.e("th", "the location" + (size - oppo));
                    fullInstallList.set(i, installList.get(size - oppo));
                    installLogAdapter.installsList = fullInstallList;
                    recyclerView.getAdapter().notifyDataSetChanged();
                    oppo--;
                    if (pos == last) {
                        itemsloaded = true;
                        fragmentInstallLogsBinding.progessbar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void handleFailure(Call<NewInstallDataandCount> call, Throwable t) {
                Log.e("the data ", "the data not found");
            }
        });
    }
}
