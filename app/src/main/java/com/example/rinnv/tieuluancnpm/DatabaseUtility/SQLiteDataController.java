package com.example.rinnv.tieuluancnpm.DatabaseUtility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rinnv.tieuluancnpm.Entity.Maintopic;
import com.example.rinnv.tieuluancnpm.Entity.Topic;
import com.example.rinnv.tieuluancnpm.Entity.Word;
import com.example.rinnv.tieuluancnpm.Entity.WordRelationShip;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
        if (checkExistDataBase())
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                    SQLiteDatabase.OPEN_READWRITE);
    }

    public String exportDB() {

        Cursor curCSV = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            curCSV = db.rawQuery("SELECT * FROM MainTopic", null);
            CharSequence  charSequence = "";
            String time = DateFormat.getDateTimeInstance().format(new Date()).toString().trim().replace(" ","-").replace(":","-").replace(",","-");
            String fileName = time + ".xls";
            Workbook wb = new HSSFWorkbook();
            Cell cell = null;
            //New Sheet
            Sheet sheet1 = null;
            // Create MainTopic Sheel
            sheet1 = wb.createSheet("MainTopic");
            List<String> listColumName = Arrays.asList(curCSV.getColumnNames());
            Row row = sheet1.createRow(0);
            for (int i = 0; i < listColumName.size(); i++) {
                row.createCell(i).setCellValue(listColumName.get(i));
            }
            int rowID = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                row = sheet1.createRow(rowID);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    row.createCell(colum).setCellValue(value.get(colum));
                }
                rowID++;
            }

            for (int i = 0; i < listColumName.size(); i++) {
                sheet1.setColumnWidth(i, (15 * 500));
            }
            curCSV.close();

            //Create Topic Sheet
            curCSV = db.rawQuery("SELECT * FROM Topic", null);
            sheet1 = wb.createSheet("Topic");
            listColumName = Arrays.asList(curCSV.getColumnNames());
            row = sheet1.createRow(0);
            for (int i = 0; i < listColumName.size(); i++) {
                row.createCell(i).setCellValue(listColumName.get(i));
            }
            rowID = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                row = sheet1.createRow(rowID);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    row.createCell(colum).setCellValue(value.get(colum));
                }
                rowID++;
            }

            for (int i = 0; i < listColumName.size(); i++) {
                sheet1.setColumnWidth(i, (15 * 500));
            }

            //Create Word Sheet
            curCSV = db.rawQuery("SELECT * FROM Word", null);
            sheet1 = wb.createSheet("Word");
            listColumName = Arrays.asList(curCSV.getColumnNames());
            row = sheet1.createRow(0);
            for (int i = 0; i < listColumName.size(); i++) {
                row.createCell(i).setCellValue(listColumName.get(i));
            }
            rowID = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                row = sheet1.createRow(rowID);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    row.createCell(colum).setCellValue(value.get(colum));
                }
                rowID++;
            }

            for (int i = 0; i < listColumName.size(); i++) {
                sheet1.setColumnWidth(i, (15 * 500));
            }
            curCSV.close();

            //Create RalationShip Word Sheet
            curCSV = db.rawQuery("SELECT * FROM Relationship", null);
            sheet1 = wb.createSheet("Relationship");
            listColumName = Arrays.asList(curCSV.getColumnNames());
            row = sheet1.createRow(0);
            for (int i = 0; i < listColumName.size(); i++) {
                row.createCell(i).setCellValue(listColumName.get(i));
            }
            rowID = 1;
            while (curCSV.moveToNext()) {
                List<String> value = getListData(curCSV);
                row = sheet1.createRow(rowID);
                for (int colum = 0; colum < listColumName.size(); colum++) {
                    row.createCell(colum).setCellValue(value.get(colum));
                }
                rowID++;
            }

            for (int i = 0; i < listColumName.size(); i++) {
                sheet1.setColumnWidth(i, (15 * 500));
            }
            curCSV.close();
            Log.d(TAG, "exportDB: "+fileName);
            try {
                // Create a path where we will place our List of objects on external storage
                //File file = new File(Environment.getExternalStorageDirectory(), fileName);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
                if (!file.exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file);
                wb.write(os);
                return "Export successful! File location is: " + file.getAbsolutePath();
            } catch (Exception ex) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
                if (!file.exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file);
                wb.write(os);
                return "Export successful! File location is: " + file.getAbsolutePath();
            } finally {
                wb.close();
            }

        } catch (Exception ex) {
            Log.e(TAG, "exportDB: ", ex);
            return "Export fail with error: " + ex.getMessage();
        } finally {
            curCSV.close();
            close();
        }
    }

    public boolean importDB(String filePath) {
        try {
            getReadableDatabase();
            File file = new File(filePath);
            FileInputStream myInput = new FileInputStream(file);
            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);


            //IMPORT MAINTOPIC SHEET
            // Get the first sheet from workbook
            HSSFSheet mySheetMainTopic = myWorkBook.getSheet("MainTopic");

            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheetMainTopic.rowIterator();
            // Skip header row
            rowIter.next();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                List<String> value = new ArrayList<>();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    value.add(myCell.toString());
                }
                if (!isMainTopicExist(value.get(1))) {
                    Log.d(TAG, "importDB: begin import");
                    insertMaintopic(value.get(1), value.get(2));

                }
            }

            //IMPORT TOPIC SHEET
            HSSFSheet mySheetTopic = myWorkBook.getSheet("Topic");
            rowIter = mySheetTopic.rowIterator();
            // Skip header row
            rowIter.next();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                // Lấy list gia tri cua row
                List<String> value = new ArrayList<>();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    value.add(myCell.toString());
                }
                // lấy row maintopc trong sheet maintopic
                Row MaintopicRow = findMainTopicRow(mySheetMainTopic, value.get(0));
                String MainTopicSting = MaintopicRow.getCell(1).getStringCellValue();

                if (!isTopicExist(MainTopicSting, value.get(2))) {
                    importTopic(value.get(2), value.get(3), value.get(0), mySheetMainTopic);

                }

            }


            //IMPOER WORD SHEET
            HSSFSheet mySheetWord = myWorkBook.getSheet("Word");
            rowIter = mySheetWord.rowIterator();
            // Skip header row
            rowIter.next();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                // Lấy list gia tri cua row
                List<String> value = new ArrayList<>();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    value.add(myCell.toString());
                }

                // lấy row maintopc trong sheet maintopic
                Row TopicRow = findTopicOrWordRow(mySheetTopic, value.get(0));
                String TopicSting = TopicRow.getCell(2).getStringCellValue();

                if (!isWordExist(TopicSting, value.get(2))) {
                    importWord(value.get(2), value.get(3), value.get(0), mySheetTopic, mySheetMainTopic);
                }
            }


            HSSFSheet mySheetRelationShip = myWorkBook.getSheet("Relationship");
            rowIter = mySheetRelationShip.rowIterator();
            rowIter.next();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                // Lấy list gia tri cua row
                List<String> value = new ArrayList<>();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    value.add(myCell.toString());
                }

                if (!isRelationExist(value.get(1), value.get(0))) {
                    importRelationWord(value.get(1), value.get(2), value.get(0), value.get(3), mySheetWord);
                }
            }

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private void importTopic(String topicEN, String topicVN, String Maintopicid, HSSFSheet mySheetMainTopic) {
        Log.d(TAG, "importX: topic");
        Row rowFilter = null;
        rowFilter = findMainTopicRow(mySheetMainTopic, Maintopicid);
        if (rowFilter != null) {
            String MainTopic = rowFilter.getCell(1).getStringCellValue();

            if (isMainTopicExist(MainTopic)) {
                int MaintopicID = GetMaintopicID(MainTopic);
                if (MaintopicID != 0)
                    insertTopic(topicEN, topicVN, MaintopicID);
                else {
                    insertMaintopic(rowFilter.getCell(2).getStringCellValue(), rowFilter.getCell(3).getStringCellValue());
                    MaintopicID = GetMaintopicID(MainTopic);
                    insertTopic(topicEN, topicVN, MaintopicID);
                }
            }
        }

    }

    private void importWord(String wordEN, String WordVN, String TopicID, HSSFSheet mySheetTopic, HSSFSheet mySheetMainTopic) {
        Log.d(TAG, "importX: word " + wordEN + "|" + WordVN + "|" + TopicID);
        try {
            Row rowFilter = null;
            rowFilter = findTopicOrWordRow(mySheetTopic, TopicID);
            if (rowFilter != null) {
                //topic name and maintopic id
                String TopicEN = rowFilter.getCell(2).getStringCellValue();
                String TopicVN = rowFilter.getCell(3).getStringCellValue();
                String MainTopicID = rowFilter.getCell(0).getStringCellValue();
                String WordType = rowFilter.getCell(9).getStringCellValue();

                Row maintopicRow = findMainTopicRow(mySheetMainTopic, MainTopicID);
                String MainTopic = maintopicRow.getCell(1).getStringCellValue();
                if (isTopicExist(MainTopic, TopicEN)) {
                    String topicID = getTopicID(TopicEN, MainTopic);
                    insertWord(topicID, wordEN, WordVN, WordType);
                } else {
                    int maintopicID = GetMaintopicID(MainTopic);
                    insertTopic(TopicEN, TopicVN, maintopicID);
                    String topicId = getTopicID(TopicEN, MainTopic);
                    insertWord(topicId, wordEN, WordVN, WordType);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "importWord: ", e);
        }
    }

    private void importRelationWord(String wordEn, String wordVn, String rootId, @Nullable String wordType, HSSFSheet myWordSheet) {
        Log.d(TAG, "importRelationWord " + wordEn + "|" + wordVn + "|" + rootId);
        Row WordRowFilter = null;
        WordRowFilter = findTopicOrWordRow(myWordSheet, rootId);
        if (WordRowFilter != null) {
            //get word title
            String WordEN = WordRowFilter.getCell(2).getStringCellValue();
            String WordVN = WordRowFilter.getCell(3).getStringCellValue();
            String TopicID = WordRowFilter.getCell(0).getStringCellValue();
            String WordType = WordRowFilter.getCell(9).getStringCellValue();

            if (isWordExist(WordRowFilter.getCell(0).getStringCellValue(), WordEN)) {
                String wordID = getWordID(WordEN, WordRowFilter.getCell(0).getStringCellValue());
                insertRelationship(wordID, wordEn, wordVn, wordType);
            } else {
                insertWord(TopicID, WordEN, WordVN, WordType);
                String wordId = getWordID(WordEN, TopicID);
                insertRelationship(wordId, wordEn, wordVn, wordType);
            }
        }
    }

    private String getWordID(String word, String topicID) {
        Cursor cs = null;
        try {
            openDataBase();
            String query = "SELECT Word.Word_Id FROM Topic,Word where Topic.Topic_Id = Word.Topic_Id and UPPER( Word_Title )= "
                    + '"' + word.toUpperCase() + '"' +
                    " and Word.Topic_Id = " + '"' + topicID + '"';
            cs = database.rawQuery(query, null);
            cs.moveToNext();
            return cs.getString(0);
        } catch (Exception e) {
            if (cs != null)
                cs.close();
            return null;
        }

    }

    private String getTopicID(String topic, String maintopic) {
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "select * from Topic,MainTopic where Topic.MainTopic_Id = MainTopic.MainTopic_Id and Topic_Title = "
                    + '"' + topic.toUpperCase() + '"' + " and MainTopic_Title = " + '"' + maintopic + '"';
            cs = database.rawQuery(query, null);
            cs.moveToNext();
            return cs.getString(1);

        } catch (Exception e) {
            Log.d(TAG, "getTopicID: " + e.getLocalizedMessage());
        } finally {
            if (cs != null)
                cs.close();
        }
        return null;
    }

    private int GetMaintopicID(String maintopic) {
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "select * from MainTopic where MainTopic_Title = " + '"' + maintopic.toUpperCase() + '"';
            cs = database.rawQuery(query, null);
            cs.moveToNext();
            return cs.getInt(0);
        } catch (Exception e) {

            return 0;
        } finally {
            if (cs != null)
                cs.close();
        }
    }

    private static Row findMainTopicRow(HSSFSheet sheet, String cellContent) {
        for (Row row : sheet) {
            if (row.getCell(0).getRichStringCellValue().getString().equals(cellContent)) {
                return row;
            }
        }
        return null;
    }

    private static Row findTopicOrWordRow(HSSFSheet sheet, String cellContent) {
        for (Row row : sheet) {
            if (row.getCell(1).getRichStringCellValue().getString().equals(cellContent)) {
                return row;
            }
        }
        return null;
    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
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
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "select * from Word where Word_Title = " + '"' +
                    word.toUpperCase() + '"' + " and Topic_Id = " + '"' + topicID + '"';
            cs = database.rawQuery(query, null);
            if (cs.getCount() != 0) {
                x = true;
            } else {
                x = false;
            }
        } catch (Exception e) {
            if (cs != null)
                cs.close();
            e.printStackTrace();
        }
        return x;
    }

    public boolean isMainTopicExist(String maintopic) {
        boolean x = false;
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "select * from MainTopic where MainTopic_Title = " + '"' + maintopic.toUpperCase() + '"';
            cs = database.rawQuery(query, null);
            if (cs.getCount() != 0) {
                x = true;
            } else {
                x = false;
            }
        } catch (Exception e) {
            if (cs != null)
                cs.close();

            e.printStackTrace();
        }
        return x;
    }

    public boolean isTopicExist(String mainTopic, String topicTitle) {
        boolean x = false;
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "select * from Topic,MainTopic where Topic.MainTopic_Id = MainTopic.MainTopic_Id and Topic_Title = " + '"' + topicTitle.toUpperCase() + '"' +
                    " and MainTopic_Title = " + '"' + mainTopic + '"';
            cs = database.rawQuery(query, null);
            if (cs.getCount() != 0) {
                x = true;
            } else {
                x = false;
            }
        } catch (Exception e) {
            if (cs != null)
                cs.close();

            e.printStackTrace();
        }
        return x;
    }

    public boolean isWordExist(String topic, String word) {
        boolean x = false;
        Cursor cs = null;
        try {

            openDataBase();

            String query = "SELECT * FROM Topic,Word where Topic.Topic_Id = Word.Topic_Id and Word_Title = "
                    + '"' + word.toUpperCase() + '"' +
                    " and Topic_Title = " + '"' + topic.toUpperCase() + '"';
            cs = database.rawQuery(query, null);
            if (cs.getCount() > 0) {
                x = true;
            } else {
                Log.d(TAG, "isWordExist: " + query);
                Log.d(TAG, "isWordExist: " + topic + "|" + word + "|" + x + "|");
                x = false;
            }

        } catch (Exception e) {
            Log.e(TAG, "isWordExist: ex", e);
        } finally {
            if (cs != null)
                cs.close();

        }
        return x;
    }

    public boolean isRelationExist(String relationWord, String WordID) {
        boolean x = false;
        Cursor cs = null;
        try {
            openDataBase();
            String query = "Select * from Relationship where Root = '" + WordID + "'" + " and Word_Title = '" + relationWord + "'";
            cs = database.rawQuery(query, null);
            if (cs.getCount() != 0) {
                x = true;
            } else {
                x = false;
            }
        } catch (Exception e) {
            Log.e(TAG, "isRelationExist: ", e);
        } finally {
            if (cs != null)
                cs.close();

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

    public void openDataBase() throws Exception {
        try {
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.d(TAG, "openDataBase: " + e.getMessage());
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


    public ArrayList<Maintopic> getListMainTopic() {

        ArrayList<Maintopic> listMainTopic = new ArrayList<>();
        // mo ket noi
        Cursor cs = null;
        try {

            openDataBase();
            cs = database.rawQuery("select * from MainTopic", null);
            Maintopic maintopic;
            while (cs.moveToNext()) {
                maintopic = new Maintopic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getInt(3), cs.getInt(4));
                listMainTopic.add(maintopic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();

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
        Cursor cx = null;
        try {
            getReadableDatabase();
            // thay cờ check đả học hay chưa
            ContentValues values = new ContentValues();
            values.put("Word_check", ischeck ? 1 : 0);
            database.update("Word", values, "Word_Id=" + word.getWord_Id(), null);

            // tiến hành update thong so process cua topic hiện tai
            int count = 0, sum = 0;
            cx = database.rawQuery("select * from Word where Word.Word_check = 1 and Word.Topic_Id = '" + word.getTopic_Id() + "'", null);
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
            database.update("Topic", values, "Topic_Id = '" + word.getTopic_Id() + "'", null);


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
            database.update("MainTopic", values, "MainTopic_Id = '" + MainTopicID + "'", null);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cx != null)
                cx.close();

        }
    }

    public void CheckWordRemind(boolean ischeck, Word word) {

        try {
            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Word_Remind", ischeck ? 1 : 0);
            database.update("Word", values, "Word_Id=" + word.getWord_Id(), null);
            Cursor cx = database.rawQuery("select * from Word where Word_Id=" + word.getWord_Id(), null);
            Log.d(TAG, "CheckWordRemind: " + cx.getCount());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public ArrayList<Word> getLisCheckedtWord() {
        ArrayList<Word> list = new ArrayList<>();
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            cs = database.rawQuery("select * from Word where Word.Word_check = 1", null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5), cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
                list.add(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
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
        Cursor cs = null;
        try {
            {
                getReadableDatabase();
            }
            cs = database.rawQuery("select * from Word where Word.Word_Remind = 1", null);
            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5),
                        cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
                list.add(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
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
        Cursor cs = null;
        try {
            openDataBase();
            String query = "select * from Word where Word.Topic_Id = '" + t.getTopic_Id() + "'";
            cs = database.rawQuery(query, null);

            Word topic;
            while (cs.moveToNext()) {
                topic = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5),
                        cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
                list.add(topic);
            }

            Log.d(TAG, "getListWord: " + query);
        } catch (Exception e) {
            Log.e(TAG, "getListWord: ", e);
        } finally {
            if (cs != null)
                cs.close();
        }
        Collections.sort(list, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        Log.d(TAG, "getListWord: " + list.size());
        return list;

    }

    public void deleteRelationShip(WordRelationShip wordRelationShip) {
        try {
                openDataBase();

            String query = "delete from Relationship  Where Word_Title = '" + wordRelationShip.getWord_Title() + "'";
            database.execSQL(query);
            // List cs = GetRalationShipWord(wordRelationShip.getWord_Root());
        } catch (Exception e) {
            Log.d(TAG, "deleteRelationShop: " + e.getMessage());
        }
    }

    private void deleteRelationshipByRootId(int rootId) {
        try {
            openDataBase();
            database.execSQL("delete from Relationship  Where Root = '" + rootId + "'");

        } catch (Exception e) {
            Log.d(TAG, "deleteRelationShop: " + e.getMessage());
        }
    }

    public void deleteWord(Word word) {
        Cursor cs = null;
        try {
            openDataBase();
            database.execSQL("delete from  Word where word.Word_Id = '" + word.getWord_Id() + "'");
            deleteRelationshipByRootId(word.getWord_Id());
            cs = database.rawQuery("select * from Word where word.Topic_Id ='" + word.getTopic_Id().toString() + "'", null);
            int x = cs.getCount();
            ContentValues values = new ContentValues();
            values.put("Count_Word", x);
            database.update("Topic", values, "Topic_Id='" + word.getTopic_Id() + "'", null);

            Log.d(TAG, "deleteWord: delete complete word: "+word.getWord_Title());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
        }
    }

    public void deleteTopic(Topic topic) {
        ArrayList<Word> list = new ArrayList<>();
        Cursor cs = null;
        try {
            openDataBase();
            cs = database.rawQuery("select * from Word where Word.Topic_Id = '" + topic.getTopic_Id() + "'", null);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5),
                        cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
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

            database.update("MainTopic", values, "MainTopic_Id='" + topic.getMainTopic_Id() + "'", null);


        } catch (Exception e) {
            Log.d("Tag", "deleteWord: topic" + e.getMessage());
        } finally {
            if (cs != null)
                cs.close();
        }
    }

    public void deleteMaintopic(Maintopic maintopic) {
        Cursor cs = null;
        try {
            openDataBase();
            ArrayList<Topic> listTopic = new ArrayList<>();
            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
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
            Log.d("Tag", "deleteWord: maintopic " + e.getMessage());
        } finally {
            if (cs != null)
                cs.close();
        }
    }

    public ArrayList<Word> getListWord() {
        ArrayList<Word> list = new ArrayList<>();
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            cs = database.rawQuery("select * from Word ", null);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), cs.getInt(4), cs.getString(5),
                        cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
                list.add(word);
            }

        } catch (Exception e) {
            Log.d("Tag", "getListWord: " + e.getLocalizedMessage());
        } finally {
            if (cs != null)
                cs.close();
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
        Cursor cs = null;
        try {
            {
                openDataBase();
            }

            ArrayList<Topic> listTopic = new ArrayList<>();
            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
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
                            cs.getString(3), cs.getInt(4), cs.getString(5),
                            cs.getString(6), cs.getInt(7), cs.getInt(8), cs.getString(9));
                    list.add(word);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
            close();
        }

        return list;
    }

    public ArrayList<Topic> getListTopic(Maintopic maintopic) {
        ArrayList<Topic> list = new ArrayList<>();
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + maintopic.getMaintopic_ID() + "'", null);
            Topic topic;
            while (cs.moveToNext()) {


                topic = new Topic(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3), cs.getInt(4), cs.getInt(5));
                list.add(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
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

    public static String VietHoa(String s) {
        if (s.isEmpty())
            return s;

        String result = "";

        //lấy danh sách các từ

        String[] words = s.split(" ");
        int i = 0;
        for (String word : words) {
            if (i == 0) {
                // từ nào là các khoảng trắng thừa thì bỏ
                if (word.trim() != "") {
                    if (word.length() > 1)
                        result += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
                    else
                        result += word.toUpperCase() + " ";
                }
                i++;
            } else {
                if (word.trim() != "") {
                    if (word.length() > 1)
                        result += word.substring(0).toLowerCase() + " ";
                    else
                        result += word.toLowerCase() + " ";
                }
            }
        }
        return result.trim();
    }

    public boolean insertMaintopic(String MainTopic_EN, String MainTopic_VN) {
        Log.d(TAG, "importX: main");
        boolean result = false;
        try {

            {
                openDataBase();
            }
            if (isMainTopicExist(MainTopic_EN))
                return false;

            ContentValues values = new ContentValues();
            values.put("MainTopic_Title", MainTopic_EN.toUpperCase());
            values.put("MainTopic_Title_VN", VietHoa(MainTopic_VN.toLowerCase()));
            values.put("MainTopic_Process", 0);


            long rs = database.insert("Maintopic", null, values);
            Log.d(TAG, "insertMaintopic: complete");
            if (rs > 0) {
                result = true;
                Log.d("Tag", "insertMaintopic: compele");
            }


        } catch (Exception e) {
            Log.d(TAG, "insertMaintopic: " + e.getLocalizedMessage());
        } finally {
            close();
        }
        return result;
    }

    public boolean insertTopic(String Topic_Title, String Topic_Title_VN, int MainTopic_Id) {
        boolean result = false;
        Cursor cs = null;
        try {

            openDataBase();
            Log.d("Tag", "insertTopic: " + MainTopic_Id);
            cs = database.rawQuery("select * from Topic where Topic.MainTopic_Id = '" + MainTopic_Id + "'", null);
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
            values.put("Topic_Title", Topic_Title.toUpperCase());
            values.put("Topic_Title_VN", VietHoa(Topic_Title_VN.toLowerCase()));
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

            database.update("MainTopic", values, "MainTopic_Id='" + MainTopic_Id + "'", null);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
            close();
        }
        return result;
    }

    public boolean insertWord(String topicID, String WordTittle_EN, String WordTittle_VN, String wordType) {
        boolean result = false;
        Cursor cs = null;
        try {
            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Topic_Id", topicID);
            values.put("Word_Title", WordTittle_EN.toUpperCase());
            values.put("Word_Title_VN", VietHoa(WordTittle_VN.toLowerCase()));
            values.put("Type_Word", wordType.toLowerCase());
            long rs = database.insert("Word", null, values);
            if (rs > 0) {
                result = true;
            }
            cs = database.rawQuery("select * from Word where word.Topic_Id ='" + topicID + "'", null);
            int x = cs.getCount();
            values = new ContentValues();
            values.put("Count_Word", x);
            database.update("Topic", values, "Topic_Id='" + topicID + "'", null);

        } catch (Exception e) {
            Log.d("Tag", "insertWord: " + e.getLocalizedMessage());
            result = false;
        } finally {
            if (cs != null)
                cs.close();
            close();
        }
        Log.d(TAG, "insertWord: " + result);
        return result;
    }

    public boolean insertRelationship(String wordID, String WordTittle_EN, String WordTittle_VN, @Nullable String wordType) {
        boolean result = false;
        try {
            openDataBase();
            ContentValues values = new ContentValues();
            values.put("Root", wordID);
            values.put("Word_Title", WordTittle_EN.toUpperCase());
            values.put("Word_Title_VN", VietHoa(WordTittle_VN.toLowerCase()));
            values.put("Type_Word", wordType.toLowerCase());
            long rs = database.insert("Relationship", null, values);
            if (rs > 0) {
                result = true;
            }

        } catch (Exception e) {
            Log.d("Tag", "Relationship: " + e.getMessage());
        } finally {
            close();
        }

        return result;
    }

    public ArrayList<Word> SearchWord(String s) {
        ArrayList<Word> list = new ArrayList<>();
        Cursor cs = null;
        try {
            openDataBase();

            String query = "Select * from ( SELECT Word.Topic_ID, Word.Word_Id, Word.Word_Title, Word.Word_Title_VN, MainTopic.MainTopic_Id " +
                    " , MainTopic.MainTopic_Title,Topic.Topic_Title , Word.Type_Word, MainTopic.MainTopic_Title_VN " +
                    "FROM Word,Topic,MainTopic where Topic.MainTopic_Id = MainTopic.MainTopic_Id and Word.Topic_Id = Topic.Topic_Id ) Where Word_Title LIKE '%" + s
                    + "%'  or Word_Title_VN like  '%" + s + "%'";
            cs = database.rawQuery(query, null);

            Log.d(TAG, "SearchWord: " + query);
            Word word;
            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), 0, cs.getString(5),
                        cs.getString(6), 0, cs.getInt(4), cs.getString(7));
                word.Maintopic_Tile = cs.getString(8);
                list.add(word);
            }


            query="Select Word.Topic_ID, Relationship.Root, Relationship.Word_Title, Relationship.Word_Title_VN,MainTopic.MainTopic_Id, MainTopic.MainTopic_Title,Topic.Topic_Title , Relationship.Type_Word,MainTopic.MainTopic_Title_VN"+
                    " from Relationship,Word,Topic,MainTopic Where (Relationship.Word_Title LIKE '%" + s + "%'  or Relationship.Word_Title_VN like  '%" + s + "%')" +
                    " and Word.Word_Id = Relationship.Root and Topic.MainTopic_Id = MainTopic.MainTopic_Id and Word.Topic_Id = Topic.Topic_Id ";
            Log.d(TAG, "SearchWord: " + query);
            cs = database.rawQuery(query, null);

            while (cs.moveToNext()) {
                word = new Word(cs.getString(0), cs.getInt(1), cs.getString(2),
                        cs.getString(3), 0, cs.getString(5),
                        cs.getString(6), 0, cs.getInt(4), cs.getString(7));
                word.Maintopic_Tile = cs.getString(8);
                list.add(word);
            }


        } catch (Exception e) {
            Log.e(TAG, "SearchWord: ", e);
        } finally {
            if (cs != null)
                cs.close();
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

            {
                openDataBase();
            }

            ContentValues values = new ContentValues();

            values.put("Word_Pronoun", score);
            long rs = database.update("Word", values, "Word_Title='" + word + "'", null);

            if (rs > 0) {
                result = true;

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public ArrayList<WordRelationShip> GetRalationShipWord(int wordId) {
        ArrayList<WordRelationShip> list = new ArrayList<>();
        Cursor cs = null;
        try {
            {
                openDataBase();
            }
            String query = "Select * from Relationship where Root = " + '"' + wordId + '"';
            cs = database.rawQuery(query, null);
            WordRelationShip word;
            while (cs.moveToNext()) {
                word = new WordRelationShip(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getString(3));
                list.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
            database.close();
        }

        Collections.sort(list, new Comparator<WordRelationShip>() {
            @Override
            public int compare(WordRelationShip o1, WordRelationShip o2) {
                return o1.getWord_Title().toLowerCase().compareToIgnoreCase(o2.getWord_Title());
            }
        });
        return list;
    }


}