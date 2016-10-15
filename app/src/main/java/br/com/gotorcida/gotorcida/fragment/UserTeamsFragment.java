package br.com.gotorcida.gotorcida.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.TeamActivity;
import br.com.gotorcida.gotorcida.adapter.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_TEAM;

/**
 * Created by dougl on 15/10/2016.
 */

public class UserTeamsFragment extends Fragment{
    View mView;

    ListView listTeams;
    ArrayAdapter<JSONObject> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_teams, container, false);

        listTeams = (ListView) mView.findViewById(R.id.userteams_listview_teams);

        GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_TEAM, SaveSharedPreference.getUserName(getActivity()), "user");
        try {
            getRequest.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject json = getRequest.getMessage().getData();

        JSONArray teams = null;
        try {

            if (getRequest.getMessage().getSystem().getInt("code") == 500){
                Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                //finish();
                //return;
            }

            teams = json.getJSONArray("teams");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<JSONObject> teamsList = new ArrayList<>();

        for (int i=0; i < teams.length(); i++) {
            try {
                teamsList.add(teams.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new UserTeamsListAdapter(getActivity(), teamsList);
        listTeams.setAdapter(adapter);

        listTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView teamId = (TextView) view.findViewById(R.id.userteams_textview_teamid);

                if (teamId != null && !teamId.getText().toString().equals("")) {
                    Intent it = new Intent(getActivity(), TeamActivity.class);
                    it.putExtra("teamId", teamId.getText().toString());
                    startActivity(it);
                }
            }
        });


        return mView;
    }
}
