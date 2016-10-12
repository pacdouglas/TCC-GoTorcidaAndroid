package br.com.gotorcida.gotorcida.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamAthletesListAdapter;
import br.com.gotorcida.gotorcida.adapter.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class TeamActivity extends AppCompatActivity {

    TextView teamName;
    TextView teamWebsite;
    TextView teamEmail;
    ListView athletesList;
    ArrayAdapter<JSONObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        String teamId = (String) getIntent().getExtras().get("teamId");

        if (teamId == null || teamId.equals("")) {
            finish();
            return;
        }

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, teamId);
        try {
            getRequest.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                Toast.makeText(TeamActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject team = new JSONObject(getRequest.getMessage().getData().getString("team"));

            teamName = (TextView) findViewById(R.id.team_textview_teamname);
            teamWebsite = (TextView) findViewById(R.id.team_textview_teamwebsite);
            teamEmail = (TextView) findViewById(R.id.team_textview_teamemail);
            athletesList = (ListView) findViewById(R.id.team_listview_athletes);

            teamName.setText(team.getString("name"));
            teamWebsite.setText(team.getString("website"));
            teamEmail.setText(team.getString("emailAddress"));

            getRequest = new GetRequest(URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM, "1", teamId);
            try {
                getRequest.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            JSONObject json = getRequest.getMessage().getData();

            JSONArray athletes = null;

            if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                Toast.makeText(TeamActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                return;
            }

            athletes = json.getJSONArray("athletes");

            final ArrayList<JSONObject> list = new ArrayList<>();

            for (int i=0; i < athletes.length(); i++) {
                try {
                    list.add(athletes.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter = new TeamAthletesListAdapter(this, list);
            athletesList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
