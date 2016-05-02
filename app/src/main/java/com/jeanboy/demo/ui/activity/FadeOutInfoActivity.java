package com.jeanboy.demo.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.jeanboy.demo.R;
import com.jeanboy.demo.model.entity.DummyContent;
import com.jeanboy.demo.ui.adapter.DemoAdapter;
import com.jeanboy.demo.ui.adapter.ItemAdapter;
import com.jeanboy.demo.ui.base.BaseActivity;
import com.jeanboy.demo.utils.DividerItemDecoration;
import com.jeanboy.demo.utils.ToolBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FadeOutInfoActivity extends BaseActivity {


    @Bind(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.backdrop)
    ImageView mImageView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.list)
    RecyclerView mRecyclerView;

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


        mToolbar.setTitle("标题内容");
        setSupportActionBar(mToolbar);

        ToolBarUtil.setCollapsingToolbar(this, mCoordinatorLayout, mAppBarLayout, mImageView, mToolbar);

        demoAdapter = new DemoAdapter(mValues);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(demoAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        for (int i = 0; i < 25; i++) {
            mValues.add("" + i);
        }
        demoAdapter.notifyDataSetChanged();

    }

    @Override
    public void initData() {

    }


}
