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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.sunchen.calendarmi.Activity.EditActivity;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.HistoryGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.Others.DescriptionPopUpWIndow;
import com.example.sunchen.calendarmi.R;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import razerdp.basepopup.QuickPopupBuilder;

public class HistoryGoalAdapter extends RecyclerView.Adapter<HistoryGoalAdapter.HistoryGoalViewHolder>  {
    private List<HistoryGoal> historyGoalList;
    OkHttpClient client = new OkHttpClient();
    private Context context;

    public HistoryGoalAdapter(List<HistoryGoal> list) {
        this.historyGoalList = list;
    }

    @NonNull
    @Override
    public HistoryGoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_history,viewGroup,false);
        return new HistoryGoalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryGoalViewHolder historyGoalViewHolder, int i) {
        HistoryGoal cg = historyGoalList.get(i);


        historyGoalViewHolder.title_history.setTitle(cg.getHistory_title());
//        historyGoalViewHolder.period.setText(cg.getPeriod());
//        historyGoalViewHolder.avg_time.setText(cg.getAvg_time());

        historyGoalViewHolder.title_history.setTitle(cg.getTitle());
        historyGoalViewHolder.tt.setText(cg.getTitle());
        historyGoalViewHolder.until.setText(cg.getUntil());

    }

    @Override
    public int getItemCount() {
        return historyGoalList.size();
    }

    class HistoryGoalViewHolder extends RecyclerView.ViewHolder{
        protected ExpandableCardView title_history;
        protected TextView tt;
        protected TextView until;
        protected Button moreButton;
        protected User currentUser;

        public HistoryGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            currentUser = new User();
            title_history = itemView.findViewById(R.id.history_profile_card);
            tt = itemView.findViewById(R.id.title_history);
            until = itemView.findViewById(R.id.until_history);
            moreButton = itemView.findViewById(R.id.button_description_history);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("name", tt.getText().toString());
                            builder.add("email", currentUser.getCurrentUser().getEmail());

                            String responseResult = "";
                            try {
                                int url = R.string.searchgoal_history_server_link;
                                String link = context.getString(url);
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
                                String[] results = s.split(";");

                                DescriptionPopUpWIndow dPopWindow = new DescriptionPopUpWIndow(context,
                                        results[0], results[1], results[2], results[3],
                                        results[4], results[5], results[6]);

                                dPopWindow.showPopupWindow();
                                //Add the response result
                            } else {
//                                Toast.makeText(context, "Loading Information Fails", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    atask.execute();
                }
            });

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
