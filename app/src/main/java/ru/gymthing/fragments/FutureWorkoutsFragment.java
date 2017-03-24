package ru.gymthing.fragments;

/**
 * Created by Mart on 20/03/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import ru.gymthing.R;
import ru.gymthing.WorkoutActivity;
import ru.gymthing.model.Workout;
import ru.gymthing.utils.DBHelper;
import ru.gymthing.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with Cards.
 */
public class FutureWorkoutsFragment extends Fragment {
    private static ContentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView name;
        public TextView description;
        public ImageButton finishedWorkoutButton;
        public ImageButton skippedWorkoutButton;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.future_workout_item, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            finishedWorkoutButton = (ImageButton) itemView.findViewById(R.id.workout_complete_button);
            skippedWorkoutButton = (ImageButton) itemView.findViewById(R.id.workout_skip_button);
        }

    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static int LENGTH = 7;
        private static List<Workout> workouts = new ArrayList<>();
        private final Drawable[] mPlacePictures;

        public ContentAdapter(Context context) {
            workouts = new DBHelper(context).readFromDatabaseWithFinishedValueOf(0);
            TypedArray a = context.getResources().obtainTypedArray(R.array.places_picture);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (workouts.size() > 0) {
                Workout workout = workouts.get(position);
                holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
                holder.name.setText(workout.getDay());
                holder.description.setText(workout.getContent());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), WorkoutActivity.class);
                        intent.putExtra("Workout", workout);
                        view.getContext().startActivity(intent);
                    }
                });
                if (position > 0) {
                    holder.finishedWorkoutButton.setVisibility(View.INVISIBLE);
                    holder.skippedWorkoutButton.setVisibility(View.INVISIBLE);
                }
                holder.finishedWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setFinishedStatus(view.getContext(), position, workout, 1);
                    }
                });
                holder.skippedWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setFinishedStatus(view.getContext(), position, workout, -1);
                    }
                });
            }
        }

        private static void setFinishedStatus(Context context, int position, Workout workout, int finishedValue) {
            DBHelper dbHelper = new DBHelper(context);
            Workout newWorkout = new Workout(workout.getContent(), workout.getDay(),
                    DateUtil.addDays(workout.getDate(), 7));

            workout.setFinished(finishedValue);
            dbHelper.updateWorkoutDB(workout);
            dbHelper.insertIntoDB(newWorkout);
            workouts.remove(workout);
            workouts.add(newWorkout);
            if (finishedValue == 1) FinishedWorkoutsFragment.ContentAdapter.getWorkouts().add(workout);
            else SkippedWorkoutsFragment.ContentAdapter.getWorkouts().add(workout);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, workouts.size());
        }


        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
