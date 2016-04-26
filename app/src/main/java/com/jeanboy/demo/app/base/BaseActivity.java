package com.jeanboy.demo.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public String TAG;

    public Toolbar toolbar;

    public BaseActivity() {
        TAG = this.getClass().getSimpleName();
    }

    public abstract Class getTag(Class clazz);

    public abstract int getLayoutId();

    public abstract void setupView();

    public abstract void setupActionBar();

    public abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        TAG = getTag(BaseActivity.class).getSimpleName();
        ButterKnife.bind(this);
        setupView();
        setupActionBar();
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
    }


    /**
     * tool bar back button operation
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
