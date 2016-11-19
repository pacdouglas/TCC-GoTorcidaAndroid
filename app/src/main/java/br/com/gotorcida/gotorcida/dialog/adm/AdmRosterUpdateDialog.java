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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_INSERT_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_AVAILABLE_ATHLETES;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_NEWS;

public class AdmRosterUpdateDialog extends DialogFragment {
    View mView;

    EditText mName;
    EditText mDateOfBirth;
    EditText mCity;
    EditText mEmail;
    EditText mWebSite;
    EditText mFacebook;
    EditText mInstagram;
    EditText mTwitter;
    Spinner mSpinnerPosition;
    ImageView mPhotoProfile;
    Button mBtnSelectPhoto;
    Button mBtnSend;
    ScrollView mLayout;
    ProgressBar mProgressBar;

    private String mTeamId;
    private ArrayList<StringWithTag> positionIds;

    public AdmRosterUpdateDialog(String teamID){
        super();
        this.mTeamId = teamID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_roster_update, null);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.dialog_adm_roster_update_progressbar);
        mLayout = (ScrollView) mView.findViewById(R.id.dialog_adm_roster_update_layout);
        mName = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_name);
        mDateOfBirth = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_date_of_birth);
        mDateOfBirth.addTextChangedListener(Mask.insert("##/##/####", mDateOfBirth));
        mCity = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_city);
        mEmail = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_email);
        mWebSite = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_website);
        mFacebook = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_facebook);
        mInstagram = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_instagram);
        mTwitter = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_twitter);
        mSpinnerPosition = (Spinner) mView.findViewById(R.id.dialog_adm_roster_update_spinner_positions);
        mPhotoProfile = (ImageView) mView.findViewById(R.id.dialog_adm_roster_update_imageview_profilephoto);

        mBtnSelectPhoto = (Button) mView.findViewById(R.id.dialog_adm_roster_update_button_selectphoto);

        builder.setTitle("Alterando atleta");
        LoadAthleteInfoTask loadAthleteInfoTask = new LoadAthleteInfoTask();
        loadAthleteInfoTask.execute();

        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                JSONObject postParameters = new JSONObject();
                try {
                    postParameters.getString("");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


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


    public class LoadAthleteInfoTask extends AsyncTask {

        private JSONArray athletes;
        private JSONArray positions;

        protected void onPreExecute() {
            mLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
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

            positionIds = new ArrayList<StringWithTag>();

            if (positions != null && athletes.length() > 0) {
                try {
                    for (int i = 0; i < positions.length(); i ++) {
                        JSONObject position = new JSONObject(positions.getString(i));
                        positionsStringList.add(position.getString("description"));
                        positionIds.add(new StringWithTag(position.getString("description"), position.getString("initials")));
                    }

                    mSpinnerPosition.setAdapter(new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            positionsStringList));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            mLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public class SaveNewsTask extends AsyncTask {

        private final boolean isUpdatingNews;
        private final JSONObject postParameters;

        public SaveNewsTask(boolean isUpdatingNews, JSONObject postParameters) {
            this.isUpdatingNews = isUpdatingNews;
            this.postParameters = postParameters;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {

            PostRequest postRequest;

            if (isUpdatingNews) {
                postRequest = new PostRequest(URL_SERVER_JSON_UPDATE_NEWS);
            } else {
                postRequest = new PostRequest(URL_SERVER_JSON_INSERT_NEWS);
            }

            postRequest.execute(postParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {

        }
    }

}
