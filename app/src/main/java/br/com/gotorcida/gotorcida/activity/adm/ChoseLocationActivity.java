package br.com.gotorcida.gotorcida.activity.adm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.fragment.adm.MapsAdmFragment;

public class ChoseLocationActivity extends AppCompatActivity {

    EditText mLocation;
    Button mBtnLocationAgain;
    Button mBtnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_location);
        Bundle bundle = getIntent().getExtras();
        mLocation = (EditText) findViewById(R.id.chose_location_edittext_address);
        mBtnConfirm = (Button) findViewById(R.id.chose_location_button_location_ok);
        mBtnLocationAgain =(Button) findViewById(R.id.chose_location_button_location_again);

        String location = bundle.getString("location");
        mLocation.setText(location);

        final MapsAdmFragment mapsAdmFragment = new MapsAdmFragment(location);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.chose_location_frame_map, mapsAdmFragment).commit();

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLong = mapsAdmFragment.getLatitudeLongitudeString();
                Intent itResult = new Intent();
                itResult.putExtra("latitude", Double.toString(latLong.latitude));
                itResult.putExtra("longitude", Double.toString(latLong.longitude));
                itResult.putExtra("address", mLocation.getText().toString());
                setResult(RESULT_OK, itResult);
                finish();
            }
        });

        mBtnLocationAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsAdmFragment.setNewAddress(mLocation.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }
}
