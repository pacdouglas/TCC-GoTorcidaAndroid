package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import br.com.gotorcida.gotorcida.R;
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
                    GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_SPORTS, null);
                    try {
                        getRequest.execute().get();
                        serverOn = true;
                    }  catch (InterruptedException e) {
                        serverOn = false;
                    } catch (ExecutionException e) {
                        serverOn = false;
                    }

                    if(serverOn){
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
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