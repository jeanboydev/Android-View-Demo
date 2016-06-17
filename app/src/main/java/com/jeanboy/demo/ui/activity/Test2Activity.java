package com.jeanboy.demo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeanboy.demo.R;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import tyrantgit.explosionfield.ExplosionField;

public class Test2Activity extends AppCompatActivity {

    private RelativeLayout ll_body;

    private RippleBackground rippleBackground;

    private TextView tv_test1, tv_test2, tv_test3, tv_test4, tv_test5, tv_test6;


    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        mExplosionField = ExplosionField.attach2Window(this);

        ll_body = (RelativeLayout) findViewById(R.id.ll_body);
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        tv_test1 = (TextView) findViewById(R.id.tv_test1);
        tv_test2 = (TextView) findViewById(R.id.tv_test2);
        tv_test3 = (TextView) findViewById(R.id.tv_test3);
        tv_test4 = (TextView) findViewById(R.id.tv_test4);
        tv_test5 = (TextView) findViewById(R.id.tv_test5);
        tv_test6 = (TextView) findViewById(R.id.tv_test6);

        ImageView imageView = (ImageView) findViewById(R.id.iv_avatar);


        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        /**
         * 1. Palette.Swatch s1 = Palette.getVibrantSwatch(); //充满活力的色板
         2. Palette.Swatch s2 = Palette.getDarkVibrantSwatch(); //充满活力的暗色类型色板
         3. Palette.Swatch s3 = Palette.getLightVibrantSwatch(); //充满活力的亮色类型色板
         4. Palette.Swatch s4 = Palette.getMutedSwatch(); //黯淡的色板
         5. Palette.Swatch s5 = Palette.getDarkMutedSwatch(); //黯淡的暗色类型色板（翻译过来没有原汁原味的赶脚啊！）
         6. Palette.Swatch s6 = Palette.getLightMutedSwatch(); //黯淡的亮色类型色板

         */
        Palette.generateAsync(image, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();//充满活力的色板
                if (vibrant != null) {
                    tv_test1.setBackgroundColor(vibrant.getRgb());
                    tv_test1.setTextColor(vibrant.getTitleTextColor());
                    rippleBackground.setRippleColor(vibrant.getRgb());
                    vibrant.getPopulation();
                }
                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();//充满活力的暗色类型色板
                if (darkVibrant != null) {
                    tv_test2.setBackgroundColor(darkVibrant.getRgb());
                    tv_test2.setTextColor(darkVibrant.getTitleTextColor());
                }
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();//充满活力的亮色类型色板
                if (lightVibrant != null) {
                    tv_test3.setBackgroundColor(lightVibrant.getRgb());
                    tv_test3.setTextColor(lightVibrant.getTitleTextColor());
                }

                Palette.Swatch muted = palette.getMutedSwatch(); //黯淡的色板
                if (muted != null) {
                    tv_test4.setBackgroundColor(muted.getRgb());
                    tv_test4.setTextColor(muted.getTitleTextColor());
                }
                Palette.Swatch darkMuted = palette.getDarkMutedSwatch(); //黯淡的暗色类型色板
                if (darkMuted != null) {
                    tv_test5.setBackgroundColor(darkMuted.getRgb());
                    tv_test5.setTextColor(darkMuted.getTitleTextColor());
                }
                Palette.Swatch lightMuted = palette.getLightMutedSwatch(); //黯淡的亮色类型色板
                if (lightMuted != null) {
                    tv_test6.setBackgroundColor(lightMuted.getRgb());
                    tv_test6.setTextColor(lightMuted.getTitleTextColor());
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rippleBackground.isRippleAnimationRunning()) {
                    rippleBackground.stopRippleAnimation();
                    mExplosionField.explode(view);
//                    view.setOnClickListener();
                } else {
                    rippleBackground.startRippleAnimation();
                }
            }
        });
    }

    private void foundDevice() {
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(400);
//        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        ArrayList<Animator> animatorList = new ArrayList<Animator>();
//        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
//        animatorList.add(scaleXAnimator);
//        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
//        animatorList.add(scaleYAnimator);
//        animatorSet.playTogether(animatorList);
//        foundDevice.setVisibility(View.VISIBLE);
//        animatorSet.start();
    }

    public void addOne(final View view) {

        final RippleBackground ripple = new RippleBackground(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 500);
        params.topMargin = 200;
        params.leftMargin = 400;
        ripple.setLayoutParams(params);
        ripple.setRippleColor(Color.RED);
        ripple.setBackgroundColor(Color.BLUE);
        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ripple.isRippleAnimationRunning()) {
                    ripple.stopRippleAnimation();
                    mExplosionField.explode(v);
                } else {
                    ripple.startRippleAnimation();
                }
            }
        });

        ll_body.addView(ripple);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);

//        越来越快	AccelerateInterpolator()	@android:anim/accelerate_interpolator
//        越来越慢	DecelerateInterpolator()	@android:anim/decelerate_interpolator
//        先快后慢	AccelerateDecelerateInterpolator()	@android:anim/accelerate_decelerate_interpolator
//        先后退一小步然后向前加速	AnticipateInterpolator()	@android:anim/anticipate_interpolator
//        快速到达终点超出一小步然后回到终点	OvershootInterpolator()	@android:anim/overshoot_interpolator
//        到达终点超出一小步然后回到终点	AnticipateOvershootInterpolator()	@android:anim/anticipate_overshoot_interpolator
//        弹球效果，弹几下回到终点	BounceInterpolator()	@android:anim/bounce_interpolator
//        均匀速度	LinearInterpolator()      @android:anim/linear_interpolator

//        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        animatorSet.setInterpolator(new BounceInterpolator());//很有力度
        animatorSet.setInterpolator(new LinearInterpolator());//很有力度

        ArrayList<Animator> animators = new ArrayList<>();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ripple, "alpha", 0.0f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ripple, "ScaleX", 0f, 1.1f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ripple, "ScaleY", 0f, 1.1f, 1f);
        animators.add(alphaAnimator);
        animators.add(scaleXAnimator);
        animators.add(scaleYAnimator);

        animatorSet.playTogether(animators);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                ripple.setRippleColor(Color.RED);
//                ripple.startRippleAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
