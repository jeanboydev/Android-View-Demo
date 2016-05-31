package com.jeanboy.demo.ui.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jeanboy.demo.R;

public class AnimationActivity extends AppCompatActivity {


    private ImageView iv_ico;
    private View v_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        iv_ico = (ImageView) findViewById(R.id.iv_ico);
        v_light = findViewById(R.id.v_light);
    }

    public void startAnim(View v) {

        //补间动画 机制：复制一份view，隐藏原view，复制view执行动画，最后显示原view
//        TranslateAnimation animation=new TranslateAnimation(0,300,0,0);
//        animation.setDuration(4000);
//        iv_ico.startAnimation(animation);

        //3.0之后 属性动画 改变自身属性
//        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_ico, "translationX", 0f, 100f, 200f, 100f);
//        animator.setDuration(4000);
//        animator.start();

        ObjectAnimator animator = ObjectAnimator.ofObject(v_light, "backgroundColor", new ArgbEvaluator(), Color.RED, Color.BLACK, Color.BLUE);
        animator.setDuration(1000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);//设置动画重复次数，无穷
        animator.setRepeatMode(ObjectAnimator.REVERSE);//动画重复模式，REVERSE反转
        animator.start();

        //帧动画

    }
}
