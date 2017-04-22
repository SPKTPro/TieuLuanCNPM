package com.example.rinnv.tieuluancnpm;

/**
 * Created by rinnv on 4/22/2017.
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class ExportDatabaseCSVTask extends AsyncTask<String, String, String> {
    private final ProgressDialog dialog = new ProgressDialog(MainActivity.rootView.getContext());

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    protected String doInBackground(final String... args) {
        return MainActivity.db.exportDB() ? "" : "Error";
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(final String success) {

        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (success.isEmpty()) {
            Toast.makeText(MainActivity.rootView.getContext(), "Export successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.rootView.getContext(), "Export failed!", Toast.LENGTH_SHORT).show();
        }
    }
}