package com.ournet.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.data.ILocation;
import com.ournet.weather.data.Place;
import com.ournet.weather.fragments.BaseFragment;
import com.ournet.weather.fragments.ForecastReportFragment;
import com.ournet.weather.fragments.PlacesFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnPlaceChanged, ViewPager.OnPageChangeListener, OnLoadingTask {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    //    private Toolbar toolbar;
    protected Place place;
    protected UserPlaces places;
    private LinearLayout pager_indicator;
    private ImageView[] dots;
    private TextView pageTitle;
    private TextView pageSubTitle;
    private View refreshButton;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        pageTitle = (TextView) findViewById(R.id.appbar_title);
        pageSubTitle = (TextView) findViewById(R.id.appbar_subtitle);
        refreshButton = findViewById(R.id.appbar_refresh_btn);
        loadingIndicator = findViewById(R.id.appbar_loading_indicator);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mViewPager.addOnPageChangeListener(this);

        setUiPageViewController();

//        mViewPager.addOnPageChangeListener();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    private void setUiPageViewController() {

        int dotsCount = mSectionsPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setClickable(true);
            dots[i].setTag(i);
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem((int) v.getTag());
                }
            });
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    public void setPlace(Place place) {
        if (place == null) {
            return;
        }
        this.place = place;

        String title = Utils.name(place);
        String subTitle = Utils.regionName(place);

        pageTitle.setText(title);
        pageSubTitle.setText(subTitle);
    }

    public void goToForecast() {
        mViewPager.setCurrentItem(1);
    }

    public void goToPlaces() {
        mViewPager.setCurrentItem(0);
    }

    public void onClickRefreshForecast(View v) {
//        refreshButton.setVisibility(View.GONE);
//        refreshButton.setClickable(false);
        mSectionsPagerAdapter.getForecastReportFragment().refreshForecastForecast();
//        refreshButton.setClickable(true);
//        refreshButton.setVisibility(View.VISIBLE);
//        refreshButton.setEnabled(true);
    }

    @Override
    public void onPlaceChanged(Place place) {
        setPlace(place);

        if (place != null) {
            mFirebaseAnalytics.setUserProperty("placeId", place.id.toString());
            mFirebaseAnalytics.setUserProperty("placeName", place.name);
            mFirebaseAnalytics.setUserProperty("country_code", place.country_code);
        }

        goToForecast();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStartLoadingTask() {
//        refreshButton.setClickable(false);
    }

    @Override
    public void onEndLoadingTask() {
//        refreshButton.setClickable(true);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        PlacesFragment placesFragment;
        ForecastReportFragment forecastReportFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ForecastReportFragment getForecastReportFragment() {
            return forecastReportFragment;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (placesFragment == null) {
                        placesFragment = new PlacesFragment();
                        placesFragment.addOnLoadingTasks(MainActivity.this);
                        placesFragment.addOnPlaceChanged(MainActivity.this);
                    }
                    return placesFragment;

                case 1:
                    if (forecastReportFragment == null) {
                        forecastReportFragment = new ForecastReportFragment();
                        forecastReportFragment.addOnLoadingTasks(MainActivity.this);
                        placesFragment.addOnPlaceChanged(forecastReportFragment);
                    }
                    return forecastReportFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
