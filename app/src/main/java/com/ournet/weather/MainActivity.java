package com.ournet.weather;

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

import android.widget.TextView;

import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.data.ILocation;
import com.ournet.weather.data.Place;
import com.ournet.weather.fragments.BaseFragment;
import com.ournet.weather.fragments.ForecastReportFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnPlaceChanged {

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    protected Place place;
    protected UserPlaces places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);

        exploreForecast(place);
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
        if (this.activeFragment != null && ForecastReportFragment.class == this.activeFragment.getClass()) {
            ((ForecastReportFragment) this.activeFragment).setForecastReport(report);
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

        if (place != null) {
            setPlace(place);
        }
    }

    @Override
    public void placeChanged(Place place) {
        setPlace(place);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

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
                    ForecastReportFragment f = new ForecastReportFragment();
                    f.setPlace(place);
                    f.setForecastReport(report);
                    fragment = f;
                    break;
            }

            activeFragment = fragment;

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }
    }
}
