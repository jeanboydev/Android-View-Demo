package com.jeanboy.demo.ui.view;

import com.jeanboy.demo.model.entity.User;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public interface UserView {

    interface Login extends BaseView {
        void loginSuccess(User user);
    }

    interface Info extends BaseView {
        void getInfo(User user);

        void updateInfo(User user);
    }



}
