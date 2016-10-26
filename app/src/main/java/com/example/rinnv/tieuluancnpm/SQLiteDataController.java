package com.example.rinnv.tieuluancnpm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by rinnv on 25/10/2016.
 */

public class SQLiteDataController extends SQLiteOpenHelper {


    public String DB_PATH = "//data//data//%s//databases//";
    // đường dẫn nơi chứa database
    private static String DB_NAME = "vocabulary";
    public SQLiteDatabase database;
    private final Context mContext;

    public SQLiteDataController(Context con) {
        super(con, DB_NAME, null, 1);
        DB_PATH = String.format(DB_PATH, con.getPackageName());
        this.mContext = con;
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
                maintopic = new Maintopic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getInt(3));
                listMainTopic.add(maintopic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listMainTopic;
    }


    public void CheckWord(boolean ischeck, Word word) {
        try {

            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Word_check", ischeck ? 1:0);
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
            Cursor cs = database.rawQuery("select * from Word where Word.Word_check = 1" , null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return list;

    }



    public ArrayList<Word> getListWord(Topic t) {
        ArrayList<Word> list = new ArrayList<>();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from Word where Word.Topic_Id = " + t.getTopic_Id(), null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return list;

    }

    public ArrayList<Word> getListWord(Maintopic maintopic) {
        ArrayList<Word> list = new ArrayList<>();

        try {
            openDataBase();

            ArrayList<Topic> listTopic = new ArrayList<>();
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = " + maintopic.getMaintopic_ID(), null);
            Topic topic;
            while (cs.moveToNext()) {
                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4));
                listTopic.add(topic);
            }

            for (Topic t : listTopic
                    ) {
                cs = database.rawQuery("select * from Word where Word.Topic_Id = " + t.getTopic_Id(), null);
                Word word;
                while (cs.moveToNext()) {
                    word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                            cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6));
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
            Cursor cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = " + maintopic.getMaintopic_ID(), null);
            Topic topic;
            while (cs.moveToNext()) {
                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getInt(4));
                list.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return list;

    }

    public boolean insertMaintopic(String MainTopic_EN,String MainTopic_VN) {
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

}