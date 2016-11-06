package br.com.gotorcida.gotorcida.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_TEAM;

public class UserTeamsFragment extends Fragment {
    View mView;

    ListView listTeams;
    ArrayAdapter<JSONObject> adapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_teams, container, false);

        progressBar = (ProgressBar) mView.findViewById(R.id.user_teams_progress);
        listTeams = (ListView) mView.findViewById(R.id.userteams_listview_teams);

        MakeListViewTeams makeListViewTeams = new MakeListViewTeams();
        makeListViewTeams.execute();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                if (fragment.getClass().equals(TeamNewsFragment.class)) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                } else if (fragment.getClass().equals(TeamRosterFragment.class)) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                } else if (fragment.getClass().equals(MatchesTableFragment.class)) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                } else if (fragment.getClass().equals(TeamContactFragment.class)) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        }

        return mView;
    }

    public class MakeListViewTeams extends AsyncTask {


        protected void onPreExecute() {
            listTeams.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object... args) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_TEAM,
                    SaveSharedPreference.getUserName(getActivity()), "user");

            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();

            JSONArray teams = null;
            try {

                if (getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
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
            return null;
        }

        @Override
        public void onProgressUpdate(Object... values) {

        }

        @Override
        public void onPostExecute(Object result) {
            listTeams.setAdapter(adapter);

            listTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    TeamFragment fragment = new TeamFragment();

                    TextView teamId = (TextView) view.findViewById(R.id.userteams_textview_teamid);

                    Bundle bundle = new Bundle();
                    bundle.putString("teamId", teamId.getText().toString());
                    fragment.setArguments(bundle);

                    ft.replace(R.id.home_user_frame_fragment, fragment).commit();
                }
            });
            progressBar.setVisibility(View.GONE);
            listTeams.setVisibility(View.VISIBLE);
        }

    }
}

