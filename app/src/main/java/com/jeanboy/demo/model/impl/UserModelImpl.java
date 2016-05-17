package com.jeanboy.demo.model.impl;

import com.jeanboy.demo.model.entity.User;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public interface UserModelImpl {

    boolean saveOrUpdate(User userBean);

    User getFromDB();

    User getFromService(long id);


    void login(String username, String password, Callback callback);

    void getInfo(long userId, Callback callback);
}
