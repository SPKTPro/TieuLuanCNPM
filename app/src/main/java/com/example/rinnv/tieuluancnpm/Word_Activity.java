package com.example.rinnv.tieuluancnpm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import java.util.Locale;

public class Word_Activity extends AppCompatActivity implements TextToSpeech.OnInitListener  {

    public static int ID = 0;
    public static TextToSpeech mTts;
    private static final int SPEECH_API_CHECK = 0;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;
    final Context context = this;
    @Override
    public void onDestroy()
    {
        // Don't forget to shutdown!
        if (mTts != null)
        {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
        } else {
            // Initialization failed.
            Log.e("app", "Could not initialize TextToSpeech.");
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {

        if (requestCode == SPEECH_API_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
                int result = mTts.setLanguage(Locale.US);

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    private void CheckTTS()
    {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, SPEECH_API_CHECK);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(SaveObject.saveMaintopic.getMaintopic_Tittle());
        toolbar.setSubtitle(SaveObject.saveTopic.getTopic_Title());
        CheckTTS();
        final SQLiteDataController db = new SQLiteDataController(this);



        final GridView listView_Word = (GridView) findViewById(R.id.list_item) ;
        listView_Word.setAdapter(new Adapter_Word(this,db.getListWord(SaveObject.saveTopic)));

        listView_Word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word= (Word) listView_Word.getItemAtPosition(position);
                mTts.setLanguage(Locale.ENGLISH);
                mTts.speak(word.getWord_Title().trim(), TextToSpeech.QUEUE_FLUSH, null);


            }
        });
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action2);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action3);
        floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action4);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent = new Intent(Word_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 1);
                intent.putExtra("level", "topic");
                startActivity(intent);
                materialDesignFAM.close(false);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                Intent intent = new Intent(Word_Activity.this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 2);
                intent.putExtra("level", "topic");
                startActivity(intent);
                materialDesignFAM.close(false);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

                Intent intent = new Intent(Word_Activity.this, Test.class);
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

                Maintopic_EN.setText("english");
                Maintopic_VN.setText("viet nam");
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

                                                                //insert Word
                                                                boolean x=true;
                                                                 x = db.insertWord(SaveObject.saveTopic.getTopic_Id(),Maintopic_EN.getText().toString().trim(),
                                                                        Maintopic_VN.getText().toString().trim());

                                                                listView_Word.setAdapter(new Adapter_Word(context,db.getListWord(SaveObject.saveTopic)));
                                                                listView_Word.invalidate();

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
