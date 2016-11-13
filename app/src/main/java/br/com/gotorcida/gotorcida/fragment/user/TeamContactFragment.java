package br.com.gotorcida.gotorcida.fragment.user;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class TeamContactFragment extends Fragment {

    View mView;
    TextView mTeamWebSite;
    TextView mTeamEmail;
    TextView mTeamCity;
    ImageView mTeamFacebook;
    ImageView mTeamTwitter;
    ImageView mTeamInstagram;
    private String mTeamId;

    public TeamContactFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_contact, container, false);

        mTeamWebSite = (TextView) mView.findViewById(R.id.team_contact_textview_website);
        mTeamEmail = (TextView) mView.findViewById(R.id.team_contact_textview_email);
        mTeamCity = (TextView) mView.findViewById(R.id.team_contact_textview_city);
        mTeamFacebook = (ImageView) mView.findViewById(R.id.team_contact_imageview_facebook);
        mTeamTwitter = (ImageView) mView.findViewById(R.id.team_contact_imageview_twitter);
        mTeamInstagram = (ImageView) mView.findViewById(R.id.team_contact_imageview_instagram);

        LoadTeamData loadTeamData = new LoadTeamData();
        loadTeamData.execute();
        return mView;
    }

    private class LoadTeamData extends AsyncTask {
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
                mTeamWebSite.append(team.getString("website"));
                Linkify.addLinks(mTeamWebSite, Linkify.WEB_URLS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mTeamEmail.append(team.getString("emailAddress"));
                Linkify.addLinks(mTeamEmail, Linkify.EMAIL_ADDRESSES);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
