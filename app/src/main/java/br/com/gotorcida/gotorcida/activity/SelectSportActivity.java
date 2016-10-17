package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.SportsListAdapter;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;


public class SelectSportActivity extends AppCompatActivity {

    private static final int REQUEST_EXIT = 99;
    RecyclerView listSports;
    Bundle bundle;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);

        bundle = getIntent().getExtras();
        listSports = (RecyclerView) findViewById(R.id.selectsports_listview_sports);
        progressBar = (ProgressBar) findViewById(R.id.select_sport_progress);

        MakeListSportsTask makeListSportsTask = new MakeListSportsTask();
        makeListSportsTask.execute();
    }


    public class MakeListSportsTask extends AsyncTask {

        ArrayList<JSONObject> sportsList;
        protected void onPreExecute() {
            listSports.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object... args) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS);

            getRequest.execute();

            JSONObject json = getRequest.getMessage().getData();

            JSONArray sports = null;
            try {
                sports = json.getJSONArray("sports");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sportsList = new ArrayList<>();

            for(int i=0; i < sports.length(); i++)
            {
                try {
                    sportsList.add((JSONObject) sports.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            listSports.setAdapter(new SportsListAdapter(sportsList, getBaseContext()));

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getBaseContext());
            listSports.setLayoutManager(layout);
            progressBar.setVisibility(View.GONE);
            listSports.setVisibility(View.VISIBLE);
        }
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
                    startActivityForResult(intent, REQUEST_EXIT);
                } else {
                    Toast.makeText(SelectSportActivity.this, "É necessário escolher ao menos um esporte.", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }
}
