package com.example.sunchen.calendarmi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sunchen.calendarmi.R;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;
import io.github.dreierf.materialintroscreen.animations.IViewTranslation;


public class OnBoardingActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder().
                        backgroundColor(R.color.first_slide_background).
                        buttonsColor(R.color.first_slide_buttons).
                        title("No time to clean up your table?").
                        description("(Use our application to remind you monthly!)").
                        image(R.mipmap.ic_cleanup_foreground).
                        build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OnBoardingActivity.this, EditActivity.class);
                        intent.putExtra("name", "Clean Table");
                        intent.putExtra("description", "Clean dining table every day!");
                        intent.putExtra("frequency", "Daily");
                        intent.putExtra("importance", "Low");
                        intent.putExtra("location", "Home");
                        intent.putExtra("until","02/28/2020");
                        intent.putExtra("schedule", "Monday");
                        //Add the response result
                        startActivity(intent);
                    }
                }, "Yes"));

        addSlide(new SlideFragmentBuilder().
                backgroundColor(R.color.second_slide_background).
                buttonsColor(R.color.second_slide_buttons).
                title("Would you like to read more books?").
                description("(Set goal to remind for your daily progress!)").
                        image(R.mipmap.ic_books_foreground).
                        build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OnBoardingActivity.this, EditActivity.class);
                        intent.putExtra("name", "Read Books");
                        intent.putExtra("description", "Read Books every day!");
                        intent.putExtra("frequency", "Daily");
                        intent.putExtra("importance", "Low");
                        intent.putExtra("location", "Home");
                        intent.putExtra("until","02/28/2020");
                        intent.putExtra("schedule", "Tuesday");
                        //Add the response result
                        startActivity(intent);
                    }
                }, "Yes"));

        addSlide(new SlideFragmentBuilder().
                        backgroundColor(R.color.third_slide_background).
                        buttonsColor(R.color.third_slide_buttons).
                        title("Do you want to become healthier?").
                        description("(Try to exercise every week!)").
                        image(R.mipmap.ic_exercise_foreground).
                        build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OnBoardingActivity.this, EditActivity.class);
                        intent.putExtra("name", "Exercise");
                        intent.putExtra("description", "Strengthen your body every day!");
                        intent.putExtra("frequency", "Daily");
                        intent.putExtra("importance", "Low");
                        intent.putExtra("location", "Home");
                        intent.putExtra("until","02/28/2021");
                        intent.putExtra("schedule", "Monday");
                        //Add the response result
                        startActivity(intent);
                    }
                }, "Yes"));

        addSlide(new SlideFragmentBuilder().
                        backgroundColor(R.color.fourth_slide_background).
                        buttonsColor(R.color.fourth_slide_buttons).
                        title("Welcome to Improov!").
                        build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
