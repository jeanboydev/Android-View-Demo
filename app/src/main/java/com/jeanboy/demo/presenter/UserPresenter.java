package com.jeanboy.demo.presenter;

import com.jeanboy.demo.model.impl.UserModelImpl;
import com.jeanboy.demo.model.UserModel;
import com.jeanboy.demo.ui.view.UserView;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class UserPresenter {

    private UserView mUserView;
    private UserModelImpl mUserModel;

    public UserPresenter(UserView mUserView) {
        this.mUserView = mUserView;
        mUserModel = new UserModel();
    }

    public void login(String username, String password) {
        mUserView.toast("正在登录，请稍候...");
        mUserModel.login(username, password);
        mUserView.toast("登陆执行完毕！");

    }
}
