package com.jeanboy.demo.app.model;

import com.jeanboy.demo.app.bean.UserBean;

/**
 * Created by JeanBoy on 2016/4/26.
 */
public interface IUserModel {

    boolean saveOrUpdate(UserBean userBean);

    UserBean getFromDB();

    UserBean getFromService(long id);

    void login(String username, String password);
}
