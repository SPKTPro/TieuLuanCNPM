package com.example.rinnv.tieuluancnpm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.CircularProgressBar;

import java.util.ArrayList;

public class Test extends AppCompatActivity {

    private ArrayList<Word> listWord = new ArrayList<>();
    private int count, QuizID, QuizNow, Life, Score, startQuiz;
    private Word word1;
    private TextView question;
    private Button btn1, btnStart;
    private EditText answer;
    private CircularProgressBar circularProgressBar;
    private String QuizQuestion, RightAnswer;

    //typeQuiz=1, Quiz Anh-Viet, typeQuiz=2 Quiz Viet Anh
    // bien startQuiz dung de ngan tinh trang nhan nut start nhieu lan
    // startQuiz =0 la chua ban nut Start, = 1 la da ban nut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //123

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String level = bundle.getString("level");


        SQLiteDataController db = new SQLiteDataController(this);
        // level = topic thì lấy tat ca cac từ trong topic đó,
        // level = maintopic thi lay tat ca cac tu trong maintopic do
        startQuiz = 0;
        if (level.equals("maintopic")) {
            Maintopic m = SaveObject.saveMaintopic;

            listWord = db.getListWord(m);


        } else if (level.equals("topic")) {
            Topic topic = SaveObject.saveTopic;
            listWord = db.getListWord(topic);
        } else {
            Toast.makeText(this, "There are some problem", Toast.LENGTH_SHORT).show();
        }
        question = (TextView) findViewById(R.id.word);
        btnStart = (Button) findViewById(R.id.btnstart);
        btn1 = (Button) findViewById(R.id.submit);
        answer = (EditText) findViewById(R.id.inputText);


        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularprogressbar2);
        PrepareforGame();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startQuiz == 0) {
                    Score = 0;
                    startQuiz = 1;
                    Life = 3;
                    CreatQuiz();
                } else {

                }
            }
        });


    }

    private void PrepareforGame() {
        question.setText("Question is here");
        btn1.setText("Submit");
        circularProgressBar.setTitle("0");
        circularProgressBar.setSubTitle("Score");

    }

    public void CreatQuiz() {
        int x1 = LaysoRandom(listWord, 1);
        Log.d("Tag", "CreatQuiz: " + x1 + " " + listWord.size());

        word1 = listWord.get(x1);

        QuizQuestion = word1.getWord_Title_VN();
        RightAnswer = word1.getWord_Title();

        Log.d("Tag", "CreatQuiz: "+RightAnswer);
        StartGame();

    }

    private void StartGame() {
        SetControlInterface();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = answer.getText().toString();

                if (startQuiz == 1) {
                    if (RightAnswer.toLowerCase().equals(s.toLowerCase())) {
                        Score++;
                        CreatQuiz();

                    } else {
                        if (Life >= 1) {
                            Life--;
                            CreatQuiz();

                        } else {
                            startQuiz = 0;
                            end_Game(Score);
                        }
                    }
                }
            }
        });


    }

    public void end_Game(int score) {
        try {
            startQuiz = 0;

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.score_final_layout);
            dialog.setTitle("Score");

            TextView text = (TextView) dialog.findViewById(R.id.textView);
            text.setText(Score + " Score");
            text.setTextSize(20);
            Button dialogButton = (Button) dialog.findViewById(R.id.button);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Score = 0;
                    PrepareforGame();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {

        }

    }

    private void SetControlInterface() {
        question.setText(QuizQuestion);
        btn1.setText("Answer");
        if (Life == 3) {
            circularProgressBar.setProgress(100);
        }
        if (Life == 2) {
            circularProgressBar.setProgress(75);
        }
        if (Life == 1) {
            circularProgressBar.setProgress(50);
        }
        if (Life ==0)
        {
            circularProgressBar.setProgress(25);
        }

        circularProgressBar.setTitle(Score + "");
        circularProgressBar.setSubTitle("Score");
    }

    protected int LaysoRandom(ArrayList<Word> listWord, int mode) {
        // mode 1 lay random xem nut nao se luu gia tri dung, nut nao luu gia tri sai
        // mode 2 lay random cac tu trong listWord


        if (mode == 1) {
            int x = listWord.size() - 1;
            int result = 0 + (int) (Math.random() * ((x) + 1));
            return result;
        } else {
            int result = 1 + (int) (Math.random() * (3));
            return result;
        }
    }

}
