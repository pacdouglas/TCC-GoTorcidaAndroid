package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;


public class SelectLeagueActivity extends AppCompatActivity {
    ListView listSports;
    ArrayAdapter<JSONObject> adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_league);

        listSports = (ListView) findViewById(R.id.list_select_sports);

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS);
        try {
            getRequest.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject json = getRequest.getMessage().getData();

        JSONArray sports = null;
        try {
            sports = json.getJSONArray("sports");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<JSONObject> sportsList = new ArrayList<>();

        for(int i=0; i < sports.length(); i++)
        {
            try {
                sportsList.add((JSONObject) sports.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new SportAdapter(this, sportsList);
        listSports.setAdapter(adapter);

    }

}
