package youtube.cpm.com.youtube.util;

/**
 * Created by upendrak on 11-05-2017.
 */
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
public class Myservice extends Service {

    private ConnectionDetector cd;
    Handler mHandler=new Handler();
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void runa() throws Exception{
        mHandler.post(new Runnable(){
            public void run(){
                if(!cd.isConnectingToInternet())
                {
                    Toast.makeText(Myservice.this, "Internet is Slow or not Available...",
                            Toast.LENGTH_LONG).show();
                }else{
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mMobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mWifi.isConnected()){
                        Toast.makeText(Myservice.this,"Wifi connection is Enabled :) ....",Toast.LENGTH_LONG).show();
                    }

                    if (mMobile.isConnected()) {
                        Toast.makeText(Myservice.this,"Mobile connection is Enabled :) ....",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @SuppressLint("NewApi") @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cd=new ConnectionDetector(this);
        try {
            runa();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}