package com.example.rinnv.tieuluancnpm.FrameWork;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.Entity.Word;

import java.util.ArrayList;

/**
 * Created by rinnv on 25/10/2016.
 */

public class SaveObject {
    public static Maintopic currentMaintopic;
    public static Topic saveTopic;
    public static boolean allowAInsert;

    public static ArrayList<Word> remindWord;
    public static TextToSpeech mTts;
    public static Context rootContext;
}

