package br.com.gotorcida.gotorcida.fragment;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.gotorcida.gotorcida.R;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private String mTitleMark;

    public MapsFragment(double latitude, double longitude, String titleMark) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        if(titleMark != null){
            this.mTitleMark = titleMark;
        }else{
            this.mTitleMark = "GoTorcida!";
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(location).title(mTitleMark));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(location, 16);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        //TODO: Vai ser Util p parte do Adm: !! ! ! ! ! !
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
        //ou google.navigation:q=a+street+address
        //   google.navigation:q=latitude,longitude
        //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        //mapIntent.setPackage("com.google.android.apps.maps");
        //startActivity(mapIntent);

    }
}
