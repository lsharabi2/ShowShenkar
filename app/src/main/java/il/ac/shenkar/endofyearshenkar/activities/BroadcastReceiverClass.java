package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import il.ac.shenkar.endofyearshenkar.utils.NetworkUtil;

/**
 * Created by User on 23/06/2017.
 */
public class BroadcastReceiverClass extends android.content.BroadcastReceiver {
    private String status;

    @Override
    public void onReceive(Context context, Intent intent) {
        status = NetworkUtil.getConnectivityStatusString(context);
        System.out.println("Liron broadcastReceiver");
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
