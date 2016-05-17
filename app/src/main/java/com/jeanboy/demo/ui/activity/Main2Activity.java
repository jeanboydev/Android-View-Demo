package com.jeanboy.demo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeanboy.demo.R;
import com.jeanboy.demo.model.entity.User;
import com.jeanboy.demo.presenter.UserPresenter;
import com.jeanboy.demo.ui.view.UserView;

public class Main2Activity extends AppCompatActivity implements UserView.Login, UserView.Info {

    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mUserPresenter = new UserPresenter();
    }


    public void toLogin() {
        mUserPresenter.login(this, "", "");
    }

    public void getInfo() {
        mUserPresenter.getInfo(this, 0);
    }


    @Override
    public void loginSuccess(User user) {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress(String msg) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void getInfo(User user) {

    }

    @Override
    public void updateInfo(User user) {

    }
}
