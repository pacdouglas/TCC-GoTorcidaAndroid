package br.com.gotorcida.gotorcida.fragment.user;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.MatchesTableListAdapter;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

/**
 * Created by dougl on 15/10/2016.
 */

public class MatchesTableFragment extends Fragment {

    View mView;
    RecyclerView mMatchesList;
    ProgressBar mProgressBar;
    private String mTeamId;
    boolean mFilterByUser; //true = user false = team

    public MatchesTableFragment(boolean team, String teamId){
        this.mFilterByUser = team;
        this.mTeamId = teamId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_matches_table, container, false);

        mMatchesList = (RecyclerView) mView.findViewById(R.id.matches_table_listview_matches);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.matches_table_progress);

        ConstructMatchesListView constructMatchesListView = new ConstructMatchesListView();
        constructMatchesListView.execute();

        return mView;
    }

    public class ConstructMatchesListView extends AsyncTask {

        ArrayList<JSONObject> eventsList;
        JSONArray eventsArray;

        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mMatchesList.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest;
            if(mFilterByUser){
                getRequest = new GetRequest(Constants.URL_SERVER_JSON_LIST_EVENTS_BY_USER, SaveSharedPreference.getUserName(getActivity().getBaseContext()));
            }else{
                getRequest = new GetRequest(Constants.URL_SERVER_JSON_LIST_EVENTS_BY_TEAM, mTeamId);
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

                    card.put("eventDate", eventsArray.getJSONObject(i).getString("formatedEventDate"));
                    card.put("firstTeamName", firstTeam.getString("name"));
                    card.put("firstTeamScore", eventResult.getString("firstTeamScore"));
                    card.put("firstTeamImageURL", firstTeam.getString("urlImage"));

                    card.put("secondTeamName", secondTeam.getString("name"));
                    card.put("secondTeamScore", eventResult.getString("secondTeamScore"));
                    card.put("secondTeamImageURL", secondTeam.getString("urlImage"));
                    eventsList.add(card);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            mMatchesList.setHasFixedSize(true);
            MatchesTableListAdapter adapter = new MatchesTableListAdapter(eventsList, getActivity().getBaseContext());

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext());
            mMatchesList.setLayoutManager(layout);

            mMatchesList.setAdapter(adapter);

            mProgressBar.setVisibility(View.GONE);
            mMatchesList.setVisibility(View.VISIBLE);
        }
    }

}
