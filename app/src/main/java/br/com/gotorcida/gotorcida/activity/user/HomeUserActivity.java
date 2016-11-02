package br.com.gotorcida.gotorcida.activity.user;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.adm.HomeAdmActivity;
import br.com.gotorcida.gotorcida.dialog.AboutDialog;
import br.com.gotorcida.gotorcida.fragment.user.EventsFragment;
import br.com.gotorcida.gotorcida.fragment.user.MatchesTableFragment;
import br.com.gotorcida.gotorcida.fragment.user.MyTeamHereFragment;
import br.com.gotorcida.gotorcida.fragment.user.UserTeamsFragment;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

public class HomeUserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    TextView mUserName;
    TextView mUserEmail;
    ImageView mUserImgProfile;
    Spinner mSpinnerSelectPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
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
        getMenuInflater().inflate(R.menu.home_user, menu);
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
            ft.replace(R.id.home_user_frame_fragment, new UserTeamsFragment()).commit();
        } else if (id == R.id.nav_events) {
            ft.replace(R.id.home_user_frame_fragment, new EventsFragment()).commit();
        } else if (id == R.id.nav_matches_table) {
            ft.replace(R.id.home_user_frame_fragment, new MatchesTableFragment()).commit();
        } else if (id == R.id.nav_my_team_here) {
            ft.replace(R.id.home_user_frame_fragment, new MyTeamHereFragment()).commit();
        } else if (id == R.id.nav_about) {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(), "TAG");
        } else if (id == R.id.nav_logout) {
            SaveSharedPreference.clearUserName(HomeUserActivity.this);
            startActivity(new Intent(HomeUserActivity.this, LoginActivity.class));
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
        ArrayAdapter<StringWithTag> adapter;
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
                mGetRequest = new GetRequest(Constants.URL_SERVER_NEW_USER + "/" +SaveSharedPreference.getUserName(HomeUserActivity.this));
                mGetRequest.execute();
                JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));
                userName = userData.getString("fullName");
                userEmail = userData.getString("emailAddress");

                if(userData.getString("userType").equals("Team")){
                    userTeamAdm = true;

                    List<StringWithTag> spinnerArray =  new ArrayList<>();
                    spinnerArray.add(new StringWithTag(userName,
                            Integer.getInteger(SaveSharedPreference.getUserName(HomeUserActivity.this))));
                    spinnerArray.add(new StringWithTag("Paulínia Mavericks", 1)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag("Basquete", 22)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag("Voley", 13)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag("Ha", 12)); //TODO: ID do time criar ciclo

                    adapter = new ArrayAdapter<>(
                            HomeUserActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
            } catch (Exception e) {
                Toast.makeText(HomeUserActivity.this, "Erro em carregar os dados do usuário", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mUserName.setText(userName);
            mUserEmail.setText(userEmail);
            if(userTeamAdm){
                mSpinnerSelectPerfil.setAdapter(adapter);
                mSpinnerSelectPerfil.setVisibility(View.VISIBLE);
                mUserName.setVisibility(View.GONE);

                mSpinnerSelectPerfil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position != 0){
                            StringWithTag selected = (StringWithTag) mSpinnerSelectPerfil.getItemAtPosition(position);

                            Intent it = new Intent(HomeUserActivity.this, HomeAdmActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("teamName", selected.toString());
                            bundle.putString("teamId", selected.getTag().toString());
                            bundle.putString("userName", userName);
                            it.putExtras(bundle);
                            startActivity(it);

                            finish();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


}
