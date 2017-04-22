package com.example.rinnv.tieuluancnpm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.content.ContentValues.TAG;

/**
 * Created by rinnv on 25/10/2016.
 */

public class SQLiteDataController extends SQLiteOpenHelper {


    public String DB_PATH = "//data//data//%s//databases//";
    // đường dẫn nơi chứa database
    private static String DB_NAME = "vocabulary";
    public SQLiteDatabase database;
    private final Context mContext;

    public SQLiteDataController(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = String.format(DB_PATH, context.getPackageName());
        this.mContext = context;
    }

    public void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File backupDB = new File(sd, "vocabulary.xls");
        try {
            backupDB.createNewFile();

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook = Workbook.createWorkbook(backupDB, wbSettings);
            WritableSheet sheet = workbook.createSheet("MainTopic", 0);
            SQLiteDatabase db = getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM MainTopic", null);

            List<String> listColumName = Arrays.asList(curCSV.getColumnNames());
            for (int i = 0; i < listColumName.size(); i++) {
                sheet.addCell(new Label(i, 0, listColumName.get(i)));
            }
            int row = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    sheet.addCell(new Label(colum, row, value.get(colum)));
                }
                row++;
            }


            curCSV = db.rawQuery("SELECT * FROM Topic", null);
            sheet = workbook.createSheet("Topic", 0);
            listColumName = Arrays.asList(curCSV.getColumnNames());
            for (int i = 0; i < listColumName.size(); i++) {
                sheet.addCell(new Label(i, 0, listColumName.get(i)));
            }
            row = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    sheet.addCell(new Label(colum, row, value.get(colum)));
                }
                row++;
            }

            curCSV = db.rawQuery("SELECT * FROM Word", null);
            sheet = workbook.createSheet("Word", 0);
            listColumName = Arrays.asList(curCSV.getColumnNames());
            for (int i = 0; i < listColumName.size(); i++) {
                sheet.addCell(new Label(i, 0, listColumName.get(i)));
            }
            row = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    sheet.addCell(new Label(colum, row, value.get(colum)));
                }
                row++;
            }

            workbook.write();
            workbook.close();
            curCSV.close();
            db.close();


        } catch (Exception ex) {
            Log.d(TAG, "exportDB: " + ex.getLocalizedMessage());
        }


    }


    private List<String> getListData(Cursor curCSV) {
        List<String> listString = new ArrayList<>();
        int totalColum = curCSV.getColumnCount();
        for (int i = 0; i < totalColum; i++) {
            listString.add(curCSV.getString(i));
        }
        return listString;
    }

    private void copyDataBase() throws IOException {

        try {
            InputStream myInput = mContext.getAssets().open("vocabulary.sqlite");
            OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.d("Tag", "copyDataBase: " + e.getLocalizedMessage() + "  " + DB_PATH + DB_NAME);
        }
    }

    public boolean isExist(String word, String topicID) {
        boolean x = false;
        try {
            openDataBase();
            String query = "select * from Word where Word_Title = " + '"' +
                    word.toUpperCase() + '"' + " and Topic_Id = " + '"' + topicID + '"';
            Cursor cs = database.rawQuery(query, null);
            if (cs.getCount() != 0) {
                x = true;
            } else {
                x = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return x;
    }

    public boolean isCreatedDatabase() throws IOException {
        // Default là đã có DB
        boolean result = true;
        // Nếu chưa tồn tại DB thì copy từ Asses vào Data

        if (!checkExistDataBase()) {
            this.getReadableDatabase();

            try {
                copyDataBase();
                result = false;

            } catch (Exception e) {
                Log.d("Tag", "abc: " + e.getMessage());
            }
        }

        return result;
    }

    private boolean checkExistDataBase() {
        try {
            String myPath = DB_PATH + DB_NAME;
            File fileDB = new File(myPath);

            if (fileDB.exists()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        if (file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public void openDataBase() throws SQLException {
        try {
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.d("Tag", "openDataBase: " + e.getMessage());
        }
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // do nothing
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public int deleteData_From_Table(String tbName) {

        int result = 0;
        try {
            openDataBase();
            database.beginTransaction();
            result = database.delete(tbName, null, null);
            if (result >= 0) {
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            database.endTransaction();
            close();
        } finally {
            database.endTransaction();
            close();
        }

        return result;
    }

    public ArrayList<Maintopic> getListMainTopic() {

        ArrayList<Maintopic> listMainTopic = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from MainTopic", null);
            Maintopic maintopic;
            while (cs.moveToNext()) {
                maintopic = new Maintopic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getInt(3), cs.getInt(4));
                listMainTopic.add(maintopic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }


        Collections.sort(listMainTopic, new Comparator<Maintopic>() {
            @Override
            public int compare(Maintopic o1, Maintopic o2) {
                return o1.getMaintopic_Tittle().toLowerCase().compareToIgnoreCase(o2.getMaintopic_Tittle());

            }
        });

        return listMainTopic;
    }

    public void CheckWord(boolean ischeck, Word word) {
        try {

            openDataBase();
            // thay cờ check đả học hay chưa
            ContentValues values = new ContentValues();
            values.put("Word_check", ischeck ? 1 : 0);
            int rs = database.update("Word", values, "Word_Id=" + word.getWord_Id(), null);

            // tiến hành update thong so process cua topic hiện tai
            int count = 0, sum = 0;
            Cursor cx = database.rawQuery("select * from Word where Word.Word_check = 1 and Word.Topic_Id = '" + word.getTopic_Id() + "'", null);
            count = cx.getCount();
            cx = database.rawQuery("select * from Word where Word.Topic_Id = '" + word.getTopic_Id() + "'", null);
            sum = cx.getCount();
            values = new ContentValues();
            int x = 0;
            try {
                x = (count * 100 / sum * 100) / 100;
            } catch (Exception e) {
                x = 0;
            }
            values.put("Topic_Process", x);
            rs = database.update("Topic", values, "Topic_Id = '" + word.getTopic_Id() + "'", null);


            // tiến hành update thong so process cua main topic hiện tai
            cx = database.rawQuery("select * from Topic where Topic.Topic_Id = '" + word.getTopic_Id() + "'", null);
            cx.moveToFirst();
            int MainTopicID = cx.getInt(0);


            cx = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + MainTopicID + "'", null);
            sum = 0;
            count = 0;
            while (cx.moveToNext()) {

                count += cx.getInt(4);
                sum += 100;

            }
            x = 0;
            try {
                x = (count * 100 / sum * 100) / 100;
            } catch (Exception e) {
                x = 0;
            }
            values = new ContentValues();
            values.put("MainTopic_Process", x);
            rs = database.update("MainTopic", values, "MainTopic_Id = '" + MainTopicID + "'", null);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void CheckWordRemind(boolean ischeck, Word word) {
        try {

            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Word_Remind", ischeck ? 1 : 0);
            int rs = database.update("Word", values, "Word_Id=" + word.getWord_Id(), null);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public ArrayList<Word> getLisCheckedtWord() {
        ArrayList<Word> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word where Word.Word_check = 1", null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        return list;

    }

    public ArrayList<Word> getListRemindWord() {
        ArrayList<Word> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word where Word.Word_Remind = 1", null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        return list;
    }

    public ArrayList<Word> getListWord(Topic t) {
        ArrayList<Word> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word where Word.Topic_Id = '" + t.getTopic_Id() + "'", null);

            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });

        return list;

    }

    public void deleteWord(Word word) {
        try {
            openDataBase();
            database.execSQL("delete from  Word where word.Word_Id = '" + word.getWord_Id() + "'");


            Cursor cs = database.rawQuery("select * from Word where word.Topic_Id ='" + word.getTopic_Id().toString() + "'", null);
            int x = cs.getCount();


            ContentValues values = new ContentValues();
            values.put("Count_Word", x);

            int rs = database.update("Topic", values, "Topic_Id='" + word.getTopic_Id() + "'", null);


        } catch (Exception e) {
            Log.d("Tag", "deleteWord: " + e.getMessage());
        }
    }

    public void deleteTopic(Topic topic) {
        ArrayList<Word> list = new ArrayList<>();

        Log.d("Tag", "deleteTopic: " + topic.getTopic_Id());
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word where Word.Topic_Id = '" + topic.getTopic_Id() + "'", null);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                list.add(word);
            }

            for (Word x : list) {
                deleteWord(x);
            }
            database.execSQL("delete from  Topic where Topic.Topic_Id = '" + topic.getTopic_Id() + "'");

            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + topic.getMainTopic_Id() + "'", null);
            int x = cs.getCount();

            ContentValues values = new ContentValues();
            values.put("Count_Topic", x);

            int rs = database.update("MainTopic", values, "MainTopic_Id='" + topic.getMainTopic_Id() + "'", null);


        } catch (Exception e) {
            Log.d("Tag", "deleteWord: " + e.getMessage());
        }
    }

    public void deleteMaintopic(Maintopic maintopic) {
        try {
            openDataBase();
            ArrayList<Topic> listTopic = new ArrayList<>();
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
            Topic topic;
            while (cs.moveToNext()) {
                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getInt(5));
                listTopic.add(topic);
            }

            for (Topic t : listTopic) {
                deleteTopic(t);
            }
            database.execSQL("delete from  MainTopic where MainTopic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Word> getListWord() {
        ArrayList<Word> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word ", null);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                list.add(word);
            }

        } catch (Exception e) {
            Log.d("Tag", "getListWord: " + e.getLocalizedMessage());
        } finally {
            close();
        }

        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        return list;
    }

    public ArrayList<Word> getListWord(Maintopic maintopic) {
        ArrayList<Word> list = new ArrayList<>();

        try {
            openDataBase();

            ArrayList<Topic> listTopic = new ArrayList<>();
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
            Topic topic;
            while (cs.moveToNext()) {
                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getInt(5));
                listTopic.add(topic);
            }

            for (Topic t : listTopic
                    ) {
                cs = database.rawQuery("select * from Word where Word.Topic_Id = '" + t.getTopic_Id() + "'", null);
                Word word;
                while (cs.moveToNext()) {
                    word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                            cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8));
                    list.add(word);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return list;
    }

    public ArrayList<Topic> getListTopic(Maintopic maintopic) {
        ArrayList<Topic> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
            Topic topic;
            while (cs.moveToNext()) {


                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getInt(4), cs.getInt(5));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }


        Collections.sort(list, new Comparator<Topic>() {
            @Override
            public int compare(Topic o1, Topic o2) {
                return o1.getTopic_Title().toLowerCase().compareToIgnoreCase(o2.getTopic_Title());
            }
        });
        return list;

    }

    public boolean insertMaintopic(String MainTopic_EN, String MainTopic_VN) {
        boolean result = false;
        try {

            openDataBase();

            ContentValues values = new ContentValues();

            values.put("MainTopic_Title", MainTopic_EN);
            values.put("MainTopic_Title_VN", MainTopic_VN);
            values.put("MainTopic_Process", 0);

            long rs = database.insert("Maintopic", null, values);

            if (rs > 0) {
                result = true;
                Log.d("Tag", "insertMaintopic: compele");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean insertTopic(String Topic_Title, String Topic_Title_VN, int MainTopic_Id) {
        boolean result = false;
        try {

            openDataBase();

            Log.d("Tag", "insertTopic: " + MainTopic_Id);
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + MainTopic_Id + "'", null);
            cs.moveToPosition(cs.getCount() - 1);
            String topicID = "";
            try {
                String topicIDLast = cs.getString(1);
                String[] s = topicIDLast.split("-");
                int TopicID_real = Integer.parseInt(s[s.length - 1]) + 1;
                topicID = s[s.length - 2] + "-" + TopicID_real + "";
            } catch (Exception e) {
                topicID = MainTopic_Id + "-1";

            }

            ContentValues values = new ContentValues();

            values.put("Topic_Id", topicID);
            values.put("Topic_Title", Topic_Title);
            values.put("Topic_Title_VN", Topic_Title_VN);
            values.put("MainTopic_Id", MainTopic_Id);
            values.put("Topic_Process", 0);

            long rs = database.insert("Topic", null, values);

            if (rs > 0) {
                result = true;

            }


            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + MainTopic_Id + "'", null);
            int x = cs.getCount();

            values = new ContentValues();
            values.put("Count_Topic", x);

            rs = database.update("MainTopic", values, "MainTopic_Id='" + MainTopic_Id + "'", null);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean insertWord(String topicID, String WordTittle_EN, String WordTittle_VN) {
        boolean result = false;
        try {

            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Topic_Id", topicID);
            values.put("Word_Title", WordTittle_EN.toUpperCase());
            values.put("Word_Title_VN", WordTittle_VN);

            long rs = database.insert("Word", null, values);

            if (rs > 0) {
                result = true;

            }
            Cursor cs = database.rawQuery("select * from Word where word.Topic_Id ='" + topicID + "'", null);
            int x = cs.getCount();


            values = new ContentValues();
            values.put("Count_Word", x);

            rs = database.update("Topic", values, "Topic_Id='" + topicID + "'", null);

        } catch (SQLException e) {
            Log.d("Tag", "insertWord: " + e.getMessage());
        } finally {
            close();
        }

        return result;
    }

    public ArrayList<Word> SearchWord(String s) {
        ArrayList<Word> list = new ArrayList<>();

        try {
            openDataBase();
            Cursor cs = database.rawQuery("Select * from ( SELECT Word.Topic_ID, Word.Word_Id, Word.Word_Title, Word.Word_Title_VN,Word.Word_check" +
                    " , MainTopic.MainTopic_Title,Topic.Topic_Title , Word.Word_check " +
                    "FROM Word,Topic,MainTopic where Topic.MainTopic_Id = MainTopic.MainTopic_Id and Word.Topic_Id = Topic.Topic_Id ) Where Word_Title LIKE '%" + s
                    + "%'  or Word_Title_VN like  '%" + s + "%'", null);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), 0, 0);
                list.add(word);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        return list;
    }

    public boolean updateScorePronoun(String word, int score) {
        boolean result = false;
        try {

            openDataBase();

            ContentValues values = new ContentValues();

            values.put("Word_Pronoun", score);
            long rs = database.update("Word", values, "Word_Title='" + word + "'", null);

            if (rs > 0) {
                result = true;
                Log.d("Tag", "insertMaintopic: compele");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }


}