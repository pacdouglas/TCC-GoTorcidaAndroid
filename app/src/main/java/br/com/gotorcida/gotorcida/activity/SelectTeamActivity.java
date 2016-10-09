package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);

        listTeams = (RecyclerView) findViewById(R.id.selectteams_listview_teams);

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

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        listTeams.setLayoutManager(layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_next_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_button_toolbar:
                Toast.makeText(SelectTeamActivity.this, "Faz ae sua bixa.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
