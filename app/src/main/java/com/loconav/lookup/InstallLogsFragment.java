package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.loconav.lookup.adapter.InstallLogAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentInstallLogsBinding;
import com.loconav.lookup.model.Installs;
import com.loconav.lookup.model.InstallDatandTotalInstallCount;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InstallLogsFragment extends BaseFragment {
    FragmentInstallLogsBinding fragmentInstallLogsBinding;
    InstallLogAdapter installLogAdapter;
    ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
    List<Installs> fullInstallList=new ArrayList<>(),installList=new ArrayList<>();
    int totalitem;
    int start = 0,end=8,oppo;
    int placeholdersToLoad=20;
    boolean loadmore=true,itemsloaded=true;
    FragmentController fragmentController=new FragmentController();
    @Override
    public int setViewId() {
        return R.layout.fragment_install_logs;
    }
    @Override
    public void onFragmentCreated() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Install Logs");
        Bundle bundle = this.getArguments();
        int layout = bundle.getInt("layout");

        apiInterface.getInstallLogs(start,end).enqueue(new RetrofitCallback<InstallDatandTotalInstallCount>() {
            @Override
            public void handleSuccess(Call<InstallDatandTotalInstallCount> call, Response<InstallDatandTotalInstallCount> response) {
                installList=response.body().getData();
                totalitem=response.body().getTotalcount();
                for (Installs installs:installList)
                fullInstallList.add(installs);
                installLogAdapter.notifyDataSetChanged();
            }

            @Override
            public void handleFailure(Call<InstallDatandTotalInstallCount> call, Throwable t) {
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
                    fragmentController.loadFragment(installDetailFragment, getFragmentManager(), layout, true);
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
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if(AppUtils.isNetworkAvailable()) {
                            fragmentInstallLogsBinding.retry.setVisibility(View.GONE);
                            if(loadmore) {
                                if (pastVisibleItems + visibleItemCount >= totalItemCount && itemsloaded) {
                                    fragmentInstallLogsBinding.progessbar.setVisibility(View.VISIBLE);
                                    itemsloaded = false;
                                    if (totalItemCount+placeholdersToLoad<totalitem && loadmore) {
                                        for (int i = 0; i < placeholdersToLoad; i++) {
                                            Installs installs = new Installs();
                                            installs = null;
                                            fullInstallList.add(installs);
                                        }
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        getInstallLogs(totalItemCount,totalItemCount+placeholdersToLoad,recyclerView);
                                    } else {
                                        Log.e("the last", "you have reached the last");
                                        loadmore = false;
                                        for (int i = 0; i < totalitem-(totalItemCount); i++) {
                                            Installs installs = new Installs();
                                            installs = null;
                                            fullInstallList.add(installs);
                                        }
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        getInstallLogs(totalItemCount,totalItemCount+(totalitem-(totalItemCount)),recyclerView);
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
                if(!recyclerView.canScrollVertically(1)&& dy>0)
                {
                    if(itemsloaded) {
                        if (loadmore)
                            fragmentInstallLogsBinding.progessbar.setVisibility(View.VISIBLE);
                        onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_DRAGGING);
                    }

                }
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
    public void getInstallLogs(int first,int last,RecyclerView recyclerView)
    {
        apiInterface.getInstallLogs(first, last).enqueue(new RetrofitCallback<InstallDatandTotalInstallCount>() {
            @Override
            public void handleSuccess(Call<InstallDatandTotalInstallCount> call, Response<InstallDatandTotalInstallCount> response) {
                installList = response.body().getData();
                oppo=0;
                for (int i = first; i < last; i++) {
                    fullInstallList.set(i, installList.get(oppo));
                    recyclerView.getAdapter().notifyDataSetChanged();
                    oppo++;
                }
                itemsloaded = true;
                fragmentInstallLogsBinding.progessbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void handleFailure(Call<InstallDatandTotalInstallCount> call, Throwable t) {
                Log.e("the data ", "the data not found");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentInstallLogsBinding.unbind();
    }
}
