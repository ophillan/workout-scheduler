package ru.gymthing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ru.gymthing.model.Workout;
import ru.gymthing.utils.DBHelper;
import ru.gymthing.utils.DateUtil;

import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Workout workout = (Workout) getIntent().getExtras().get("Workout");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(workout.getDay());
            toolbar.setTitleTextColor(Color.WHITE);
        }

        TextView workoutDescription = (TextView) findViewById(R.id.workout_description_body);
        workoutDescription.setText(workout.getContent());

        TextView workoutDate =  (TextView) findViewById(R.id.workout_date_body);
        workoutDate.setText(String.valueOf(DateUtil.getProperDateFormat(workout.getDate())));

        if (Objects.equals(workout.getComments(), "")) {
            showCommentsBox();
            EditText commentsForm = (EditText) findViewById(R.id.workout_comments_form);
            Button button = (Button) findViewById(R.id.submit_comments);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitComments(view, workout, commentsForm);
                }
            });
        } else {
            hideCommentsForm();
            TextView workoutComments =  (TextView) findViewById(R.id.workout_comments_body);
            workoutComments.setText(workout.getComments());
        }
    }

    private void submitComments(View view, Workout workout, EditText commentsForm) {
        DBHelper dbHelper = new DBHelper(view.getContext());
        String comment = commentsForm.getText().toString();
        workout.setComments(comment);
        dbHelper.updateWorkoutDB(workout);
        TextView workoutComments =  (TextView) findViewById(R.id.workout_comments_body);
        workoutComments.setText(workout.getComments());
        hideCommentsForm();
    }

    private void hideCommentsForm() {
        findViewById(R.id.workout_comments_body).setVisibility(View.VISIBLE);
        findViewById(R.id.workout_comments_input).setVisibility(View.INVISIBLE);
        findViewById(R.id.submit_comments).setVisibility(View.INVISIBLE);
    }

    private void showCommentsBox() {
        findViewById(R.id.workout_comments_body).setVisibility(View.INVISIBLE);
        findViewById(R.id.workout_comments_input).setVisibility(View.VISIBLE);
        findViewById(R.id.submit_comments).setVisibility(View.VISIBLE);
    }
}
