package com.loconav.lookup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.AppUpdateController.LATER;

public class AppUpdateDialog extends BaseDialogFragment {

    Button laterButton;
    Button okButton;
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_update, new LinearLayout(getActivity()), false);
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
        forceUpdate = getArguments().getBoolean("force_update");
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
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("url"))));
    }
}
