package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;


public class SelectSportActivity extends AppCompatActivity {

    RecyclerView listSports;
    SportsListAdapter adapter;
    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);

        listSports = (RecyclerView) findViewById(R.id.list_select_sports);

        buttonOK = (Button) findViewById(R.id.button_sports_ok);

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

        listSports.setAdapter(new SportsListAdapter(sportsList, this));

        RecyclerView.LayoutManager layout = new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
        listSports.setLayoutManager(layout);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
