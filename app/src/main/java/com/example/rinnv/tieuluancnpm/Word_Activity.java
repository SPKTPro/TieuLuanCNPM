package com.example.rinnv.tieuluancnpm;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.rinnv.tieuluancnpm.SaveObject.mTts;


public class Word_Activity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {

    public String TAG = "Tag";
    public String your_word = "";
    public Dialog dialog;
    public static int ID = 0;
    private final int SPEECH_RECOGNITION_CODE = 1001;
    public String saveWord = "";
    private static ArrayList<String> SuggestWord = new ArrayList<>();
    private Adapter_Word adapterWord;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;
    final Context context = this;
    SQLiteDataController db;

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void startSpeechToText(String word) {
        your_word = word;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, word);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                new Long(1000));

        startActivityForResult(intent, SPEECH_RECOGNITION_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNITION_CODE && resultCode == RESULT_OK) {
            final GridView listView_Word = (GridView) findViewById(R.id.list_item);
            final Adapter_Word adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic));

            final ArrayList<String> matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String[] matches_text2 = matches_text.toArray(new String[matches_text.size()]);


            int a = 0;
            // tai sao chua chi da set bang 1
            db.updateScorePronoun(your_word, 1);

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
            listView_Word.setAdapter(new Adapter_Word(context, db.getListWord(SaveObject.saveTopic)));
            listView_Word.invalidate();

            //refresh dialog
            refreshDialog(a);
        }
    }

    public void refreshDialog(int count) {
        adapterWord.RefreshDialogView(count);
    }

    private EditText Word_EN;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(SaveObject.saveMaintopic.getMaintopic_Tittle());
        toolbar.setSubtitle(SaveObject.saveTopic.getTopic_Title());

        db = new SQLiteDataController(this);
        adapterWord = new Adapter_Word(this, db.getListWord(SaveObject.saveTopic));

        SuggestWord.add("");
        final GridView listView_Word = (GridView) findViewById(R.id.list_item);
        listView_Word.setAdapter(adapterWord);

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
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xóa từ vựng")
                        .setMessage("Bạn có chắc muốn xóa từ vựng này không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteWord(word);
                                listView_Word.setAdapter(adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic)));
                                listView_Word.invalidate();
                            }

                        })
                        .setNegativeButton("Hủy", null)
                        .show();

                return true;
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
                intent.putExtra("level", "topic");
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                Word_EN = (EditText) promptsView.findViewById(R.id.mainTopic_EN);
                final EditText Maintopic_VN = (EditText) promptsView.findViewById(R.id.mainTopic_VN);


                // set dialog message
                alertDialogBuilder
                        .setView(promptsView)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        // bien kiem tra cho phep luu
                                        if (Word_EN.getText().toString().contentEquals("") || Word_EN.getText().toString().isEmpty())
                                            Toast.makeText(context, "Vui lòng nhập từ vựng", Toast.LENGTH_LONG).show();

                                        boolean isExist = db.isExist(Word_EN.getText().toString().trim());
                                        if (isExist) {
                                            Toast.makeText(context, "this word is exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // kiem tra chinh ta
                                            saveWord = Word_EN.getText().toString().toLowerCase();
                                            fetchSuggestionsFor(saveWord);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d(TAG, "run: " + SuggestWord.size());
                                                    if (SuggestWord.size() == 0) {
                                                        new AlertDialog.Builder(context)
                                                                .setTitle("Error")
                                                                .setMessage("There is no word detect for this input")
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .show();

                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Word_Activity.this);
                                                        builder.setIconAttribute(android.R.attr.alertDialogIcon)
                                                                .setTitle("Which word do you mean ?")
                                                                .setItems(SuggestWord.toArray(new String[SuggestWord.size()]), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        final String word = SuggestWord.get(i);

                                                                        Snackbar.make(v, "Chọn UNDO để hủy thao tác", Snackbar.LENGTH_LONG)
                                                                                .setCallback(new Snackbar.Callback() {
                                                                                    @Override
                                                                                    public void onDismissed(Snackbar snackbar, int event) {
                                                                                        switch (event) {
                                                                                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                                                                                Toast.makeText(context, "Hủy thao tác", Toast.LENGTH_LONG).show();
                                                                                                break;
                                                                                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                                                                                                //insert Word
                                                                                                boolean x = true;
                                                                                                x = db.insertWord(SaveObject.saveTopic.getTopic_Id(), word,
                                                                                                        Maintopic_VN.getText().toString().trim());

                                                                                                listView_Word.setAdapter(adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic)));
                                                                                                listView_Word.invalidateViews();

                                                                                                Toast.makeText(context, x ? "Thêm từ vựng thành công" : "Thêm thất bại", Toast.LENGTH_LONG).show();
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
                                                                .create()
                                                                .show();
                                                    }
                                                }
                                            }, 1000);

                                        }
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
                materialDesignFAM.close(false);
                // show it
                alertDialog.show();
            }
        });


    }


    private final int NUMBER_OF_SUGGESTIONS = 10;

    private void fetchSuggestionsFor(String input) {
        TextServicesManager tsm = (TextServicesManager) this.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        SpellCheckerSession session = tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);

        if (session != null) {
            session.getSuggestions(new TextInfo(input), 10);
        } else {
            // Show the message to user
            Toast.makeText(this, "Please turn on the spell checker from setting", Toast.LENGTH_LONG).show();
            // You can even open the settings page for user to turn it ON
            ComponentName componentToLaunch = new ComponentName("com.android.settings", "com.android.settings.Settings$SpellCheckersSettingsActivity");
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(componentToLaunch);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Error
            }
        }
        //session.getSentenceSuggestions(new TextInfo[]{new TextInfo(input)}, NUMBER_OF_SUGGESTIONS);
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] suggestionsInfos) {
        SuggestWord = new ArrayList<>();
        for (int i = 0; i < suggestionsInfos.length; ++i) {
            final int len = suggestionsInfos[i].getSuggestionsCount();
            for (int j = 0; j < len; ++j) {
                SuggestWord.add(suggestionsInfos[i].getSuggestionAt(j).toLowerCase());
            }
        }
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {

       /* SuggestWord = new ArrayList<>();
        for (SentenceSuggestionsInfo result : results) {
            int n = result.getSuggestionsCount();
            for (int i = 0; i < n; i++) {
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();

                if ((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() &
                        SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO)
                    continue;

                for (int k = 0; k < m; k++) {
                    SuggestWord.add(result.getSuggestionsInfoAt(i).getSuggestionAt(k).toLowerCase());
                }

            }
        }

        SuggestWord.remove(0);*/


    }
}
