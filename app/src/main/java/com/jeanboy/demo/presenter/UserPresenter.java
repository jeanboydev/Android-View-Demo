package com.jeanboy.demo.presenter;

import com.jeanboy.demo.model.UserModel;
import com.jeanboy.demo.model.entity.User;
import com.jeanboy.demo.model.impl.Callback;
import com.jeanboy.demo.model.impl.UserModelImpl;
import com.jeanboy.demo.ui.view.UserView;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class UserPresenter {

    private UserModelImpl mUserModel;

    public UserPresenter() {
        mUserModel = new UserModel();
    }

    public void login(final UserView.Login mLogin, String username, String password) {
        mLogin.showProgress("正在登录，请稍候...");
        mUserModel.login(username, password, new Callback() {
            @Override
            public void success(String response) {
                mLogin.hideProgress();
                User user = new User();
                mLogin.loginSuccess(user);
            }

            @Override
            public void error(String msg) {
                mLogin.hideProgress();
                mLogin.toast(msg);
            }
        });
    }

    public void getInfo(final UserView.Info mInfo, final long userId) {
        mInfo.showProgress("正在登录，请稍候...");
        mUserModel.getInfo(userId, new Callback() {
            @Override
            public void success(String response) {
                mInfo.hideProgress();
                User user = new User();
                mInfo.getInfo(user);
            }

            @Override
            public void error(String msg) {
                mInfo.hideProgress();
                mInfo.toast(msg);
            }
        });
    }
}
