package br.com.gotorcida.gotorcida.activity.user;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.utils.TestInternetConnectionAndClose;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;

public class SplashScreenActivity extends AppCompatActivity {


    boolean serverOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        serverOn = false;
        ConnectionTestTask connectionTestTask = new ConnectionTestTask();
        connectionTestTask.execute();
    }


    public class ConnectionTestTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(connectionInternetTest()){
                GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS);
                serverOn = getRequest.execute();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(serverOn){
                if(SaveSharedPreference.getUserName(SplashScreenActivity.this).length() == 0){
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }else{
                    startActivity(new Intent(SplashScreenActivity.this, HomeUserActivity.class));
                }
                finish();
            }
            else{
                TestInternetConnectionAndClose.execute(SplashScreenActivity.this);
            }
        }

        public  boolean connectionInternetTest() {
            boolean ret;
            ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conectivtyManager.getActiveNetworkInfo() != null
                    && conectivtyManager.getActiveNetworkInfo().isAvailable()
                    && conectivtyManager.getActiveNetworkInfo().isConnected()) {
                ret = true;
            } else {
                ret = false;
            }
            return ret;
        }
    }

}