package br.com.gotorcida.gotorcida.fragment.adm;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import br.com.gotorcida.gotorcida.adapter.adm.AdmMatchesTableListAdapter;
import br.com.gotorcida.gotorcida.dialog.adm.AdmMatchesInsertDialog;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static android.app.Activity.RESULT_OK;

public class TeamAdmMatchesFragment extends Fragment{

    public static final int DIALOG_FRAGMENT = 666;
    View mView;
    String mTeamId;
    RecyclerView mMatchesList;
    ProgressBar mProgressBar;
    public TeamAdmMatchesFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_matches, container, false);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.fragment_adm_matches_progressbar);
        mMatchesList = (RecyclerView) mView.findViewById(R.id.fragment_adm_matches_listview_matches);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fragment_adm_matches_floatingbutton_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        MatchesListTask matchesListTask = new MatchesListTask();
        matchesListTask.execute();
        return mView;
    }

    private void showDialog() {
        AdmMatchesInsertDialog admMatchesInsertDialog = new AdmMatchesInsertDialog(mTeamId);
        admMatchesInsertDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        admMatchesInsertDialog.show(getActivity().getSupportFragmentManager().beginTransaction(),  "");
    }

    public class MatchesListTask extends AsyncTask {

        ArrayList<JSONObject> eventsList;
        JSONArray eventsArray;
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mMatchesList.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest;
            getRequest = new GetRequest(Constants.URL_SERVER_JSON_LIST_EVENTS_BY_TEAM, mTeamId);
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
            AdmMatchesTableListAdapter adapter = new AdmMatchesTableListAdapter(eventsList, getActivity().getBaseContext(), getFragmentManager(), TeamAdmMatchesFragment.this);
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext());
            mMatchesList.setLayoutManager(layout);
            mMatchesList.setAdapter(adapter);

            mProgressBar.setVisibility(View.GONE);
            mMatchesList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            MatchesListTask matchesListTask = new MatchesListTask();
            matchesListTask.execute();
        }
    }


}
