package br.com.gotorcida.gotorcida.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.UserTeamsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.SLEEP_THREAD;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_TEAM;


public class UserTeamsFragment extends Fragment {
    View mView;

    ListView listTeams;
    ArrayAdapter<JSONObject> adapter;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_teams, container, false);

        progressBar = (ProgressBar) mView.findViewById(R.id.user_teams_progress);
        listTeams = (ListView) mView.findViewById(R.id.userteams_listview_teams);

        MakeListViewTeams makeListViewTeams = new MakeListViewTeams();

        makeListViewTeams.execute();
        return mView;
    }

    public class MakeListViewTeams extends AsyncTask {


        protected void onPreExecute() {
            listTeams.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object... args) {
            SystemClock.sleep(SLEEP_THREAD);
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_TEAM, SaveSharedPreference.getUserName(getActivity()), "user");

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
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.dashboard_frame_fragment, new TeamFragment())
                            .commit();
                    //TextView teamId = (TextView) view.findViewById(R.id.userteams_textview_teamid);
                    //if (teamId != null && !teamId.getText().toString().equals("")) {
                    //    Intent it = new Intent(getActivity(), TeamActivity.class);
                    //    it.putExtra("teamId", teamId.getText().toString());
                    //    startActivity(it);
                    //}
                }
            });
            progressBar.setVisibility(View.GONE);
            listTeams.setVisibility(View.VISIBLE);
        }

    }
}
