package br.com.gotorcida.gotorcida.fragment.adm;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
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
import br.com.gotorcida.gotorcida.adapter.adm.AdmTeamNewsListAdapter;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.dialog.user.MyTeamHereDialog;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;

public class TeamAdmNewsFragment extends Fragment{
    View mView;
    String mTeamId;
    RecyclerView newsList;
    ProgressBar progressBar;

    public static final int DIALOG_FRAGMENT = 666;

    public TeamAdmNewsFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_news, container, false);
        newsList = (RecyclerView) mView.findViewById(R.id.fragment_adm_news_listview_news);
        progressBar = (ProgressBar) mView.findViewById(R.id.fragment_adm_news_progressbar);
        newsList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        MakeListNewsTask makeListNewsTask = new MakeListNewsTask();
        makeListNewsTask.execute();
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fragment_adm_news_floatingbutton_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return mView;
    }

    private void showDialog() {
        AdmNewsEditDialog admNewsEditDialog = new AdmNewsEditDialog(mTeamId);
        admNewsEditDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        admNewsEditDialog.show(getActivity().getSupportFragmentManager().beginTransaction(),  "");
    }

    public class MakeListNewsTask extends AsyncTask {
        ArrayList<JSONObject> listNews;
        protected void onPreExecute() {

        }
        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS, "team", mTeamId);

            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            JSONArray news = null;
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), "Não foi possível carregar a lista de atletas.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    news = json.getJSONArray("newsList");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listNews = new ArrayList<>();
            for(int i = 0; i < news.length(); i++){
                try {
                    listNews.add(new JSONObject(news.getString(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        public void onPostExecute(Object result) {
            newsList.setHasFixedSize(true);
            AdmTeamNewsListAdapter adapter = new AdmTeamNewsListAdapter(listNews, getActivity().getBaseContext(), getActivity().getSupportFragmentManager(), TeamAdmNewsFragment.this);

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext());
            newsList.setLayoutManager(layout);

            newsList.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            newsList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            MakeListNewsTask makeListNewsTask = new MakeListNewsTask();
            makeListNewsTask.execute();
        }
    }
}
