package com.example.rinnv.tieuluancnpm;

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

    @Override
    protected void onPostExecute(final String success) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
            MainActivity.adapterMaintopic = new Adapter_Maintopic(MainActivity.rootView.getContext(),
                    MainActivity.db.getListMainTopic());
            MainActivity.listView_Maintopic.setAdapter(MainActivity.adapterMaintopic);
        }
        if (success.isEmpty()) {
            Toast.makeText(MainActivity.rootView.getContext(), "Export successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.rootView.getContext(), "Export failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
