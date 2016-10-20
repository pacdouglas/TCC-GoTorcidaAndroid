package br.com.gotorcida.gotorcida.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.AthleteActivity;
import br.com.gotorcida.gotorcida.activity.TeamActivity;
import br.com.gotorcida.gotorcida.adapter.TeamAthletesListAdapter;
import br.com.gotorcida.gotorcida.adapter.TeamNewsListAdapter;
import br.com.gotorcida.gotorcida.adapter.TeamTabAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.SLEEP_THREAD;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;
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
    ListView athletesList;
    ListView newsList;
    ArrayAdapter<JSONObject> athleteAdapter;
    ArrayAdapter<JSONObject> newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team, container, false);

        mTabLayout = (TabLayout) mView.findViewById(R.id.team_tab_layout);
        mViewPager = (ViewPager) mView.findViewById(R.id.team_viwer_pager);

        mViewPager.setAdapter(new TeamTabAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getResources().getStringArray(R.array.titles_tabs)));

        mTabLayout.setupWithViewPager(mViewPager);

        teamName = (TextView) mView.findViewById(R.id.team_textview_teamname);
        teamLogo = (ImageView) mView.findViewById(R.id.team_imageview_teamlogo);
        teamWebsite = (TextView) mView.findViewById(R.id.team_textview_teamwebsite);
        teamEmail = (TextView) mView.findViewById(R.id.team_textview_teamemail);
        teamRegistrationDate = (TextView) mView.findViewById(R.id.team_textview_teamregistrationdate);

        athletesList = (ListView) mView.findViewById(R.id.team_listview_athletes);
        newsList = (ListView) mView.findViewById(R.id.team_listview_news);

        Bundle bundle = getArguments();
        if (bundle != null) {
            getActivity().getFragmentManager().popBackStack();
        }

        String teamId = (String) bundle.get("teamId");

        LoadTeamData loadTeamData = new LoadTeamData();
        loadTeamData.execute(teamId);
        return mView;
    }


    private class LoadTeamData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            SystemClock.sleep(SLEEP_THREAD);

            String teamId = params[0].toString();

            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, teamId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }

                //JSONObject team = new JSONObject(getRequest.getMessage().getData().getString("team"));
                //teamName.setText(team.getString("name"));
                //teamWebsite.setText(team.getString("website"));
                //teamEmail.setText(team.getString("emailAddress"));
                //teamRegistrationDate.setText(team.getString("formatedRegistrationDate"));

                //qGlide.with(getActivity()).load(URL_IMAGES_BASE + team.getString("urlImage")+".png").into(teamLogo);

                getRequest = new GetRequest(URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM,
                        SaveSharedPreference.getUserName(getActivity()), teamId);

                getRequest.execute();

                JSONObject json = getRequest.getMessage().getData();

                JSONArray athletes = null;

                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), "Não foi possível carregar a lista de atletas.", Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }

                athletes = json.getJSONArray("athletes");

                ArrayList<JSONObject> list = new ArrayList<>();

                for (int i=0; i < athletes.length(); i++) {
                    try {
                        list.add(athletes.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                athleteAdapter = new TeamAthletesListAdapter(getActivity(), list);

                /*getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS, SaveSharedPreference.getUserName(getActivity()), "team", teamId);

                getRequest.execute();


                json = getRequest.getMessage().getData();

                JSONArray news = null;

                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), "Não foi possível carregar as notícias.", Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }

                news = json.getJSONArray("newsList");
                list = new ArrayList<>();

                for (int i=0; i < news.length(); i++) {
                    try {
                        list.add(news.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                newsAdapter = new TeamNewsListAdapter(getActivity(), list);
                */
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        public void onPostExecute(Object result) {
            athletesList.setAdapter(athleteAdapter);
            //newsList.setAdapter(newsAdapter);

            athletesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView athleteId = (TextView) view.findViewById(R.id.team_textview_athleteid);

                    if (athleteId != null && !athleteId.getText().toString().equals("")) {
                    }
                }
            });
        }
    }

}
