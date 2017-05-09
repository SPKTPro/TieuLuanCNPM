package com.example.rinnv.tieuluancnpm.DatabaseUtility;

/**
 * Created by rinnv on 4/22/2017.
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Activity.MainActivity;

public class ExportDatabaseCSVTask extends AsyncTask<String, String, String> {
    private final ProgressDialog dialog = new ProgressDialog(MainActivity.rootView.getContext());

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    protected String doInBackground(final String... args) {
        return MainActivity.db.exportDB();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(final String success) {

        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        Toast.makeText(MainActivity.rootView.getContext(), success, Toast.LENGTH_SHORT).show();
    }
}