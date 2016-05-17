package com.jeanboy.demo.model;

import com.jeanboy.demo.model.entity.User;
import com.jeanboy.demo.model.impl.Callback;
import com.jeanboy.demo.model.impl.UserModelImpl;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public class UserModel implements UserModelImpl {


    @Override
    public boolean saveOrUpdate(User userBean) {
        return false;
    }

    @Override
    public User getFromDB() {
        return null;
    }

    @Override
    public User getFromService(long id) {
        return null;
    }

    @Override
    public void login(String username, String password, Callback callback) {



    }

}
