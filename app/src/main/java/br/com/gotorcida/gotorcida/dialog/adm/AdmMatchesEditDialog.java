package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.adm.ChoseLocationActivity;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_EVENT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_EVENT;

public class AdmMatchesEditDialog extends DialogFragment {
    View mView;
    private String mEventId;

    public AdmMatchesEditDialog(String eventId){
        super();
        this.mEventId = eventId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_matches_insert, null);
        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Toast.makeText(getActivity(), "Num esquece dessa tela maluco", Toast.LENGTH_LONG).show();
        return builder.create();
    }
}
