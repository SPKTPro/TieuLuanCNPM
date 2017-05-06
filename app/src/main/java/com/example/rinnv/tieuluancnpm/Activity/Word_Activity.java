package com.example.rinnv.tieuluancnpm.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
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

    /*
        public void showDetailWord(List<WordRelationShip> wordRelationShips, String Example,String ExampleVN){
            AlertDialog.Builder builder = new AlertDialog.Builder(Word_Activity.this);
            builder.setTitle("Modify Customer Details");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_detail_dialog,);
            builder.setView(view);
            builder.create().show();


            *//*Dialog dialog = new Dialog(Word_Activity.this);
        dialog.setTitle("Title");
        dialog.setContentView(R.layout.custom_detail_dialog);
        TextView textEN = (TextView) dialog.findViewById(R.id.example_EN);
        textEN.setText(Example);

        TextView textVN = (TextView) dialog.findViewById(R.id.example_VN);
        textVN.setText(ExampleVN);


        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < wordRelationShips.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Word_Title", wordRelationShips.get(i).getWord_Title());
            map.put("Word_Title_VN", wordRelationShips.get(i).getWord_Title_VN());
            fillMaps.add(map);
        }
        if (fillMaps.size() > 0) {

            ListView listView = (ListView) dialog.findViewById(R.id.lst_relationship);
            listView.setAdapter(new SimpleAdapter(Word_Activity.this, fillMaps, R.id.lst_relationship,
                    new String[]{"Word_Title", "Word_Title_VN"}, new int[]{R.id.example_EN, R.id.example_VN}));
        }
        dialog.create();
        dialog.show();*//*
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNITION_CODE && resultCode == RESULT_OK) {
            final GridView listView_Word = (GridView) findViewById(R.id.list_item);
            final Adapter_Word adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic), Word_Activity.this);

            final ArrayList<String> matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches_text.size() > 0) {
                String[] matches_text2 = matches_text.toArray(new String[matches_text.size()]);

                Toast.makeText(this.context, matches_text2[0] + " " + matches_text2[1] + " " + matches_text2[2] + " " + your_word, Toast.LENGTH_SHORT).show();
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


        setTitle(SaveObject.currentMaintopic.getMaintopic_Tittle());
        toolbar.setSubtitle(SaveObject.saveTopic.getTopic_Title());

        db = new SQLiteDataController(this);
        adapterWord = new Adapter_Word(this, db.getListWord(SaveObject.saveTopic), Word_Activity.this);


        listView_Word = (GridView) findViewById(R.id.list_item);
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
                                listView_Word.setAdapter(adapterWord = new Adapter_Word(context, db.getListWord(SaveObject.saveTopic), Word_Activity.this));
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
        floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.action4);


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


    }


}
