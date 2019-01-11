package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentUploadDocumentsBinding;
import com.loconav.lookup.dialog.DocumentPickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;

public class UploadDocumentsFragment extends BaseFragment {
    FragmentUploadDocumentsBinding binding;
    DocumentPickerDialog documentPickerDialog;
    FirebaseVisionImage image;
    @Override
    public int setViewId() {
        return R.layout.fragment_upload_documents;
    }

    @Override
    public void onFragmentCreated() {
        //This class is incomplete
        EventBus.getDefault().register(this);
        binding.attachDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentPickerDialog=DocumentPickerDialog.newInstance("ss",1);
                documentPickerDialog.show(getFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    @Override
    public void bindView(View view) {
        binding=DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uri(String uri) throws IOException {
        if(!uri.isEmpty()) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(uri));
            image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getCloudTextRecognizer();
            FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                    .setLanguageHints(Arrays.asList("en", "hi"))
                    .build();
            Task<FirebaseVisionText> result =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    Toast.makeText(getContext(), ""+firebaseVisionText.getText(), Toast.LENGTH_SHORT).show();
                                    Log.e("fsjf",firebaseVisionText.getText());
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
            getContext().getContentResolver().delete(Uri.parse(uri), null, null);
            if(bitmap!=null) {
                binding.image.setImageBitmap(bitmap);
                Toast.makeText(getContext(), "The bitmap is not null", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Bitmap is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        binding.unbind();
    }
}
