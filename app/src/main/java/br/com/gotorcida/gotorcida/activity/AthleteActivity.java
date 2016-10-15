package br.com.gotorcida.gotorcida.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_ATHLETE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

public class AthleteActivity extends AppCompatActivity {

    TextView athleteName;
    TextView athleteWebsite;
    TextView athleteEmail;
    TextView athleteRegistrationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        String athleteId = (String) getIntent().getExtras().get("athleteId");

        if (athleteId == null || athleteId.equals("")) {
            finish();
            return;
        }

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_ATHLETE, athleteId);
        try {
            getRequest.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                Toast.makeText(AthleteActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject athlete = new JSONObject(getRequest.getMessage().getData().getString("athlete"));

            athleteName = (TextView) findViewById(R.id.athlete_textview_teamname);
            athleteWebsite = (TextView) findViewById(R.id.athlete_textview_teamwebsite);
            athleteEmail = (TextView) findViewById(R.id.athlete_textview_teamemail);
            athleteRegistrationDate = (TextView) findViewById(R.id.athlete_textview_registrationdate);

            athleteName.setText(athlete.getString("name"));
            athleteWebsite.setText(athlete.getString("website"));
            athleteEmail.setText(athlete.getString("emailAddress"));
            athleteRegistrationDate.setText(athlete.getString("formatedRegistrationDate"));
        } catch (JSONException ex) {

        }

    }
}
