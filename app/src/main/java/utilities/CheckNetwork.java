package utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Thanh Huy on 7/28/2016.
 */
public class CheckNetwork {
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    NetworkInfo m3g;
    Context context;

    public CheckNetwork(Context context) {
        this.context = context;
        connManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }

    public boolean checkNetwork() {
        boolean b = true;
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            b = false;
        }
        return b;
    }
}
