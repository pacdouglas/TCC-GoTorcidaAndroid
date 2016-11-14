package br.com.gotorcida.gotorcida.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.TeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_DASHBOARD_SAVECONFIG;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class SelectTeamActivity extends AppCompatActivity {

    RecyclerView listTeams;
    Bundle bundle;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);
        listTeams = (RecyclerView) findViewById(R.id.selectteams_listview_teams);
        progressBar = (ProgressBar) findViewById(R.id.select_teams_progress);
        bundle = getIntent().getExtras();

        MakeListTeamsTask makeListTeamsTask = new MakeListTeamsTask();
        makeListTeamsTask.execute();
    }

    public class MakeListTeamsTask extends AsyncTask {
        ArrayList<JSONObject> teamsList;
        protected void onPreExecute() {
            listTeams.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object... args) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, "1",
                                                        bundle.getString("selectedSports"));
            getRequest.execute();

            JSONObject json = getRequest.getMessage().getData();

            JSONArray teams = null;
            try {
                teams = json.getJSONArray("teams");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teamsList = new ArrayList<>();

            for (int i=0; i < teams.length(); i++) {
                try {
                    JSONArray array = teams.getJSONArray(i);

                    for (int j = 0; j < array.length(); j++) {
                        array.getJSONObject(i).put("isChecked", false);
                        teamsList.add(array.getJSONObject(j));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            listTeams.setAdapter(new TeamsListAdapter(teamsList, getBaseContext()));

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getBaseContext());
            listTeams.setLayoutManager(layout);

            listTeams.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

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
                String userId = SaveSharedPreference.getUserName(this);
                String selectedSports = bundle.getString("selectedSports");

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

                        SaveDashboardOptionsTask saveDashboardOptionsTask = new SaveDashboardOptionsTask(postParameters);
                        saveDashboardOptionsTask.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SelectTeamActivity.this, "É necessário escolher ao menos um time.", Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    public class SaveDashboardOptionsTask extends AsyncTask {

        private final JSONObject postParameters;

        public SaveDashboardOptionsTask(JSONObject postParameters){
            this.postParameters = postParameters;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest = new PostRequest(URL_SERVER_DASHBOARD_SAVECONFIG);
            postRequest.execute(postParameters.toString());

            try {
                if (postRequest.getMessage().getSystem().get("code").equals(200)) {
                    SaveSharedPreference.setUserName(SelectTeamActivity.this, postParameters.getString("userId"));
                    Intent intent = new Intent(SelectTeamActivity.this, HomeUserActivity.class);
                    startActivity(intent);
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    Toast.makeText(SelectTeamActivity.this, postRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException ex) {

            }
            return null;
        }
    }
}
