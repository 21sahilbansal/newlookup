package com.loconav.lookup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;

public class UserActivitySubActivity extends BaseActivity {
    String task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub);
         task= getIntent().getStringExtra("task");
        if( task.equals("RepairLogs")) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", R.id.task);
            RepairLogs repairLogs = new RepairLogs();
            repairLogs.setArguments(bundle);
            FragmentController.loadFragment(repairLogs, getSupportFragmentManager(), R.id.task, false);
        }
        else if(task.equals("InstallLogs")) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", R.id.task);
            InstallLogs installLogs = new InstallLogs();
            installLogs.setArguments(bundle);
            FragmentController.loadFragment(installLogs, getSupportFragmentManager(), R.id.task, false);
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
