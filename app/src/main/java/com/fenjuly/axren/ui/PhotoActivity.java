package com.fenjuly.axren.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.Picture;
import com.fenjuly.axren.ui.fragment.PhotoFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aisen.android.common.utils.KeyGenerator;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by liurongchan on 16/3/22.
 */
public class PhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ViewPager mViewPager;

    MyViewPagerAdapter myViewPagerAdapter;

    List<Picture> pictures;
    int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            final Type pictureListType = new TypeToken<List<Picture>>() {
            }.getType();
            Gson gson = new Gson();
            pictures = savedInstanceState == null ? (List<Picture>) gson.fromJson(intent.getStringExtra("pictures"), pictureListType)
                    : (List<Picture>) gson.fromJson(savedInstanceState.getString("pictures"), pictureListType);
            Log.e("pictures", pictures.toString());
            index = savedInstanceState == null ? intent.getIntExtra("index", 0)
                    : savedInstanceState.getInt("index");
        }
        setContentView(R.layout.photo_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(String.format("%d/%d", index + 1, pictures.size()));
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.setCurrentItem(index);
        mViewPager.setOnPageChangeListener(this);
    }

    protected Fragment newFragment(int position) {
        return PhotoFragment.newInstance(getPicture(position).getThumbnail_pic().replace("thumbnail", "large"));
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(makeFragmentName(position));
            if (fragment == null) {
                fragment = newFragment(position);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            if (pictures != null) {
                return pictures.size();
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(makeFragmentName(position));
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        protected String makeFragmentName(int position) {
            return KeyGenerator.generateMD5(getPicture(position).getThumbnail_pic());
        }

    }

    private Picture getPicture(int position) {
        if (pictures != null) {
            return pictures.get(position);
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putString("pictures", (new Gson()).toJson(pictures));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle((String.format("%d/%d", position + 1, pictures.size())));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
