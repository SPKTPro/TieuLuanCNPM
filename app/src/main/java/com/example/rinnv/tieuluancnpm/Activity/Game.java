package com.example.rinnv.tieuluancnpm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rinnv.tieuluancnpm.DatabaseUtility.SQLiteDataController;
import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.FrameWork.CircularProgressBar;
import com.example.rinnv.tieuluancnpm.FrameWork.SaveObject;
import com.example.rinnv.tieuluancnpm.R;

import java.util.ArrayList;


public class Game extends AppCompatActivity {
    private static final String TAG =   "Gamexxx" ;
    private ArrayList<Word> listWord = new ArrayList<>();
    private int Clicked = 0, Score = 0;
    private Button btn1, btn2, btnStart;
    private Word word1, word2;
    private TextView question,max;
    private CircularProgressBar circularProgressBar;
    private String QuizQuestion, Answer1, Answer2, RightAnswer,Max;
    private int count, QuizID, QuizNow, typeQuiz, timeDelay, QuizLevel, startQuiz;
    //typeQuiz=1, Quiz Anh-Viet, typeQuiz=2 Quiz Viet Anh
    // bien startQuiz dung de ngan tinh trang nhan nut start nhieu lan
    // startQuiz =0 la chua ban nut Start, = 1 la da ban nut


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String level = bundle.getString("level");
        typeQuiz = bundle.getInt("type");

        SQLiteDataController db =  SQLiteDataController.GetSQLController();
        // level = topic thì lấy tat ca cac từ trong topic đó,
        // level = maintopic thi lay tat ca cac tu trong maintopic do


         startQuiz = 0;
        if (level.equals("maintopic")) {
            Maintopic m = SaveObject.currentMaintopic;
            listWord = db.getListWord(m);

        } else {
            if (level.equals("topic")) {
                Topic topic = SaveObject.saveTopic;
                listWord = db.getListWord(topic);
            } else {
                if ((level.equals("rememberWord"))) {

                    listWord = db.getLisCheckedtWord();
                } else {

                    if ((level.equals("all"))) {
                        listWord = db.getListWord();
                    } else {
                        Toast.makeText(this, "There are some problem", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        Max= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MaxScore", "0");
        question = (TextView) findViewById(R.id.word);
        max= (TextView)findViewById(R.id.max);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btnStart = (Button) findViewById(R.id.btnstart);
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        //Setup luc bat dau

        max.setText(Max);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clicked = 1;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clicked = 2;
            }
        });

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularprogressbar2);
        PrepareforGame();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listWord.size() == 1) {
                    Toast.makeText(Game.this, "Requires at least 2 words in the list", Toast.LENGTH_LONG).show();
                } else {
                    if (startQuiz == 0) {
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                        btnStart.setVisibility(View.INVISIBLE);
                        QuizID = 1;
                        QuizNow = 0;
                        Score = 0;
                        timeDelay = 50;
                        QuizLevel = 0;
                        Clicked = 0;
                        startQuiz = 1;
                        CreatQuiz();
                    }
                }
            }
        });

    }

    private void PrepareforGame() {
        question.setText("Question is here");
        btn1.setText("Answer 1 is here");
        btn2.setText("Answer 2 is here");
        circularProgressBar.setTitle("0");
        circularProgressBar.setSubTitle("Score");

    }

    private void StartGame() {
        final Thread ThreadGame = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (count = 100; count >= 0; count--) {

                        try {
                            if (QuizLevel * 10 < Score) {
                                QuizLevel++;

                            }
                            Thread.sleep(timeDelay); //every 1sec
                            // here you check for the result if correct
                            //i use count == 5 as an example, uncomment to see

                            int x = WaitForClic();
                            if (x == 1) {
                                count = 100;
                                QuizID++;
                                CreatQuiz();
                                break;

                            } else {
                                if (x == 2) {
                                    count = 0;
                                }
                            }

                        } catch (Exception i) {
                            i.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circularProgressBar.setProgress(count);
                                if (QuizID != QuizNow) {
                                    SetControlInterface();
                                    QuizNow = QuizID;
                                }

                                if (count < 0) {
                                    end_Game(Score);
                                }
                            }
                        });

                    }
                } catch (Exception e)

                {
                    Log.d("Count", "run: " + e.getMessage());
                }
            }
        }

        );
        ThreadGame.start();
    }

    public void end_Game(final int score) {
        try {
            startQuiz = 0;
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            final Dialog dialog = new Dialog(this);
            final Dialog dialog1 = new Dialog(this);
            dialog.setContentView(R.layout.score_final_layout);
            dialog.setTitle("Score");
            dialog1.setContentView(R.layout.remind_game_layout);
            dialog1.setTitle("Gợi ý");
            TextView text = (TextView) dialog.findViewById(R.id.textView);
            text.setText(score+"");
            dialog.show();
     //       TextView textQues = (TextView) dialog.findViewById(R.id.txt);



            Button dialogButtonOK = (Button) dialog.findViewById(R.id.button);
            final Button dialogButtonOK1 = (Button) dialog1.findViewById(R.id.buttonOK);
            Button dialogButtonOK2 = (Button) dialog1.findViewById(R.id.buttonCancel);
            final int maxScore = Integer.parseInt(Max);

            if (score > maxScore) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                        edit().putString("MaxScore", score + "").commit();
                Max= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MaxScore", "0");
                max.setText(Max);
            }

            dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Score = 0;
                    dialog1.show();
                    dialog.dismiss();
                }
            });
            dialogButtonOK1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Làm code chỗ này
                   SQLiteDataController db =  SQLiteDataController.GetSQLController();
                    // them tu vao learn word
                    db.CheckWord(true,word1);
                    // them tu vao remind word
                    db.CheckWordRemind(true,word1);

                    PrepareforGame();
                    dialog1.dismiss();

                }
            });
            dialogButtonOK2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    PrepareforGame();
                    dialog1.dismiss();

                }
            });



        } catch (Exception e) {

        }

    }


    public void CreatQuiz() {
        int x1 = LaysoRandom(listWord, 1);
        int x2 = 0;
        do {
            x2 = LaysoRandom(listWord, 1);
        } while (x1 == x2);

        word1 = listWord.get(x1);
        word2 = listWord.get(x2);

        //typeQuiz=1, Quiz Anh Viet
        //typeQuiz=2, Quiz Viet Anh
        if (typeQuiz == 1) {

            QuizQuestion = word1.getWord_Title();
            RightAnswer = word1.getWord_Title_VN();

            x1 = LaysoRandom(listWord, 2);
            if (x1 == 1) {
                Answer1 = word1.getWord_Title_VN();
                Answer2 = word2.getWord_Title_VN();
            } else {
                Answer1 = word2.getWord_Title_VN();
                Answer2 = word1.getWord_Title_VN();

            }
        } else if (typeQuiz == 2) {
            QuizQuestion = word1.getWord_Title_VN();
            RightAnswer = word1.getWord_Title();
            x1 = LaysoRandom(listWord, 2);
            if (x1 == 1) {
                Answer1 = word1.getWord_Title();
                Answer2 = word2.getWord_Title();
            } else {
                Answer1 = word2.getWord_Title();
                Answer2 = word1.getWord_Title();

            }
        }
        StartGame();

    }

    private void SetControlInterface() {
        question.setText(QuizQuestion);
        btn1.setText(Answer1);
        btn2.setText(Answer2);

        circularProgressBar.setTitle(Score + "");
        circularProgressBar.setSubTitle("Score");
    }

    private int WaitForClic() {
        int returnValue = 0;
        if (Clicked == 1) {
            if (RightAnswer.compareToIgnoreCase(Answer1)==0) {
                returnValue = 1;
                Score++;
                Clicked = 0;
            } else {
                returnValue = 2;
                Clicked = 0;
            }

        } else if (Clicked == 2) {
            if (RightAnswer.compareToIgnoreCase(Answer2)==0) {
                returnValue = 1;
                Score++;
                Clicked = 0;
            } else {
                returnValue = 2;
                Clicked = 0;
            }
        }


        return returnValue;
    }

    protected int LaysoRandom(ArrayList<Word> listWord, int mode) {
        // mode 1 lay random xem nut nao se luu gia tri dung, nut nao luu gia tri sai
        // mode 2 lay random cac tu trong listWord


        int Min = 1, Max = listWord.size();
        if (mode == 1) {

            int result = Min + (int) (Math.random() * ((Max - Min) + 1));

            return result - 1;
        } else {
            int x = listWord.size();
            int result = 1 + (int) (Math.random() * (3));
            return result;
        }
    }
}
