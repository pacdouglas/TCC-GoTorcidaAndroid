package br.com.gotorcida.gotorcida.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.R.id.email;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_NEW_USER;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ScrollView formView;
    private ProgressBar progressBar;
    private SignInButton googleSignInButton;
    private GoogleApiClient mGoogleApiClient;

    private LoginButton facebookSignInButton;
    private CallbackManager callbackManager;

    private static final int GOOGLE_LOGIN = 1;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        mEmailView = (AutoCompleteTextView) findViewById(email);
        mPasswordView = (EditText) findViewById(R.id.password);
        formView = (ScrollView) findViewById(R.id.login_form);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginTask userLoginTask = new UserLoginTask(mEmailView.getText().toString(),
                        mPasswordView.getText().toString(), "", "", "NORMAL");

                userLoginTask.execute();
            }
        });

        final Button btnRegister = (Button) findViewById(R.id.main_button_signup);

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(it, 1);
            }
        });

        googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this);

        getGoogleDefaultSettings();

        facebookSignInButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        facebookSignInButton.setReadPermissions("email, user_birthday", "public_profile");

        facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String fullname = object.getString("first_name") + " " + object.getString("last_name");
                                    String email = object.getString("email");
                                    String birthdate = null;

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                    if (object.getString("birthday").toString() != null || !object.getString("birthday").toString().equals("")){
                                        Date dateOfBith = null;
                                        try {
                                            dateOfBith = new SimpleDateFormat("MM/dd/yyyy").parse(object.getString("birthday").toString());
                                            birthdate = simpleDateFormat.format(dateOfBith);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        birthdate = simpleDateFormat.format(new Date());
                                    }

                                    UserLoginTask userLoginTask = new UserLoginTask(email, "", fullname, birthdate, "FACEBOOK");
                                    userLoginTask.execute();
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Erro ao obter informações facebook", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email, birthday");
                request.setParameters(parameters);

                try {
                    request.executeAsync();
                }catch (Exception e){
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Erro ao reailzar login com Facebook.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGoogleDefaultSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.google_sign_in_button) {
            signInWithGoogle();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Erro ao entrar com Google: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT);
        Log.e(TAG, "onConnectionFailed " + connectionResult.getErrorMessage());
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mLoginType;
        private final String mFullname;
        private final String mBirthdate;
        private GetRequest mGetRequest;
        private PostRequest mPostRequest;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        UserLoginTask(String email, String password, String fullname, String birthdate, String loginType) {
            mEmail = email;
            mPassword = password;
            mFullname = fullname;
            mBirthdate = birthdate;
            mLoginType = loginType;
        }

        @Override
        protected void onPreExecute() {
            formView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            switch (mLoginType) {
                case "NORMAL":
                    mGetRequest = new GetRequest(Constants.URL_SERVER_LOGIN, mEmail, mPassword);
                    mGetRequest.execute();
                    try {
                        if (mGetRequest.getMessage().getSystem().get("code").equals(200)) {
                            JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                            if (userData.getString("firstAccess").equals("S")) {
                                SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
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
                    break;

                case "GOOGLE":
                    mGetRequest = new GetRequest(Constants.URL_SERVER_FIND_USER, mEmail);
                    mGetRequest.execute();

                    try {
                            if (mGetRequest.getMessage().getSystem().get("code").equals(404)) {

                                    mPostRequest = new PostRequest(URL_SERVER_NEW_USER);
                                    JSONObject userData = new JSONObject();
                                    userData.put("emailAddress", mEmail);
                                    userData.put("password", "gotorcida");
                                    userData.put("fullName", mFullname);
                                    userData.put("dateOfBirth", dateFormat.format(new Date()));
                                    mPostRequest.execute(userData.toString());

                                    if (mPostRequest.getMessage().getSystem().get("code").equals(200)) {
                                            mGetRequest = new GetRequest(Constants.URL_SERVER_FIND_USER, mEmail);
                                            mGetRequest.execute();

                                            if (mGetRequest.getMessage().getSystem().get("code").equals(200)) {

                                                    userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                                                    if (userData.getString("firstAccess").equals("S")) {
                                                            SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                                            Intent it = new Intent(LoginActivity.this, SelectSportActivity.class);
                                                            startActivity(it);
                                                    } else {
                                                            SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                                            Intent it = new Intent(LoginActivity.this, DashboardActivity.class);
                                                            startActivity(it);
                                                    }
                                            } else {
                                                    Toast.makeText(LoginActivity.this, "Erro interno da aplicação.", Toast.LENGTH_SHORT).show();
                                            }
                                    } else {
                                            Toast.makeText(LoginActivity.this, mPostRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                                    }
                            } else {
                                    JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                                    if (userData.getString("firstAccess").equals("S")) {
                                            SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                            Intent it = new Intent(LoginActivity.this, SelectSportActivity.class);
                                            startActivity(it);
                                    } else {
                                            SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                            Intent it = new Intent(LoginActivity.this, DashboardActivity.class);
                                            startActivity(it);
                                    }
                            }
                    } catch (JSONException ex) {
                            Toast.makeText(LoginActivity.this, "Erro interno da aplicação.", Toast.LENGTH_SHORT).show();
                    }
                break;

                case "FACEBOOK":
                    mGetRequest = new GetRequest(Constants.URL_SERVER_FIND_USER, mEmail);
                    mGetRequest.execute();

                    try {
                        if (mGetRequest.getMessage().getSystem().get("code").equals(404)) {

                            mPostRequest = new PostRequest(URL_SERVER_NEW_USER);
                            JSONObject userData = new JSONObject();
                            userData.put("emailAddress", mEmail);
                            userData.put("password", "gotorcida");
                            userData.put("fullName", mFullname);
                            userData.put("dateOfBirth", mBirthdate);
                            mPostRequest.execute(userData.toString());

                            if (mPostRequest.getMessage().getSystem().get("code").equals(200)) {
                                mGetRequest = new GetRequest(Constants.URL_SERVER_FIND_USER, mEmail);
                                mGetRequest.execute();

                                if (mGetRequest.getMessage().getSystem().get("code").equals(200)) {

                                    userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                                    if (userData.getString("firstAccess").equals("S")) {
                                        SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                        Intent it = new Intent(LoginActivity.this, SelectSportActivity.class);
                                        startActivity(it);
                                    } else {
                                        SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                        Intent it = new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(it);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Erro interno da aplicação.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, mPostRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));

                            if (userData.getString("firstAccess").equals("S")) {
                                SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                Intent it = new Intent(LoginActivity.this, SelectSportActivity.class);
                                startActivity(it);
                            } else {
                                SaveSharedPreference.setUserName(LoginActivity.this, userData.getString("id"));
                                Intent it = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(it);
                            }
                        }
                    } catch (JSONException ex) {
                        Toast.makeText(LoginActivity.this, "Erro interno da aplicação.", Toast.LENGTH_SHORT).show();
                    }

                break;

                default:
                    return true;
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
        if (requestCode == GOOGLE_LOGIN) {
            GoogleSignInResult googleResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleResult.isSuccess()) {
                GoogleSignInAccount acct = googleResult.getSignInAccount();
                String email = acct.getEmail();
                String fullname = acct.getDisplayName();

                UserLoginTask userLoginTask = new UserLoginTask(email, "", fullname, "", "GOOGLE");
                userLoginTask.execute();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}

