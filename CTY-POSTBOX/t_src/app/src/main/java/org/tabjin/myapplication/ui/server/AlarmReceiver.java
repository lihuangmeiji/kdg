package org.tabjin.myapplication.ui.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("关机", "alarm onReceive reboot");
             /*   SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getContext().getApplicationContext());
                Calendar calendar = Calendar.getInstance();
                sharedPreferenceHelper.saveLong(AlarmUtils.LAST_REBOOT_TIMESTAMP_PREFERENCE_KEY, calendar.getTimeInMillis());*/
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});  //关机
            proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
