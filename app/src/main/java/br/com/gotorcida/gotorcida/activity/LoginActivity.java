package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;


public class LoginActivity extends AppCompatActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ScrollView formView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        formView = (ScrollView) findViewById(R.id.login_form);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginTask userLoginTask = new UserLoginTask(mEmailView.getText().toString(),
                        mPasswordView.getText().toString());

                userLoginTask.execute();
            }
        });

        Button btnRegister = (Button) findViewById(R.id.main_button_signup);

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(it, 1);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private GetRequest mGetRequest;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            formView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mGetRequest = new GetRequest(Constants.URL_SERVER_LOGIN, mEmail, mPassword);
            mGetRequest.execute();
            try {
                if (mGetRequest.getMessage().getSystem().get("code").equals(200)){
                    JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                    if (userData.getString("firstAccess").equals("S")){
                        SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id")); //todo: lembrar de alterar p bundle
                        Intent it = new Intent(LoginActivity.this, SelectSportActivity.class);
                        startActivity(it);
                    } else {
                        SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                        Intent it = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(it);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, mGetRequest.getMessage().getSystem().getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == RESULT_OK){

            UserLoginTask userLoginTask = new UserLoginTask(data.getExtras().getString("user"),
                    data.getExtras().getString("password"));

            userLoginTask.execute();
        }
    }
}

