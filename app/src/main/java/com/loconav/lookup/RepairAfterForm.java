package com.loconav.lookup;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairAfterForm extends BaseFragment {
    @BindView (R.id.Vehicleimage) CustomImagePicker vehicleimage;
    @BindView (R.id.proceedRep) Button proceedRep;
    @BindView (R.id.pbHeaderProgress) ProgressBar pbHeaderProgress;
    private ApiInterface apiService = StagingApiClient.getClient().create(ApiInterface.class);
    @Override
    public int setViewId() {
        return R.layout.repair_after_form;
    }

    @Override
    public void onFragmentCreated() {
        final Uri uri= Uri.parse(getArguments().getString("Image"));
       final Uri uri2= Uri.parse(getArguments().getString("Image2"));
        final RepairRequirements repairRequirements= (RepairRequirements)getArguments().getSerializable("req");
        Log.e("gh",""+uri.getPath()+" jk  "+uri2.getPath()+"             "+repairRequirements.getDevice_id());
        proceedRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vehicleimage.GetimagesList().size() >= 1) {
                    proceedRep.setVisibility(View.GONE);
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                    new UtilCompress(getContext()).execute(getRealPathFromURI(uri));
                    String str=((EnterDetails) getActivity()).convertToBitmap( bitmapTOuri(uri));
                   // Bitmap m = EncodingDecoding.decodeBase64(str);
//                    String str1 = EncodingDecoding.encodeToBase64(m, Bitmap.CompressFormat.PNG, 50);
//                    Bitmap n = EncodingDecoding.decodeBase64(str1);
//                    String str2 = "data:image/png;base64,"+EncodingDecoding.encodeToBase64(n, Bitmap.CompressFormat.PNG, 50);

                    ArrayList<String> al=new ArrayList<>();
                    al.add(str);
//                    al.add(str2);
//                    al.add(str2);
//                    al.add(str2);
//                    al.add(str2);
//                    al.add(str2);
//                    al.add(str2);



                    repairRequirements.setPre_repair_images(al);
                    String str5=((EnterDetails) getActivity()).convertToBitmap( bitmapTOuri(uri2));
                    String str3=((EnterDetails) getActivity()).convertToBitmap( bitmapTOuri(vehicleimage.GetimagesList().get(0).getUri()));
                    ArrayList<String> al1=new ArrayList<>();
                    al1.add(str5);
                    al1.add(str3);
                    repairRequirements.setPost_repair_images(al1);
                    hitApi(repairRequirements);
                } else {
                    Toast.makeText(getContext(), "Add Vehicle Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
public  Bitmap bitmapTOuri(Uri imageUri){
        Bitmap bm=null;
    try {
        bm=(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri));

    } catch (IOException e) {
        e.printStackTrace();
    }
    return bm;
}
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }
    public void hitApi(RepairRequirements repairRequirements){

        apiService.addRepairs(repairRequirements).enqueue(new RetrofitCallback<RepairResponse>() {

            @Override
            public void handleSuccess(Call<RepairResponse> call, Response<RepairResponse> response) {
                proceedRep.setVisibility(View.VISIBLE);
                pbHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFailure(Call<RepairResponse> call, Throwable t) {
                proceedRep.setVisibility(View.VISIBLE);
                pbHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImagePickingEvents(GalleryEvents event) {
        if(event.getMessage().equals(GalleryEvents.IMAGE_COMPRESSED)) {
            Toast.makeText(getContext(), "Image Compressed", Toast.LENGTH_LONG).show();
            File sd = Environment.getExternalStorageDirectory();
            File image = new File((String)event.getObject(), "");
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            String r = EncodingDecoding.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 0);
            Log.e("print ", "getImagePickingEvents: " + r.length() );
        }
    }
}
