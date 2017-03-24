package ru.gymthing.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.gymthing.model.Workout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Karl on 20.02.2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NewDB.db";
    private static final String TABLE_NAME = "workouts";

    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_DAY = "day";
    private static final String KEY_FINISHED = "finished";
    private static final String KEY_DATE = "date";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CONTENT + " VARCHAR(500), "
                    + KEY_COMMENTS + " VARCHAR(1000), "
                    + KEY_DAY + " VARCHAR(10), "
                    + KEY_FINISHED + " INTEGER, "
                    + KEY_DATE + " DATE" + ")";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "Creating table");
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void truncateDB() {
        Log.d("Database", "Truncating DB");
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("VACUUM " + TABLE_NAME);
    }

    public boolean insertIntoDB(Workout workout) {
        Log.d("Database", "Inserting into database " + workout);
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CONTENT, workout.getContent());
            values.put(KEY_COMMENTS, workout.getComments());
            values.put(KEY_DAY, workout.getDay());
            values.put(KEY_FINISHED, workout.getFinishedCode());
            values.put(KEY_DATE, String.valueOf(workout.getDate()));
            db.insert(TABLE_NAME, null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateWorkoutDB(Workout workout) {
        Log.d("Database", "Updating database values of " + workout);
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CONTENT, workout.getContent());
        cv.put(KEY_COMMENTS, workout.getComments());
        cv.put(KEY_DAY, workout.getDay());
        cv.put(KEY_DATE, String.valueOf(workout.getDate()));
        cv.put(KEY_FINISHED, workout.getFinishedCode());
        db.update(TABLE_NAME, cv, "date='" + workout.getDate() + "'", null);
    }

    public ArrayList<Workout> readFromDatabaseWithFinishedValueOf(int finishedValue) {
        return readFromDB("SELECT  * FROM " + TABLE_NAME + " WHERE finished=" + finishedValue);
    }

    public ArrayList<Workout> readSpecificFromDB(String day) {
        return readFromDB("SELECT * FROM " + TABLE_NAME + " WHERE date='" + day + "'");
    }

    public ArrayList<Workout> readFromDB(String selectQuery) {
        Log.d("Database", "Reading from database");
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Workout> list = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        if (cursor == null) {
            Log.d("Database", "Database is empty");
            return new ArrayList<>();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    Workout newWorkout = new Workout();
                    newWorkout.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                    newWorkout.setComments(cursor.getString(cursor.getColumnIndex(KEY_COMMENTS)));
                    newWorkout.setDay(cursor.getString(cursor.getColumnIndex(KEY_DAY)));
                    newWorkout.setFinished(cursor.getInt(cursor.getColumnIndex(KEY_FINISHED)));

                    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    try {
                        Date date = formatter.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                        newWorkout.setDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    list.add(newWorkout);
                    //Log.d("Database", "query from database: " + newWorkout);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return list;
        }
    }

    public int calcDate(int margin) {
        Calendar cal0 = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();

        cal0.set(2015, 01, 01);
        if (margin == 1) {
            cal1.add(Calendar.DAY_OF_MONTH, -1);
        } else if (margin == 2) {
            cal1.add(Calendar.WEEK_OF_MONTH, -1);
        } else if (margin == 3) {
            cal1.add(Calendar.MONTH, -1);
        }

        return (int) ((cal1.getTime().getTime() - cal0.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}