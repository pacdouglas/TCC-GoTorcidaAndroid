package br.com.gotorcida.gotorcida.dialog.adm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.adm.ChoseLocationActivity;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_EVENT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_EVENT;

@SuppressLint("ValidFragment")
public class AdmMatchesEditDialog extends DialogFragment {
    View mView;
    static final int PICK_LOCATION_REQUEST = 56;
    private EditText mLocation;
    private EditText mLocationCity;
    private EditText mNameEvent;
    private EditText mDate;
    private EditText mHour;
    private EditText mCost;
    private EditText mDescription;
    private Spinner mFirstTeam;
    private Spinner mSecondTeam;
    private Button mBtnLocation;
    private TextView mAddressFixed;

    private String mLatitude = null;
    private String mLongitude = null;

    TextInputLayout textInputLayout, textInputLayout2;
    private String mEventId;
    private String mFirstTeamId;
    private String mSecondTeamId;
    private ProgressBar mProgressBar;
    private ScrollView mLayout;

    public AdmMatchesEditDialog(String eventId) {
        super();
        this.mEventId = eventId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_matches_insert, null);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.dialog_adm_matches_insert_progressbar);
        mLayout = (ScrollView) mView.findViewById(R.id.dialog_adm_matches_insert_layout);
        textInputLayout = (TextInputLayout) mView.findViewById(R.id.textInput1);
        textInputLayout2 = (TextInputLayout) mView.findViewById(R.id.textInput2);
        mAddressFixed = (TextView) mView.findViewById(R.id.adm_matches_insert_textview_location_address_fixed);
        mLocation = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_address);
        mLocationCity = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_location_city);
        mNameEvent = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_title);
        mDate = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_date);
        mDate.addTextChangedListener(Mask.insert("##/##/####", mDate));
        mHour = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_hour);
        mCost = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_cost);
        mHour.addTextChangedListener(Mask.insert("##:##", mHour));
        mDescription = (EditText) mView.findViewById(R.id.adm_matches_insert_edittext_description);
        mFirstTeam = (Spinner) mView.findViewById(R.id.adm_matches_insert_spinner_firt_team);
        mSecondTeam = (Spinner) mView.findViewById(R.id.adm_matches_insert_spinner_second_team);
        mBtnLocation = (Button) mView.findViewById(R.id.adm_matches_insert_button_location);
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

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation.getText().toString().isEmpty() || mLocationCity.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Preencha o endereço e a cidade!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String location = mLocation.getText().toString() + " - " + mLocationCity.getText().toString();
                Intent it = new Intent(getContext(), ChoseLocationActivity.class);
                it.putExtra("location", location);
                startActivityForResult(it, PICK_LOCATION_REQUEST);
            }
        });
        LoadEventInfoTask loadEventInfoTask = new LoadEventInfoTask();
        loadEventInfoTask.execute();
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();

        if (d != null) {
            final Button positiveButton  = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringWithTag firstTeamId = (StringWithTag) mFirstTeam.getSelectedItem();
                    StringWithTag secondTeamId = (StringWithTag) mSecondTeam.getSelectedItem();

                    if (!firstTeamId.getTag().toString().equals(mFirstTeamId) && !secondTeamId.getTag().toString().equals(mFirstTeamId)) {
                        Toast.makeText(mView.getContext(), "Você só pode criar partidas em que sua equipe participe.", Toast.LENGTH_LONG).show();
                        return;
                    } else if (firstTeamId.getTag().toString().equals(secondTeamId.getTag().toString())) {
                        Toast.makeText(mView.getContext(), "Escolha equipes diferentes.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (CollectionUtils.ValidateFields(getContext(), mNameEvent, mDate, mHour, mCost)) {
                        if (mAddressFixed.getVisibility() == View.VISIBLE) {
                            positiveButton.setEnabled(false);
                            JSONObject parameters = new JSONObject();
                            try {
                                parameters.put("eventOwner", SaveSharedPreference.getUserName(getContext()));
                                parameters.put("name", mNameEvent.getText().toString());
                                parameters.put("date", mDate.getText().toString());
                                parameters.put("time", mHour.getText().toString());
                                parameters.put("costs", mCost.getText().toString());
                                parameters.put("location", mLocation.getText().toString() + ":" + mLocationCity.getText().toString());
                                parameters.put("description", mDescription.getText().toString());
                                parameters.put("firstTeam", firstTeamId.getTag().toString());
                                parameters.put("secondTeam", secondTeamId.getTag().toString());
                                parameters.put("latitude", mLatitude);
                                parameters.put("longitude", mLongitude);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            UpdateEventTask updateEventTask = new UpdateEventTask(parameters);
                            updateEventTask.execute();
                        } else {
                            Toast.makeText(getContext(), "É necessário confirmar o endereço do evento.", Toast.LENGTH_SHORT).show();
                        }
                        Fragment fragment = getTargetFragment();
                        Integer targetRequestCode = getTargetRequestCode();
                        Activity activity = getActivity();
                        Intent intent = activity.getIntent();
                        fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent);
                    }
                }
            });
        }
    }

    public class UpdateEventTask extends AsyncTask {

        private final JSONObject postParameters;

        public UpdateEventTask(JSONObject postParameters) {
            this.postParameters = postParameters;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_UPDATE_EVENT + "/" + mEventId);
            postRequest.execute(postParameters.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            AdmMatchesEditDialog.this.dismiss();
        }
    }

    public class LoadEventInfoTask extends AsyncTask {
        boolean success;
        String date = "";
        String hour = "";
        String address = "";
        String city = "";
        String cost = "";
        String description = "";
        String title = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            success = true;
            mProgressBar.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_EVENT, mEventId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().get("code").equals(200)) {
                    JSONObject requestResult = getRequest.getMessage().getData();

                    JSONObject event = new JSONObject(requestResult.getString("event"));
                    JSONObject firstTeam = new JSONObject(event.getString("firstTeam"));
                    JSONObject secondTeam = new JSONObject(event.getString("secondTeam"));
                    mFirstTeamId = firstTeam.getString("id");
                    mSecondTeamId = secondTeam.getString("id");
                    mLatitude = event.getString("latitude");
                    mLongitude = event.getString("longitude");
                    date = event.getString("formatedEventDate");
                    hour = event.getString("time");
                    address = event.getString("location");
                    cost = event.getString("costs");
                    city = address.substring(address.indexOf(":")+1, address.length());
                    address = address.substring(0, address.indexOf(":"));
                    description = event.getString("description");
                    title = event.getString("name");

                } else {
                    success = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (success) {
                mLocation.setText(address);
                mLocationCity.setText(city);
                mNameEvent.setText(title);
                mDate.setText(date);
                mHour.setText(hour);
                mCost.setText(cost);
                mDescription.setText(description);
                LoadSpinnersTeams loadSpinnersTeams = new LoadSpinnersTeams();
                loadSpinnersTeams.execute();
            } else {
                Toast.makeText(getContext(), "Erro Interno", Toast.LENGTH_SHORT).show();
                AdmMatchesEditDialog.this.dismiss();
            }
        }
    }

    public class LoadSpinnersTeams extends AsyncTask {
        ArrayList<StringWithTag> teamsList;
        int positionSelectedSpinnerFirstTeam = 0;
        int positionSelectedSpinnerSecondTeam = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            String sportId = null;
            GetRequest getRequestSportId = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, mFirstTeamId);
            getRequestSportId.execute();
            try {
                String aux = getRequestSportId.getMessage().getData().getString("team");
                JSONObject auxJson = new JSONObject(aux);
                sportId = "[" + auxJson.getJSONObject("sport").getString("id") + "]";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, "1",
                    sportId);
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();

            JSONArray teams = null;
            try {
                teams = json.getJSONArray("teams");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teamsList = new ArrayList<>();
            JSONObject object;
            StringWithTag stringWithTag = null;

            for (int i = 0; i < teams.length(); i++) {
                try {
                    JSONArray array = teams.getJSONArray(i);
                    for (int j = 0; j < array.length(); j++) {
                        object = new JSONObject(array.getString(j));
                        stringWithTag = new StringWithTag(object.getString("name"), object.getString("id"));
                        teamsList.add(stringWithTag);
                        if (stringWithTag.getTag().toString().equals(mFirstTeamId)) {
                            positionSelectedSpinnerFirstTeam = j;
                        }
                        if (stringWithTag.getTag().toString().equals(mSecondTeamId)) {
                            positionSelectedSpinnerSecondTeam = j;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayAdapter<StringWithTag> adapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_spinner_dropdown_item, teamsList);
            mFirstTeam.setAdapter(adapter);
            mSecondTeam.setAdapter(adapter);
            mFirstTeam.setSelection(positionSelectedSpinnerFirstTeam);
            mSecondTeam.setSelection(positionSelectedSpinnerSecondTeam);

            mProgressBar.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        }
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
