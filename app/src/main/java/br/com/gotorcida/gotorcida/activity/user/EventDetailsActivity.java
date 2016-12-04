package br.com.gotorcida.gotorcida.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.fragment.user.MapsFragment;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class EventDetailsActivity extends AppCompatActivity {

    String mEventId;
    ProgressBar progressBar;
    LinearLayout mLayout;
    TextView mFirstTeamName;
    TextView mFirstTeamScore;
    ImageView mFirstTeamLogo;

    TextView mSecondTeamName;
    TextView mSecondTeamScore;
    ImageView mSecondTeamLogo;

    TextView mAddress;
    TextView mCost;
    TextView mDate;
    TextView mTitle;
    TextView mDescription;

    String mLatitude, mLongitude;
    Button mBtnRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Bundle bundle = getIntent().getExtras();
        mEventId = bundle.getString("eventId");

        progressBar = (ProgressBar) findViewById(R.id.event_details_progress);
        mLayout = (LinearLayout) findViewById(R.id.event_details_layout);

        mFirstTeamName = (TextView) findViewById(R.id.event_details_textview_firstTeamName);
        mFirstTeamScore = (TextView) findViewById(R.id.event_details_textview_scoreFirstTeam);
        mFirstTeamLogo = (ImageView) findViewById(R.id.event_details_imageview_firstTeam);

        mSecondTeamName = (TextView) findViewById(R.id.event_details_textview_secondTeamName);
        mSecondTeamScore = (TextView) findViewById(R.id.event_details_textview_scoreSecondTeam);
        mSecondTeamLogo = (ImageView) findViewById(R.id.event_details_imageview_secondTeam);

        mTitle = (TextView) findViewById(R.id.event_details_textview_title);
        mDescription = (TextView) findViewById(R.id.event_details_textview_description);
        mAddress = (TextView) findViewById(R.id.event_details_textview_address);
        mCost = (TextView) findViewById(R.id.event_details_textview_cost);
        mDate = (TextView) findViewById(R.id.event_details_textview_eventDate);
        mBtnRoute = (Button) findViewById(R.id.event_details_button_route);

        mBtnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+mLatitude+","+mLongitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        EventDetailsTask eventDetailsTask = new EventDetailsTask();
        eventDetailsTask.execute();
    }

    public class EventDetailsTask extends AsyncTask {
        String firstTeamName = "";
        String secondTeamName = "";
        String urlFirstTeam = "";
        String urlSecondTeam = "";
        String scoreFirstTeam = "";
        String scoreSecondTeam = "";
        String date = "";
        String address = "";
        double latitude = 0;
        double longitude = 0;
        String description = "";
        String title = "";
        String costs = "";
        boolean success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
            success = true;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest;
            getRequest = new GetRequest(Constants.URL_SERVER_JSON_FIND_EVENT, mEventId);
            getRequest.execute();
            JSONObject requestResult = getRequest.getMessage().getData();

            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    success = false;
                } else {
                    JSONObject event = new JSONObject(requestResult.getString("event"));
                    JSONObject firstTeam = new JSONObject(event.getString("firstTeam"));
                    JSONObject secondTeam = new JSONObject(event.getString("secondTeam"));
                    JSONObject eventResult = new JSONObject(requestResult.getString("eventResult"));
                    firstTeamName = firstTeam.getString("name");
                    urlFirstTeam = firstTeam.getString("urlImage");
                    scoreFirstTeam = eventResult.getString("firstTeamScore");
                    scoreFirstTeam = scoreFirstTeam.substring(0, scoreFirstTeam.indexOf("."));
                    if(scoreFirstTeam.equals("-1")){
                        scoreFirstTeam = "?";
                    }
                    secondTeamName = secondTeam.getString("name");
                    urlSecondTeam = secondTeam.getString("urlImage");
                    scoreSecondTeam = eventResult.getString("secondTeamScore");
                    scoreSecondTeam = scoreSecondTeam.substring(0, scoreSecondTeam.indexOf("."));
                    if(scoreSecondTeam.equals("-1")){
                        scoreSecondTeam = "?";
                    }
                    latitude = event.getDouble("latitude");
                    longitude = event.getDouble("longitude");
                    mLatitude = event.getString("latitude");
                    mLongitude = event.getString("longitude");
                    date = event.getString("formatedEventDate");
                    address = event.getString("location");
                    address.replace(':', '-');
                    description = event.getString("description");
                    title = event.getString("name");
                    costs = event.getString("costs");
                }
            } catch (JSONException e) {
                e.printStackTrace();
               success = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(success){
                mFirstTeamName.setText(firstTeamName);
                mSecondTeamName.setText(secondTeamName);
                Glide.with(EventDetailsActivity.this).load(URL_IMAGES_BASE + urlFirstTeam+".png")
                        .into(mFirstTeamLogo);
                Glide.with(EventDetailsActivity.this).load(URL_IMAGES_BASE + urlSecondTeam+".png")
                        .into(mSecondTeamLogo);
                mFirstTeamScore.setText(scoreFirstTeam);
                mSecondTeamScore.setText(scoreSecondTeam);
                mDate.setText(date);
                mAddress.setText(address);
                mTitle.setText(title);
                mCost.setText(costs);
                mDescription.setText(description);

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.event_details_frame, new MapsFragment(latitude,longitude, title)).commit();

                progressBar.setVisibility(View.GONE);
                mLayout.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(EventDetailsActivity.this, "Não foi possível carregar os dados.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
