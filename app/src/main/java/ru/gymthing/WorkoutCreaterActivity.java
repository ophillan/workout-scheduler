package ru.gymthing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import ru.gymthing.model.Workout;
import ru.gymthing.utils.DBHelper;
import ru.gymthing.utils.DateUtil;

import java.util.Date;

public class WorkoutCreaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_creater);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Create Schedule");
            toolbar.setTitleTextColor(Color.WHITE);
        }
    }

    public void submitForm(View view) {
        EditText mondayInput = (EditText) findViewById(R.id.input_monday);
        EditText tuesdayInput = (EditText) findViewById(R.id.input_tuesday);
        EditText wednesdayInput = (EditText) findViewById(R.id.input_wednesday);
        EditText thursdayInput = (EditText) findViewById(R.id.input_thursday);
        EditText fridayInput = (EditText) findViewById(R.id.input_friday);
        EditText saturdayInput = (EditText) findViewById(R.id.input_saturday);
        EditText sundayInput = (EditText) findViewById(R.id.input_sunday);
        addAllWorkoutsToDB(view, mondayInput, tuesdayInput, wednesdayInput, thursdayInput, fridayInput,
                saturdayInput, sundayInput);
        finish();
    }

    private void addAllWorkoutsToDB(View view, EditText... editTexts) {
        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        DBHelper dbHelper = new DBHelper(view.getContext());
        if (dbHelper.readFromDatabaseWithFinishedValueOf(0).size() != 0) dbHelper.truncateDB();

        Date date = new Date();
        int day = date.getDay();
        for (int i = 0; i < 7; i++, day++) {
            int index = (day - 1) % 7;
            Date date1 = DateUtil.addDays(date, i);
            dbHelper.insertIntoDB(new Workout(editTexts[index].getText().toString(), days[index], date1));
        }
    }
}
