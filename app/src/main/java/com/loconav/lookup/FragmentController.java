package com.loconav.lookup;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.List;

/**
 * Created by sejal on 12-07-2018.
 */

public class FragmentController {

    private String TAG_NAME_FRAGMENT = "ACTIVITY_FRAGMENT";

    public FragmentController()
    {}


    public  void loadFragment(final Fragment fragment,FragmentManager fragmentManager,int resId,Boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(resId, fragment,"");
        if(addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }
    public  void deleteFragment(final  Fragment fragment,FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public  void replaceFragment(final Fragment fragment,FragmentManager fragmentManager,int resId,Boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(resId, fragment,"");
        if(addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    public Fragment getFragmentsStack(FragmentManager fragmentManager) {
        List<Fragment> fragmentList = fragmentManager.getFragments();
            int size = fragmentList.size();
            Fragment fragment = null;
            if(size!=0) {
                fragment = fragmentList.get(size - 1);
            }
            return fragment;
    }

    public  void deleteFragmentStack(FragmentManager fm)
    {

        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
