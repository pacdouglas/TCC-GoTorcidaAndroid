package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;


public class SelectSportActivity extends AppCompatActivity {
    ListView listSports;
    ArrayAdapter<JSONObject> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);

        GetRequest getRequest = new GetRequest("http://10.0.2.2:8080/gotorcidaws/sport");
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

        ArrayList<JSONObject> sportsList = new ArrayList<>();

        for(int i=0; i < sports.length(); i++)
        {
            try {
                sportsList.add((JSONObject) sports.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listSports = (ListView) findViewById(R.id.list_select_sports);

        adapter = new SportAdapter(this, sportsList);

        listSports.setAdapter(adapter);
    }
}
