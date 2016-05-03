package com.jeanboy.demo.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.jeanboy.demo.R;
import com.jeanboy.demo.model.entity.DummyContent;
import com.jeanboy.demo.ui.adapter.DemoAdapter;
import com.jeanboy.demo.ui.base.BaseActivity;
import com.jeanboy.demo.ui.fragment.ItemFragment;
import com.jeanboy.demo.ui.listener.OnListFragmentInteractionListener;
import com.jeanboy.demo.utils.ToolBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FadeOutInfoActivity extends BaseActivity implements OnListFragmentInteractionListener {


    @Bind(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.backdrop)
    ImageView mImageView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

//    @Bind(R.id.list)
//    RecyclerView mRecyclerView;

    @Bind(R.id.tab_layout)
    TabLayout tab_layout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private MyFragmentAdapter myFragmentAdapter;
    private List<ItemFragment> pageList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    private DemoAdapter demoAdapter;
    private List<String> mValues = new ArrayList<>();

    @Override
    public Class getTag(Class clazz) {
        return FadeOutInfoActivity.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fade_out_info;
    }

    @Override
    public void setupView() {

        mCollapsingToolbarLayout.setTitle("标题内容");

//
//        ToolBarUtil.setCollapsingToolbar(this, mCoordinatorLayout, mAppBarLayout, mImageView, mToolbar);
//
//        demoAdapter = new DemoAdapter(mValues);
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.setAdapter(demoAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//        for (int i = 0; i < 25; i++) {
//            mValues.add("" + i);
//        }
//        demoAdapter.notifyDataSetChanged();


        ToolBarUtil.setCollapsingToolbar(this, mCoordinatorLayout, mAppBarLayout, mImageView, mToolbar);
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
