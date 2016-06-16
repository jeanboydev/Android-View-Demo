package com.jeanboy.demo.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeanboy.demo.R;
import com.skyfishjy.library.RippleBackground;

import tyrantgit.explosionfield.ExplosionField;

public class Test2Activity extends AppCompatActivity {

    private RippleBackground rippleBackground;

    private TextView tv_test1, tv_test2, tv_test3, tv_test4, tv_test5, tv_test6;


    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        mExplosionField=ExplosionField.attach2Window(this);

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
}
