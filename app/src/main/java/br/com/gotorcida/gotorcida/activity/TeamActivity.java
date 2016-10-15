package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamAthletesListAdapter;
import br.com.gotorcida.gotorcida.adapter.TeamNewsListAdapter;
import br.com.gotorcida.gotorcida.adapter.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class TeamActivity extends AppCompatActivity {

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
            teamLogo = (ImageView) findViewById(R.id.team_imageview_teamlogo);
            teamWebsite = (TextView) findViewById(R.id.team_textview_teamwebsite);
            teamEmail = (TextView) findViewById(R.id.team_textview_teamemail);
            teamRegistrationDate = (TextView) findViewById(R.id.team_textview_teamregistrationdate);

            athletesList = (ListView) findViewById(R.id.team_listview_athletes);
            newsList = (ListView) findViewById(R.id.team_listview_news);

            teamName.setText(team.getString("name"));
            teamWebsite.setText(team.getString("website"));
            teamEmail.setText(team.getString("emailAddress"));
            teamRegistrationDate.setText(team.getString("formatedRegistrationDate"));

            Glide.with(this).load(URL_IMAGES_BASE + team.getString("urlImage")+".png").into(teamLogo);

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
                Toast.makeText(TeamActivity.this, "Não foi possível carregar a lista de atletas.", Toast.LENGTH_SHORT).show();
                return;
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

            athleteAdapter = new TeamAthletesListAdapter(this, list);
            athletesList.setAdapter(athleteAdapter);

            getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS, SaveSharedPreference.getUserName(this), "team", teamId);
            try {
                getRequest.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            json = getRequest.getMessage().getData();

            JSONArray news = null;

            if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                Toast.makeText(TeamActivity.this, "Não foi possível carregar as notícias.", Toast.LENGTH_SHORT).show();
                return;
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

            newsAdapter = new TeamNewsListAdapter(this, list);
            newsList.setAdapter(newsAdapter);

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
