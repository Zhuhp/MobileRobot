package com.timyrobot.ui.activity.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.robot.R;

/**
 * 引导activity
 * Created by zhangtingting on 15/9/27.
 */
public class GuideActivity extends Activity{

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager)findViewById(R.id.vp_guide);
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);
    }

    private class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.item_guide_viewpager, container, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.iv_guide_item);
            return super.instantiateItem(container, position);
        }


    }
}
