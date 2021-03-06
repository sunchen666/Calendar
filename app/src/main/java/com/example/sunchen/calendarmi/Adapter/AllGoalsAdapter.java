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

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.sunchen.calendarmi.Activity.EditActivity;
import com.example.sunchen.calendarmi.Fragment.AllGoalsFrag;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllGoalsAdapter extends RecyclerView.Adapter<AllGoalsAdapter.CurrentGoalViewHolder> {
    static final int EDIT_GOAL_REQUEST_CODE = 111;  // The request code
    private List<CurrentGoal> currentGoalList;
    private String previousAllGoalString = "";
    OkHttpClient client = new OkHttpClient();
    private Context context;

    public AllGoalsAdapter(List<CurrentGoal> list) {
        this.currentGoalList = list;
    }

    public AllGoalsAdapter() {
        currentGoalList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CurrentGoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_allgoals,viewGroup,false);
        return new CurrentGoalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllGoalsAdapter.CurrentGoalViewHolder currentGoalViewHolder, int i) {
        CurrentGoal cg = currentGoalList.get(i);

        currentGoalViewHolder.tTitle_cv.setTitle(cg.getTitle());
        currentGoalViewHolder.tTitle.setText(cg.getTitle());
        currentGoalViewHolder.tDes.setText(cg.getDecrip());
        currentGoalViewHolder.tFre.setText(cg.getSchedule());

    }

    @Override
    public int getItemCount() {
        return currentGoalList.size();
    }

    class CurrentGoalViewHolder extends RecyclerView.ViewHolder{
        final protected ExpandableCardView tTitle_cv;
        protected TextView tTitle;
        protected TextView tDes;
        protected TextView tFre;
        protected Button editButton;
        protected Button completeButton;
        protected Button deleteButton;

        public CurrentGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            tTitle_cv = itemView.findViewById(R.id.all_goals_profile_card);
            tTitle = itemView.findViewById(R.id.tt_long_goal);
            tDes = itemView.findViewById(R.id.description_long_goal);
            tFre = itemView.findViewById(R.id.frequency_long_goal);
            editButton = itemView.findViewById(R.id.button_more_long_goal);
            completeButton = itemView.findViewById(R.id.button_complete_long_goal);
            deleteButton = itemView.findViewById(R.id.button_delete_long_goal);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("name", tTitle.getText().toString());
                            builder.add("email", User.getCurrentUser().getEmail());

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

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> ctask = new AsyncTask<String, Integer, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("status", "complete");
                            builder.add("email", User.getCurrentUser().getEmail());
                            builder.add("name", tTitle.getText().toString());

                            String responseResult = "";
                            try {
                                int url = R.string.finish_delete_allGoal_server_link;
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
                            System.out.println("complete response: "+s);
//                            if (s.contains("Successfully")) {
//                                for (int i = 0; i < currentGoalList.size(); i++) {
//                                    if (currentGoalList.get(i).getTitle().equals(tTitle.getText().toString())) {
//                                        currentGoalList.remove(i);
//                                        break;
//                                    }
//                                }
//                                AllGoalsAdapter.this.notifyDataSetChanged();
//                                Toast.makeText(context, "Complete Successfully", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(context, "Complete Fails", Toast.LENGTH_LONG).show();
//                            }

                            for (int i = 0; i < currentGoalList.size(); i++) {
                                if (currentGoalList.get(i).getTitle().equals(tTitle.getText().toString())) {
                                    currentGoalList.remove(i);
                                    break;
                                }
                            }
                            AllGoalsAdapter.this.notifyDataSetChanged();
                        }
                    };

                    ctask.execute();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> dtask = new AsyncTask<String, Integer, String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("status", "delete");
                            builder.add("email", User.getCurrentUser().getEmail());
                            builder.add("name", tTitle.getText().toString());

                            String responseResult = "";
                            try {
                                int url = R.string.finish_delete_allGoal_server_link;
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
                            System.out.println("delete response: "+ s);
//                            if (s.contains("Successfully")) {
//                                for (int i = 0; i < currentGoalList.size(); i++) {
//                                    if (currentGoalList.get(i).getTitle().equals(tTitle.getText().toString())) {
//                                        currentGoalList.remove(i);
//                                        break;
//                                    }
//                                }
//                                AllGoalsAdapter.this.notifyDataSetChanged();
//                                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(context, "Delete Fails", Toast.LENGTH_LONG).show();
//                            }
                            for (int i = 0; i < currentGoalList.size(); i++) {
                                if (currentGoalList.get(i).getTitle().equals(tTitle.getText().toString())) {
                                    currentGoalList.remove(i);
                                    break;
                                }
                            }
                            AllGoalsAdapter.this.notifyDataSetChanged();
                        }
                    };

                    dtask.execute();
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


    public void fetchAllGoals() {
        final Semaphore semp = new Semaphore(0);
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("email", User.getCurrentUser().getEmail());
                int url = R.string.allgoal_server_link;
                String responseResult = "";
                try {
                    responseResult = post(context.getString(url), builder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!responseResult.equals(previousAllGoalString)) {
                    previousAllGoalString = responseResult;
                    if (responseResult.endsWith("\n")) {
                        responseResult = responseResult.substring(0, responseResult.length() - 1);
                    }
                    String[] goalStrings = responseResult.split(";;");
                    currentGoalList.clear();
                    for (String goalString : goalStrings) {
                        if (goalString.equals("")) {
                            continue;
                        }
                        System.out.println("cur goal string: "+goalString);
                        currentGoalList.add(CurrentGoal.getFromString(goalString));
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
}
