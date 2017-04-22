package com.example.rinnv.tieuluancnpm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by rinnv on 4/22/2017.
 */

public class ImportDatabaseCSVTask extends AsyncTask<String, String, String> {
    private final ProgressDialog dialog = new ProgressDialog(MainActivity.rootView.getContext());
    private final String filePath;

    public ImportDatabaseCSVTask(String filename) {
        filePath = filename;
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    protected String doInBackground(final String... args) {
        return MainActivity.db.importDB(filePath) ? "" : "Error";
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
