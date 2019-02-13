package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.ID;

public class InstallLogsFragment extends BaseFragment {
    private FragmentInstallLogsBinding binding;
    private InstallLogAdapter installLogAdapter;
    private final List<Installs> fullInstallList=new ArrayList<>();
    private final int itemsToLoad =20;
    private LinearLayoutManager layoutManager;
    private final FragmentController fragmentController=new FragmentController();
    @Override
    public int setViewId() {
        return R.layout.fragment_install_logs;
    }
    @Override
    public void onFragmentCreated() {
        //It is to initially load the number of items
        int start = 0,end=8;
        getInstallLogs(start,end);
        //set the adapter
        setInstallLogAdapter();
    }

    public void setInstallLogAdapter() {
        installLogAdapter=new InstallLogAdapter(fullInstallList, object -> {
            Bundle bundle = new Bundle();
            Installs installs = (Installs) object;
            if(installs!=null) {
                InstallDetailFragment installDetailFragment=new InstallDetailFragment();
                bundle.putString(ID, installs.getId());
                installDetailFragment.setArguments(bundle);
                fragmentController.loadFragment(installDetailFragment,getFragmentManager(),R.id.fragment_host,true);
            }
        });

        RecyclerView.OnScrollListener  scrollListener=new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)&& dy>0) {
                    int totalItemCount = layoutManager.getItemCount();
                    binding.progessbar.setVisibility(View.VISIBLE);
                    getInstallLogs(totalItemCount,totalItemCount+ itemsToLoad);
                }
            }
        };
        binding.installs.addOnScrollListener(scrollListener);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        binding.installs.setLayoutManager(layoutManager);
        binding.installs.setAdapter(installLogAdapter);
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    private void getInstallLogs(int first, int last)
    {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getInstallLogs(first, last).enqueue(new RetrofitCallback<InstallDatandTotalInstallCount>() {
            @Override
            public void handleSuccess(Call<InstallDatandTotalInstallCount> call, Response<InstallDatandTotalInstallCount> response) {
                List<Installs> installList = response.body().getData();
                if(!installList.isEmpty()) {
                    fullInstallList.addAll(installList);
                    installLogAdapter.notifyDataSetChanged();
                }
                else{
                    binding.installs.removeOnScrollListener(null);
                }
                binding.progessbar.setVisibility(View.INVISIBLE);
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
        binding.unbind();
    }
}
