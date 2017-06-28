package com.example.rinnv.tieuluancnpm.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Adapter.Adapter_Word;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.FrameWork.CreateItemType;
import com.example.rinnv.tieuluancnpm.FrameWork.PracticeType;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.MiniFragment.MenuPracticeFragment;
import com.example.rinnv.tieuluancnpm.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import static com.example.rinnv.tieuluancnpm.FrameWork.SaveObject.mTts;


public class Word_Activity extends AppCompatActivity {

    public String TAG = "Tag";
    public String your_word = "";
    public Dialog dialog;
    public static int ID = 0;
    private final int SPEECH_RECOGNITION_CODE = 1001;
    public String saveWord = "";
    public static Adapter_Word adapterWord;
    public static GridView listView_Word;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton5, floatingActionButton4;
    final Context context = this;
    SQLiteDataController db;
    private static ArrayList<Word> wordList;

        public void startSpeechToText(String word) {
        your_word = word;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, word);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                new Long(1000));

        startActivityForResult(intent, SPEECH_RECOGNITION_CODE);

        //code check refresh sao
       /* if (ischek)
        {
           refreshDialog(1);
            ischek=!ischek;
        }else {
           refreshDialog(3);
            ischek=!ischek;
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNITION_CODE && resultCode == RESULT_OK) {
            final GridView listView_Word = (GridView) findViewById(R.id.list_item);
            try {
                final ArrayList<String> matches_text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (matches_text.size() > 0) {
                    String[] matches_text2 = matches_text.toArray(new String[matches_text.size()]);

                    Toast.makeText(this.context, matches_text2[0] + " " + matches_text2[1] + " " + matches_text2[2], Toast.LENGTH_SHORT).show();
                    int a = 0;

                    // chua bat truong hop matches_text2 khong có hoac chi co 1 2 từ
                    if (matches_text2[0].equals(your_word.toLowerCase())) {
                        a = 3;
                        db.updateScorePronoun(your_word, 3);
                    } else if (matches_text2[1].equals(your_word.toLowerCase())) {
                        a = 2;
                        db.updateScorePronoun(your_word, 2);
                    } else if (matches_text2[2].equals(your_word.toLowerCase())) {
                        a = 1;
                        db.updateScorePronoun(your_word, 1);
                    } else {
                        db.updateScorePronoun(your_word, 0);
                    }
                    refreshDialog(a);

                    listView_Word.setAdapter(new Adapter_Word(context, db.getListWord(SaveObject.saveTopic), Word_Activity.this));
                    listView_Word.invalidate();


                }
            } catch (Exception ex) {
                Toast.makeText(this, "There are some errors, please speak again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshDialog(int count) {
        Log.d(TAG, "refreshDialog: " + count);
        adapterWord.RefreshDialogView(count);
    }

    private EditText Word_EN;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int fucusItem = 0;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                fucusItem = 0;
            } else {
                fucusItem = extras.getInt("Focus");
            }
        } else {
            fucusItem = (int) savedInstanceState.getSerializable("Focus");
        }

        int positionFocus = 0;
        setTitle(SaveObject.currentMaintopic.getMaintopic_Tittle());
        toolbar.setSubtitle(SaveObject.saveTopic.getTopic_Title());

        db = SQLiteDataController.GetSQLController();
        wordList = db.getListWord(SaveObject.saveTopic);
        if (fucusItem != 0) {
            for (Word word : wordList) {
                if (word.getWord_Id() == fucusItem) {
                    positionFocus = wordList.indexOf(word);
                    break;
                }
            }
        }
        adapterWord = new Adapter_Word(this, wordList, Word_Activity.this);

        listView_Word = (GridView) findViewById(R.id.list_item);
        listView_Word.setAdapter(adapterWord);
        listView_Word.setSelection(positionFocus);

        listView_Word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = (Word) listView_Word.getItemAtPosition(position);
                mTts.speak(word.getWord_Title().trim(), TextToSpeech.QUEUE_FLUSH, null);


            }
        });
        listView_Word.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Word word = (Word) listView_Word.getItemAtPosition(position);


                return true;
            }
        });


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action1);
        floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action4);
        floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action5);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                new MenuPracticeFragment().createMenuPractice(Word_Activity.this, PracticeType.oneTopic);
            }
        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                materialDesignFAM.close(false);
                new MenuPracticeFragment().createMenuAddItem(Word_Activity.this, findViewById(android.R.id.content), CreateItemType.Word);

            }
        });
        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                materialDesignFAM.close(false);
                new MenuPracticeFragment().createMenuSearchWord(Word_Activity.this);

            }
        });

    }


}
