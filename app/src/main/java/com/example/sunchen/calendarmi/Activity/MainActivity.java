package com.example.sunchen.calendarmi.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Fragment.AllGoalsFrag;
import com.example.sunchen.calendarmi.Fragment.CalendarFrag;
import com.example.sunchen.calendarmi.Fragment.GoalsFrag;
import com.example.sunchen.calendarmi.Fragment.HistoryFrag;
import com.example.sunchen.calendarmi.Fragment.SettingFrag;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;
import com.example.sunchen.calendarmi.Service.TodayGoalUpdateService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

import com.google.android.libraries.places.api.Places;
import com.google.android.gms.common.api.Status;

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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Notification
    private NotificationManagerCompat mNotificationManagerCompat;
    private DrawerLayout mMainRelativeLayout;

    public static final int NEW_GOAL_REQUEST_CODE = 1;

    private GoogleApiClient mGoogleApiClient;

    private CalendarFrag calendarFrag;
    private SettingFrag settingFrag;
    private AllGoalsFrag allGoalsFrag;
    private GoalsFrag goalsFrag;
    private HistoryFrag historyFrag;
    private TextView email_view;
    private TextView name_view;
    private User user;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    GoogleSignInAccount signInAccount;

    OkHttpClient client = new OkHttpClient();

    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = new User();

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

//                generateNotification();

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

        name_view = navigationView.getHeaderView(0).findViewById(R.id.drawer_nav_name);
        email_view = navigationView.getHeaderView(0).findViewById(R.id.drawer_nav_email);
        name_view.setText(user.getCurrentUser().getName());
        email_view.setText(user.getCurrentUser().getEmail());

        //Initialize the fragment
        calendarFrag = new CalendarFrag();
        settingFrag = new SettingFrag();
        allGoalsFrag = new AllGoalsFrag();
        goalsFrag = new GoalsFrag();
        historyFrag = new HistoryFrag();

//        TodayGoalUpdateService updateService = new TodayGoalUpdateService();
        Intent startServiceIntent = new Intent(getApplicationContext(), TodayGoalUpdateService.class);
        Log.i("tag1", "in activity");
        getApplicationContext().startService(startServiceIntent);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oauth_request_id_google))
                .requestEmail()
                .requestServerAuthCode(getString(R.string.oauth_request_id_google))
                .requestId()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (User.getCurrentUser().getmGoogleSignInClient() != null) {
            mGoogleSignInClient = User.getCurrentUser().getmGoogleSignInClient();
        }

        try {
            placeInit();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mAuth = FirebaseAuth.getInstance();

        //Set to Main fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frames, goalsFrag);
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
//        mGoogleApiClient.disconnect();
        super.onStop();
    }


//    public void generateNotification(String title, int id) {
//        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "s")
//                .setSmallIcon(R.drawable.ic_mood_black_24dp)
//                .setContentTitle(title)
//                .setContentText("Stick to your goals today!")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(title))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        createNotificationChannel();
//
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "s")
////                .setSmallIcon(R.drawable.ic_map_black_24dp)
////                .setContentTitle("Implement your daily goal!")
////                .setContentText("There are five grocery stores nearby. Click here to check when you are free.")
////                .setStyle(new NotificationCompat.BigTextStyle()
////                        .bigText("There are five grocery stores nearby. Click here to check when you are free."))
////                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        createNotificationChannel();
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        notificationManager.notify(id, builder2.build());
////        notificationManager.notify(1002, builder.build());
//
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "s";
//            String description = "Easy";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("s", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

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
            Fragment fragment = settingFrag;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frames, fragment);
            ft.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
//        if (mAuth.getCurrentUser() != null) {
//            final FirebaseUser user = mAuth.getCurrentUser();
//
//            if (calendarFrag.getIsSyncCalendar()) {
//                try {
//                    Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
//
//                    while(!task.isComplete()) {
//                    }
//
//                    signInAccount = task.getResult(ApiException.class);
//                    User.getCurrentUser().setmGoogleSignInClient(mGoogleSignInClient);
//                    calendarFrag.sendAuthInfo(signInAccount.getServerAuthCode(), signInAccount.getEmail());
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
//        }
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
                Log.e("checked Email", signInAccount.getEmail());
                if (signInAccount.getEmail().equals(User.getCurrentUser().getEmail())) {
                    User.getCurrentUser().setmGoogleSignInClient(mGoogleSignInClient);
                    firebaseAuthWithGoogle(signInAccount);
                } else {
                    mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Log.e("Testing", "Try to do it");
                            mAuth.signOut();
                        }
                    });
                    Toast.makeText(MainActivity.this, "Please log the same email as account!", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("Checked", "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        } else if (requestCode == 222 || requestCode == 223 || requestCode == 224) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                Log.i("place", "Place: " + place.getName() + ", " + place.getId());

//                getPlaceById(place.getId());
                String placeName = place.getName();
                setPlaceTask(placeName, requestCode);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("place", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void setPlaceTask(final String placeName, int requestCode) {
        final String type;
        switch (requestCode) {
            case 222:
                type = "home";
                break;
            case 223:
                type = "school";
                break;
            case 224:
                type = "working place";
                break;
            default:
                type = "home";
                break;
        }
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("email", User.getCurrentUser().getEmail());
                builder.add("place", placeName);
                builder.add("type", type);

                String responseResult = "";
                try {
                    int url = R.string.set_place_server_link;
                    String link = getString(url);
                    Log.e("CheckedC:", link);
                    responseResult = post(link, builder.build());
                    System.out.println("responseResult: "+responseResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return responseResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("CHecked for answer:", s);
                Toast.makeText(MainActivity.this, "Set place successfully", Toast.LENGTH_LONG).show();
            }
        };
        atask.execute();
    }
    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .header("Connection", "close")
                .build();
        System.out.println("before newCall");
        String res = "";
        while (res.equals("")) {
            try (Response response = client.newCall(request).execute()) {
                res = response.body().string();
                System.out.println("post response: "+res);

            }
        }
        return res;
    }

    public void getPlaceById(String placeId) {
//        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
//                .setResultCallback(new ResultCallback<PlaceBuffer>() {
//                    @Override
//                    public void onResult(PlaceBuffer places) {
//                        if (places.getStatus().isSuccess()) {
//                            final Place myPlace = places.get(0);
//                            LatLng queriedLocation = myPlace.getLatLng();
//                            Log.v("Latitude is", "" + queriedLocation.latitude);
//                            Log.v("Longitude is", "" + queriedLocation.longitude);
//                        }
//                        places.release();
//                    }
//                });
    }

    public void placeInit() throws PackageManager.NameNotFoundException {
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, null)
//                .build();

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyBAnPWGqAiqris1VXMhISCYkLCv_hRcG5U");

    // Create a new Places client instance.
        placesClient = Places.createClient(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
