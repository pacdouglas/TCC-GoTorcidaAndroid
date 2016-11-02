package br.com.gotorcida.gotorcida.fragment.user;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.TeamTabAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.SLEEP_THREAD;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class TeamFragment extends Fragment {
    View mView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    ImageView teamLogo;
    TextView teamName;
    TextView teamWebsite;
    TextView teamEmail;
    TextView teamRegistrationDate;

    ProgressBar progressBar;
    RelativeLayout layoutHeader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            getActivity().getFragmentManager().popBackStack();
        }
        String teamId = (String) bundle.get("teamId");

        progressBar = (ProgressBar) mView.findViewById(R.id.team_progress);
        layoutHeader = (RelativeLayout) mView.findViewById(R.id.team_relative_header);

        mTabLayout = (TabLayout) mView.findViewById(R.id.team_tab_layout);
        mViewPager = (ViewPager) mView.findViewById(R.id.team_viwer_pager);

        mViewPager.setAdapter(new TeamTabAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getResources().getStringArray(R.array.titles_tabs), teamId));

        mTabLayout.setupWithViewPager(mViewPager);

        teamName = (TextView) mView.findViewById(R.id.team_textview_teamname);
        teamLogo = (ImageView) mView.findViewById(R.id.team_imageview_teamlogo);
        teamWebsite = (TextView) mView.findViewById(R.id.team_textview_teamwebsite);
        teamEmail = (TextView) mView.findViewById(R.id.team_textview_teamemail);
        teamRegistrationDate = (TextView) mView.findViewById(R.id.team_textview_teamregistrationdate);


        LoadTeamData loadTeamData = new LoadTeamData();
        loadTeamData.execute(teamId);
        return mView;
    }

    private class LoadTeamData extends AsyncTask {
        String sTeamName;
        String sTeamWebsite;
        String sTeamEmail;
        String sTeamRegistrationDate;

        JSONObject team;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String teamId = params[0].toString();

            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, teamId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }

                team = new JSONObject(getRequest.getMessage().getData().getString("team"));

                sTeamName = team.getString("name");
                sTeamWebsite = team.getString("website");
                sTeamEmail = team.getString("emailAddress");
                sTeamRegistrationDate = team.getString("formatedRegistrationDate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(SLEEP_THREAD);
            return null;
        }
        @Override
        public void onPostExecute(Object result) {
            try {
                Glide.with(getContext()).load(URL_IMAGES_BASE + team.getString("urlImage")+".png")
                        .into(teamLogo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            teamName.setText(sTeamName);
            teamWebsite.setText(sTeamWebsite);
            teamEmail.setText(sTeamEmail);
            teamRegistrationDate.setText(sTeamRegistrationDate);

            progressBar.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.VISIBLE);
        }
    }

}
