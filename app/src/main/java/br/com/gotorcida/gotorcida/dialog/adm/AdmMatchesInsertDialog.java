package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.adm.ChoseLocationActivity;

import static android.app.Activity.RESULT_OK;

public class AdmMatchesInsertDialog extends DialogFragment {
    View mView;
    static final int PICK_LOCATION_REQUEST = 56;
    private String mTeamId;
    private EditText mLocation;
    private EditText mLocationCity;
    private Button mBtnLocation;
    private TextView mAddressFixed;

    private String mLatitude = null;
    private String mLongitude = null;

    TextInputLayout textInputLayout, textInputLayout2;

    public AdmMatchesInsertDialog(String teamId){
        super();
        this.mTeamId = teamId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_matches_insert, null);
        textInputLayout = (TextInputLayout) mView.findViewById(R.id.textInput1);
        textInputLayout2 = (TextInputLayout) mView.findViewById(R.id.textInput2);
        mAddressFixed = (TextView) mView.findViewById(R.id.adm_matches_insert_textview_location_address_fixed);
        mLocation = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_address);
        mLocationCity = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_city);
        mBtnLocation = (Button) mView.findViewById(R.id.adm_matches_insert_button_location);
        builder.setView(mView);

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mLocation.getText().toString()+" - "+mLocationCity.getText().toString();

                Intent it = new Intent(getContext(), ChoseLocationActivity.class);
                it.putExtra("location", location);
                startActivityForResult(it, PICK_LOCATION_REQUEST);
            }
        });

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });



        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_LOCATION_REQUEST){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                mLatitude = bundle.getString("latitude");
                mLongitude = bundle.getString("longitude");
                String address = bundle.getString("address");
                mAddressFixed.setText(address);

                mLocation.setVisibility(View.GONE);
                mLocationCity.setVisibility(View.GONE);
                textInputLayout.setVisibility(View.GONE);
                textInputLayout2.setVisibility(View.GONE);
                mBtnLocation.setVisibility(View.GONE);
                mAddressFixed.setVisibility(View.VISIBLE);
            }
        }

    }
}
