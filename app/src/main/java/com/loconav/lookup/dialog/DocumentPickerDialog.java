package com.loconav.lookup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogImagePickerBinding;

public class DocumentPickerDialog extends BaseDialogFragment {
    private static final int REQUEST_CODE = 99;
    private DialogImagePickerBinding binding;
    public static DocumentPickerDialog newInstance() {
        return new DocumentPickerDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_image_picker, new LinearLayout(getActivity()),
                        false);
        binding= DataBindingUtil.bind(dialogView);
        binding.camera.setOnClickListener(v -> cameraIntent());

        binding.gallery.setOnClickListener(v -> galleryIntent());

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        return builder;
    }

    private void galleryIntent() {
//        Intent intent= new Intent(getContext(), ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
//        startActivityForResult(intent, Constants.REQUEST_CODE_FOR_DOCUMENT);
    }
    private void cameraIntent()
    {
//        Intent intent= new Intent(getContext(), ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);
//        startActivityForResult(intent,Constants.REQUEST_CODE_FOR_DOCUMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
//            EventBus.getDefault().post(uri.toString());
        }
        dismiss();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}

