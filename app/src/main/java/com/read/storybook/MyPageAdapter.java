package com.read.storybook;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.read.storybook.util.AppCache;

import java.util.HashMap;
import java.util.List;

class MyPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private Fragment firstPage;
    FragmentManager fm;
    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        if(position == 3){
            AppCache.getInstance().setPageOneDestroyed(true);
        }
        return this.fragments.get(position);
    }
    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((PageStoryFragment)object).destroyAudio();
        super.destroyItem(container, position, object);
    }


}