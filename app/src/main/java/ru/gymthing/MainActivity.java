package ru.gymthing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import ru.gymthing.fragments.FutureWorkoutsFragment;
import ru.gymthing.fragments.FinishedWorkoutsFragment;
import ru.gymthing.fragments.SkippedWorkoutsFragment;
import ru.gymthing.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FinishedWorkoutsFragment(), "Finished");
        adapter.addFragment(new FutureWorkoutsFragment(), "Schedule");
        adapter.addFragment(new SkippedWorkoutsFragment(), "Skipped");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        //viewPager.setCurrentItem(1);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        // lul workaround method to make notifydataset work
        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when notifyDataSetChanged is called
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_schedule:
                startActivity(new Intent(getApplicationContext(), WorkoutCreaterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new DBHelper(this).closeDB();
    }
}
