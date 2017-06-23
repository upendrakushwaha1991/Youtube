package youtube.cpm.com.youtube;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();
        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

               /* SharedPreferences sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                boolean isLoggedIn = sharedpreferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {

                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);

                }*/


                // close this activity

                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }

}
