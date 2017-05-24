package com.example.jayanthrajakumar.arduinoglassassistant;

/**
 * Created by Jayanth Rajakumar on 20-Feb-17.
 */

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG,"**********  onNotificationPosted");

        ((MyApplication) this.getApplication()).notif1=sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();
        ((MyApplication) this.getApplication()).notif2=sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString();
        ((MyApplication) this.getApplication()).notif3=sbn.getPackageName();
        char[] bigs= new char[112];
        int k=0,kmax=((MyApplication) this.getApplication()).notif1.length();
        for(int i=0;i<32;i++)
        {
          if(i<kmax)
          {
              bigs[i]=((MyApplication) this.getApplication()).notif1.charAt(i);

          }
            else
          {
              bigs[i]=' ';
          }


        }

        k=32;
        kmax=((MyApplication) this.getApplication()).notif2.length();
        for(int i=0;i<32;i++)
        {
            if(i<kmax)
            {
                bigs[k]=((MyApplication) this.getApplication()).notif2.charAt(i);
                k++;
            }
            else
            {
                bigs[k]=' ';
                k++;
            }


        }

        k=64;
        kmax=((MyApplication) this.getApplication()).notif3.length();
        for(int i=0;i<32;i++)
        {
            if(i<kmax)
            {
                bigs[k]=((MyApplication) this.getApplication()).notif3.charAt(i);
                k++;
            }
            else
            {
                bigs[k]=' ';
                k++;
            }


        }
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

        k=96;
        kmax=currentDateTimeString.length();
        for(int i=0;i<15;i++)
        {
            if(i<kmax)
            {
                bigs[k]=currentDateTimeString.charAt(i);
                k++;
            }
            else
            {
                bigs[k]=' ';
                k++;
            }


        }

        String txt0=new String(bigs);


        //((MyApplication) this.getApplication()).notif4 = "2," + ((MyApplication) this.getApplication()).notif1 + "," + ((MyApplication) this.getApplication()).notif2 + "," + ((MyApplication) this.getApplication()).notif3;
        ((MyApplication) this.getApplication()).notif4 = "2," + txt0;


        MainActivity.notif4dash = ((MyApplication) this.getApplication()).notif4;
       // Toast.makeText(getApplicationContext(),  "llllollll", Toast.LENGTH_SHORT).show();

        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificatfionPosted :" + sbn.getPackageName() + "n");
        sendBroadcast(i);


    }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {
            Log.i(TAG,"********** onNOtificationRemoved");
            Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
            Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
            i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "n");

            sendBroadcast(i);
        }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }

}