package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_SPORTS;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private TextView txtError;
    boolean serverOn;
    private static final long SPLASH_DURATION = 2500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        txtError = (TextView) findViewById(R.id.error_connection);
        txtError.setVisibility(View.GONE);
        serverOn = false;
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                try{
                    GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS);
                    serverOn = getRequest.execute();

                    if(true){
                        if(SaveSharedPreference.getUserName(SplashScreenActivity.this).length() == 0){
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        }else{
                            startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                        }
                        finish();
                    }
                    else{
                        txtError.setVisibility(View.VISIBLE);
                    }

                }catch(Exception e) {
                    txtError.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

}