package br.com.gotorcida.gotorcida.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamAthletesListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;

public class TeamRosterFragment extends Fragment {
    View mView;
    ListView athletesList;
    private String teamId;

    ProgressBar progressBar;
    RelativeLayout listLayout;
    public TeamRosterFragment(String teamId) {
        this.teamId = teamId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_roster, container, false);

        athletesList = (ListView) mView.findViewById(R.id.team_roster_listview_athletes);
        progressBar = (ProgressBar) mView.findViewById(R.id.team_roster_progress);
        listLayout = (RelativeLayout) mView.findViewById(R.id.team_roster_layout);

        MakeListTeamRosterTask makeListTeamRosterTask = new MakeListTeamRosterTask();
        makeListTeamRosterTask.execute();
        return mView;
    }

    public class MakeListTeamRosterTask extends AsyncTask {
        ArrayAdapter<JSONObject> athleteAdapter;
        protected void onPreExecute() {
            listLayout.setVisibility(View.GONE);
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

            ArrayList<JSONObject> list = new ArrayList<>();
            for(int i = 0; i < athletes.length(); i++){
                try {
                    list.add(athletes.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            athleteAdapter = new TeamAthletesListAdapter(getActivity(), list);
            return null;
        }
        @Override
        public void onPostExecute(Object result) {
            athletesList.setAdapter(athleteAdapter);

            athletesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView athleteId = (TextView) mView.findViewById(R.id.team_textview_athleteid);
                    Toast.makeText(getActivity(), athleteId.getText(), Toast.LENGTH_SHORT).show();
                    if (athleteId != null && !athleteId.getText().toString().equals("")) {
                    }
                }
            });
            progressBar.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        }
    }
}