package com.jeanboy.demo.app;

import android.test.AndroidTestCase;

/**
 * Created by yule on 2016/5/26.
 */
public class MyTest extends AndroidTestCase {

    public void testAdd() {//必须以test开头

        int a = 1;
        int b = 2;
        int c = a + b;
        assertEquals(c, 3);
    }
}
