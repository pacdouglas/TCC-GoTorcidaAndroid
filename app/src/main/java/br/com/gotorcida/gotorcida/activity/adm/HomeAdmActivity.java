package br.com.gotorcida.gotorcida.activity.adm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.HomeUserActivity;
import br.com.gotorcida.gotorcida.fragment.adm.TeamAdmEditInfoFragment;
import br.com.gotorcida.gotorcida.fragment.adm.TeamAdmMachesFragment;
import br.com.gotorcida.gotorcida.fragment.adm.TeamAdmNewsFragment;
import br.com.gotorcida.gotorcida.fragment.adm.TeamAdmRosterFragment;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

public class HomeAdmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String mTeamId;
    String mUserName;
    Spinner mSpinnerSelectPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        mTeamId = bundle.getString("teamId");
        mUserName = bundle.getString("userName");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSpinnerSelectPerfil = (Spinner) navigationView.getHeaderView(0).findViewById(R.id.nav_header_spinner_select_perfil_adm);

        AdmConfigTask admConfigTask = new AdmConfigTask();
        admConfigTask.execute();
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
        getMenuInflater().inflate(R.menu.home_adm, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_adm_matches) {
            ft.replace(R.id.home_adm_frame_fragment, new TeamAdmMachesFragment()).commit();
        } else if (id == R.id.nav_adm_news) {
            ft.replace(R.id.home_adm_frame_fragment, new TeamAdmNewsFragment()).commit();
        } else if (id == R.id.nav_adm_roster) {
            ft.replace(R.id.home_adm_frame_fragment, new TeamAdmRosterFragment()).commit();
        } else if (id == R.id.nav_adm_contact) {
            ft.replace(R.id.home_adm_frame_fragment, new TeamAdmEditInfoFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AdmConfigTask extends AsyncTask<Void, Void, Boolean> {
        private GetRequest mGetRequest;
        ArrayAdapter<StringWithTag> adapter;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mGetRequest = new GetRequest(Constants.URL_SERVER_NEW_USER + "/" + SaveSharedPreference.getUserName(HomeAdmActivity.this));
                mGetRequest.execute();
                JSONObject userData = new JSONObject(mGetRequest.getMessage().getData().getString("user"));
                String userName = userData.getString("fullName");

                if(userData.getString("userType").equals("Team")){

                    List<StringWithTag> spinnerArray =  new ArrayList<>();
                    spinnerArray.add(new StringWithTag("Paulínia Mavericks", 1)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag(userName,
                            Integer.getInteger(SaveSharedPreference.getUserName(HomeAdmActivity.this))));
                    spinnerArray.add(new StringWithTag("Basquete", 22)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag("Voley", 13)); //TODO: ID do time criar ciclo
                    spinnerArray.add(new StringWithTag("Ha", 12)); //TODO: ID do time criar ciclo

                    adapter = new ArrayAdapter<>(
                            HomeAdmActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
            } catch (Exception e) {
                Toast.makeText(HomeAdmActivity.this, "Erro em carregar os dados do usuário", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerSelectPerfil.setAdapter(adapter);
            mSpinnerSelectPerfil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        startActivity(new Intent(HomeAdmActivity.this, HomeUserActivity.class));
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
