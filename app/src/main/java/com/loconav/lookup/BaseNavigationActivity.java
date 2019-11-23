package com.loconav.lookup;

import android.os.Bundle;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.tutorial.View.TutorialFragment;

import java.util.Objects;

import static com.loconav.lookup.Constants.FRAGMENT_NAME;

//This activity is used to open RepairLogsFragment and RepairDetailFragment
public class BaseNavigationActivity extends BaseActivity {
    private final FragmentController fragmentController=new FragmentController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_repair);
        Bundle bundle=getIntent().getExtras();
        String fragmentName= Objects.requireNonNull(bundle).getString(FRAGMENT_NAME);
        if(fragmentName.equals(getString(R.string.install_log_fragment))) {
            InstallLogsFragment installLogsFragment=new InstallLogsFragment();
            setTitle(getString(R.string.install_log_heading));
            fragmentController.loadFragment(installLogsFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }
        else if(fragmentName.equals(getString(R.string.repair_log_fragment)))
        {
            RepairLogsFragment repairLogsFragment=new RepairLogsFragment();
            setTitle(getString(R.string.repair_log_heading));
            fragmentController.loadFragment(repairLogsFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }
        else if(fragmentName.equals(getString(R.string.screenshot_fragment)))
        {
            ScreenshotFragment screenshotFragment=new ScreenshotFragment();
            setTitle(getString(R.string.screenshot_heading));
            fragmentController.loadFragment(screenshotFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }
        else if(fragmentName.equals(getString(R.string.user_profile_fragment)))
        {
            UserProfileFragment userProfileFragment=new UserProfileFragment();
            setTitle(getString(R.string.user_profile_heading));
            fragmentController.loadFragment(userProfileFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }
        else if(fragmentName.equals(getString(R.string.upload_documents_fragment)))
        {
            UploadDocumentsFragment uploadDocumentsFragment=new UploadDocumentsFragment();
            setTitle(getString(R.string.upload_documents_heading));
            fragmentController.loadFragment(uploadDocumentsFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }
        else if(fragmentName.equals("Tutorials Fragment")){
            TutorialFragment tutorialFragment = new TutorialFragment();
            setTitle("Tutorials");
            fragmentController.loadFragment(tutorialFragment,getSupportFragmentManager(),R.id.fragment_host,false);
        }

    }

    @Override
    public boolean showBackButton() {
        return true;
    }
}
