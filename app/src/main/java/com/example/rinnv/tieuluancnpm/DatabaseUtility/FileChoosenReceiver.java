package com.example.rinnv.tieuluancnpm.DatabaseUtility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rinnv on 4/22/2017.
 */

public class FileChoosenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String filename = intent.getStringExtra("FilePath");

        ImportDatabaseCSVTask task = new ImportDatabaseCSVTask(filename);
        task.execute();
    }
}
