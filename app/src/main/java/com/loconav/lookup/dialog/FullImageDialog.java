package com.loconav.lookup.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.FragmentFullImageBinding;
import com.squareup.picasso.Picasso;

public class FullImageDialog extends BaseDialogFragment {
    private FragmentFullImageBinding binding;
    String imageUrl;
    public static FullImageDialog newInstance(String imageurl) {
        FullImageDialog fullImageDialog = new FullImageDialog();
        Bundle bundle=new Bundle();
        bundle.putString("imageurl",imageurl);
        fullImageDialog.setArguments(bundle);
        return fullImageDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_full_image, new LinearLayout(getActivity()),
                        false);
        binding= DataBindingUtil.bind(dialogView);
        imageUrl=getArguments().getString("imageurl");
        Dialog builder = new Dialog(getActivity(),R.style.CustomDialog);
        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(binding.image);
        binding.delete.setVisibility(View.INVISIBLE);
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               builder.dismiss();
            }
        });
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        return builder;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }

}
