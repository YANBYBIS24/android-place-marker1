package com.android.place.marker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**

 */

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();

        ImageView imageView = (ImageView) findViewById(R.id.splash_img);
        TextView splashText = (TextView) findViewById(R.id.splash_text);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.animation);
        imageView.startAnimation(animationFadeIn);
        splashText.startAnimation(animationFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
            }
        },2500);
    }
}
