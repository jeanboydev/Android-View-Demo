package com.jeanboy.demo.model.impl;

/**
 * Created by JeanBoy on 2016/5/17.
 */
public interface Callback {

    void success(String response);

    void error(String msg);
}
