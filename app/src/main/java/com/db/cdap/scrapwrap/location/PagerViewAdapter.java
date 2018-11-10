package com.db.cdap.scrapwrap.location;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by panalk on 9/26/2018.
 */

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                UsersFragment usersFragment = new UsersFragment();
                return usersFragment;

            case 1:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //No of tabs
        return 2;
    }
}
