package br.com.gotorcida.gotorcida.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_ATHLETE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class AthleteActivity extends AppCompatActivity {

    TextView athleteName;
    TextView athleteWebsite;
    TextView athleteEmail;
    TextView athleteRegistrationDate;
    String athleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        athleteName = (TextView) findViewById(R.id.athlete_textview_teamname);
        athleteWebsite = (TextView) findViewById(R.id.athlete_textview_teamwebsite);
        athleteEmail = (TextView) findViewById(R.id.athlete_textview_teamemail);
        athleteRegistrationDate = (TextView) findViewById(R.id.athlete_textview_registrationdate);

        athleteId = (String) getIntent().getExtras().get("athleteId");

        if (athleteId == null || athleteId.equals("")) {
            finish();
            return;
        }

        AthleteInfoTask athleteInfoTask = new AthleteInfoTask();
        athleteInfoTask.execute();
    }

    public class AthleteInfoTask extends AsyncTask {
        JSONObject athlete;

        @Override
        protected void onPreExecute() {
                // todo: VAI COMEÇA O JOGO! NAO ESQUECER DE POR O PRORESSBAR
        }
        @Override
        protected Object doInBackground(Object... args) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_ATHLETE, athleteId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(AthleteActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                athlete = new JSONObject(getRequest.getMessage().getData().getString("athlete"));
            } catch (JSONException ex) {

            }
            return true;
        }
        @Override
        public void onPostExecute(Object result) {
            try {
                athleteName.setText(athlete.getString("name"));
                athleteWebsite.setText(athlete.getString("website"));
                athleteEmail.setText(athlete.getString("emailAddress"));
                athleteRegistrationDate.setText(athlete.getString("formatedRegistrationDate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
