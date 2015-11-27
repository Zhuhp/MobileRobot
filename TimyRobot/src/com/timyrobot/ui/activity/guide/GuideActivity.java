package com.timyrobot.ui.activity.guide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robot.R;

import java.util.ArrayList;

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
        GuideAdapter adapter = new GuideAdapter(this);
        mViewPager.setAdapter(adapter);
    }

    private class GuideAdapter extends PagerAdapter{

        private ArrayList<Integer> mImgRes;
        Context mContext;

        public GuideAdapter(Context context){
            mContext = context;
            mImgRes = new ArrayList<>();
            mImgRes.add(R.drawable.bg_guide_one);
            mImgRes.add(R.drawable.bg_guide_two);
            mImgRes.add(R.drawable.bg_guide_three);
            mImgRes.add(R.drawable.bg_guide_four);
        }

        @Override
        public int getCount() {
            return mImgRes.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_guide_viewpager, null, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.iv_guide_item);
            imageView.setImageResource(mImgRes.get(position));
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

    }
}
