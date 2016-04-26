package com.jeanboy.demo.app.presenter;

import com.jeanboy.demo.app.model.IUserModel;
import com.jeanboy.demo.app.model.UserModel;
import com.jeanboy.demo.app.view.IUserView;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class UserPresenter {

    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter(IUserView mUserView) {
        this.mUserView = mUserView;
        mUserModel = new UserModel();
    }

    public void login(String username, String password) {
        mUserView.toast("正在登录，请稍候...");
        mUserModel.login(username, password);
        mUserView.toast("登陆执行完毕！");

    }
}
