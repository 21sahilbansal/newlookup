package com.loconav.lookup;

import android.os.Bundle;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;

public class UserActivitySubActivity extends BaseActivity {
    String task;
    FragmentController fragmentController=new FragmentController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub);
         task= getIntent().getStringExtra("task");
        if( task.equals("RepairLogs")) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", R.id.task);
            RepairLogsFragment repairLogsFragment = new RepairLogsFragment();
            repairLogsFragment.setArguments(bundle);
            fragmentController.loadFragment(repairLogsFragment, getSupportFragmentManager(), R.id.task, false);
        }
        else if(task.equals("InstallLogs")) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", R.id.task);
            InstallLogsFragment installLogsFragment = new InstallLogsFragment();
            installLogsFragment.setArguments(bundle);
            fragmentController.loadFragment(installLogsFragment, getSupportFragmentManager(), R.id.task, false);
        }
    }

    @Override
    public boolean showBackButton() {
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Destroyed","Destroyed");
    }
}
