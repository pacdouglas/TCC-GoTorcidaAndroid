package br.com.gotorcida.gotorcida.fragment;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;

/**
 * Created by dougl on 15/10/2016.
 */

public class AboutFragment extends Fragment {
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_about, container, false);
        return mView;
    }

}
