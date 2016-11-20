package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_AVAILABLE_ATHLETES;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_SAVE_ATHLETE_ON_TEAM;

public class AdmMatchesInsertDialog extends DialogFragment {
    View mView;

    private String mTeamId;
    private EditText mLocation;
    private EditText mLocationCity;
    private Button mBtnLocation;

    public AdmMatchesInsertDialog(String teamId){
        super();
        this.mTeamId = teamId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_matches_insert, null);

        mLocation = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_address);
        mLocationCity = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_city);
        mBtnLocation = (Button) mView.findViewById(R.id.adm_matches_insert_button_location);
        builder.setView(mView);

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mLocation.getText().toString()+" - "+mLocationCity.getText().toString();
                location.replaceAll(" ", "+");

                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
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

}
