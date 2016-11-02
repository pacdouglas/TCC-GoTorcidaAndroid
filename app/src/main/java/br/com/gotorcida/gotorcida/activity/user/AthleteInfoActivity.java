package br.com.gotorcida.gotorcida.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_ATHLETE;

public class AthleteInfoActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RelativeLayout layoutBody;
    TextView athleteName;
    TextView athletePosition;
    TextView athleteWebsite;
    TextView athleteEmail;
    TextView athleteRegistrationDate;
    String athleteId;

    ImageView athletePhoto;
    ImageView athleteFacebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_info);
        Bundle bundle = getIntent().getExtras();
        athleteId = bundle.getString("athleteId");
        athleteName = (TextView) findViewById(R.id.athlete_textview_name);
        athleteEmail = (TextView) findViewById(R.id.athlete_textview_teamemail);
        athletePosition = (TextView) findViewById(R.id.athlete_textview_position);
        athleteWebsite = (TextView) findViewById(R.id.athlete_textview_website);
        athleteRegistrationDate = (TextView) findViewById(R.id.athlete_textview_registrationdate);
        athletePhoto = (ImageView) findViewById(R.id.athlete_imageview_perfil_photo);
        athleteFacebook = (ImageView) findViewById(R.id.athlete_imageview_facebook);
        progressBar = (ProgressBar) findViewById(R.id.athlete_perfil_progress);
        layoutBody = (RelativeLayout) findViewById(R.id.athlete_body_layout);

        AthleteInfoTask athleteInfoTask = new AthleteInfoTask();
        athleteInfoTask.execute();
    }


    public class AthleteInfoTask extends AsyncTask{
        JSONObject athlete;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutBody.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_ATHLETE, athleteId);
            getRequest.execute();

            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(AthleteInfoActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                }else{
                    athlete = new JSONObject(getRequest.getMessage().getData().getString("athlete"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                athleteName.setText(athlete.getString("name"));
                athleteWebsite.setText(athlete.getString("website"));
                athleteEmail.setText(athlete.getString("emailAddress"));
                athleteRegistrationDate.setText(athlete.getString("formatedRegistrationDate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            layoutBody.setVisibility(View.VISIBLE);
        }
    }

}
