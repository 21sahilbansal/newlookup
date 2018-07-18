package com.loconav.lookup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class FragmentBackStack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_back_stack);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Create FragmentOne instance.
        Repair fragmentOne = new Repair();

        // Add fragment one with tag name.
        fragmentTransaction.add(R.id.fragmentContainer, fragmentOne, "Fragment One");
        fragmentTransaction.commit();
    }


    public void setActionBarTitle(String title){
         if(getSupportActionBar()!=null){
        getSupportActionBar().setTitle(title);}
        else {
             Log.e("ss","null action bar");
         }
    }
}
