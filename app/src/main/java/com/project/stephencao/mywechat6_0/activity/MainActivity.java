package com.project.stephencao.mywechat6_0.activity;

import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import com.project.stephencao.mywechat6_0.R;
import com.project.stephencao.mywechat6_0.fragment.TabFragment;
import com.project.stephencao.mywechat6_0.view.MyBottomBarView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = new String[]{"Wechat", "Contacts", "Discovery", "Me"};
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<MyBottomBarView> mMyBottomBarViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowButtonAlwaysDisplay();
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int i1) {
                if(positionOffset>0){
                    MyBottomBarView left = mMyBottomBarViews.get(position);
                    MyBottomBarView right = mMyBottomBarViews.get(position + 1);
                    left.setIconAndTextAlphaValue(1-positionOffset);
                    right.setIconAndTextAlphaValue(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, mTitles[i]);
            tabFragment.setArguments(bundle);
            mFragments.add(tabFragment);
        }
        mFragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }


    private void initView() {
        mViewPager = findViewById(R.id.id_viewpager);
        MyBottomBarView wechatView = findViewById(R.id.id_bottom_bar_wechat);
        MyBottomBarView contactsView = findViewById(R.id.id_bottom_bar_contact);
        MyBottomBarView discoveryView = findViewById(R.id.id_bottom_bar_discovery);
        MyBottomBarView meView = findViewById(R.id.id_bottom_bar_me);
        mMyBottomBarViews.add(wechatView);
        mMyBottomBarViews.add(contactsView);
        mMyBottomBarViews.add(discoveryView);
        mMyBottomBarViews.add(meView);
        wechatView.setOnClickListener(this);
        contactsView.setOnClickListener(this);
        discoveryView.setOnClickListener(this);
        meView.setOnClickListener(this);
        wechatView.setIconAndTextAlphaValue(1.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void setOverflowButtonAlwaysDisplay() {
        ViewConfiguration configuration = ViewConfiguration.get(this);
        try {
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(configuration, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bottom_bar_wechat: {
                resetAllIconAndTextColor();
                mMyBottomBarViews.get(0).setIconAndTextAlphaValue(1.0f);
                mViewPager.setCurrentItem(0);
                break;
            }
            case R.id.id_bottom_bar_contact: {
                resetAllIconAndTextColor();
                mMyBottomBarViews.get(1).setIconAndTextAlphaValue(1.0f);
                mViewPager.setCurrentItem(1);
                break;
            }
            case R.id.id_bottom_bar_discovery: {
                resetAllIconAndTextColor();
                mMyBottomBarViews.get(2).setIconAndTextAlphaValue(1.0f);
                mViewPager.setCurrentItem(2);
                break;
            }
            case R.id.id_bottom_bar_me: {
                resetAllIconAndTextColor();
                mMyBottomBarViews.get(3).setIconAndTextAlphaValue(1.0f);
                mViewPager.setCurrentItem(3);
                break;
            }
        }
    }

    public void resetAllIconAndTextColor() {
        for (MyBottomBarView view : mMyBottomBarViews) {
            view.setIconAndTextAlphaValue(0.0f);
        }

    }
}
