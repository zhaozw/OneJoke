package com.smarter.onejoke.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smarter.onejoke.ui.JokeFragment;
import com.smarter.onejoke.ui.PictureFragment;
import com.smarter.onejoke.utils.MyApplication;

/**
 * Created by panl on 15/2/7.
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    private static final int ITEMS_NUM = 2;

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return ITEMS_NUM;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(MyApplication.getContext(), JokeFragment.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(MyApplication.getContext(), PictureFragment.class.getName());
                break;
        }
        return fragment;
    }
}
