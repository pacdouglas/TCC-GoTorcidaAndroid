package br.com.gotorcida.gotorcida.fragment.adm;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.TeamRosterListAdapter;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmRosterInsertDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmRosterUpdateDialog;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM;

public class TeamAdmRosterFragment extends Fragment {

    View mView;
    String mTeamId;
    RecyclerView rosterList;
    ProgressBar progressBar;

    private static final int INSERT = 1;
    private static final int UPDATE = 2;
    public static final int DIALOG_FRAGMENT = 666;

    public TeamAdmRosterFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_roster, container, false);

        rosterList = (RecyclerView) mView.findViewById(R.id.fragment_adm_roster_listview_athletes);
        progressBar = (ProgressBar) mView.findViewById(R.id.fragment_adm_roster_progress);

        rosterList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CreateRosterListTask createRosterListTask = new CreateRosterListTask();
        createRosterListTask.execute();

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fragment_adm_roster_floatingbutton_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(INSERT);
            }
        });
        return mView;
    }

    private void showDialog(int mode) {
        switch (mode) {
            case INSERT:
                AdmRosterInsertDialog admRosterInsertDialog = new AdmRosterInsertDialog(mTeamId);
                admRosterInsertDialog.setTargetFragment(this, DIALOG_FRAGMENT);
                admRosterInsertDialog.show(getActivity().getSupportFragmentManager().beginTransaction(),  "");
            break;

            case UPDATE:
                AdmRosterUpdateDialog admRosterUpdateDialog = new AdmRosterUpdateDialog(mTeamId);
                admRosterUpdateDialog.setTargetFragment(this, DIALOG_FRAGMENT);
                admRosterUpdateDialog.show(getActivity().getSupportFragmentManager().beginTransaction(),  "");

            break;
        }
    }

    public class CreateRosterListTask extends AsyncTask {
        ArrayList<JSONObject> listAthlete;

        protected void onPreExecute() {
            rosterList.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM,
                    SaveSharedPreference.getUserName(getActivity()) ,mTeamId);
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            JSONArray athletes = null;
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), "Não foi possível carregar a lista de atletas.", Toast.LENGTH_SHORT).show();
                }else{
                    athletes = json.getJSONArray("athletes");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listAthlete = new ArrayList<>();
            for(int i = 0; i < athletes.length(); i++){
                try {
                    listAthlete.add(new JSONObject(athletes.getString(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            rosterList.setHasFixedSize(true);
            TeamRosterListAdapter adapter = new TeamRosterListAdapter(listAthlete, getActivity().getBaseContext(), mTeamId);

            RecyclerView.LayoutManager layout = new StaggeredGridLayoutManager(2, 1);
            rosterList.setLayoutManager(layout);
            rosterList.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            rosterList.setVisibility(View.VISIBLE);
        }
    }
}
