package com.loconav.lookup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loconav.lookup.base.BaseFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sejal on 12-07-2018.
 */

public class Repair extends BaseFragment {
//    @BindView(R.id.devImage) ImageView devImage;
//    @BindView (R.id.vehImage) ImageView vehImage;
    @BindView (R.id.proceed) Button proceed;
//    @BindView (R.id.vehLinear) LinearLayout vehLinear;
//    @BindView (R.id.devLinear) LinearLayout devLinear;
    private boolean permissionResult;
    private boolean iv1,iv2;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    @Override
    public int setViewId() {
        return R.layout.repair;
    }

    @Override
    public void onFragmentCreated() {

//        devImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               iv1=true;
//                checkPermissions();
//            }
//        });
//        vehImage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                iv2=true;
//                checkPermissions();
//            }
//        });
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }


//    private void checkPermissions(){
//    //    permissionResult=checkAndRequestPermissions();
//        if(permissionResult){
//            selectImage();
//        }
//    }
//    private void selectImage() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask ="Take Photo";
//                  //  boolean resultCamera= checkAndRequestPermissions();
////                    if(resultCamera) {
////                        cameraIntent();}
//
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask ="Choose from Library";
////                    boolean resultGallery=checkAndRequestPermissions();
////                    if(resultGallery) {
////                        galleryIntent();}
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//    void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
//    }
//
//    public void cameraIntent()
//    {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }
//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        setImage(thumbnail);
//    }
//
//    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        setImage(bm);
//    }
//
//    private void setImage(Bitmap bitmap){
//        if(iv1) {
//            devImage.setImageBitmap(bitmap);
//            devLinear.setVisibility(View.INVISIBLE);
//            iv1=false;
//            devImage.setTag("done");
//        }else if(iv2){
//            vehImage.setImageBitmap(bitmap);
//            vehLinear.setVisibility(View.INVISIBLE);
//            iv2=false;
//            vehImage.setTag("done");
//        }
//        if(devImage.getTag()=="done" && vehImage.getTag()=="done") {
//            proceed.setEnabled(true);
//            proceed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onProceedClicked();
//                }
//            });
//        }
//    }

    private void onProceedClicked(){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Create FragmentOne instance.
            RepairForm fragmentRepairForm = new RepairForm();
            // Add fragment one with tag name.
            fragmentTransaction.add(R.id.fragmentContainerSub, fragmentRepairForm, "Fragment One");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }


}
