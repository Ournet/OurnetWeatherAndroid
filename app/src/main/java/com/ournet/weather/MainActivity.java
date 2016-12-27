package com.ournet.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.data.ILocation;
import com.ournet.weather.data.Place;
import com.ournet.weather.fragments.BaseFragment;
import com.ournet.weather.fragments.ForecastReportFragment;
import com.ournet.weather.fragments.PlacesFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnPlaceChanged, ViewPager.OnPageChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Forecast forecast;
    private ForecastReport report;
    private BaseFragment activeFragment;
    private ForecastReportFragment reportFragment;

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

        pageTitle = (TextView) findViewById(R.id.appbar_title);
        pageSubTitle = (TextView) findViewById(R.id.appbar_subtitle);
        refreshButton = findViewById(R.id.appbar_refresh_btn);
        loadingIndicator = findViewById(R.id.appbar_loading_indicator);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mViewPager.addOnPageChangeListener(this);
        setUiPageViewController();

        goToForecast();

        this.places = new UserPlaces(this);
        this.forecast = new Forecast(this);

        explorePlace();

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

        String lang = Settings.language();
        String title = place.name(lang);
        String subTitle = place.country_code.toUpperCase();
        if (place.region != null) {
            subTitle = place.region.name(lang) + ", " + subTitle;
        }

        pageTitle.setText(title);
        pageSubTitle.setText(subTitle);


        if (activeFragment != null) {
            activeFragment.placeChanged(place);
        }

        exploreForecast(place);
    }

    public void goToForecast() {
        mViewPager.setCurrentItem(1);
    }

    public void goToPlaces() {
        mViewPager.setCurrentItem(0);
    }

    public void refreshForecast() {
        exploreForecast(place, new Date());
    }

    private void exploreForecast(Place place) {
        exploreForecast(place, null);
    }

    private void exploreForecast(Place place, Date date) {
        ForecastReport report = null;
        try {
            if (date != null) {
                report = new ReportTask().execute(place, date.getTime()).get();
            } else {
                report = new ReportTask().execute(place).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        this.report = report;
        if (reportFragment != null && report != null) {
            reportFragment.setForecastReport(report);
        }
    }

    private void explorePlace() {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            // fetch data
//        } else {
//            // display error
//        }
        Place place = places.getSelected();
        if (place == null) {
            try {
                place = new PlaceTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (place != null) {
                this.places.setSelected(place);
            }
        }

        if (place == null) {
            goToPlaces();
        } else {
            setPlace(place);
        }
    }

    public void onClickMoreForecast(View v) {
        String url = Links.Weather.place(place.country_code, place.id);
        if (url == null) {
            Log.e("main", "NO url: " + place.country_code);
        } else {
            Log.e("main", "Starging browser url: " + url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    public void onClickRefreshForecast(View v) {
        refreshForecast();
    }

    @Override
    public void placeChanged(Place place) {
        setPlace(place);
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
        if (this.reportFragment != null && position == 1) {
//            this.reportFragment.setForecastReport(report);
            exploreForecast(place);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class PlaceTask extends AsyncTask<String, Void, Place> {

        @Override
        protected Place doInBackground(String... params) {
            try {
                return Settings.exploreSelectedPlace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class ReportTask extends AsyncTask<Object, Void, ForecastReport> {

        @Override
        protected ForecastReport doInBackground(Object... params) {
            try {
                Date date = null;
                if (params.length > 1 && params[1] != null) {
                    date = new Date((long) params[1]);
                }
                return forecast.getReport((ILocation) params[0], date);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    PlacesFragment placesFragment = new PlacesFragment();
                    placesFragment.placeChanged(place);
                    placesFragment.setPlaces(places);
                    fragment = placesFragment;
                    break;
                case 1:
                    reportFragment = new ForecastReportFragment();
                    reportFragment.placeChanged(place);
                    reportFragment.setForecastReport(report);
                    fragment = reportFragment;
                    break;
            }

            activeFragment = fragment;

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
