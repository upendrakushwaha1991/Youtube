package youtube.cpm.com.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import youtube.cpm.com.youtube.util.Myservice;

public class WifiChackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_chack);
    }
    public void startService(View view) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        startService(new Intent(getBaseContext(), Myservice.class));


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // toast.cancel();
                            }
                        }, 5000);

                    }
                });
            }
        }, 0, 5000);

    }


    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), Myservice.class));
    }

}
