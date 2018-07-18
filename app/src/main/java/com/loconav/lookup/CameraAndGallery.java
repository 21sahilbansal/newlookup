//package com.loconav.lookup;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.loconav.UtilityVehicles;
//import com.loconav.lookup.adapter.AdapterGridView;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * Created by sejal on 03-07-2018.
// */
//
//public abstract class CameraAndGallery extends BaseCameraFragment {
//    public static String userChoosenTask;
//    private static ImageView imageView;
//    private static Context context1;
//    private static Activity activity1;
//
//    public  void setData(Activity activity,Context context,ImageView iv){
//        context1=context;
//        imageView=iv;
//        activity1=activity;
//        selectImage();
//    }
//
//    public void selectImage(){
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity1);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask ="Take Photo";
//                    boolean resultCamera= UtilityVehicles.checkPermissionCamera(activity1);
//                    if(resultCamera) {
//                        Log.e("s",""+userChoosenTask);
//                        cameraIntent();
//                        Log.e("e",""+userChoosenTask);
//                    }
//
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask ="Choose from Library";
//                    boolean resultGallery= UtilityVehicles.checkPermission(activity1);
//                    if(resultGallery) {
//                        Log.e("s",""+userChoosenTask);
//                        galleryIntent();
//                        Log.e("e",""+userChoosenTask);
//                    }
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    abstract void galleryIntent();
//
//    abstract void cameraIntent();
//
//    public void onCaptureImageResult(Intent data) {
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
//        if(!addMore) {
//            imagesUriArrayList = new ArrayList<>();
//            imagesUriArrayList.clear();
//        }
//        thumbnail=Bitmap.createScaledBitmap(thumbnail,thumbnail.getWidth()*3 , thumbnail.getHeight()*3, true);
//        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),thumbnail, "Title", null);
//        imagesUriArrayList.add(Uri.parse(path));
//        adapter = new AdapterGridView(getContext(),R.layout.gridview_layout,imagesUriArrayList);
//        gridview.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        addMore=true;
//        button_truck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });
//    }
//
//    @SuppressWarnings("deprecation")
//    public static void onSelectFromGalleryResult(Intent data) {
//
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(context1.getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        imageView.setImageBitmap(bm);
//    }
//}
