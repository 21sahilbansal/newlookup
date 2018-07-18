package com.loconav.lookup;

import com.loconav.lookup.adapter.RecycleGridView;
import com.loconav.lookup.databinding.FragmentFastagBinding;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.adapter.VehiclesAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.StagingApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 28-06-2018.
 */

public class FastTagFragment extends BaseCameraFragment {

    private FragmentFastagBinding binding;
    private SearchView.SearchAutoComplete truckSearchAutoComplete;

    @Override
    public int setViewId() {
        return R.layout.fragment_fastag;
    }

    @Override
    public void bindView(View view) {
      binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {}

    @Override
    public void onAllPermissionsGranted() {
        Log.e("succededd", "onAllPermissionsGranted: ");
    }

    @Override
    public void onFragmentCreated() {
        super.onFragmentCreated();
        setVehicleSearchView();

        binding.truckSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });
    }

    private void setVehicleSearchView() {
        binding.searchTruck.setIconified(true);
        truckSearchAutoComplete = (SearchView.SearchAutoComplete)binding.searchTruck.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        truckSearchAutoComplete.setAdapter(new VehiclesAdapter(getContext(), new ArrayList<VehiclesList>()));
        truckSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehiclesList vehiclesList = (VehiclesList) parent.getItemAtPosition(position);
                truckSearchAutoComplete.setText(vehiclesList.getNumber());
                truckSearchAutoComplete.setSelection(vehiclesList.getNumber().length());
            }
        });

    }
}
