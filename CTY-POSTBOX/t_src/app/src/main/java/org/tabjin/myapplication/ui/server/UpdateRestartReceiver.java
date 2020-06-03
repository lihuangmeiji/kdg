package org.tabjin.myapplication.ui.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.tabjin.myapplication.ui.MainActivity;


public class UpdateRestartReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            //Toast.makeText(context,"已升级到新版本",Toast.LENGTH_SHORT).show();
 
            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
 
        }
    }
}
