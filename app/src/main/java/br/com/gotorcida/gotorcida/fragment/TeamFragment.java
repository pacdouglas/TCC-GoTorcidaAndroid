package br.com.gotorcida.gotorcida.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamTabAdapter;

public class TeamFragment extends Fragment {
    View mView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team, container, false);

        mTabLayout = (TabLayout) mView.findViewById(R.id.team_tab_layout);
        mViewPager = (ViewPager) mView.findViewById(R.id.team_viwer_pager);

        mViewPager.setAdapter(new TeamTabAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getResources().getStringArray(R.array.titles_tabs)));

        mTabLayout.setupWithViewPager(mViewPager);
        return mView;
    }



}
