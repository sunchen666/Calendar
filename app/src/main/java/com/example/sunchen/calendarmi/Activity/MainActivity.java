package com.example.sunchen.calendarmi.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.example.sunchen.calendarmi.Fragment.AllGoalsFrag;
import com.example.sunchen.calendarmi.Fragment.CalendarFrag;
import com.example.sunchen.calendarmi.Fragment.GoalsFrag;
import com.example.sunchen.calendarmi.Fragment.LoginFrag;
import com.example.sunchen.calendarmi.Fragment.PreferenceFrag;
import com.example.sunchen.calendarmi.Fragment.SettingFrag;
import com.example.sunchen.calendarmi.Fragment.TestFrag1;
import com.example.sunchen.calendarmi.Fragment.TestFrag2;
import com.example.sunchen.calendarmi.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CalendarFrag calendarFrag;
    private SettingFrag settingFrag;
    private AllGoalsFrag allGoalsFrag;
    private  GoalsFrag goalsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //Commit to change the fragment
                PreferenceFrag frag = new PreferenceFrag();
                Bundle bundle = new Bundle();
                bundle.putString("goal", "");
                frag.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frames, frag);
                ft.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initialize the fragment
        calendarFrag = new CalendarFrag();
        settingFrag = new SettingFrag();
        allGoalsFrag = new AllGoalsFrag();
        goalsFrag = new GoalsFrag();

        //Set to Main fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frames, goalsFrag);
        ft.commit();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        //Using fragments
        Fragment fragment = goalsFrag;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_activity:
                fragment = goalsFrag;
                break;
            case R.id.nav_history:
                break;
            case R.id.nav_setting:
                fragment = settingFrag;
                break;
            case R.id.nav_specific_area:
                fragment = allGoalsFrag;
                break;
            case R.id.nav_sync_cal:
                fragment = calendarFrag;
                break;
        }

        //Commit to change the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frames, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startCalendarFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, calendarFrag)
                .commit();
    }
//    public void goToPreference(String goal) {
//        PreferenceFrag frag = new PreferenceFrag();
//        Bundle bundle = new Bundle();
//        bundle.putString("goal", goal);
//        frag.setArguments(bundle);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.goal_grid, frag);
//        ft.commit();
//    }
//    public void saveNewGoal(String newGoal) {
//        goalsFrag.goalsText.add(newGoal);
//        goalsFrag.render();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_frames, goalsFrag);
//        ft.commit();
//    }
//    public void changeOldGoal(String oldGoal, String newGoal) {
//        for (int i = 0; i < goalsFrag.goalsText.size(); i ++) {
//            if (goalsFrag.goalsText.get(i).equals(oldGoal)) {
//                goalsFrag.goalsText.remove(i);
//                goalsFrag.goalsText.add(i, newGoal);
//                break;
//            }
//        }
//        goalsFrag.render();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_frames, goalsFrag);
//        ft.commit();
//    }

}
