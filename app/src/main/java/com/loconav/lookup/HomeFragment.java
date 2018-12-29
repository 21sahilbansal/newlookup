package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.adapter.WhatToDoAdapter;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.databinding.FragmentHomeBinding;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;

import java.util.ArrayList;
import java.util.List;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;
import static com.loconav.lookup.Constants.TUTORIAL_KEY;

public class HomeFragment extends BaseFragment {
    FragmentHomeBinding binding;
    private PassingReason passingReason = new PassingReason();
    WhatToDoAdapter adapter;
    ReasonResponse reasonResponse;
    Toolbar toolbar;
    ArrayList<ReasonResponse> jsonLog = new ArrayList<>();
    View view;
    @Override
    public int setViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onFragmentCreated() {
        toolbar = binding.toolbar;
        toolbar.inflateMenu(R.menu.user);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ic_lookup_app_icon);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        String reasonsResponse = SharedPrefHelper.getInstance().getStringData(REASONS_RESPONSE);
        Gson gson = new Gson();
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {
        }.getType());
        if (jsonLog != null) {
            setPhotoAdapter();
        } else {
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_LONG).show();
        }
        binding.newInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ReasonTypeResponse> reasons = new ArrayList<>();
                ArrayList<Input> additionalFields = new ArrayList<>();
                ReasonResponse newreasonResponse = new ReasonResponse(1, "New Install", reasons, additionalFields, "abc");
                reasonResponse = newreasonResponse;
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        firstTimeTutorial();
    }

    private void setPhotoAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvTasks.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                reasonResponse = (ReasonResponse) object;
                reasonResponse.setName("Repairs");
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        binding.rvTasks.setAdapter(adapter);
        binding.rvTasks.setNestedScrollingEnabled(false);
        LayoutAnimationController layoutAnimationController= AnimationUtils.loadLayoutAnimation(binding.rvTasks.getContext(),R.anim.layout_animation);
        binding.rvTasks.setLayoutAnimation(layoutAnimationController);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    private void passIntent() {
        Intent intent = new Intent(getActivity(), LookupSubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PassingReason", passingReason);
        bundle.putSerializable("reasonResponse", reasonResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //These 3 function(firTimeTutorial,showTutorial1,showTutorial2) these are used to show the tutorial and they no much use
    public void firstTimeTutorial() {
        Boolean firstTime = SharedPrefHelper.getInstance().getBooleanData(TUTORIAL_KEY);//first time it will be false
        if(!firstTime)
        {
            showTutorial1();
            SharedPrefHelper.getInstance().setBooleanData(TUTORIAL_KEY,true); // next time it will always be true
        }
    }
    public void showTutorial1() {
        view =toolbar.findViewById(R.id.action_user);
        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.user_profile))
                .setContentText("1. "+getString(R.string.check_profile)+"\n\n"+
                        "2. "+getString(R.string.check_repairs_logs)+"\n\n"+
                        "3. "+getString(R.string.check_install_logs))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        showTutorial2();
                    }
                })
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
    }
    public void showTutorial2() {
        view =getActivity().findViewById(R.id.newInstall);
        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.create_new_install))
                .setContentText(getString(R.string.creation_installation))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
    }

    @Override
    public void bindView(View view) {
        binding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
