package br.com.gotorcida.gotorcida.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.gotorcida.gotorcida.R;

/**
 * Created by dougl on 15/10/2016.
 */

public class TeamRosterFragment extends Fragment {
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_roster, container, false);
        return mView;
    }

}
