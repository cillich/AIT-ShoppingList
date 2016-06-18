package hu.ait.android.shoppinglists;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.splashLayout);
        final Animation splashAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_splash);
        Handler handler = new Handler();

        splashLayout.startAnimation(splashAnim);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);

        splashAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
