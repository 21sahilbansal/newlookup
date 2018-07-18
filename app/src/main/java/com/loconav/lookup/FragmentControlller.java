package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import java.util.List;

/**
 * Created by sejal on 12-07-2018.
 */

public class FragmentControlller {

    public final static String TAG_NAME_FRAGMENT = "ACTIVITY_FRAGMENT";
    FragmentBackStack fragmentBackStack=new FragmentBackStack();

    // Get exist Fragment by it's tag name.
    public static Fragment getFragmentByTagName(FragmentManager fragmentManager, String fragmentTagName)
    {
        Fragment ret = null;

        // Get all Fragment list.
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if(fragmentList!=null)
        {
            int size = fragmentList.size();
            for(int i=0;i<size;i++)
            {
                Fragment fragment = fragmentList.get(i);

                if(fragment!=null) {
                    String fragmentTag = fragment.getTag();

                    // If Fragment tag name is equal then return it.
                    if (fragmentTag.equals(fragmentTagName)) {
                        ret = fragment;
                    }
                }
            }
        }

        return ret;
    }

    public FragmentControlller(final FragmentManager fragmentManager, final Activity activity, final Context context){

        fragmentManager.addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        public void onBackStackChanged() {
                            if (activity != null) {
                                Log.e("sss",""+activity.toString() );
                            }else{
                                Log.e("as","sdfrf activity is null");
                            }
                            //
                           Fragment fragment= getFragmentsStack(fragmentManager);
                            if(fragment!=null) {
                                if(fragment instanceof titleFragment) {
                            //        Log.e("sss",""+((titleFragment) fragment).getTitle() );
                                    //(FragmentBackStack)fragment.getActivity().setActionBarTitle(((titleFragment) fragment).getTitle());

                              //      fragmentBackStack.setActionBarTitle(((titleFragment) fragment).getTitle());
                                }
                            }
                        }
                    });

        }

    public static void loadFragment(final Fragment fragment,FragmentManager fragmentManager) {

        // create repair transaction for transition here
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // put the fragment in place
        transaction.replace(R.id.fragmentContainer, fragment);

        // this is the part that will cause repair fragment to be added to backstack,
        // this way we can return to it at any time using this tag
        transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();
    }
    private Fragment getFragmentsStack(FragmentManager fragmentManager) {
        List<Fragment> fragmentList = fragmentManager.getFragments();
            int size = fragmentList.size();
            Fragment fragment = null;
            if(size!=0) {
                fragment = fragmentList.get(size - 1);
            }
            return fragment;
    }
}
