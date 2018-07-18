package com.loconav.lookup;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class lookupSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Create FragmentOne instance.
        Repair fragmentRepair = new Repair();
        // Add fragment one with tag name.
        fragmentTransaction.add(R.id.fragmentContainerSub, fragmentRepair, "Fragment One");
        fragmentTransaction.commit();
    }
}
