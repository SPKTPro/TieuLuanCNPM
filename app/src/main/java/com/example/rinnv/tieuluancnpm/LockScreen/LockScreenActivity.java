package com.example.rinnv.tieuluancnpm.LockScreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.Activity.MainActivity;
import com.example.rinnv.tieuluancnpm.R;
import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class LockScreenActivity extends AppCompatActivity {

    private static final int SPEECH_API_CHECK = 0;
    TextToSpeech mTts;

    public void CheckTTS() {
        Log.d(TAG, "CheckTTS: ");
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, SPEECH_API_CHECK);
    }

    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult: ");
        if (requestCode == SPEECH_API_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {

                            int result = mTts.setLanguage(Locale.US);
                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "This Language is not supported");
                            } else {
                                Log.d(TAG, "onInit: ok");
                            }
                        } else {
                            // Initialization failed.
                            Log.e("app", "Could not initialize TextToSpeech.");
                        }
                    }
                });


                mTts.speak("ABC", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaveObject.rootContext = getApplicationContext();
        CheckTTS();
        makeFullScreen();
        //startService(new Intent(this,LockScreenService.class));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lock_screen);

        TextView txtVN = (TextView) findViewById(R.id.txtVN);
        final TextView txtEN = (TextView) findViewById(R.id.txtEN);

        Word x = null;
        final SQLiteDataController db = SQLiteDataController.GetSQLController();
        try {
            int Min = 1, Max = SaveObject.remindWord.size();
            int result = Min + (int) (Math.random() * ((Max - Min) + 1)) - 1;

            x = SaveObject.remindWord.get(result);
            txtEN.setText(x.getWord_Title().toUpperCase());
            txtVN.setText(x.getWord_Title_VN());
        } catch (Exception e) {
            txtEN.setText("There is no word to remind");
            txtVN.setText("Không có từ để nhắc");

        }


        Button SpeekButton = (Button) findViewById(R.id.btnspeak);
        SpeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(txtEN.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        Button ExitButton = (Button) findViewById(R.id.btnExit);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Process.killProcess(Process.myPid());
                finish();

            }
        });

        Button btnOpenApp = (Button) findViewById(R.id.btnOpenApp);
        btnOpenApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LockScreenActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        Button btnUncheckWord = (Button) findViewById(R.id.btnUncheckWord);
        final Word finalX1 = x;
        btnUncheckWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.CheckWordRemind(false, finalX1);
                SQLiteDataController db = SQLiteDataController.GetSQLController();
                SaveObject.remindWord = db.getListRemindWord();
                Log.d(TAG, "onClick: " + SaveObject.remindWord.size());
                Toast.makeText(LockScreenActivity.this, "Đã xóa khỏi danh sách nhắc từ", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void makeFullScreen() {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        if (Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

}

