package com.example.donation.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.donation.Account;
import com.example.donation.Account_NGO;
import com.example.donation.Create_Post;
import com.example.donation.Feed;
import com.example.donation.R;
import com.example.donation.Requests;
import com.example.donation.Requests_NGO;
import com.example.donation.Search;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterNGO extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.create,R.string.tab_text_3,R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapterNGO(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Create_Post c = new Create_Post();
                return c;
            case 1:
                Requests_NGO re = new Requests_NGO();
                return re;
            case 2:
                Account_NGO a = new Account_NGO();
                return a;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 3;
    }
}