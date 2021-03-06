package br.com.gotorcida.gotorcida.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.SportsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_NEW_USER;


public class SelectSportActivity extends AppCompatActivity {

    private static final int REQUEST_EXIT = 99;
    RecyclerView listSports;
    Bundle bundle;
    ProgressBar progressBar;
    JSONArray sports;
    private static ArrayList<Boolean> arrayListChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);
        arrayListChecked = new ArrayList<>();

        bundle = getIntent().getExtras();
        listSports = (RecyclerView) findViewById(R.id.selectsports_listview_sports);
        progressBar = (ProgressBar) findViewById(R.id.select_sport_progress);

        MakeListSportsTask makeListSportsTask = new MakeListSportsTask();
        makeListSportsTask.execute();
    }

    public static void setArrayListChecked(int pos, boolean isChecked) {
        arrayListChecked.set(pos, isChecked);
    }


    public class MakeListSportsTask extends AsyncTask {
        boolean success;
        ArrayList<JSONObject> sportsList;
        protected void onPreExecute() {
            listSports.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            success = true;
        }

        @Override
        protected Object doInBackground(Object... args) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().get("code").equals(200)) {
                    JSONObject json = getRequest.getMessage().getData();

                    GetRequest getRequestUser = new GetRequest(URL_SERVER_NEW_USER, SaveSharedPreference.getUserName(SelectSportActivity.this));
                    getRequestUser.execute();
                    JSONObject jsonUser = getRequestUser.getMessage().getData();

                    sports = null;
                    try {
                        sports = json.getJSONArray("sports");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sportsList = new ArrayList<>();

                    JSONArray arraySportsUser = null;
                    boolean edit = false;
                    try {
                        arraySportsUser = new JSONArray(new JSONObject(jsonUser.getString("user")).getString("sports"));
                        if(arraySportsUser.length() > 0){
                            edit = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    for(int i=0; i < sports.length(); i++)
                    {
                        try {
                            JSONObject aux = (JSONObject)sports.get(i);
                            if(!edit){
                                aux.put("isChecked", false);
                                sportsList.add(aux);
                                arrayListChecked.add(false);
                            }else{
                                String sportIdAux = aux.getString("id");
                                boolean test = false;
                                for(int j = 0; j < arraySportsUser.length(); j++){
                                    if(sportIdAux.compareTo(arraySportsUser.getJSONObject(j).getString("id")) == 0){
                                        test = true;
                                    }
                                }
                                if(test){
                                    aux.put("isChecked", true);
                                    sportsList.add(aux);
                                    arrayListChecked.add(true);
                                }else{
                                    aux.put("isChecked", false);
                                    sportsList.add(aux);
                                    arrayListChecked.add(false);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    success = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                success = false;
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            if(success){
                listSports.setAdapter(new SportsListAdapter(sportsList, getBaseContext()));
                listSports.setHasFixedSize(true);
                RecyclerView.LayoutManager layout = new LinearLayoutManager(getBaseContext());
                listSports.setLayoutManager(layout);

                progressBar.setVisibility(View.GONE);
                listSports.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(SelectSportActivity.this, "Não foi possível carregar os dados. Tente novamente mais tarde", Toast.LENGTH_SHORT).show();
            }
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
                for (int i = 0; i < arrayListChecked.size(); i++) {
                    if (arrayListChecked.get(i)) {
                        String sportID = null;
                        try {
                            sportID = sports.getJSONObject(i).getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        selectedSports.put(sportID);
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
