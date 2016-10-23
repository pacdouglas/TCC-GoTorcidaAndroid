package br.com.gotorcida.gotorcida.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.fragment.AboutFragment;
import br.com.gotorcida.gotorcida.fragment.EventsFragment;
import br.com.gotorcida.gotorcida.fragment.MatchesTableFragment;
import br.com.gotorcida.gotorcida.fragment.MyTeamHereFragment;
import br.com.gotorcida.gotorcida.fragment.UserTeamsFragment;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    TextView mUserName;
    TextView mUserEmail;
    ImageView mUserImgProfile;
    Spinner mSpinnerSelectPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_text_view_username);
        mUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_text_view_email);
        mUserImgProfile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_image_view_profile);
        mSpinnerSelectPerfil = (Spinner) navigationView.getHeaderView(0).findViewById(R.id.nav_header_spinner_select_perfil);

        UserConfigTask userConfigTask = new UserConfigTask();
        userConfigTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_my_teams) {
            ft.replace(R.id.dashboard_frame_fragment, new UserTeamsFragment()).commit();
        } else if (id == R.id.nav_events) {
            ft.replace(R.id.dashboard_frame_fragment, new EventsFragment()).commit();
        } else if (id == R.id.nav_matches_table) {
            ft.replace(R.id.dashboard_frame_fragment, new MatchesTableFragment()).commit();
        } else if (id == R.id.nav_my_team_here) {
            ft.replace(R.id.dashboard_frame_fragment, new MyTeamHereFragment()).commit();
        } else if (id == R.id.nav_about) {
            ft.replace(R.id.dashboard_frame_fragment, new AboutFragment()).commit();
        } else if (id == R.id.nav_logout) {
            SaveSharedPreference.clearUserName(DashboardActivity.this);
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UserConfigTask extends AsyncTask<Void, Void, Boolean> {
        String userName;
        String userEmail;
        boolean userTeamAdm;
        private GetRequest mGetRequest;
        UserConfigTask() {

        }

        @Override
        protected void onPreExecute() {
            userName = "userName";
            userEmail = "email";
            userTeamAdm = false;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mGetRequest = new GetRequest(Constants.URL_SERVER_NEW_USER + "/" +SaveSharedPreference.getUserName(DashboardActivity.this));
                mGetRequest.execute();
                JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));
                userName = userData.getString("fullName");
                userEmail = userData.getString("emailAddress");

                if(userData.getString("userType").equals("Team")){
                    userTeamAdm = true;

                    //TODO: adicionar ao spinner as opções
                }
            } catch (Exception e) {
                Toast.makeText(DashboardActivity.this, "Erro em carregar os dados do usuário", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mUserName.setText(userName);
            mUserEmail.setText(userEmail);
            if(userTeamAdm){
                mSpinnerSelectPerfil.setVisibility(View.VISIBLE);
            }
        }
    }
}
