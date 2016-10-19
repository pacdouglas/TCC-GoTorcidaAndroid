package br.com.gotorcida.gotorcida.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.gotorcida.gotorcida.fragment.TeamContactFragment;
import br.com.gotorcida.gotorcida.fragment.TeamMatchesFragment;
import br.com.gotorcida.gotorcida.fragment.TeamNewsFragment;
import br.com.gotorcida.gotorcida.fragment.TeamRosterFragment;

/**
 * Created by dougl on 18/10/2016.
 */

public class TeamTabAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    public TeamTabAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TeamMatchesFragment();
            case 1:
                return new TeamRosterFragment();
            case 2:
                return new TeamNewsFragment();
            case 3:
                return new TeamContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }
}
