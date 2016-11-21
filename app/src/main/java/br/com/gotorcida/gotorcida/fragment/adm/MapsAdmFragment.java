package br.com.gotorcida.gotorcida.fragment.adm;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapsAdmFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mAddress;
    private LatLng mLocation;
    public MapsAdmFragment(String address) {
        this.mAddress = address;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mLocation = getLocationFromAddress(mAddress);
        mMap.addMarker(new MarkerOptions().position(mLocation).title(mAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));

        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(mLocation, 16);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    public LatLng getLatitudeLongitudeString(){
        return this.mLocation;
    }

    public void setNewAddress(String newAddress){

        mLocation = getLocationFromAddress(newAddress);
        mMap.addMarker(new MarkerOptions().position(mLocation).title(mAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));

        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(mLocation, 16);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }


    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude()),
                     (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
