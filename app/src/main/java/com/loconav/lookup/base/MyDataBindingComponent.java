package com.loconav.lookup.base;

/**
 * Created by prateek on 08/08/18.
 */

public class MyDataBindingComponent implements android.databinding.DataBindingComponent {

    @Override
    public ImageBindingAdapters getImageBindingAdapters() {
        return new ImageBindingAdapters();
    }

}
