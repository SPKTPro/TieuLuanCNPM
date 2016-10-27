package com.example.rinnv.tieuluancnpm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

public class Word_Activity extends AppCompatActivity implements TextToSpeech.OnInitListener  {

    public static int ID = 0;
    public static TextToSpeech mTts;
    private static final int SPEECH_API_CHECK = 0;
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
        SQLiteDataController db = new SQLiteDataController(this);



        final GridView listView_Word = (GridView) findViewById(R.id.list_item) ;
        listView_Word.setAdapter(new Adapter_Word(this,db.getListWord(SaveObject.saveTopic)));

        listView_Word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word= (Word) listView_Word.getItemAtPosition(position);

                Log.d("Tag", "onItemClick:"+word.getWord_Title_VN());
                mTts.setLanguage(Locale.ENGLISH);
                mTts.speak(word.getWord_Title().trim(), TextToSpeech.QUEUE_FLUSH, null);


            }
        });




    }

}
