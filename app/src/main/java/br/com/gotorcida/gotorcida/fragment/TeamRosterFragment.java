package br.com.gotorcida.gotorcida.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamRosterListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;

public class TeamRosterFragment extends Fragment {
    View mView;
    RecyclerView athletesList;
    private String teamId;

    ProgressBar progressBar;
    public TeamRosterFragment(String teamId) {
        this.teamId = teamId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_roster, container, false);

        athletesList = (RecyclerView) mView.findViewById(R.id.team_roster_listview_athletes);
        progressBar = (ProgressBar) mView.findViewById(R.id.team_roster_progress);

        athletesList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        MakeListTeamRosterTask makeListTeamRosterTask = new MakeListTeamRosterTask();
        makeListTeamRosterTask.execute();
        return mView;
    }

    public class MakeListTeamRosterTask extends AsyncTask  {
        ArrayList<JSONObject> listAthlete;
        protected void onPreExecute() {
            athletesList.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM,
                    SaveSharedPreference.getUserName(getActivity())
                    , teamId);
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            JSONArray athletes = null;
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), "Não foi possível carregar a lista de atletas.", Toast.LENGTH_SHORT).show();
                }else{
                    athletes = json.getJSONArray("athletes");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listAthlete = new ArrayList<>();
            for(int i = 0; i < athletes.length(); i++){
                try {
                    listAthlete.add(athletes.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        public void onPostExecute(Object result) {
            athletesList.setHasFixedSize(true);
            TeamRosterListAdapter adapter = new TeamRosterListAdapter(listAthlete, getActivity().getBaseContext());

            RecyclerView.LayoutManager layout = new StaggeredGridLayoutManager(2, 1);
            athletesList.setLayoutManager(layout);
            athletesList.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            athletesList.setVisibility(View.VISIBLE);
        }
    }
}