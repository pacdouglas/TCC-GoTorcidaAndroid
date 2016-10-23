package br.com.gotorcida.gotorcida.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.TeamNewsListAdapter;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;

public class TeamNewsFragment extends Fragment {
    View mView;
    private String teamId;
    RecyclerView newsList;
    ProgressBar progressBar;
    public TeamNewsFragment(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_news, container, false);
        newsList = (RecyclerView) mView.findViewById(R.id.team_news_listview_news);
        progressBar = (ProgressBar) mView.findViewById(R.id.team_news_progress);
        newsList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        MakeListNewsTask makeListNewsTask = new MakeListNewsTask();
        makeListNewsTask.execute();
        return mView;
    }

    public class MakeListNewsTask extends AsyncTask {
        ArrayList<JSONObject> listNews;
        protected void onPreExecute() {

        }
        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS
                    , SaveSharedPreference.getUserName(getActivity().getBaseContext()),"team",teamId);

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
                    listNews.add(news.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        public void onPostExecute(Object result) {
            newsList.setHasFixedSize(true);
            TeamNewsListAdapter adapter = new TeamNewsListAdapter(listNews, getActivity().getBaseContext());

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext());
            newsList.setLayoutManager(layout);

            newsList.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            newsList.setVisibility(View.VISIBLE);
        }
    }
}
