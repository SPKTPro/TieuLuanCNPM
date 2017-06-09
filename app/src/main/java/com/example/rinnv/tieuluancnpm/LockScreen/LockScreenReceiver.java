package com.example.rinnv.tieuluancnpm.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;

import static android.content.ContentValues.TAG;

public class LockScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: "+action);
        //If the screen was just turned on or it just booted up, start your Lock Activity
        if (action.equals(Intent.ACTION_SCREEN_ON) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (SaveObject.remindWord.size() > 0) {
                Intent i = new Intent(context, LockScreenActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                Log.d("Tag", "onReceive: Khong co tu de nhac");
            }
        }
    }


}
