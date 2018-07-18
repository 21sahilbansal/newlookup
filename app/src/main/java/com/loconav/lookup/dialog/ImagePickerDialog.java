package com.loconav.lookup.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prateek on 09/07/18.
 */

public class ImagePickerDialog extends BaseDialogFragment {
    @BindView(R.id.camera)
    LinearLayout camera;
    @BindView(R.id.gallery)
    LinearLayout gallery;

    public static ImagePickerDialog newInstance() {
        ImagePickerDialog imagePickerDialog = new ImagePickerDialog();
        return imagePickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_image_picker, new LinearLayout(getActivity()),
                        false);
        ButterKnife.bind(this, dialogView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        return builder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
