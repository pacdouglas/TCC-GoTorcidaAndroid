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

public class EventsFragment extends Fragment implements LocationListener {
    View mView;

    public String myLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_events, container, false);

        Toast.makeText(getActivity(), myLocation, Toast.LENGTH_SHORT).show();

        return mView;
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
        Toast.makeText(getActivity(), myLocation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
