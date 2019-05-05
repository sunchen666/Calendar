package com.example.sunchen.calendarmi.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Adapter.AllGoalsAdapter;
import com.example.sunchen.calendarmi.Adapter.HistoryGoalAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.HistoryGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFrag extends Fragment {
    private HistoryGoalAdapter adapter;

    OkHttpClient client = new OkHttpClient();
    List<HistoryGoal> clist = new ArrayList<>();
    String previousHistoryString = "";

    public HistoryFrag() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView rv = view.findViewById(R.id.history_card_list);
        rv.setHasFixedSize(true);

        //Create a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        fetchHistory();

        adapter = new HistoryGoalAdapter(clist);
        adapter.setContext(getActivity());

        rv.setAdapter(adapter);

        return view;
    }

    private void initInfo() {
        HistoryGoal hg1 = new HistoryGoal();
        hg1.setTitle("Play switch");
        hg1.setUntil("04/20/2022");

        HistoryGoal hg2 = new HistoryGoal();
        hg2.setTitle("Prepare for interviews of full-time jobs");
        hg2.setUntil("2017.3.5");

        HistoryGoal hg3 = new HistoryGoal();
        hg3.setTitle("Run from home to College");
        hg3.setUntil("2018.5.5");

        clist.add(hg1);
        clist.add(hg2);
        clist.add(hg3);
    }
    private void fetchHistory() {
        final Semaphore semp = new Semaphore(0);
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("email", User.getCurrentUser().getEmail());
                int url = R.string.history_server_link;
                String responseResult = "";
                try {
                    responseResult = post(getString(url), builder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!responseResult.equals(previousHistoryString)) {
                    previousHistoryString = responseResult;
                    if (responseResult.endsWith("\n")) {
                        responseResult = responseResult.substring(0, responseResult.length() - 1);
                    }
                    String[] goalStrings = responseResult.split(";;");
                    clist.clear();
                    for (String goalString : goalStrings) {
                        if (goalString.equals("")) {
                            continue;
                        }
                        System.out.println("cur history string: "+goalString);
                        clist.add(HistoryGoal.getFromString(goalString));
                    }
                }
                semp.release();
                return responseResult;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        };
        atask.execute();
        try {
            semp.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .header("Connection", "close")
                .build();
        System.out.println("before newCall");
        String res = "";
//        while (res.equals("")) {
            try (Response response = client.newCall(request).execute()) {

                res = response.body().string();
                System.out.println("post response: " + res);

            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
        return res;
    }
}
