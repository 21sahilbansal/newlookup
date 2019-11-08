package com.loconav.lookup.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.loconav.lookup.R;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseDialogFragment;

import java.util.Objects;

import static com.loconav.lookup.AppUpdateController.LATER;

public class AppUpdateDialog extends BaseDialogFragment {

    private Button laterButton;
    private Button okButton;
    private boolean forceUpdate;
    private int nextVersion;

    public static AppUpdateDialog newInstance(boolean forceUpdate, int nextVersion, String url) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("force_update", forceUpdate);
        bundle.putInt("next_version", nextVersion);
        bundle.putString("url", url);
        AppUpdateDialog appUpdateDialog = new AppUpdateDialog();
        appUpdateDialog.setArguments(bundle);
        return appUpdateDialog;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.dialog_update, new LinearLayout(getActivity()), false);
        laterButton = view.findViewById(R.id.later_button);
        okButton = view.findViewById(R.id.ok_button);
        attachListeners();
        setCancelable(true);
        Dialog builder = new Dialog(getActivity());
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_UP) {
                dismiss(false);
                if(forceUpdate)
                    getActivity().finish();
                return true;
            }
            return false;
        });
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        forceUpdate = Objects.requireNonNull(getArguments()).getBoolean("force_update");
        nextVersion = getArguments().getInt("next_version");
        initViews();
        return builder;
    }

    private void attachListeners() {
        okButton.setOnClickListener(view -> goToPlayStore());
        laterButton.setOnClickListener(view -> {
            this.dismiss(false);
            SharedPrefHelper.getInstance().setIntData(LATER, nextVersion);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initViews() {
        if(forceUpdate) {
            laterButton.setVisibility(View.GONE);
            setCancelable(false);
        }
    }

    private void goToPlayStore() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Objects.requireNonNull(getArguments()).getString("url"))));
    }
}
