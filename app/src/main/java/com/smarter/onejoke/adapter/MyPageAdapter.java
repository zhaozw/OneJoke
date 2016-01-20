package com.smarter.onejoke.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smarter.onejoke.ui.JokeFragment;
import com.smarter.onejoke.ui.PictureFragment;

/**
 * Adapter
 * Created by panl on 15/2/7.
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    private static final int ITEMS_NUM = 2;
    private String[] tabTitle = {"每日笑话", "每日趣图"};
    Context context;
    JokeFragment jokeFragment = null;
    PictureFragment pictureFragment = null;

    public MyPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public int getCount() {
        return ITEMS_NUM;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (jokeFragment == null)
                    jokeFragment = new JokeFragment();
                return jokeFragment;
            case 1:
                if (pictureFragment == null)
                    pictureFragment = new PictureFragment();
                return pictureFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
