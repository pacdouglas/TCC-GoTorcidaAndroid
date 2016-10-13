package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamsListAdapter;
import br.com.gotorcida.gotorcida.adapter.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class UserTeamsActivity extends AppCompatActivity {

    ListView listTeams;
    ArrayAdapter<JSONObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_teams);

        listTeams = (ListView) findViewById(R.id.userteams_listview_teams);

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_TEAM, SaveSharedPreference.getUserName(this), "user");
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

            if (getRequest.getMessage().getSystem().getInt("code") == 500){
                Toast.makeText(UserTeamsActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            teams = json.getJSONArray("teams");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<JSONObject> teamsList = new ArrayList<>();

        for (int i=0; i < teams.length(); i++) {
            try {
                teamsList.add(teams.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new UserTeamsListAdapter(this, teamsList);
        listTeams.setAdapter(adapter);

        listTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView teamId = (TextView) view.findViewById(R.id.userteams_textview_teamid);

                if (teamId != null && !teamId.getText().toString().equals("")) {
                    Intent it = new Intent(UserTeamsActivity.this, TeamActivity.class);
                    it.putExtra("teamId", teamId.getText().toString());
                    startActivity(it);
                }
            }
        });
    }

}
