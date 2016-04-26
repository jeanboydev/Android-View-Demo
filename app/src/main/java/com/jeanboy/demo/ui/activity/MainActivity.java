package com.jeanboy.demo.ui.activity;

import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeanboy.demo.R;
import com.jeanboy.demo.ui.base.BaseActivity;
import com.jeanboy.demo.presenter.UserPresenter;
import com.jeanboy.demo.ui.view.UserView;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements UserView {

    @Bind(R.id.et_username)
    EditText et_username;

    private UserPresenter mUserPresenter;

    @Override
    public Class getTag(Class clazz) {
        return MainActivity.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setupView() {
        mUserPresenter = new UserPresenter(this);
    }

    @Override
    public void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("登录");
    }

    @Override
    public void initData() {

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(String msg) {

    }

    @Override
    public void setUsername(String username) {
        et_username.setText(username);
    }

    @OnClick(R.id.btn_login)
    void login() {
        mUserPresenter.login("", "");
    }
}
