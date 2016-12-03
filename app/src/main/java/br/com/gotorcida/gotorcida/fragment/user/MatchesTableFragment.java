package br.com.gotorcida.gotorcida.fragment.user;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.MatchesTableListAdapter;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_EVENTS_BY_SPORT;

public class MatchesTableFragment extends Fragment {

    View mView;
    RecyclerView mMatchesList;
    ProgressBar mProgressBar;
    TextView mTitle;
    private String mTeamId;
    boolean mFilterByUser; //true = user false = team
    boolean mNextEvents = false;

    public MatchesTableFragment(){
        mNextEvents = true;
    }

    @SuppressLint("ValidFragment")
    public MatchesTableFragment(boolean team, String teamId){
        this.mFilterByUser = team;
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_matches_table, container, false);

        mMatchesList = (RecyclerView) mView.findViewById(R.id.matches_table_listview_matches);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.matches_table_progress);
        mTitle = (TextView) mView.findViewById(R.id.matches_table_textview_title);

        ConstructMatchesListView constructMatchesListView = new ConstructMatchesListView();
        constructMatchesListView.execute();

        return mView;
    }

    public class ConstructMatchesListView extends AsyncTask {

        ArrayList<JSONObject> eventsList;
        JSONArray eventsArray;
        String title;
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mMatchesList.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest;
            if(!mNextEvents){
                if(mFilterByUser){
                    getRequest = new GetRequest(Constants.URL_SERVER_JSON_LIST_EVENTS_BY_USER, SaveSharedPreference.getUserName(getActivity().getBaseContext()));
                    title = "Partidas das suas equipes preferidas";
                }else{
                    getRequest = new GetRequest(Constants.URL_SERVER_JSON_LIST_EVENTS_BY_TEAM, mTeamId);
                    title = "";
                }
            }else{
                getRequest = new GetRequest(URL_SERVER_JSON_LIST_EVENTS_BY_SPORT, SaveSharedPreference.getUserName(getActivity().getBaseContext()));
                title = "Partidas que possam te interessar";
            }

            getRequest.execute();
            JSONObject requestResult = getRequest.getMessage().getData();

            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), "Não foi possível carregar a tabela de jogos.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    eventsArray = requestResult.getJSONArray("eventsList");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            eventsList = new ArrayList<>();
            for(int i = 0; i < eventsArray.length(); i++){
                try {
                    JSONObject card = new JSONObject();
                    JSONObject firstTeam = eventsArray.getJSONObject(i).getJSONObject("firstTeam");
                    JSONObject secondTeam = eventsArray.getJSONObject(i).getJSONObject("secondTeam");
                    JSONObject eventResult = eventsArray.getJSONObject(i).getJSONObject("result");
                    String locationAux = eventsArray.getJSONObject(i).getString("location");
                    card.put("location", locationAux.substring(locationAux.indexOf(":")+1, locationAux.length()));
                    card.put("eventDate", eventsArray.getJSONObject(i).getString("formatedEventDate"));
                    card.put("time", eventsArray.getJSONObject(i).getString("time"));
                    card.put("firstTeamName", firstTeam.getString("name"));
                    card.put("firstTeamScore", eventResult.getString("firstTeamScore"));
                    card.put("firstTeamImageURL", firstTeam.getString("urlImage"));

                    card.put("secondTeamName", secondTeam.getString("name"));
                    card.put("secondTeamScore", eventResult.getString("secondTeamScore"));
                    card.put("secondTeamImageURL", secondTeam.getString("urlImage"));
                    card.put("id", eventsArray.getJSONObject(i).getString("id"));
                    eventsList.add(card);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            mTitle.setText(title);
            MatchesTableListAdapter adapter = new MatchesTableListAdapter(eventsList, getActivity().getBaseContext());

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext());
            mMatchesList.setLayoutManager(layout);

            mMatchesList.setAdapter(adapter);

            mProgressBar.setVisibility(View.GONE);
            mMatchesList.setVisibility(View.VISIBLE);
            if (!title.isEmpty()){
                mTitle.setVisibility(View.VISIBLE);
            }
        }
    }

}
