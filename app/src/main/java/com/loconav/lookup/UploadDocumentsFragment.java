package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadDocumentsFragment extends BaseFragment {
    private FragmentUploadDocumentsBinding binding;
    DocumentPickerDialog documentPickerDialog;
    private FirebaseVisionImage image;
    @Override
    public int setViewId() {
        return R.layout.fragment_upload_documents;
    }

    @Override
    public void onFragmentCreated() {
        //This class is incomplete
        EventBus.getDefault().register(this);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.insurance);
        binding.image.setImageBitmap(b);
        binding.attachDocs.setOnClickListener(v -> {
            image = FirebaseVisionImage.fromBitmap(b);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getCloudTextRecognizer();
            FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                    .setLanguageHints(Arrays.asList("en", "hi"))
                    .build();
            Task<FirebaseVisionText> result =
                    detector.processImage(image)
                            .addOnSuccessListener(firebaseVisionText -> {
                                Toaster.makeToast(firebaseVisionText.getText());
                                Log.e("sourav", firebaseVisionText.getText());
                                String string = firebaseVisionText.getText();
                                String regex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
                                Pattern p = Pattern.compile(regex);
                                Matcher m1 = p.matcher(string);
                                while (m1.find()) {
                                    //Toast.makeText(getContext(), "ss"+(m1.group()), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(
                                    e -> {
                                        // Task failed with an exception
                                        // ...
                                        //Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
            if(b !=null) {
                binding.image.setImageBitmap(b);
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
        //Still to work on not complete

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        binding.unbind();
    }
}
