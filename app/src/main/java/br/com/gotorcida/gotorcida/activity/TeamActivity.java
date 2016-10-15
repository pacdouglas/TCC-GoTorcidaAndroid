package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class TeamActivity extends AppCompatActivity {

    TextView teamName;
    TextView teamWebsite;
    TextView teamEmail;
    TextView teamRegistrationDate;
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
            teamRegistrationDate = (TextView) findViewById(R.id.team_textview_teamregistrationdate);

            athletesList = (ListView) findViewById(R.id.team_listview_athletes);

            teamName.setText(team.getString("name"));
            teamWebsite.setText(team.getString("website"));
            teamEmail.setText(team.getString("emailAddress"));
            teamRegistrationDate.setText(team.getString("formatedRegistrationDate"));

            getRequest = new GetRequest(URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM, SaveSharedPreference.getUserName(this), teamId);
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

            athletesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView athleteId = (TextView) view.findViewById(R.id.team_textview_athleteid);

                    if (athleteId != null && !athleteId.getText().toString().equals("")) {
                        Intent it = new Intent(TeamActivity.this, AthleteActivity.class);
                        it.putExtra("athleteId", athleteId.getText().toString());
                        startActivity(it);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
