package br.com.gotorcida.gotorcida.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;

public class EventDetailsActivity extends AppCompatActivity {

    String mEventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();
        mEventId = bundle.getString("eventId");
    }

    public class EventDetailsTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}
