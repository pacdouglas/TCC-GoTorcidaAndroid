package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.gotorcida.gotorcida.fragment.user.MatchesTableFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamContactFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamNewsFragment;
import br.com.gotorcida.gotorcida.fragment.user.TeamRosterFragment;

/**
 * Created by dougl on 18/10/2016.
 */

public class TeamTabAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private String teamId;
    public TeamTabAdapter(FragmentManager fm, String[] tabTitles, String teamId) {
        super(fm);
        this.tabTitles = tabTitles;
        this.teamId = teamId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MatchesTableFragment(false, teamId);
            case 1:
                return new TeamRosterFragment(teamId);
            case 2:
                return new TeamNewsFragment(teamId);
            case 3:
                return new TeamContactFragment(teamId);
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
