package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_INSERT_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_AVAILABLE_ATHLETES;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_NEWS;
import static java.lang.System.in;

public class AdmRosterInsertDialog extends DialogFragment {
    View mView;

    private Spinner spinnerAthlete;
    private Spinner spinnerPosition;
    private TextView textViewNumber;
    private ProgressBar progressBar;
    private LinearLayout form;

    private String mTeamId;

    public AdmRosterInsertDialog(String teamId){
        super();
        this.mTeamId = teamId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_roster_insert, null);

        progressBar = (ProgressBar) mView.findViewById(R.id.dialog_adm_roster_insert_progressbar);
        form = (LinearLayout) mView.findViewById(R.id.dialog_adm_roster_insert_form);

        spinnerAthlete = (Spinner) mView.findViewById(R.id.dialog_adm_roster_insert_spinner_athletes);
        spinnerPosition = (Spinner) mView.findViewById(R.id.dialog_adm_roster_insert_spinner_position);
        textViewNumber = (TextView) mView.findViewById(R.id.dialog_adm_roster_insert_textview_number);

        builder.setTitle("Novo atleta no time:");
        LoadNewsTask loadNewsTask = new LoadNewsTask();
        loadNewsTask.execute(mTeamId);

        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                /*JSONObject postParameters = new JSONObject();
                try {
                    if (!newsID.equals("")){
                        postParameters.put("id", newsID);
                    }

                    postParameters.put("title", newsTitle.getText().toString());
                    postParameters.put("description", newsDescription.getText().toString());
                    postParameters.put("date", newsDate.getText().toString());
                    postParameters.put("user", SaveSharedPreference.getUserName(getContext()));
                    postParameters.put("teamId", teamID);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                SaveNewsTask saveNewsTask = new SaveNewsTask(updating, postParameters);
                saveNewsTask.execute();
                */

                Fragment fragment = getTargetFragment();
                Integer targetRequestCode = getTargetRequestCode();

                Activity activity = getActivity();
                Intent intent = activity.getIntent();

                fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fragment = getTargetFragment();
                Integer targetRequestCode = getTargetRequestCode();

                Activity activity = getActivity();
                Intent intent = activity.getIntent();

                fragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, intent);
            }
        });

        return builder.create();
    }


    public class LoadNewsTask extends AsyncTask {

        private JSONArray athletes;
        private JSONArray positions;

        protected void onPreExecute() {
            form.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_AVAILABLE_ATHLETES, params[0].toString());
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getContext(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.athletes = json.getJSONArray("athletes");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getRequest = new GetRequest(URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT, params[0].toString());
            getRequest.execute();
            json = getRequest.getMessage().getData();
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getContext(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.positions = json.getJSONArray("positions");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Object result) {

            ArrayList<String> athleteStringList = new ArrayList<String>();
            ArrayList<String> positionsStringList = new ArrayList<String>();

            if (athletes != null && athletes.length() > 0) {
                try {
                    for (int i = 0; i < athletes.length(); i ++) {
                        athleteStringList.add(new JSONObject(athletes.getString(i)).getString("name"));
                    }

                    spinnerAthlete.setAdapter(new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    athleteStringList));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            if (positions != null && athletes.length() > 0) {
                try {
                    for (int i = 0; i < positions.length(); i ++) {
                        positionsStringList.add(new JSONObject(positions.getString(i)).getString("description"));
                    }

                    spinnerPosition.setAdapter(new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            positionsStringList));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            form.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public class SaveAthleteTask extends AsyncTask {

        private final JSONObject postParameters;

        public SaveAthleteTask(JSONObject postParameters) {
            this.postParameters = postParameters;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {

            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_UPDATE_NEWS);
            postRequest.execute(postParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {

        }
    }

}
