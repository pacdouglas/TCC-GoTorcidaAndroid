package br.com.gotorcida.gotorcida.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;

public class NewsDetailsActivity extends AppCompatActivity {
    TextView newsTitle;
    TextView newsBody;
    TextView newsDate;
    TextView newsAuthor;
    String teamId;
    String newsType;
    String newsId;
    LinearLayout relativeLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Bundle bundle = getIntent().getExtras();
        teamId = bundle.getString("teamId");
        newsType = bundle.getString("newsType");
        newsId = bundle.getString("id");
        progressBar = (ProgressBar) findViewById(R.id.news_details_progress);
        relativeLayout = (LinearLayout) findViewById(R.id.news_details_all_layout);
        newsTitle = (TextView) findViewById(R.id.news_details_textview_title);
        newsBody = (TextView) findViewById(R.id.news_details_textview_body);
        newsDate = (TextView) findViewById(R.id.news_details_textview_date);
        newsAuthor = (TextView) findViewById(R.id.news_details_textview_author);

        NewsDetailsTask newsDetailsTask = new NewsDetailsTask();
        newsDetailsTask.execute();
    }

    public class NewsDetailsTask extends AsyncTask {
        JSONObject news;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relativeLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS, newsType, teamId);
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            JSONArray newsArray = null;
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(NewsDetailsActivity.this, getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                }else{
                    newsArray = json.getJSONArray("newsList");
                }

                for(int i = 0; i < newsArray.length(); i++){
                    if(newsArray.getJSONObject(i).getString("id").equals(newsId)){
                        news = newsArray.getJSONObject(i);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                newsTitle.setText(news.getString("title"));
                newsBody.setText(news.getString("description"));
                newsDate.setText(news.getString("formatedRegistrationDate"));
                newsAuthor.setText(news.getString("fullName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }
}
