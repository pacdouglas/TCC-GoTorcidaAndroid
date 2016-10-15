package br.com.gotorcida.gotorcida.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.fragment.AboutFragment;
import br.com.gotorcida.gotorcida.fragment.EventsFragment;
import br.com.gotorcida.gotorcida.fragment.MatchesTableFragment;
import br.com.gotorcida.gotorcida.fragment.MyTeamHereFragment;
import br.com.gotorcida.gotorcida.fragment.UserTeamsFragment;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btnEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_my_teams) {
            fragmentManager.beginTransaction()
                    .replace(R.id.dashboard_frame_fragment, new UserTeamsFragment())
                    .commit();
        } else if (id == R.id.nav_events) {
            fragmentManager.beginTransaction()
                    .replace(R.id.dashboard_frame_fragment, new EventsFragment())
                    .commit();
        } else if (id == R.id.nav_matches_table) {
            fragmentManager.beginTransaction()
                    .replace(R.id.dashboard_frame_fragment, new MatchesTableFragment())
                    .commit();
        } else if (id == R.id.nav_my_team_here) {
            fragmentManager.beginTransaction()
                    .replace(R.id.dashboard_frame_fragment, new MyTeamHereFragment())
                    .commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.dashboard_frame_fragment, new AboutFragment())
                    .commit();
        } else if (id == R.id.nav_logout) {
            SaveSharedPreference.clearUserName(DashboardActivity.this);
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
