package com.jeanboy.demo.app.model;

import com.jeanboy.demo.app.bean.UserBean;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class UserModel implements IUserModel{


    @Override
    public boolean saveOrUpdate(UserBean userBean) {
        return false;
    }

    @Override
    public UserBean getFromDB() {
        return null;
    }

    @Override
    public UserBean getFromService(long id) {
        return null;
    }

    @Override
    public void login(String username, String password) {

    }
}
