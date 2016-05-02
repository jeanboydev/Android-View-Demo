package com.jeanboy.demo.ui.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jeanboy.demo.R;
import com.jeanboy.demo.model.entity.DummyContent;
import com.jeanboy.demo.ui.base.BaseActivity;
import com.jeanboy.demo.ui.fragment.ItemFragment;
import com.jeanboy.demo.ui.listener.OnListFragmentInteractionListener;
import com.jeanboy.demo.utils.ToolBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TabLayoutActivity extends BaseActivity implements OnListFragmentInteractionListener {


    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.tab_layout)
    TabLayout tab_layout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private MyFragmentAdapter myFragmentAdapter;
    private List<ItemFragment> pageList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    public Class getTag(Class clazz) {
        return TabLayoutActivity.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tab_layout;
    }

    @Override
    public void setupView() {
        getToolbarTitleView().setText("TabLayout");

        ToolBarUtil.setToolbarTabLayout(this);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentAdapter);
        tab_layout.setupWithViewPager(viewPager);
    }

    @Override
    public void initData() {
        pageList.add(ItemFragment.newInstance(2));
        pageList.add(ItemFragment.newInstance(8));
        pageList.add(ItemFragment.newInstance(15));
        pageList.add(ItemFragment.newInstance(5));
        pageList.add(ItemFragment.newInstance(20));
        for (int i = 0; i < pageList.size(); i++) {
            titleList.add("标题" + i);
        }
        myFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        startActivity(new Intent(this, FadeOutInfoActivity.class));
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position);
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}
