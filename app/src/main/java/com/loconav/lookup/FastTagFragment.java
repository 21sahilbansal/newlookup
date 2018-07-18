package com.loconav.lookup;

import com.loconav.lookup.adapter.RecycleGridView;
import com.loconav.lookup.databinding.FragmentFastagBinding;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.InstallerCreds;
import com.loconav.lookup.model.InstallersResponse;
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
import android.view.View;
import android.view.WindowManager;
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
    FragmentFastagBinding binding;
    private ApiInterface apiService = StagingApiClient.getClient().create(ApiInterface.class);
    ArrayList<ImageUri> imagesUriArrayList ;
    RecycleGridView adapter;
    SearchView.SearchAutoComplete searchAutoComplete;
    SearchView.SearchAutoComplete searchAutoCompleteFastag;
    VehiclesList query;
    FastagsList queryFastags;
    String userChoosenTask;
    InstallerCreds installerCreds=new InstallerCreds();
    ArrayList<VehiclesList> vehiclesLists;
    ArrayList<FastagsList> fastagsLists;
    VehiclesAdapter vehiclesAdapter;
    FastagAdapter fastagAdapter;
    @BindView(R.id.searchTruck) SearchView searchView;
   @BindView(R.id.searchFastId) SearchView searchFastId;
    private Boolean addMore=false;
    boolean permissionResult=false;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    public int setViewId() {
        return R.layout.fragment_fastag;
    }


    @Override
    public void onFragmentCreated() {
        ButterKnife.bind(this,getView());
        searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoCompleteFastag = (SearchView.SearchAutoComplete)searchFastId.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        vehiclesLists=new ArrayList<>();
        vehiclesLists=getSetData(vehiclesLists);
        fastagsLists=new ArrayList<>();
        binding.buttonTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               permissionResult= checkAndRequestPermissions();
               if(permissionResult){
                   selectImage();
               }
            }
        });
        binding.truckSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked();
        }
    });
    }

    private ArrayList<FastagsList> getFastags(final ArrayList<FastagsList> fastagsLists) {
        apiService.getFastags(query.getId()).enqueue(new RetrofitCallback<List<FastagsList>>() {
            @Override
            public void handleSuccess(Call<List<FastagsList>> call, Response<List<FastagsList>> response) {
                if(response.body()!=null && response.body().size()>0) {
                    fastagsLists.clear();
                    fastagsLists.addAll(response.body());
                    setSearchViewFastags();
                } else
                    handleFailure(call, new Throwable("no lists available"));
            }

            @Override
            public void handleFailure(Call<List<FastagsList>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
        return fastagsLists;
    }

    @Override
    public void bindView(View view) {

        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    @Override
    protected void showRequestAccepted() {
     Log.e("sss","sss accepted");
    }

    private ArrayList<VehiclesList> getSetData(final ArrayList<VehiclesList> vehiclesLists) {
        apiService.getVehicles().enqueue(new RetrofitCallback<List<VehiclesList>>() {
            @Override
            public void handleSuccess(Call<List<VehiclesList>> call, Response<List<VehiclesList>> response) {
                if(response.body()!=null && response.body().size()>0) {
                    vehiclesLists.clear();
                    vehiclesLists.addAll(response.body());
                    setSearchView();
                    Log.e("error ", ""+vehiclesLists.size());
                } else
                    handleFailure(call, new Throwable("no lists available"));
            }

            @Override
            public void handleFailure(Call<List<VehiclesList>> call, Throwable t) {
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("error ", t.getMessage());
            }
        });

        return vehiclesLists;
    }

    @SuppressLint("RestrictedApi")
    private void setSearchViewFastags() {
        fastagAdapter= new FastagAdapter(getContext(),fastagsLists);
        searchAutoCompleteFastag.setThreshold(1);
        searchAutoCompleteFastag.setAdapter(fastagAdapter);
        binding.searchFastId.setActivated(true);
        binding.searchFastId.setQueryHint("Select Fastag");
      //  binding.searchFastId.setIconified(false);
        searchAutoCompleteFastag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryFastags= (FastagsList) parent.getItemAtPosition(position);
                searchAutoCompleteFastag.setText(queryFastags.getSerialNumber()+" "+queryFastags.getColor());
                searchAutoCompleteFastag.setSelection(queryFastags.getSerialNumber().length()+queryFastags.getColor().length());
                installerCreds.setFastag_id(queryFastags.getId());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void setSearchView(){
        vehiclesAdapter= new VehiclesAdapter(getContext(),vehiclesLists);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(vehiclesAdapter);
        binding.searchTruck.setActivated(true);
        binding.searchTruck.setQueryHint("Select Vehicle");
      //  searchView.setIconified(false);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query= (VehiclesList) parent.getItemAtPosition(position);
                searchAutoComplete.setText(query.getNumber());
                searchAutoComplete.setSelection(query.getNumber().length());
                installerCreds.setTruck_id(query.getId());
                fastagsLists=getFastags(fastagsLists);
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    boolean resultCamera= checkAndRequestPermissions();
                    if(resultCamera) {
                        cameraIntent();}

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    boolean resultGallery=checkAndRequestPermissions();
                    if(resultGallery) {
                        galleryIntent();}
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    public void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!addMore) {
            imagesUriArrayList = new ArrayList<>();
            imagesUriArrayList.clear();
        }
        thumbnail=Bitmap.createScaledBitmap(thumbnail,thumbnail.getWidth()*3 , thumbnail.getHeight()*3, true);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),thumbnail, "Title", null);
//        imagesUriArrayList.add(Uri.parse(path));
        adapter = new RecycleGridView(imagesUriArrayList, new Callback() {
            @Override
            public void onEventDone(Object object) {

            }
        });
        binding.recyclerImages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        addMore=true;
        binding.buttonTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectImage();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        imagesUriArrayList=new ArrayList<>();
        imagesUriArrayList.clear();
        Log.e("ss",""+data.getClipData()+data.getData());
        if(data.getClipData()==null){
            ImageUri imageUri = new ImageUri();
            imageUri.setUri(data.getData());
          imagesUriArrayList.add(imageUri);
        }else {
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {

                ImageUri imageUri = new ImageUri();
                imageUri.setUri(data.getClipData().getItemAt(i).getUri());
                imagesUriArrayList.add(imageUri);
            }
        }
        Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        binding.recyclerImages.setLayoutManager(layoutManager);
        adapter = new RecycleGridView(imagesUriArrayList, new Callback() {
            @Override
            public void onEventDone(Object object) {}
        });
        binding.recyclerImages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void onSubmitClicked() {
        apiService.createInstallation(installerCreds).enqueue(new RetrofitCallback<InstallersResponse>() {
            @Override
            public void handleSuccess(Call<InstallersResponse> call, Response<InstallersResponse> response) {
                Toast.makeText(getContext(),response.message(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFailure(Call<InstallersResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

}
