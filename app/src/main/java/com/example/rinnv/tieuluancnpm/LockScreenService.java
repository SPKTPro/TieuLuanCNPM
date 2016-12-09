package com.example.rinnv.tieuluancnpm;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


public class LockScreenService extends Service {
    BroadcastReceiver receiver;
    int i=0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d("Tag", "run: "+i++);
            handler.postDelayed(this, 1000); // 1000 - Milliseconds
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 1000);
        return START_STICKY;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate() {

        Log.d("Tag", "onCreate: Start service");
        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        //This is deprecated, but it is a simple way to disable the lockscreen in code
        key = km.newKeyguardLock("IN");

        key.disableKeyguard();
        SQLiteDataController db = new SQLiteDataController(this);
        SaveObject.remindWord =db.getListRemindWord();

        //Start listening for the Screen On, Screen Off, and Boot completed actions
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        //Set up a receiver to listen for the Intents in this Service
        receiver = new LockScreenReceiver();


        registerReceiver(receiver, filter);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //unregisterReceiver(receiver);
        Log.d("Tag", "onCreate: Stop service");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("Tag", "onUnbind: onUnbind service");
        return super.onUnbind(intent);
    }
}
