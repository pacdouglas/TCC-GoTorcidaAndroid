package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.SportsListAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;


public class SelectSportActivity extends AppCompatActivity {

    RecyclerView listSports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);

        listSports = (RecyclerView) findViewById(R.id.selectsports_listview_sports);

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS, null);
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

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        listSports.setLayoutManager(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_next_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_button_toolbar:
                JSONArray selectedSports = new JSONArray();

                for (int i = 0; i < listSports.getChildCount(); i++) {
                    View row = listSports.getChildAt(i);

                    CheckBox cbSport = (CheckBox) row.findViewById(R.id.selectsport_checkbox_sportname);

                    if (cbSport.isChecked()) {
                        TextView sportID = (TextView) row.findViewById(R.id.selectsport_textview_sportid);
                        selectedSports.put(sportID.getText());
                    }
                }

                if (selectedSports.length() > 0) {
                    Intent intent = new Intent(SelectSportActivity.this, SelectTeamActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("selectedSports", selectedSports.toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectSportActivity.this, "É necessário escolher ao menos um esporte.", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
