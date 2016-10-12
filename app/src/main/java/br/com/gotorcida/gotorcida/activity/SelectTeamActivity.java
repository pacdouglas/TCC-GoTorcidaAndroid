package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamsListAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_DASHBOARD_SAVECONFIG;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_LOGIN;

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
                String userId = "1";
                String selectedSports = getIntent().getExtras().getString("selectedSports");

                JSONArray selectedTeams = new JSONArray();

                for (int i = 0; i < listTeams.getChildCount(); i++) {
                    View row = listTeams.getChildAt(i);

                    CheckBox cbTeam = (CheckBox) row.findViewById(R.id.selectteam_checkbox_teamname);

                    if (cbTeam.isChecked()) {
                        TextView teamId = (TextView) row.findViewById(R.id.selectteam_textview_teamid);
                        selectedTeams.put(teamId.getText());
                    }
                }

                if (selectedTeams.length() > 0) {
                    PostRequest postRequest = new PostRequest(URL_SERVER_DASHBOARD_SAVECONFIG);

                    JSONObject postParameters = new JSONObject();
                    try {
                        postParameters.put("userId", userId);
                        postParameters.put("sports", selectedSports);
                        postParameters.put("teams", selectedTeams.toString());

                        postRequest.execute(postParameters.toString()).get();

                        if (postRequest.getMessage().getSystem().get("code").equals(200)) {
                            Intent intent = new Intent(SelectTeamActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SelectTeamActivity.this, postRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SelectTeamActivity.this, "É necessário escolher ao menos um time.", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
