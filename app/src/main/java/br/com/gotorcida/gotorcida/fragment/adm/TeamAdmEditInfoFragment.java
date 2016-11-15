package br.com.gotorcida.gotorcida.fragment.adm;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_TEAM_UPDATE;

public class TeamAdmEditInfoFragment extends Fragment{
    View mView;
    String mTeamId;
    ProgressBar progressBar;
    ScrollView layout;

    EditText mTeamName;
    EditText mTeamEmail;
    EditText mTeamWebSite;
    EditText mTeamFacebook;
    EditText mTeamTwitter;
    EditText mTeamInstagram;
    EditText mTeamCity;

    TextView mSucces;

    ImageView mTeamLogo;
    Button mSendPost;
    public TeamAdmEditInfoFragment(String teamId) {
        this.mTeamId = teamId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_edit_info, container, false);
        progressBar = (ProgressBar) mView.findViewById(R.id.adm_edit_info_progress);
        layout = (ScrollView) mView.findViewById(R.id.adm_edit_layout);
        mTeamName = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_name);
        mTeamEmail = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_email);
        mTeamWebSite = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_website);
        mTeamFacebook = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_facebook);
        mTeamTwitter = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_twitter);
        mTeamInstagram = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_instagram);
        mTeamCity = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_city);
        mTeamLogo = (ImageView) mView.findViewById(R.id.adm_edit_info_imageview_team_logo);
        mSendPost = (Button) mView.findViewById(R.id.adm_edit_info_button_send);
        mSucces = (TextView) mView.findViewById(R.id.adm_edit_info_textview_sucess);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        mSucces.setVisibility(View.GONE);

        mSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postParameters = new JSONObject();
                try {
                    postParameters.put("name", mTeamName.getText().toString());
                    postParameters.put("emailAddress", mTeamEmail.getText().toString());
                    postParameters.put("website", mTeamWebSite.getText().toString());
                    postParameters.put("facebook", mTeamFacebook.getText().toString());
                    postParameters.put("twitter", mTeamTwitter.getText().toString());
                    postParameters.put("instagram", mTeamInstagram.getText().toString());
                    postParameters.put("city", mTeamCity.getText().toString());

                    SaveInfoTask saveInfoTask = new SaveInfoTask(postParameters);
                    saveInfoTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        LoadTeamDataTask loadTeamDataTask = new LoadTeamDataTask();
        loadTeamDataTask.execute();
        return mView;
    }
    public class SaveInfoTask extends AsyncTask {

        JSONObject mPostParameters;

        public SaveInfoTask(JSONObject postParameters){
            this.mPostParameters = postParameters;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_TEAM_UPDATE+"/"+mTeamId);
            postRequest.execute(mPostParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            mSucces.setVisibility(View.VISIBLE);
        }
    }

    private class LoadTeamDataTask extends AsyncTask {

        JSONObject team;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, mTeamId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }else{
                    team = new JSONObject(getRequest.getMessage().getData().getString("team"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            try {
                mTeamName.setText(team.getString("name"));
                mTeamEmail.setText(team.getString("emailAddress"));
                mTeamWebSite.setText(team.getString("website"));
                mTeamFacebook.setText(team.getString("facebook"));
                mTeamTwitter.setText(team.getString("twitter"));
                mTeamInstagram.setText(team.getString("instagram"));
                mTeamCity.setText(team.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }
}
