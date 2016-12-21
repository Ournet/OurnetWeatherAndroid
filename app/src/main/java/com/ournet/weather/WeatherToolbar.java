package com.ournet.weather;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

/**
 * Created by Dumitru Cantea on 12/21/16.
 */

public class WeatherToolbar implements ViewPager.OnPageChangeListener {
    Toolbar toolbar;

    public WeatherToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle("Page..." + position);
        toolbar.setSubtitle("Subtitle..."+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
