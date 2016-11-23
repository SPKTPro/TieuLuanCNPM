package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

public class Topic_Activity extends AppCompatActivity {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;
    final Context context = this;

    static Adapter_Topic adapter_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Maintopic Maintopic_choosen = SaveObject.saveMaintopic;
        setTitle(Maintopic_choosen.getMaintopic_Tittle() + " - " + Maintopic_choosen.getMaintopic_Tittle_VN());


        final SQLiteDataController db = new SQLiteDataController(this);
        final GridView listView_Topic = (GridView) findViewById(R.id.list_item);
        adapter_topic = new Adapter_Topic(this, db.getListTopic(Maintopic_choosen));
        listView_Topic.setAdapter(adapter_topic);

        listView_Topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaveObject.saveTopic = (Topic) listView_Topic.getItemAtPosition(position);

                Intent intent = new Intent(Topic_Activity.this, Word_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent = new Intent(Topic_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 1);
                intent.putExtra("level", "maintopic");
                startActivity(intent);
                materialDesignFAM.close(false);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                Intent intent = new Intent(Topic_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 2);
                intent.putExtra("level", "maintopic");
                startActivity(intent);
                materialDesignFAM.close(false);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

                Intent intent = new Intent(Topic_Activity.this, Test.class);
                intent.putExtra("level", "maintopic");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.layout_add_maintopic, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                final EditText Maintopic_EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
                final EditText Maintopic_VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);
                // set dialog message
                alertDialogBuilder
                        .setView(promptsView)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // bien kiem tra cho phep luu

                                        Snackbar.make(v, "Tap to undo this add", Snackbar.LENGTH_LONG)
                                                .setCallback(new Snackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(Snackbar snackbar, int event) {
                                                        switch (event) {
                                                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                                                Toast.makeText(context, "Undo Complete", Toast.LENGTH_LONG).show();
                                                                break;
                                                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                                                                boolean x = db.insertTopic(Maintopic_EN.getText().toString().trim(),
                                                                        Maintopic_VN.getText().toString().trim(),
                                                                        Maintopic_choosen.getMaintopic_ID());

                                                                adapter_topic = new Adapter_Topic(context, db.getListTopic(Maintopic_choosen));
                                                                listView_Topic.setAdapter(adapter_topic);
                                                                listView_Topic.invalidate();

                                                                Toast.makeText(context, x ? "Add Main topic Successfull" : "Fail to do this", Toast.LENGTH_LONG).show();
                                                                break;
                                                        }
                                                    }

                                                })
                                                .setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(Color.RED)
                                                .show();


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


    }

}
