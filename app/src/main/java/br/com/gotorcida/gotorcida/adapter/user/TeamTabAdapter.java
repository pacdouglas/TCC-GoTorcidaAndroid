package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.gotorcida.gotorcida.fragment.user.MatchesTableFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamContactFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamNewsFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamRosterFragment;

public class TeamTabAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private String teamId;
    private FragmentManager fm;

    public TeamTabAdapter(FragmentManager fm, String[] tabTitles, String teamId) {
        super(fm);
        this.fm = fm;
        this.tabTitles = tabTitles;
        this.teamId = teamId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MatchesTableFragment(false, teamId);
                break;

            case 1:
                fragment = new TeamRosterFragment(teamId);
                break;

            case 2:
                fragment = new TeamNewsFragment(teamId);
                break;

            case 3:
                fragment = new TeamContactFragment(teamId);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return this.tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }
}
