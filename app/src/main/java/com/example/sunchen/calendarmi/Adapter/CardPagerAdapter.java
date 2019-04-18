package com.example.sunchen.calendarmi.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.EditActivity;
import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
* Refer to "https://rubensousa.github.io/2016/08/viewpagercards"
* */
public class CardPagerAdapter extends PagerAdapter implements CardAdapter {
    static final int EDIT_GOAL_REQUEST_CODE = 111;  // The request code
    private List<CardView> mViews;
    private List<TodayGoal> mData;
    private float mBaseElevation;
    private Context context;
    private FirebaseAuth mAuth;

    OkHttpClient client = new OkHttpClient();

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(TodayGoal item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_goal_homescreen, container, false);
        mAuth = FirebaseAuth.getInstance();

        Button editButton = view.findViewById(R.id.editButton_homescreen);
        final TextView titleView = view.findViewById(R.id.title_TextView_goal_home_screen);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("name", titleView.getText().toString());

                        if (mAuth.getCurrentUser().getEmail() != null) {
                            builder.add("email", mAuth.getCurrentUser().getEmail());
                        } else {
                            builder.add("email", "");
                        }
                        Log.e("CheckedC:", mAuth.getCurrentUser().getEmail());

                        String responseResult = "";
                        try {
                            int url = R.string.searchgoal_server_link;
                            String link = context.getString(url);
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
                        if (s.contains("Successfully")) {
                            Intent intent = new Intent(context, EditActivity.class);
                            String[] results = s.split(";");
                            intent.putExtra("name", results[0]);
                            intent.putExtra("description", results[1]);
                            intent.putExtra("frequency", results[2]);
                            intent.putExtra("importance", results[3]);
                            intent.putExtra("location", results[4]);
                            intent.putExtra("until", results[5]);
                            intent.putExtra("schedule", results[6]);
                            //Add the response result
                            ((Activity)context).startActivityForResult(intent, EDIT_GOAL_REQUEST_CODE);
                        }
                    }
                };
                atask.execute();
            }
        });

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView_home_screen);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(TodayGoal item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.title_TextView_goal_home_screen);
        titleTextView.setText(item.getTitle());

        TextView frequencyTextView = (TextView) view.findViewById(R.id.frequency_TextView_goal_home_screen);
        frequencyTextView.setText(item.getFrequency());

        TextView locationTextView = (TextView) view.findViewById(R.id.location_TextView_goal_home_screen);
        locationTextView.setText(item.getLocation());

        ShineButton sb = view.findViewById(R.id.like_button_goal_home_screen);
        if (context != null) {
            sb.init((Activity) context);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        System.out.println("before newCall");
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            System.out.println("post response: "+res);
            return res;
        }
    }

}
