package ru.gymthing.fragments;

/**
 * Created by Mart on 20/03/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ru.gymthing.R;
import ru.gymthing.WorkoutActivity;
import ru.gymthing.utils.DBHelper;
import ru.gymthing.model.Workout;
import ru.gymthing.utils.DateUtil;

import java.util.List;

/**
 * Provides UI for the view with Tiles.
 */
public class SkippedWorkoutsFragment extends Fragment {
    private static ContentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.finished_workout_item, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static int LENGTH;
        private static List<Workout> workouts;
        private final Drawable[] mPlacePictures;

        public ContentAdapter(Context context) {
            workouts = new DBHelper(context).readFromDatabaseWithFinishedValueOf(-1);
            LENGTH = workouts.size();
            Resources resources = context.getResources();
            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
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
            if (LENGTH > 0) {
                Workout workout = workouts.get(position);
                holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
                holder.name.setText(DateUtil.getProperDateFormat(workout.getDate()));
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), WorkoutActivity.class);
                    intent.putExtra("Workout", workout);
                    v.getContext().startActivity(intent);
                });
            }
        }

        public static List<Workout> getWorkouts() {
            return workouts;
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