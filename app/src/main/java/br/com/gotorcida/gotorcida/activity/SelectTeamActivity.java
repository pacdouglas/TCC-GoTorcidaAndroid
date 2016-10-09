package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamsListAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class SelectTeamActivity extends AppCompatActivity {

    RecyclerView listTeams;
    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);

        listTeams = (RecyclerView) findViewById(R.id.selectteams_listview_teams);
        buttonOK = (Button) findViewById(R.id.selectteams_button_ok);

        Bundle bundle = getIntent().getExtras();

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, "1", bundle.getString("selectedSports"));
        try {
            getRequest.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject json = getRequest.getMessage().getData();

        JSONArray teams = null;
        try {
            teams = json.getJSONArray("teams");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<JSONObject> teamsList = new ArrayList<>();

        for (int i=0; i < teams.length(); i++) {
            try {
                JSONArray array = teams.getJSONArray(i);

                for (int j = 0; j < array.length(); j++) {
                    teamsList.add(array.getJSONObject(j));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listTeams.setAdapter(new TeamsListAdapter(teamsList, this));

        RecyclerView.LayoutManager layout = new GridLayoutManager(SelectTeamActivity.this, 2);
        listTeams.setLayoutManager(layout);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
