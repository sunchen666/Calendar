package com.example.sunchen.calendarmi.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import com.example.sunchen.calendarmi.Fragment.AllGoalsFrag;
import com.example.sunchen.calendarmi.Fragment.CalendarFrag;
import com.example.sunchen.calendarmi.Fragment.GoalsFrag;
import com.example.sunchen.calendarmi.Fragment.HistoryFrag;
import com.example.sunchen.calendarmi.Fragment.SettingFrag;
import com.example.sunchen.calendarmi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Notification
    private NotificationManagerCompat mNotificationManagerCompat;
    private DrawerLayout mMainRelativeLayout;

    public static final int NEW_GOAL_REQUEST_CODE = 1;

    private CalendarFrag calendarFrag;
    private SettingFrag settingFrag;
    private AllGoalsFrag allGoalsFrag;
    private GoalsFrag goalsFrag;
    private HistoryFrag historyFrag;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    GoogleSignInAccount signInAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        mMainRelativeLayout = findViewById(R.id.drawer_layout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();

                if (!areNotificationsEnabled) {
                    // Because the user took an action to create a notification, we create a prompt to let
                    // the user re-enable notifications for this application again.
                    Snackbar snackbar = Snackbar
                            .make(
                                    mMainRelativeLayout,
                                    "You need to enable notifications for this app",
                                    Snackbar.LENGTH_LONG)
                            .setAction("ENABLE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Links to this app's notification settings
                                    openNotificationSettingsForApp();
                                }
                            });
                    snackbar.show();
                    return;
                }

                generateNotification();

                Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });

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
        historyFrag = new HistoryFrag();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oauth_request_id_google))
                .requestEmail()
                .requestServerAuthCode(getString(R.string.oauth_request_id_google))
                .requestId()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        //Set to Main fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frames, goalsFrag);
        ft.commit();
    }

    private void generateNotification() {
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "s")
                .setSmallIcon(R.drawable.ic_mood_black_24dp)
                .setContentTitle("Reminder from your schedule!")
                .setContentText("Your next meeting for IoT class will start in 10 minutes.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your next meeting for IoT class will start in 10 minutes."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "s")
                .setSmallIcon(R.drawable.ic_map_black_24dp)
                .setContentTitle("Implement your daily goal!")
                .setContentText("There are five grocery stores nearby. Click here to check when you are free.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There are five grocery stores nearby. Click here to check when you are free."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1001, builder2.build());
        notificationManager.notify(1002, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "s";
            String description = "Easy";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("s", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                fragment = historyFrag;
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

    /**
     * Helper method for the SnackBar action, i.e., if the user has this application's notifications
     * disabled, this opens up the dialog to turn them back on after the user requests a
     * Notification launch.
     *
     * IMPORTANT NOTE: You should not do this action unless the user takes an action to see your
     * Notifications like this sample demonstrates. Spamming users to re-enable your notifications
     * is a bad idea.
     */
    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        startActivity(intent);
    }

    public void syncGoogleCalendarSignin() {
        if (mAuth.getCurrentUser() != null) {
            final FirebaseUser user = mAuth.getCurrentUser();

            if (calendarFrag.getIsSyncCalendar()) {
                try {
                    Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();

                    while(!task.isComplete()) {
                    }

                    signInAccount = task.getResult(ApiException.class);
                    calendarFrag.sendAuthInfo(signInAccount.getServerAuthCode(), signInAccount.getEmail());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                signInAccount = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(signInAccount);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("Checked", "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {
                                if (calendarFrag.getIsSyncCalendar()) {
                                    calendarFrag.sendAuthInfo(signInAccount.getServerAuthCode(), signInAccount.getEmail());
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("CheckedAuthentication", "Google authentication failed");
                        }
                    }
                });
    }
}
