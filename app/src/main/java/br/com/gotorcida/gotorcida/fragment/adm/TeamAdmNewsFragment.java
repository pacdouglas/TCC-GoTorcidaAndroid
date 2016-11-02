package br.com.gotorcida.gotorcida.fragment.adm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.gotorcida.gotorcida.R;

public class TeamAdmNewsFragment extends Fragment{
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_news, container, false);
        return mView;
    }

}
