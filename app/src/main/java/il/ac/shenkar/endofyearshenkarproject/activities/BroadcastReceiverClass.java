package il.ac.shenkar.endofyearshenkarproject.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import il.ac.shenkar.endofyearshenkarproject.utils.NetworkUtil;

/**
 * Created by User on 23/06/2017.
 */
public class BroadcastReceiverClass extends android.content.BroadcastReceiver {
    private String status;

    @Override
    public void onReceive(Context context, Intent intent) {
        status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
