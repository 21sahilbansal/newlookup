package com.loconav.lookup;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class FragmentBackStack extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_back_stack);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        C fragmentOne = new C();
        fragmentTransaction.add(R.id.fragmentContainer, fragmentOne, "Fragment One");
        fragmentTransaction.addToBackStack("str");
        fragmentTransaction.commit();
        FragmentController fragmentController=new FragmentController(getSupportFragmentManager(),FragmentBackStack.this);
    }

}
