package br.com.gotorcida.gotorcida.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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

    ListView listSports;
    ArrayAdapter<JSONObject> adapter;
    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);

        listSports = (ListView) findViewById(R.id.list_select_sports);

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

        adapter = new SportAdapter(this, sportsList);
        listSports.setAdapter(adapter);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder result = new StringBuilder();
                for(int i=0;i<adapter.mCheckStates.size();i++)
                {
                    if(adapter.mCheckStates.get(i)==true)
                    {

                        result.append(app_info[i].applicationName);
                        result.append("\n");
                    }

                }
                Toast.makeText(MainActivity.this, result, 1000).show();
            }

                for (int i = 0; i < selectedSports.size(); i++){
                    Toast.makeText(SelectSportActivity.this, i, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
